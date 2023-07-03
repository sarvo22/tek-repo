package com.tekfilo.sso.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserSubMenusDto {

    private Integer menuId;
    private Integer mainMenuId;
    private String key;
    private String label;
    private String link;
    private Boolean add;
    private String addLink;
    private String parentKey;
}
