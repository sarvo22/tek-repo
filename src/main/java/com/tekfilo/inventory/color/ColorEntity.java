package com.tekfilo.inventory.color;

import com.tekfilo.inventory.commoditygroup.CommodityGroupEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_color")
public class ColorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "color_generator")
    @SequenceGenerator(name = "color_generator", sequenceName = "tbl_color_seq", allocationSize = 1)
    @Column(name = "color_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "color_name")
    private String colorName;

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
