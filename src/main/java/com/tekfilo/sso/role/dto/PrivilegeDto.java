package com.tekfilo.sso.role.dto;

import lombok.Data;

@Data
public class PrivilegeDto {
    private Integer mapId;
    private Integer id;
    private Integer privilegeId;
    private String privilegeName;
    private String privilegeGroup;
    private Integer menuId;
    private Boolean fullaccess;
    private Boolean view;
    private Boolean add;
    private Boolean edit;
    private Boolean delete;
    private Boolean print;
    private Boolean share;
    private Boolean email;
    private Boolean detailview;
    private Boolean detailadd;
    private Boolean detaildelete;
    private Boolean mainlock;
    private Boolean mainunlock;
    private Boolean detaillock;
    private Boolean detailunlock;
    private Boolean quickpayment;
    private Boolean changestatus;
    private Boolean upload;
    private Boolean addcharges;
    private Boolean deletecharges;
    private Boolean postaccount;
    private Boolean submit;
}
