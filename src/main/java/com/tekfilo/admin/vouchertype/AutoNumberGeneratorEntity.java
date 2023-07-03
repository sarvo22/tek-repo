package com.tekfilo.admin.vouchertype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_auto_number_generator")
public class AutoNumberGeneratorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "autonumber_generator")
    @SequenceGenerator(name = "autonumber_generator", sequenceName = "tbl_auto_number_generator_seq", allocationSize = 1)
    @Column(name = "auto_number_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "voucher_type")
    private String voucherType;

    @Column(name = "voucher_number")
    private String voucherNumber;

    @Column(name = "next_number")
    private Integer nextNumber;

    @Column(name = "current_number")
    private Integer currentNumber;

    @Column(name = "company_id")
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
    private Timestamp createdDt;

    @Column(name = "modified_by", insertable = false, updatable = true)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDt;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
