package com.tekfilo.admin.customerchart;

import com.tekfilo.admin.master.CommodityGroupEntity;
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
@Table(name = "tbl_customer_chart_det")
public class CustomerChartDetailEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerchartdet_generator")
    @SequenceGenerator(name = "customerchartdet_generator", sequenceName = "tbl_customer_chart_det_seq", allocationSize = 1)
    @Column(name = "customer_chart_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "customer_chart_id")
    private Integer customerChartId;

    @Column(name = "commodity_group_id")
    private Integer commodityGroupId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commodity_group_id", insertable = false, updatable = false, nullable = true)
    private CommodityGroupEntity commodityGroup;

    @Column(name = "markup_pct_type")
    private String markupPctType;

    @Column(name = "markup_pct_value")
    private Double markupPctValue;

    @Column(name = "roundoff")
    private Double roundOff;

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
