package com.tekfilo.account.transaction.post;

import com.tekfilo.account.accmaster.AccMasterEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_account_posting_main")
public class PostingMainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postingmain_generator")
    @SequenceGenerator(name = "postingmain_generator", sequenceName = "tbl_account_posting_main_seq", allocationSize = 1)
    @Column(name = "posting_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_type")
    private String invoiceType;

    @Column(name = "inv_id")
    private Integer invId;

    @Column(name = "inv_no")
    private String invoiceNo;

    @Column(name = "inv_dt")
    private Date invoiceDate;

    @Column(name = "inv_due_dt")
    private Date invoiceDueDate;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "note1")
    private String note1;

    @Column(name = "note2")
    private String note2;

    @Column(name = "note3")
    private String note3;

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

    @Transient
    private Integer headerAccountId;


}
