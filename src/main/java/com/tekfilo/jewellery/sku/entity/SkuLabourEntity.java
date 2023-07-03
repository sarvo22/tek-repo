package com.tekfilo.jewellery.sku.entity;

import com.tekfilo.jewellery.configmaster.entity.CollectionEntity;
import com.tekfilo.jewellery.configmaster.entity.MarketEntity;
import com.tekfilo.jewellery.configmaster.entity.ProductTypeEntity;
import com.tekfilo.jewellery.master.CustomerEntity;
import com.tekfilo.jewellery.master.EmployeeEntity;
import com.tekfilo.jewellery.master.FactoryEntity;
import com.tekfilo.jewellery.master.ProcessEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_sku_labour")
public class SkuLabourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skulabour_generator")
    @SequenceGenerator(name = "skulabour_generator", sequenceName = "tbl_sku_labour_seq", allocationSize = 1)
    @Column(name = "sku_labour_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "sku_id", insertable = true,updatable = false)
    private Integer skuId;

    @Column(name = "process_id")
    private Integer processId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "process_id", referencedColumnName = "process_id", insertable = false, updatable = false, nullable = false)
    private ProcessEntity process;

    @Column(name = "description")
    private String description;

    @Column(name = "qty")
    private Double qty;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "amount")
    private Double amount;


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
