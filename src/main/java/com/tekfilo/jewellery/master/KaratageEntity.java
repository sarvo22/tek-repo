package com.tekfilo.jewellery.master;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Immutable
@Getter
@Setter
@Entity
@Table(name = "tbl_karatage")
public class KaratageEntity {

    @Id
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

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
