package com.tekfilo.admin.factory.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_factory")
public class FactoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factory_generator")
    @SequenceGenerator(name = "factory_generator", sequenceName = "tbl_factory_seq", allocationSize = 1)
    @Column(name = "factory_id", updatable = false, insertable = true, nullable = false)
    private Integer id;
    @Column(name = "factory_code")
    private String factoryCode;
    @Column(name = "factory_name")
    private String factoryName;
    @Column(name = "factory_alias_name")
    private String factoryAliasName;

    @Column(name = "payment_type_id")
    private Integer paymentTypeId;

    @Column(name = "head_buyer_id")
    private Integer headBuyerId;

    @Column(name = "buyer_id")
    private Integer buyerId;

    @Column(name = "primary_contact_name")
    private String primaryContactName;

    @Column(name = "primary_contact_no")
    private String primaryContactNo;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "business_no")
    private String businessNo;

    @Column(name = "currency")
    private String currency;

    @Column(name = "zone")
    private String zone;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "remarks")
    private String remarks;
    @Column(name = "system_remarks")
    private String systemRemarks;
    @Column(name = "sort_seq")
    private Integer sortSequence;
    @Column(name = "is_locked")
    private Integer isLocked;

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
