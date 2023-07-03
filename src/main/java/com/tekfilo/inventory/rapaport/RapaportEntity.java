package com.tekfilo.inventory.rapaport;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_rapaport_price")
public class RapaportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rapaportprice_generator")
    @SequenceGenerator(name = "rapaportprice_generator", sequenceName = "tbl_rapaport_price_seq", allocationSize = 1)
    @Column(name = "price_id", updatable = false, insertable = true, nullable = false)
    private Integer id;


    @Column(name = "price_dt")
    private Date priceDt;

    @Column(name = "shape")
    private String shape;

    @Column(name = "color")
    private String color;

    @Column(name = "clarity")
    private String clarity;

    @Column(name = "highsize")
    private Double highSize;

    @Column(name = "lowsize")
    private Double lowSize;

    @Column(name = "currency")
    private String currency;

    @Column(name = "price")
    private Double price;

    @Column(name = "company_id")
    private Integer companyId;

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
