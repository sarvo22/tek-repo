package com.tekfilo.admin.paymentterms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "tbl_payment_type")
@Entity
public class PaymentTermsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paymentterm_generator")
    @SequenceGenerator(name = "paymentterm_generator", sequenceName = "tbl_payment_type_seq", allocationSize = 1)
    @Column(name = "payment_type_id", updatable = false, insertable = true, nullable = false)
    private Integer id;
    @Column(name = "payment_type_name")
    private String paymentTypeName;
    @Column(name = "due_months")
    private Double dueMonths;
    @Column(name = "due_days")
    private Double dueDays;

    @Column(name = "company_id",insertable = true,updatable = false)
    private Integer companyId;


    @Column(name = "sort_seq")
    private Integer sequence;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "created_by",insertable = true,updatable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_dt",insertable = true,updatable = false)
    private Timestamp createdDate;

    @Column(name = "modified_by",insertable = false,updatable = true)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false,updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
