package com.tekfilo.sso.user.dto;

import lombok.Data;

@Data
public class UserRightsRequest {
    private Integer companyId;
    private Integer subscriptionId;
    private Integer appId;
    private Integer userId;
}
