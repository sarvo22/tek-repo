package com.tekfilo.account.transaction.post;

import com.tekfilo.account.accmaster.AccMasterEntity;
import com.tekfilo.account.costcategory.CostCategoryEntity;
import com.tekfilo.account.costcenter.CostCenterEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_account_posting_det")
public class PostingDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postingdet_generator")
    @SequenceGenerator(name = "postingdet_generator", sequenceName = "tbl_account_posting_det_seq", allocationSize = 1)
    @Column(name = "posting_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "posting_id")
    private Integer postingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "posting_id", insertable = false, updatable = false)
    private PostingMainEntity postingMain;

    @Column(name = "account_id")
    private Integer accountId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private AccMasterEntity account;

    @Column(name = "cost_category_id")
    private Integer costCategoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "cost_category_id", insertable = false, updatable = false)
    private CostCategoryEntity costCategory;

    @Column(name = "cost_center_id")
    private Integer costCenterId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "cost_center_id", insertable = false, updatable = false)
    private CostCenterEntity costCenter;

    @Column(name = "in_out_flag")
    private Integer inOutFlag;

    @Column(name = "dr_cr")
    private String drCr;

    @Column(name = "amount")
    private Double amount;

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
