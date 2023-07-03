package com.tekfilo.jewellery.jewinvoice.salesinvoice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tbl_jew_sales_invoice_chgs")
public class SalesInvoiceChargesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salesinvoicechgs_generator")
    @SequenceGenerator(name = "salesinvoicechgs_generator", sequenceName = "tbl_jew_sales_invoice_chgs_seq", allocationSize = 1)
    @Column(name = "charge_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_id")
    private Integer invId;

    @Column(name = "charge_name")
    private String chargeName;

    @Column(name = "plus_minus_flag")
    private Integer plusMinusFlag;

    @Column(name = "input_pct_amt_type")
    private String inputPctAmountType;

    @Column(name = "input_pct_amt_value")
    private BigDecimal inputPctAmountValue;

    @Column(name = "input_amt")
    private BigDecimal inputAmount;

    @Column(name = "is_customer_payable")
    private Integer isCustomerPayable;

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
