package com.tekfilo.inventory.invoice.memosalesinvoice.entity;

import com.tekfilo.inventory.master.CustomerEntity;
import com.tekfilo.inventory.master.PaymentTerm;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


@Data
@Entity
@Table(name = "tbl_memo_sales_invoice_main")
public class MemoSalesInvoiceMainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memosalesinvoicemain_generator")
    @SequenceGenerator(name = "memosalesinvoicemain_generator", sequenceName = "tbl_memo_sales_invoice_main_seq", allocationSize = 1)
    @Column(name = "inv_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_type", insertable = true, updatable = false)
    private String invoiceType;

    @Column(name = "inv_no", insertable = true, updatable = false)
    private String invoiceNo;


    @Column(name = "REFERENCE_no")
    private String referenceNo;

    @Column(name = "inv_dt")
    private Date invoiceDate;

    @Column(name = "inv_due_dt")
    private Date invoiceDueDate;

    @Column(name = "inv_due_months")
    private Double invoiceDueMonths;

    @Column(name = "inv_due_days")
    private Double invoiceDueDays;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "customer_id")
    private Integer customerId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", insertable = false, updatable = false, nullable = true)
    private CustomerEntity customer;

    @Column(name = "head_salesman_id")
    private Integer headSalesmanId;

    @Column(name = "salesman_id")
    private Integer salesmanId;

    @Column(name = "payment_type_id")
    private Integer paymentTypeId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_type_id", referencedColumnName = "payment_type_id", insertable = false, updatable = false, nullable = true)
    private PaymentTerm paymentTerm;

    @Column(name = "shipment_type_id")
    private Integer shipmentTypeId;

    @Column(name = "carrier_id")
    private Integer carrierId;

    @Column(name = "shipment_company_name")
    private String shipmentCompanyName;

    @Column(name = "accounting_status")
    private String accountingStatus;

    @Column(name = "tracking_no")
    private String trackingNo;

    @Column(name = "tracking_status")
    private String trackingStatus;

    @Column(name = "inv_status")
    private String invoiceStatus;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "cancel_hold_status")
    private String cancelHoldStatus;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "total_invoice_qty1", insertable = false, updatable = false)
    private BigDecimal totalInvoiceQty1;

    @Column(name = "total_invoice_qty2", insertable = false, updatable = false)
    private BigDecimal totalInvoiceQty2;

    @Column(name = "total_invoice_amount", insertable = false, updatable = false)
    private BigDecimal totalInvoiceAmount;

    @Column(name = "total_received_amount", insertable = false, updatable = false)
    private BigDecimal totalReceivedAmount;

    @Column(name = "billing_id")
    private Integer billingAddressId;

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
    private String billingPinCode;

    @Column(name = "bill_website")
    private String billingWebsite;

    @Column(name = "bill_phone_no")
    private String billingPhoneNo;

    @Column(name = "bill_fax_no")
    private String billingFaxNo;

    @Column(name = "bill_mobile_no")
    private String billingMobileNo;

    @Column(name = "shipping_id")
    private Integer shippingAddressId;

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
    private String shipingPinCode;
    @Column(name = "ship_website")
    private String shipingWebsite;
    @Column(name = "ship_phone_no")
    private String shipingPhoneNo;
    @Column(name = "ship_fax_no")
    private String shipingFaxNo;
    @Column(name = "ship_mobile_no")
    private String shipingMobileNo;

    @Column(name = "company_id", insertable = true, updatable = false)
    private Integer companyId;

    @Column(name = "note")
    private String customerNotes;

    @Column(name = "terms_conditions")
    private String termsAndCondition;

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
