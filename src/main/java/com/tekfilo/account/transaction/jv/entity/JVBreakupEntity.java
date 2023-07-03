package com.tekfilo.account.transaction.jv.entity;

import com.tekfilo.account.costcategory.CostCategoryEntity;
import com.tekfilo.account.costcenter.CostCenterEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_jv_breakup")
public class JVBreakupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jvbreakup_generator")
    @SequenceGenerator(name = "jvbreakup_generator", sequenceName = "tbl_jv_breakup_seq", allocationSize = 1)
    @Column(name = "breakup_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_type",insertable = true,updatable = false)
    private String invoiceType;

    @Column(name = "jv_det_id", insertable = true,updatable = false)
    private Integer jvDetailId;

    @Column(name = "cost_center_id")
    private Integer costCenterId;

    @OneToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "cost_center_id", insertable = false, updatable = false)
    private CostCenterEntity costCenter;

    @Column(name = "cost_category_id")
    private Integer costCategoryId;

    @OneToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "cost_category_id", insertable = false, updatable = false)
    private CostCategoryEntity costCategory;

    @Column(name = "description")
    private String description;
    @Column(name = "tax_pct")
    private Double taxPct;

    @Column(name = "debit_amount")
    private Double debitAmount;

    @Column(name = "credit_amount")
    private Double creditAmount;

    @Column(name = "debit_tax_amount")
    private Double debitTaxAmount;

    @Column(name = "credit_tax_amount")
    private Double creditTaxAmount;

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
