package com.tekfilo.admin.currency;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_currency")
public class CurrencyEntity implements Serializable {

    @Id
    @Column(name = "currency_code", insertable = true, updatable = false)
    private String code;

    @Column(name = "currency_name")
    private String name;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "formatter")
    private String formatter;

    @Column(name = "decimal_places")
    private Integer decimalPlaces;

    @Column(name = "company_id", insertable = true, updatable = false)
    private Integer companyId;

    @Column(name = "sort_seq")
    private Integer sequence;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "created_by", insertable = true, updatable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;

    @Column(name = "modified_by", updatable = true, insertable = false)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;

}
