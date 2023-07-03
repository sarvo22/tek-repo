package com.tekfilo.inventory.product.price;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_product_price")
public class ProductPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productprice_generator")
    @SequenceGenerator(name = "productprice_generator", sequenceName = "tbl_product_price_seq", allocationSize = 1)
    @Column(name = "price_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    private Integer productId;

    @Column(name = "price_dt")
    private Date priceDate;

    @Column(name = "currency")
    private String currency;

    @Column(name = "cost_price ")
    private Double costPrice;

    @Column(name = "sales_price ")
    private Double salesPrice;

    @Column(name = "a_price")
    private Double APrice;

    @Column(name = "b_price")
    private Double BPrice;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "sort_seq")
    private Integer sequence;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "company_id")
    private Integer companyId;

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
