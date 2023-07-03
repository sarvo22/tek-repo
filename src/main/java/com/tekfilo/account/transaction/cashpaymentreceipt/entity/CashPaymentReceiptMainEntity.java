package com.tekfilo.account.transaction.cashpaymentreceipt.entity;

import com.tekfilo.account.accmaster.AccMasterEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_cash_pay_rec_main")
public class CashPaymentReceiptMainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_generator")
    @SequenceGenerator(name = "cash_generator", sequenceName = "tbl_cash_pay_rec_main_seq", allocationSize = 1)
    @Column(name = "inv_id", updatable = false, insertable = true, nullable = false)
    private Integer id;


    @Column(name = "inv_type", insertable = true, updatable = false)
    private String invoiceType;

    @Column(name = "inv_no", insertable = true, updatable = false)
    private String invoiceNo;

    @Column(name = "inv_dt")
    private Date invoiceDate;

    @Column(name = "inv_due_dt")
    private Date invoiceDueDate;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "cash_account_id")
    private Integer cashAccountId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cash_account_id", insertable = false, updatable = false)
    private AccMasterEntity account;

    @Column(name = "note")
    private String note;

    @Column(name = "accounting_status")
    private String accountingStatus;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "total_debit_amount",insertable = false, updatable = false)
    private Double totalDebitAmount;

    @Column(name = "total_credit_amount",insertable = false, updatable = false)
    private Double totalCreditAmount;

    @Column(name = "settled_amount",insertable = false, updatable = false)
    private Double settledAmount;

    @Column(name = "party_type")
    private String partyType;
    @Column(name = "party_id")
    private Integer partyId;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "company_id", insertable = true, updatable = false)
    private Integer companyId;

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

}
