package com.tekfilo.jewellery.order.sales.entity;

import com.tekfilo.jewellery.master.CustomerEntity;
import com.tekfilo.jewellery.master.PaymentTerm;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_jew_sales_order_main")
public class SalesOrderMainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jewsalesorder_generator")
    @SequenceGenerator(name = "jewsalesorder_generator", sequenceName = "tbl_jew_sales_order_main_seq", allocationSize = 1)
    @Column(name = "sales_order_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "sales_order_type")
    private String salesOrderType;

    @Column(name = "sales_order_no")
    private String salesOrderNo;

    @Column(name = "sales_order_date")
    private Date salesOrderDate;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "customer_id")
    private Integer customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", insertable = false, updatable = false, nullable = true)
    private CustomerEntity customer;

    @Column(name = "delivery_date")
    private Date deliveryDate;

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

    @Column(name = "salesman_id")
    private Integer salesmanId;

    @Column(name = "payment_type_id")
    private Integer paymentTermId;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "payment_type_id", insertable = false, updatable = false, nullable = true)
    private PaymentTerm paymentTerm;

    @Column(name = "so_status")
    private String soStatus;

    @Column(name = "invoice_status")
    private String invoiceStatus;

    @Column(name = "document")
    private String document;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "system_remarks")
    private String systemRemarks;

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
