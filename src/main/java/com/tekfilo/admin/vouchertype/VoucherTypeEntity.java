package com.tekfilo.admin.vouchertype;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_voucher_type")
public class VoucherTypeEntity {

    @Id
    @Column(name = "voucher_type", updatable = false, insertable = true, nullable = false)
    private String voucherType;

    @Column(name = "voucher_name", updatable = false, insertable = true, nullable = false)
    private String voucherName;

    @Column(name = "voucher_group", updatable = false, insertable = true, nullable = false)
    private String voucherGroup;

    @Column(name = "company_id", insertable = true, updatable = false)
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

    @Transient
    private AutoNumberGeneratorEntity autoNumberGenerator;

}
