package com.tekfilo.admin.metalrate;

import com.tekfilo.admin.master.CommodityEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_metal_rate")
public class MetalRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metalrate_generator")
    @SequenceGenerator(name = "metalrate_generator", sequenceName = "tbl_metal_rate_seq", allocationSize = 1)
    @Column(name = "metal_rate_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "metal_rate_dt")
    private Date metalRateDt;

    @Column(name = "commodity_id")
    private Integer commodityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "commodity_id", referencedColumnName = "commodity_id", insertable = false, updatable = false)
    private CommodityEntity commodity;

    @Column(name = "uom")
    private String uom;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "input_rate")
    private Double inputRate;

    @Column(name = "company_id", insertable = true, updatable = false)
    private Integer companyId;

    @Column(name = "remarks")
    private String remarks;
    @Column(name = "system_remarks")
    private String systemRemarks;
    @Column(name = "sort_seq")
    private Integer sequence;
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
