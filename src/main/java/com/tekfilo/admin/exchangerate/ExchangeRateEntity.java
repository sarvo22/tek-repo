package com.tekfilo.admin.exchangerate;

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
@Table(name = "tbl_exchange_rate")
public class ExchangeRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchangerate_generator")
    @SequenceGenerator(name = "exchangerate_generator", sequenceName = "tbl_exchange_rate_seq", allocationSize = 1)
    @Column(name = "exchange_rate_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "exchange_rate_dt")
    private Date exchangeRateDate;
    @Column(name = "currency")
    private String currency;
    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "bank_buy_rate")
    private Double bankBuyRate;
    @Column(name = "bank_sell_rate")
    private Double bankSellRate;

    @Column(name = "limit_rate")
    private Double limitRate;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "remarks")
    private String remarks;
    @Column(name = "system_remarks")
    private String systemRemarks;

    @Column(name = "sort_seq")
    private Integer sequence;
    @Column(name = "is_locked")
    private Integer isLocked;
    @Column(name = "created_by")
    private Integer createdBy;
    @CreationTimestamp
    @Column(name = "created_dt")
    private Timestamp createdDate;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @UpdateTimestamp
    @Column(name = "modified_dt")
    private Timestamp modifiedDate;
    @Column(name = "is_deleted")
    private Integer isDeleted;

}
