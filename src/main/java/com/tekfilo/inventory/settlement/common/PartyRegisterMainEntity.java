package com.tekfilo.inventory.settlement.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_party_account_register")
public class PartyRegisterMainEntity {


    @Id
    @Column(name = "inv_type_id", insertable = false, updatable = false)
    private String invoiceTypeId;

    @Column(name = "inv_id", insertable = true, updatable = false)
    private Integer invoiceId;

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

    @Column(name = "inv_due_months")
    private Double invoiceDueMonths;

    @Column(name = "inv_due_days")
    private Double invoiceDueDays;

    @Column(name = "payment_type_id")
    private Integer paymentTypeId;

    @Column(name = "party_type")
    private String partyType;

    @Column(name = "party_id")
    private Integer partyId;

    @Column(name = "status")
    private String status;

    @Column(name = "in_out_flag")
    private Integer inOutFlag;

    @Column(name = "invoice_amount")
    private Double invoiceAmount;

    @Column(name = "settled_amount")
    private Double settlementAmount;

    @Column(name = "exch_diff_currency")
    private Double exchDiffCurrency;

    @Column(name = "exch_diff_amount")
    private Double exchDiffAmount;

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

    @Transient
    private Double receiveNow;
}
