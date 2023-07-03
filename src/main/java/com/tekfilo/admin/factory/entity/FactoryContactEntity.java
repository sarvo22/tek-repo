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
@Table(name = "tbl_factory_contact")
public class FactoryContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factorycontact_generator")
    @SequenceGenerator(name = "factorycontact_generator", sequenceName = "tbl_factory_contact_seq", allocationSize = 1)
    @Column(name = "contact_id", updatable = false, insertable = true, nullable = false)
    private Integer id;
    @Column(name = "factory_id", updatable = false, insertable = true)
    private Integer factoryId;
    @Column(name = "contact_type")
    private String contactType;
    @Column(name = "name")
    private String name;
    @Column(name = "designation")
    private String designation;
    @Column(name = "phone_no")
    private String phoneNo;
    @Column(name = "mobile_no")
    private String mobileNo;
    @Column(name = "email")
    private String email;
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

