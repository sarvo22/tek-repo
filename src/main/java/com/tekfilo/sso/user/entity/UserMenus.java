package com.tekfilo.sso.user.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "tbl_main_menu")
public class UserMenus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mainmenu_generator")
    @SequenceGenerator(name = "mainmenu_generator", sequenceName = "tbl_main_menu_seq", allocationSize = 1)
    @Column(name = "main_menu_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "key")
    private String key;

    @Column(name = "label")
    private String label;

    @Column(name = "icon")
    private String icon;

    @Column(name = "link")
    private String link;

    @Column(name = "collapsed")
    private Boolean collapsed;

    @Column(name = "istitle")
    private Boolean isTitle;

    @Column(name = "badge")
    private String badge;

    @Column(name = "parentkey")
    private Integer parentKey;

    @Column(name = "showininner")
    private Boolean showInInner;

    //private List<UserSubMenus> children;

}
