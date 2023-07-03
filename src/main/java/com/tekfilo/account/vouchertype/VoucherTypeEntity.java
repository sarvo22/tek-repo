package com.tekfilo.account.vouchertype;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_voucher_type")
public class VoucherTypeEntity {

    @Id
    @Column(name = "voucher_type", updatable = false, insertable = false, nullable = false)
    private String voucherType;

    @Column(name = "voucher_group")
    private String voucherGroup;

    @Column(name = "company_id")
    private Integer companyId;

     @Column(name = "is_deleted")
    private Integer isDeleted;

}
