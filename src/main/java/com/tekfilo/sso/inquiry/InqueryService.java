package com.tekfilo.sso.inquiry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;

@Slf4j
@Service
@Transactional
@EnableAsync
public class InqueryService {

    @Autowired
    InqueryRepository inqueryRepository;

    @Value("${tekfilo.email.service.inquery}")
    private String inqueryServerEndpoint;


    public void saveInquery(InquiryDto inquiryDto) throws SQLException {
        InqueryEntity entity = new InqueryEntity();
        BeanUtils.copyProperties(inquiryDto, entity);
        entity.setIsDeleted(0);
        inqueryRepository.save(entity);
    }


    @Async
    public void sendEmail(InquiryDto enquiryDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<Object>(enquiryDto, headers);
        String result = restTemplate.postForObject(inqueryServerEndpoint, request, String.class);
        log.info(result);
    }
}
