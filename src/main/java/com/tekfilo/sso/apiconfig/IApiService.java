package com.tekfilo.sso.apiconfig;

import java.util.List;

public interface IApiService {

    void saveApiConfig(ApiConfigDto apiConfigDto) throws Exception;

    List<ApiEntity> getApiList();

    ApiConfigEntity getApiConfigById(Integer apiId, Integer companyId);
}
