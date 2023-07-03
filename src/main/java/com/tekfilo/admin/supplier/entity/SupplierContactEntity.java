package com.tekfilo.admin.supplier.entity;

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
@Table(name = "tbl_supplier_contact")
public class SupplierContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suppliercontact_generator")
    @SequenceGenerator(name = "suppliercontact_generator", sequenceName = "tbl_supplier_contact_seq", allocationSize = 1)
    @Column(name = "contact_id", updatable = false, insertable = true, nullable = false)
    private Integer id;
    @Column(name = "supplier_id", updatable = false, insertable = true)
    private Integer supplierId;
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

