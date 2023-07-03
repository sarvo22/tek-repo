package com.tekfilo.admin.currency;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table(name = "tbl_company_currency")
public class CurrencyCompanyMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "curcompseq_generator")
    @SequenceGenerator(name = "curcompseq_generator", sequenceName = "tbl_company_currency_seq", allocationSize = 1)
    @Column(name = "map_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "is_default")
    private Integer isDefault;
    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "created_by", insertable = true, updatable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;

    @Column(name = "modified_by", insertable = false, updatable = true)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    @JsonInclude()
    @Transient
    private CurrencyEntity currency;

    @JsonInclude
    @Transient
    private Date lastExchangeRateDate;

    @JsonInclude
    @Transient
    private Double lastExchangeRate;

}
