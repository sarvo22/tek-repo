package com.tekfilo.admin.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tekfilo.admin.employee.EmployeeEntity;
import com.tekfilo.admin.paymentterms.PaymentTermsEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_customer")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_generator")
    @SequenceGenerator(name = "customer_generator", sequenceName = "tbl_customer_seq", allocationSize = 1)
    @Column(name = "customer_id", updatable = false, insertable = true, nullable = false)
    private Integer id;
    @Column(name = "customer_code")
    private String customerCode;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "customer_alias_name")
    private String customerAliasName;

    @Column(name = "payment_type_id")
    private Integer paymentTypeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "payment_type_id", referencedColumnName = "payment_type_id", insertable = false, updatable = false)
    private PaymentTermsEntity paymentTerms;

    @Column(name = "head_salesman_id")
    private Integer headSalesmanId;


    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "head_salesman_id", referencedColumnName = "employee_id", insertable = false, updatable = false)
    private EmployeeEntity headSalesman;

    @Column(name = "salesman_id")
    private Integer salesmanId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "salesman_id", referencedColumnName = "employee_id", insertable = false, updatable = false)
    private EmployeeEntity salesman;

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
