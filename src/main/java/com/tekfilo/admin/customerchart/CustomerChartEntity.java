package com.tekfilo.admin.customerchart;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_customer_chart")
public class CustomerChartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerchart_generator")
    @SequenceGenerator(name = "customerchart_generator", sequenceName = "tbl_customer_chart_seq", allocationSize = 1)
    @Column(name = "customer_chart_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "company_id")
    private Integer companyId;

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
