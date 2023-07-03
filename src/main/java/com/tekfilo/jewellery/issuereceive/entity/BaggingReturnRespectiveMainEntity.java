package com.tekfilo.jewellery.issuereceive.entity;

import com.tekfilo.jewellery.master.FactoryEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tbl_bagging_return_respective_main")
public class BaggingReturnRespectiveMainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bagging_res_ret_main_generator")
    @SequenceGenerator(name = "bagging_res_ret_main_generator", sequenceName = "tbl_bagging_return_respective_main_seq", allocationSize = 1)
    @Column(name = "inv_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_type", insertable = true, updatable = false)
    private String invoiceType;

    @Column(name = "inv_no", insertable = true, updatable = false)
    private String invoiceNo;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "inv_dt")
    private Date invoiceDate;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "factory_id")
    private Integer factoryId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "factory_id", referencedColumnName = "factory_id", insertable = false, updatable = false, nullable = true)
    private FactoryEntity factory;

    @Column(name = "is_exchange")
    private Integer isExchange;

    @Column(name = "note")
    private String note;

    @Column(name = "company_id")
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
