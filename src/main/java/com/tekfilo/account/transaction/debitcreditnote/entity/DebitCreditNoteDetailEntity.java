package com.tekfilo.account.transaction.debitcreditnote.entity;

import com.tekfilo.account.accmaster.AccMasterEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptBreakupEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_debit_credit_note_det")
public class DebitCreditNoteDetailEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drcrdet_generator")
    @SequenceGenerator(name = "drcrdet_generator", sequenceName = "tbl_debit_credit_note_det_seq", allocationSize = 1)
    @Column(name = "inv_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_id", updatable = false, insertable = true, nullable = false)
    private Integer invId;

    @Column(name = "account_id")
    private Integer accountId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private AccMasterEntity account;

    @Column(name = "description")
    private String description;

    @Column(name = "tax_pct")
    private Double taxPct;

    @Column(name = "gross_amount")
    private BigDecimal grossAmount;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "net_amount")
    private BigDecimal netAmount;

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
    private List<DebitCreditNoteBreakupEntity> breakupList;
}
