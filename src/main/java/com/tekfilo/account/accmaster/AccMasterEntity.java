package com.tekfilo.account.accmaster;

import com.tekfilo.account.subgroup.SubGroupEntity;
import lombok.Data;
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
@Table(name = "tbl_account")
public class AccMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    @SequenceGenerator(name = "account_generator", sequenceName = "tbl_account_seq", allocationSize = 1)
    @Column(name = "account_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "account_code")
    private String accountCode;


    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "is_master_account")
    private Integer isMasterAccount;

    @Column(name = "subgroup_id")
    private Integer subgroupId;

    @OneToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "subgroup_id", referencedColumnName = "subgroup_id", insertable = false, updatable = false, nullable = true)
    private SubGroupEntity subGroup;

    @Column(name = "ifrs_subgroup_id")
    private Integer ifrsSubgroupId;

    @Column(name = "account_nature")
    private String accountNature;

    @Column(name = "dr_cr")
    private String debitCreditType;


    @Column(name = "account_category")
    private String accountCategory;

    @Column(name = "description")
    private String description;


    @Column(name = "is_cost_center")
    private Integer costCenterApplicable;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_no")
    private String bankAccountNo;

    @Column(name = "bank_ifsc_code")
    private String bankIfscCode;

    @Column(name = "bank_swift_code")
    private String bankSwiftCode;

    @Column(name = "bank_payee_name")
    private String bankPayeeName;

    @Column(name = "bank_account_type")
    private String bankAccountType;

    @Column(name = "bank_address")
    private String bankAddress;

    @Column(name = "party_type")
    private String partyType;

    @Column(name = "party_id")
    private Integer partyId;

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
