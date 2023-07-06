package com.tekfilo.sso.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_menu")
public class UserSubMenus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_generator")
    @SequenceGenerator(name = "menu_generator", sequenceName = "tbl_menu_seq", allocationSize = 1)
    @Column(name = "menu_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "main_menu_id")
    private Integer mainMenuId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "main_menu_id", referencedColumnName = "main_menu_id", insertable = false, updatable = false, nullable = false)
    private UserMenus userMenus;

    @Column(name = "key")
    private String key;

    @Column(name = "label")
    private String label;

    @Column(name = "link")
    private String link;

    @Column(name = "add")
    private Boolean add;

    @Column(name = "addlink")
    private String addLink;

    @Column(name = "parentkey")
    private String parentKey;
}
