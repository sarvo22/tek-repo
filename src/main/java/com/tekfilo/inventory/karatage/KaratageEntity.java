package com.tekfilo.inventory.karatage;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_karatage")
public class KaratageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "karatage_id_generator")
    @SequenceGenerator(name = "karatage_id_generator", sequenceName = "tbl_karatage_seq", allocationSize = 1)
    @Column(name = "karatage_id", updatable = false, insertable = true, nullable = false)
    private Integer id;


    @Column(name = "karatage_name")
    private String karatageName;
    @Column(name = "impure_to_pure_conv_factor")
    private Double impureToPureConvFactor;
    @Column(name = "pure_to_impure_conv_factor")
    private Double pureToImpureConvFactor;

    @Column(name = "company_id")
    private Integer companyId;

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
