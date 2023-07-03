package com.tekfilo.inventory.mixing;

import com.tekfilo.inventory.bin.BinEntity;
import com.tekfilo.inventory.product.ProductEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_mixing_det")
public class MixingDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mixdet_generator")
    @SequenceGenerator(name = "mixdet_generator", sequenceName = "tbl_mixing_det_seq", allocationSize = 1)
    @Column(name = "mixing_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "mixing_id", insertable = true, updatable = false)
    private Integer mixingId;

    @Column(name = "source_bin_id")
    private Integer sourceBinId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_bin_id", insertable = false, updatable = false)
    private BinEntity sourceBin;

    @Column(name = "dest_bin_id")
    private Integer destinationBinId;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dest_bin_id", insertable = false, updatable = false)
    private BinEntity destBin;


    @Column(name = "source_product_id")
    private Integer sourceProductId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_product_id", insertable = false, updatable = false)
    private ProductEntity sourceProduct;

    @Column(name = "dest_product_id")
    private Integer destinationProductId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dest_product_id", insertable = false, updatable = false)
    private ProductEntity destinationProduct;

    @Column(name = "mixing_qty1")
    private Double mixingQty1;

    @Column(name = "mixing_qty2")
    private Double mixingQty2;

    @Column(name = "source_image_url")
    private String sourceImageUrl;

    @Column(name = "source_cost_price")
    private Double sourceCostPrice;

    @Column(name = "destination_image_url")
    private String destinationImageUrl;

    @Column(name = "destination_cost_price")
    private Double destinationCostPrice;


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


    @Transient
    private Double sourceStockQty1;
    @Transient
    private Double sourceStockQty2;
    @Transient
    private Double destinationStockQty1;
    @Transient
    private Double destinationStockQty2;
}
