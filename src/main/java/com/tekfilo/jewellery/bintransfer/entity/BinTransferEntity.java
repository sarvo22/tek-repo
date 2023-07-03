package com.tekfilo.jewellery.bintransfer.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_jew_bin_transfer_main")
public class BinTransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bintransfer_generator")
    @SequenceGenerator(name = "bintransfer_generator", sequenceName = "tbl_jew_bin_transfer_main_seq", allocationSize = 1)
    @Column(name = "transfer_id", updatable = false, insertable = true, nullable = false)
    private Integer id;


    @Column(name = "transfer_type", insertable = true, updatable = false)
    private String transferType;

    @Column(name = "transfer_no", insertable = true, updatable = false)
    private String transferNo;

    @Column(name = "transfer_dt")
    private Date transferDt;

    @Column(name = "transfer_by")
    private Integer transferBy;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "source_bin_id")
    private Integer sourceBinId;

    @Column(name = "dest_bin_id")
    private Integer destinationBinId;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "total_qty", insertable = false, updatable = false)
    private BigDecimal totalQty;

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
