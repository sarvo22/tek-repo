package com.tekfilo.inventory.item.invoice.stock.entity;

import com.tekfilo.inventory.bin.BinEntity;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.product.ProductEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_item_stock")
public class ItemStockEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemstockid_generator")
    @SequenceGenerator(name = "itemstockid_generator", sequenceName = "tbl_item_stock_seq", allocationSize = 1)
    @Column(name = "stock_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "item_id")
    private Integer productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", insertable = false, updatable = false, nullable = true)
    private ItemEntity product;

    @Column(name = "bin_id")
    private Integer binId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bin_id", referencedColumnName = "bin_id", insertable = false, updatable = false, nullable = true)
    private BinEntity bin;

    @Column(name = "in_qty")
    private BigDecimal inQty;

    @Column(name = "out_qty")
    private BigDecimal outQty;

    @Column(name = "adj_in_qty")
    private BigDecimal adjInQty;

    @Column(name = "adj_out_qty")
    private BigDecimal adjOutQty;

    @Column(name = "balance_qty")
    private BigDecimal balanceQty;

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
