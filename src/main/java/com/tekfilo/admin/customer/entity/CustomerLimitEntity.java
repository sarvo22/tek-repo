package com.tekfilo.admin.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "tbl_customer_limit")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerLimitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerlimit_generator")
    @SequenceGenerator(name = "customerlimit_generator", sequenceName = "tbl_customer_limit_seq", allocationSize = 1)
    @Column(name = "limit_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "customer_id", updatable = false, insertable = true)
    private Integer customerId;

    @Column(name = "limit_type")
    private String limitType;

    @Column(name = "currency")
    private String currency;
    @Column(name = "exchange_rate")
    private Double exchangeRate;
    @Column(name = "limit_amt_ic")
    private BigDecimal limitAmountIC;
    @Column(name = "limit_amt_lc")
    private BigDecimal limitAmountLC;

    @Column(name = "remarks")
    private String remarks;
    @Column(name = "system_remarks")
    private String systemRemarks;
    @Column(name = "sort_seq")
    private Integer sortSequence;
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

}
