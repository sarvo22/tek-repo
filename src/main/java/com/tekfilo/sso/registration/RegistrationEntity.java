package com.tekfilo.sso.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_signup")
public class RegistrationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registration_generator")
    @SequenceGenerator(name = "registration_generator", sequenceName = "tbl_signup_seq", allocationSize = 1)
    @Column(name = "signup_id", updatable = false, insertable = true, nullable = false)
    private Integer signupId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "employee_count")
    private String employeeCount;


    @Column(name = "token")
    private String tokenVerificationCode;

    @Column(name = "is_token_verified")
    private Integer isTokenVerified;

    @Column(name = "token_verified_date")
    private Timestamp tokenVerifiedDate;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "sort_seq")
    private Integer sortSeq;

    @Column(name = "is_locked")
    private Integer locked;

    @Column(name = "created_dt")
    private Timestamp createdDate;

    @Column(name = "modified_dt")
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer deleted;

}
