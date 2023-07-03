package com.tekfilo.inventory.mixing;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_mixing_main")
public class MixingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mixmain_generator")
    @SequenceGenerator(name = "mixmain_generator", sequenceName = "tbl_mixing_main_seq", allocationSize = 1)
    @Column(name = "mixing_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "mixing_type", insertable = true, updatable = false)
    private String mixingType;


    @Column(name = "mixing_no", insertable = true, updatable = false)
    private String mixingNo;


    @Column(name = "mixing_dt")
    private Date mixingDt;

    @Column(name = "mixing_by")
    private Integer mixingBy;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "total_mix_qty1", insertable = false, updatable = false)
    private BigDecimal totalQty1;

    @Column(name = "total_mix_qty2", insertable = false, updatable = false)
    private BigDecimal totalQty2;
    
    @Column(name = "total_mix_amount", insertable = false, updatable = false)
    private BigDecimal totalAmount;

    @Column(name = "company_id", insertable = true, updatable = false)
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
}
