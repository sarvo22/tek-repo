package com.tekfilo.sso.user.dto;

import lombok.Data;

import java.util.List;


@Data
public class UserMenusDto {

    private Integer subscriptionId;
    private Integer appId;
    private Integer mainMenuId;
    private String key;
    private String label;
    private String icon;
    private String link;
    private Boolean collapsed;
    private Boolean isTitle;
    private String badge;
    private Integer parentKey;
    private Boolean showInInner;
    private List<UserSubMenusDto> children;

}
