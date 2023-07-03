package com.tekfilo.inventory.cut;

import com.tekfilo.inventory.commoditygroup.CommodityGroupEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_cut")
public class CutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cut_generator")
    @SequenceGenerator(name = "cut_generator", sequenceName = "tbl_cut_seq", allocationSize = 1)
    @Column(name = "cut_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "cut_name")
    private String cutName;

    @Column(name = "commodity_group_id")
    private Integer groupCategory;

    @Column(name = "company_id")
    private Integer companyId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commodity_group_id", referencedColumnName = "commodity_group_id", insertable = false, updatable = false, nullable = true)
    private CommodityGroupEntity group;

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
