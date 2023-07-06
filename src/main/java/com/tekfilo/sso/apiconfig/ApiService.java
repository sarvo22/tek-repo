package com.tekfilo.sso.apiconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ApiService implements IApiService {

    @Autowired
    ApiRepository apiRepository;

    @Autowired
    ApiConfigRepository apiConfigRepository;

    @Override
    public void saveApiConfig(ApiConfigDto apiConfigDto) throws Exception {
        this.apiConfigRepository.save(convert2ApiConfigEntity(apiConfigDto));
    }

    @Override
    public List<ApiEntity> getApiList() {
        return this.apiRepository.findAll();
    }

    @Override
    public ApiConfigEntity getApiConfigById(Integer apiId, Integer companyId) {
        List<ApiConfigEntity> apiConfigEntityList = apiConfigRepository.findByApiAndCompanyId(apiId, companyId);
        if (apiConfigEntityList.size() > 0) {
            return apiConfigEntityList.get(0);
        }
        return new ApiConfigEntity();
    }

    private ApiConfigEntity convert2ApiConfigEntity(ApiConfigDto apiConfigDto) {
        ApiConfigEntity apiConfigEntity = new ApiConfigEntity();
        apiConfigEntity.setId(apiConfigDto.getId());
        apiConfigEntity.setApiId(apiConfigDto.getApiId());
        apiConfigEntity.setApiUserName(apiConfigDto.getApiUserName());
        apiConfigEntity.setApiKey(apiConfigDto.getApiKey());
        apiConfigEntity.setApiSid(apiConfigDto.getApiSid());
        apiConfigEntity.setTenantUid(apiConfigDto.getTenantUid());
        apiConfigEntity.setCompanyId(apiConfigDto.getCompanyId());
        apiConfigEntity.setCronexpression(apiConfigDto.getCronexpression());
        apiConfigEntity.setConfirmation(apiConfigDto.getConfirmation());
        apiConfigEntity.setEnabled(1);
        apiConfigEntity.setSequence(0);
        apiConfigEntity.setLocked(0);
        apiConfigEntity.setCreatedBy(apiConfigDto.getUserId());
        apiConfigEntity.setModifiedBy(apiConfigDto.getUserId());
        apiConfigEntity.setIsDeleted(0);
        return apiConfigEntity;
    }
}
