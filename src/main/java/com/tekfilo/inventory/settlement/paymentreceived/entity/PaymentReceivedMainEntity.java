package com.tekfilo.inventory.settlement.paymentreceived.entity;

import com.tekfilo.inventory.master.AccMasterEntity;
import com.tekfilo.inventory.master.CustomerEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_payment_received_main")
public class PaymentReceivedMainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paymentreceived_generator")
    @SequenceGenerator(name = "paymentreceived_generator", sequenceName = "tbl_payment_received_main_seq", allocationSize = 1)
    @Column(name = "payment_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "payment_type", insertable = true,updatable = false)
    private String paymentType;

    @Column(name = "payment_no", insertable = true,updatable = false)
    private String paymentNo;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "payment_dt")
    private Date paymentDate;

    @Column(name = "customer_id")
    private Integer customerId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", insertable = false, updatable = false, nullable = true)
    private CustomerEntity customer;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "deposit_to")
    private String depositTo;

    @Column(name = "bank_cash_account_id")
    private Integer bankCashAccountId;

    @OneToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "bank_cash_account_id", referencedColumnName = "account_id", insertable = false, updatable = false, nullable = true)
    private AccMasterEntity accMaster;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "note")
    private String note;

    @Column(name = "received_amount")
    private BigDecimal amountReceived;

    @Column(name = "bank_charges_amount")
    private BigDecimal bankCharges;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "remarks")
    private String remarks;

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
