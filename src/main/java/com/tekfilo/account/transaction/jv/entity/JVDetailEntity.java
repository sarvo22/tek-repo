package com.tekfilo.account.transaction.jv.entity;

import com.tekfilo.account.accmaster.AccMasterEntity;
import com.tekfilo.account.group.GroupEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptBreakupEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_jv_det")
public class JVDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jvdet_generator")
    @SequenceGenerator(name = "jvdet_generator", sequenceName = "tbl_jv_det_seq", allocationSize = 1)
    @Column(name = "jv_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "jv_main_id")
    private Integer jvMainId;

    @Column(name = "account_id")
    private Integer accountId;

    @OneToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false, nullable = true)
    private AccMasterEntity account;

    @Column(name = "description")
    private String description;

    @Column(name = "tax_pct")
    private BigDecimal taxPct;

    @Column(name = "debit_amount")
    private BigDecimal debitAmount;

    @Column(name = "credit_amount")
    private BigDecimal creditAmount;

    @Column(name = "debit_tax_amount")
    private  BigDecimal debitTaxAmount;

    @Column(name = "credit_tax_amount")
    private BigDecimal creditTaxAmount;

    @Column(name = "sort_seq")
    private Integer sequence;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "created_by", insertable = true, updatable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDt;

    @Column(name = "modified_by", insertable = false, updatable = true)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDt;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    @Transient
    private List<JVBreakupEntity> breakupList;
}
