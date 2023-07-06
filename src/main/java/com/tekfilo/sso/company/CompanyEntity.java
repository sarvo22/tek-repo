package com.tekfilo.sso.company;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_company")
public class CompanyEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_generator")
    @SequenceGenerator(name = "company_generator", sequenceName = "tbl_company_seq", allocationSize = 1)
    @Column(name = "company_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "display_name")
    private String displayName;


    @Column(name = "business_type")
    private String businessType;

    @Column(name = "company_type")
    private String companyType;

    @Column(name = "default_currency", insertable = true, updatable = false)
    private String defaultCurrency;

    @Column(name = "country_id", insertable = true, updatable = false)
    private Integer countryId;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "area")
    private String area;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "prime_contact_name")
    private String primeContactName;

    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "mobile_no")
    private String mobileNo;
    @Column(name = "website")
    private String website;
    @Column(name = "reg_type")
    private String registerType;
    @Column(name = "reg_number")
    private String registerNo;


    @Column(name = "tax_type")
    private String taxType;

    @Column(name = "tax_number")
    private String taxNo;

    @Column(name = "status", insertable = true, updatable = false, nullable = false)
    private String status;

    @Column(name = "date_format", insertable = true, updatable = true, nullable = false)
    private String dateFormat;

    @Column(name = "ui_date_format", insertable = true, updatable = true, nullable = false)
    private String uiDateFormat;


    @Column(name = "time_zone")
    private String timeZone;

    @Column(name = "company_uid", insertable = true, updatable = false)
    private String companyUID;

    @Column(name = "fax_no")
    private String faxNo;

    @Column(name = "fiscal_year")
    private String fiscalYear;

    @Column(name = "payment_address")
    private String paymentAddress;

    @Column(name = "date_time_divider")
    private String dateTimeDivider;

    @Column(name = "log_url")
    private String logoUrl;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "system_remarks")
    private String systemRemarks;

    @Column(name = "created_by", insertable = true, updatable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;

    @Column(name = "modified_by", insertable = false, updatable = true)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;

}
