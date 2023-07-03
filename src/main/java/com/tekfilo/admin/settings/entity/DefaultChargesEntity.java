package com.tekfilo.admin.settings.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_default_invoice_chgs")
public class DefaultChargesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defaultcharge_generator")
    @SequenceGenerator(name = "defaultcharge_generator", sequenceName = "tbl_default_invoice_chgs_seq", allocationSize = 1)
    @Column(name = "default_charge_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_type")
    private String invType;

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

    @Column(name = "is_party_payable")
    private Integer isPartyPayable;

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
