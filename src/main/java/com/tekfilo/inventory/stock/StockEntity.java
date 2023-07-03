package com.tekfilo.inventory.stock;

import com.tekfilo.inventory.bin.BinEntity;
import com.tekfilo.inventory.product.ProductEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_product_stock")
public class StockEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockid_generator")
    @SequenceGenerator(name = "stockid_generator", sequenceName = "tbl_product_stock_seq", allocationSize = 1)
    @Column(name = "stock_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "company_id")
    private Integer companyId;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false, nullable = true)
//    private CompanyEntity company;

    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false, nullable = true)
    private ProductEntity product;

    @Column(name = "bin_id")
    private Integer binId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bin_id", referencedColumnName = "bin_id", insertable = false, updatable = false, nullable = true)
    private BinEntity bin;

    @Column(name = "in_qty1")
    private BigDecimal inQty1;

    @Column(name = "in_qty2")
    private BigDecimal inQty2;

    @Column(name = "out_qty1")
    private BigDecimal outQty1;

    @Column(name = "out_qty2")
    private BigDecimal outQty2;

    @Column(name = "adj_in_qty1")
    private BigDecimal adjInQty1;

    @Column(name = "adj_in_qty2")
    private BigDecimal adjInQty2;

    @Column(name = "adj_out_qty1")
    private BigDecimal adjOutQty1;

    @Column(name = "adj_out_qty2")
    private BigDecimal adjOutQty2;

    @Column(name = "balance_qty1")
    private BigDecimal balanceQty1;
    @Column(name = "balance_qty2")
    private BigDecimal balanceQty2;


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
