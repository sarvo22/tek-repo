package com.tekfilo.sso.plan.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_subscription_privilege_map")
public class SubscriptionPrivilege {

    @Id
    @Column(name = "map_id")
    private Integer id;

    @Column(name = "subscription_id")
    private Integer subscriptionId;

    @Column(name = "privilege_id")
    private Integer privilegeId;

    @Column(name = "privilege_name")
    private String privilegeName;

    @Column(name = "privilege_group")
    private String privilegeGroup;

    @Column(name = "menu_id")
    private Integer menuId;

//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "menu_id", referencedColumnName = "menu_id", insertable = false, updatable = false, nullable = false)
//    private UserSubMenus userSubMenus;

    @Column(name = "fullaccess")
    private Boolean fullaccess;

    @Column(name = "view")
    private Boolean view;
    @Column(name = "add")
    private Boolean add;

    @Column(name = "edit")
    private Boolean edit;

    @Column(name = "delete")
    private Boolean delete;

    @Column(name = "print")
    private Boolean print;

    @Column(name = "share")
    private Boolean share;

    @Column(name = "email")
    private Boolean email;

    @Column(name = "detailview")
    private Boolean detailview;

    @Column(name = "detailadd")
    private Boolean detailadd;

    @Column(name = "detaildelete")
    private Boolean detaildelete;


    @Column(name = "mainlock")
    private Boolean mainlock;

    @Column(name = "mainunlock")
    private Boolean mainunlock;

    @Column(name = "detaillock")
    private Boolean detaillock;

    @Column(name = "detailunlock")
    private Boolean detailunlock;

    @Column(name = "quickpayment")
    private Boolean quickpayment;

    @Column(name = "changestatus")
    private Boolean changestatus;

    @Column(name = "upload")
    private Boolean upload;

    @Column(name = "addcharges")
    private Boolean addcharges;

    @Column(name = "deletecharges")
    private Boolean deletecharges;

    @Column(name = "postaccount")
    private Boolean postaccount;

    @Column(name = "submit")
    private Boolean submit;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;

}
