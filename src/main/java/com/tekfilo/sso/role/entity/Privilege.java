package com.tekfilo.sso.role.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_privilege")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_generator")
    @SequenceGenerator(name = "privilege_generator", sequenceName = "tbl_privilege_seq", allocationSize = 1)
    @Column(name = "privilege_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "privilege_name")
    private String privilegeName;
    @Column(name = "privilege_group")
    private String privilegeGroup;

    @Column(name = "menu_id")
    private Integer menuId;

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

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "system_remarks")
    private String systemRemarks;


    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;


    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
