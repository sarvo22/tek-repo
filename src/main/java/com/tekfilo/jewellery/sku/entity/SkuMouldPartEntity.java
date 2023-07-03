package com.tekfilo.jewellery.sku.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_sku_mould_part")
public class SkuMouldPartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skumouldpart_generator")
    @SequenceGenerator(name = "skumouldpart_generator", sequenceName = "tbl_sku_mould_part_seq", allocationSize = 1)
    @Column(name = "sku_mould_part_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "sku_id", insertable = true,updatable = false)
    private Integer skuId;



    @Column(name = "part_name")
    private String partName;

    @Column(name = "part_no")
    private String partNo;

    @Column(name = "description")
    private String description;

    @Column(name = "pieces")
    private Double pieces;

    @Column(name = "sort_seq")
    private Integer sequence;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "created_by", insertable = true, updatable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;

    @Column(name = "modified_by", insertable = false, updatable = true)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
