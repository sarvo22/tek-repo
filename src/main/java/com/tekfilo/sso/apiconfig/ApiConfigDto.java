package com.tekfilo.sso.apiconfig;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ApiConfigDto {

    private Integer id;
    private Integer apiId;
    private String apiUserName;
    private String apiKey;
    private String apiSid;
    private Integer userId;
    private String tenantUid;
    private Integer companyId;
    private String cronexpression;
    private String confirmation;
}
