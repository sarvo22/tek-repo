package com.tekfilo.jewellery.bintransfer.entity;

import com.tekfilo.jewellery.master.BinEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_jew_bin_transfer_det")
public class BinTransferDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bintransferdet_generator")
    @SequenceGenerator(name = "bintransferdet_generator", sequenceName = "tbl_jew_bin_transfer_det_seq", allocationSize = 1)
    @Column(name = "transfer_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "transfer_id", insertable = true, updatable = false)
    private Integer transferId;

    @Column(name = "source_bin_id")
    private Integer sourceBinId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_bin_id", insertable = false, updatable = false, nullable = true)
    private BinEntity sourceBin;

    @Column(name = "dest_bin_id")
    private Integer destinationBinId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dest_bin_id", insertable = false, updatable = false, nullable = true)
    private BinEntity destBin;


    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "jew_id", insertable = false, updatable = false, nullable = true)
    private ProductEntity product;

    @Column(name = "transfer_qty")
    private Double transferQty;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "cost_price")
    private Double costPrice;

    @Column(name = "sellig_price")
    private Double sellingPrice;


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
