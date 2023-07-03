package com.tekfilo.inventory.commodity;

import com.tekfilo.inventory.commoditygroup.CommodityGroupEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_commodity")
public class CommodityEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commodity_generator")
    @SequenceGenerator(name = "commodity_generator", sequenceName = "tbl_commodity_seq", allocationSize = 1)
    @Column(name = "commodity_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "commodity_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "commodity_group_id")
    private Integer groupId;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commodity_group_id", referencedColumnName = "commodity_group_id", insertable = false, updatable = false, nullable = false)
    private CommodityGroupEntity commodityGroup;
}
