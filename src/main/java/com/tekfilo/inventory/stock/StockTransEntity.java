package com.tekfilo.inventory.stock;

import com.tekfilo.inventory.bin.BinEntity;
import com.tekfilo.inventory.product.ProductEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_product_stock_trans")
public class StockTransEntity {

    @Id
    @Column(name = "trans_id", insertable = false, updatable = false)
    private Integer id;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Integer productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false, nullable = true)
    private ProductEntity product;

    @Column(name = "bin_id", insertable = false, updatable = false)
    private Integer binId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bin_id", referencedColumnName = "bin_id", insertable = false, updatable = false, nullable = true)
    private BinEntity bin;

    @Column(name = "inv_dt", insertable = false, updatable = false)
    private Date invoiceDate;

    @Column(name = "in_out_flag", insertable = false, updatable = false)
    private Integer inOutFlag;

    @Column(name = "qty1", insertable = false, updatable = false)
    private Double qty1;

    @Column(name = "qty2", insertable = false, updatable = false)
    private Double qty2;

    @Column(name = "adj_qty1", insertable = false, updatable = false)
    private Double adjQty1;

    @Column(name = "adj_qty2", insertable = false, updatable = false)
    private Double adjQty2;

    @Column(name = "party_type", insertable = false, updatable = false)
    private String partyType;

    @Column(name = "party_id", insertable = false, updatable = false)
    private Integer partyId;

    @Column(name = "currency_code", insertable = false, updatable = false)
    private String currency;

    @Column(name = "exchange_rate", insertable = false, updatable = false)
    private Double exchangeRate;

    @Column(name = "inv_rate", insertable = false, updatable = false)
    private Double invoiceRate;

    @Column(name = "cost_price", insertable = false, updatable = false)
    private Double costPrice;

    @Column(name = "inv_type", insertable = false, updatable = false)
    private String invType;

    @Column(name = "inv_no", insertable = false, updatable = false)
    private String invNo;

    @Column(name = "company_id", insertable = false, updatable = false)
    private Integer companyId;

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
