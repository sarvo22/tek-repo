package com.tekfilo.sps.ibot.service;

import com.tekfilo.sps.company.CompanyEntity;
import com.tekfilo.sps.company.CompanyRepository;
import com.tekfilo.sps.ibot.entities.ApiConfigEntity;
import com.tekfilo.sps.ibot.entities.IBotTask;
import com.tekfilo.sps.ibot.entities.Rapaport;
import com.tekfilo.sps.ibot.repository.ApiConfigRepository;
import com.tekfilo.sps.ibot.response.MetalRateResponse;
import com.tekfilo.sps.tenant.TenantConnection;
import com.tekfilo.sps.util.SPSUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
public class IBotService {

    private static final String USD = "USD";

    @Autowired
    ApiConfigRepository apiConfigRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    TenantConnection tenantConnection;


    public List<ApiConfigEntity> getApiServiceBots() throws Exception {
        log.info("Check Current Database in ApiService call {} " + apiConfigRepository.findCurrentDatabase());


        log.info("Getting ApiConfigEntity");


        return this.apiConfigRepository.findAll();
    }

    public void executeMetalRateApi(ApiConfigEntity apiConfig) {

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiConfig.getApi().getApiEndpoint());
            builder.queryParam("access_key", apiConfig.getApiKey());
            builder.queryParam("base", this.getCompanyDefaultCurrency(apiConfig.getCompanyId()));
            builder.queryParam("symbols", "XAU,XAG,XPT,XPD,XCU");

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("Authorization", "Bearer " + apiConfig.getApiKey());

            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new
                    NoopHostnameVerifier()).build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            RestTemplate restTemplate = new RestTemplate(requestFactory);

            ResponseEntity<MetalRateResponse> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, httpEntity, MetalRateResponse.class);

            if (response != null) {
                if (response.getStatusCodeValue() == 200) {
                    insertMetalRateTables(apiConfig, response.getBody());
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    private void insertMetalRateTables(ApiConfigEntity apiConfig, MetalRateResponse body) {

    }

    private String getCompanyDefaultCurrency(Integer companyId) {
        CompanyEntity entity = this.companyRepository.findById(companyId).orElse(new CompanyEntity());
        return entity.getBaseCurrency() == null ? USD : entity.getBaseCurrency();
    }

    public void executeRapaportPriceAction(List<Rapaport> rapaportList, IBotTask iBotTask, ApiConfigEntity apiConfig) {
        try {
            List<String> sqlList = new ArrayList<>();
            rapaportList.stream().forEachOrdered(price -> {
                price.setCompanyId(apiConfig.getCompanyId());
                sqlList.add(SPSUtil.getRapaportSqlStatement(price, apiConfig.getCreatedBy()));
            });
            tenantConnection.executeTenantDatabaseScript(apiConfig.getTenantUid(), sqlList);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }
}
