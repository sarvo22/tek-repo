package com.tekfilo.admin.factorychart;

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
@Table(name = "tbl_factory_chart_det")
public class FactoryChartDetailEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factorychartdet_generator")
    @SequenceGenerator(name = "factorychartdet_generator", sequenceName = "tbl_factory_chart_det_seq", allocationSize = 1)
    @Column(name = "factory_chart_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "factory_chart_id")
    private Integer factoryChartId;

    @Column(name = "labour_type")
    private String labourType;

    @Column(name = "amount_pct_type")
    private String amountPctType;

    @Column(name = "amount_pct_value")
    private Double amountPctValue;

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
