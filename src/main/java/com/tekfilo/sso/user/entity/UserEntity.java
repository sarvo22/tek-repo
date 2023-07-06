package com.tekfilo.sso.user.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tbl_user")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "tbl_user_seq", allocationSize = 1)
    @Column(name = "user_id", updatable = false, insertable = true, nullable = false)
    private Integer userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username", insertable = true, updatable = false, nullable = false)
    private String userName;

    @Column(name = "password", insertable = true, updatable = false, nullable = false)
    private String password;

    @Column(name = "email", insertable = true, updatable = false, nullable = false)
    private String email;

    @Column(name = "user_uid")
    private String uID;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "total_login_count")
    private Integer totalLoginCount;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "sort_seq")
    private Integer sequence;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
