package com.tekfilo.account.transaction.debitcreditnote.entity;

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
@Table(name = "tbl_debit_credit_note_breakup")
public class DebitCreditNoteBreakupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "debitcreditnotebreakup_generator")
    @SequenceGenerator(name = "debitcreditnotebreakup_generator", sequenceName = "tbl_debit_credit_note_breakup_seq", allocationSize = 1)
    @Column(name = "breakup_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_type",insertable = true,updatable = false)
    private String invoiceType;

    @Column(name = "inv_det_id", insertable = true,updatable = false)
    private Integer invoiceDetailId;

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

    @Column(name = "gross_amount")
    private Double grossAmount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "net_amount")
    private Double netAmount;

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
