package com.tekfilo.inventory.settlement.paymentreceived.entity;

import com.tekfilo.inventory.settlement.common.PartyRegisterMainEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_payment_received_det")
public class PaymentReceivedDetailEntity {

    @Transient
    PaymentReceivedMainEntity paymentReceivedMain;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paymentreceiveddet_generator")
    @SequenceGenerator(name = "paymentreceiveddet_generator", sequenceName = "tbl_payment_received_det_seq", allocationSize = 1)
    @Column(name = "payment_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;
    @Column(name = "payment_id")
    private Integer paymentId;
    @Column(name = "inv_id")
    private Integer invId;
    @Column(name = "inv_type")
    private String invType;
    @Transient
    private PartyRegisterMainEntity invoice;
    @Column(name = "inv_type_no")
    private String invoiceTypeNo;
    @Column(name = "inv_currency")
    private String currency;
    @Column(name = "inv_exchange_rate")
    private Double exchangeRate;
    @Column(name = "invoice_amount")
    private BigDecimal totalInvoiceAmount;
    @Column(name = "received_amount")
    private BigDecimal receivedAmount;
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
