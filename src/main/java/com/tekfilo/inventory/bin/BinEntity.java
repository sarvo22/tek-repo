package com.tekfilo.inventory.bin;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_bin")
public class BinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bin_generator")
    @SequenceGenerator(name = "bin_generator", sequenceName = "tbl_bin_seq", allocationSize = 1)
    @Column(name = "bin_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "bin_name")
    private String binName;


    @Column(name = "bin_type")
    private String binType;

    @Column(name = "is_default")
    private Integer isDefault;

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
