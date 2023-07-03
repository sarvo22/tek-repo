package com.tekfilo.account.transaction.debitcreditnote.entity;

import com.tekfilo.account.master.PartyEntity;
import com.tekfilo.account.master.PaymentTerm;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_debit_credit_note_main")
public class DebitCreditNoteMainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drcrmain_generator")
    @SequenceGenerator(name = "drcrmain_generator", sequenceName = "tbl_debit_credit_note_main_seq", allocationSize = 1)
    @Column(name = "inv_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_type")
    private String invoiceType;

    @Column(name = "inv_no")
    private String invoiceNo;

    @Column(name = "inv_dt")
    private Date invoiceDate;

    @Column(name = "inv_due_dt")
    private Date invoiceDueDate;

    @Column(name = "inv_due_months")
    private Integer invDueMonths;

    @Column(name = "inv_due_days")
    private Integer invDueDays;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "party_type")
    private String partyType;

    @Column(name = "party_id")
    private Integer partyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "party_type",referencedColumnName = "party_type",insertable = false,updatable = false),
            @JoinColumn(name = "party_id",referencedColumnName = "party_id",insertable = false,updatable = false)
    })
    private PartyEntity party;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "payment_type_id")
    private Integer paymentTypeId;

    @OneToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "payment_type_id", insertable = false, updatable = false)
    private PaymentTerm paymentTerm;

    @Column(name = "party_account_id")
    private Integer partyAccountId;

    @Column(name = "accounting_status")
    private String accountingStatus;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "billing_id")
    private Integer billingId;

    @Column(name = "bill_address1")
    private String billingAddress1;

    @Column(name = "bill_address2")
    private String billingAddress2;

    @Column(name = "bill_country")
    private String billingCountry;

    @Column(name = "bill_state")
    private String billingState;

    @Column(name = "bill_city")
    private String billingCity;

    @Column(name = "bill_street")
    private String billingStreet;

    @Column(name = "bill_pincode")
    private String billingPincode;

    @Column(name = "bill_website")
    private String billingWebsite;

    @Column(name = "bill_phone_no")
    private String billingPhoneNo;

    @Column(name = "bill_fax_no")
    private String billingFaxNo;

    @Column(name = "bill_mobile_no")
    private String billingMobileNo;

    @Column(name = "shipping_id")
    private Integer shipingId;

    @Column(name = "ship_address1")
    private String shipingAddress1;

    @Column(name = "ship_address2")
    private String shipingAddress2;

    @Column(name = "ship_country")
    private String shipingCountry;

    @Column(name = "ship_state")
    private String shipingState;

    @Column(name = "ship_city")
    private String shipingCity;

    @Column(name = "ship_street")
    private String shipingStreet;

    @Column(name = "ship_pincode")
    private String shipingPincode;

    @Column(name = "ship_website")
    private String shipingWebsite;

    @Column(name = "ship_phone_no")
    private String shipingPhoneNo;

    @Column(name = "ship_fax_no")
    private String shipingFaxNo;

    @Column(name = "ship_mobile_no")
    private String shipingMobileNo;

    @Column(name = "note")
    private String note;

    @Column(name = "total_debit_amount", insertable = false, updatable = false)
    private Double totalDebitAmount;

    @Column(name = "total_credit_amount", insertable = false, updatable = false)
    private Double totalCreditAmount;

    @Column(name = "settled_amount",insertable = false, updatable = false)
    private Double settledAmount;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "company_id")
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
