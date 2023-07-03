package com.tekfilo.admin.supplier.entity;

import com.tekfilo.admin.employee.EmployeeEntity;
import com.tekfilo.admin.paymentterms.PaymentTermsEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_supplier")
public class SupplierEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_generator")
    @SequenceGenerator(name = "supplier_generator", sequenceName = "tbl_supplier_seq", allocationSize = 1)
    @Column(name = "supplier_id", updatable = false, insertable = true, nullable = false)
    private Integer id;
    @Column(name = "supplier_code")
    private String supplierCode;
    @Column(name = "supplier_name")
    private String supplierName;
    @Column(name = "supplier_alias_name")
    private String supplierAliasName;

    @Column(name = "payment_type_id")
    private Integer paymentTypeId;

    @OneToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "payment_type_id", referencedColumnName = "payment_type_id", insertable = false, updatable = false, nullable = true)
    private PaymentTermsEntity paymentTerms;

    @Column(name = "head_buyer_id")
    private Integer headBuyerId;

    @Column(name = "buyer_id")
    private Integer buyerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "buyer_id", referencedColumnName = "employee_id", insertable = false, updatable = false, nullable = true)
    private EmployeeEntity buyer;

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
