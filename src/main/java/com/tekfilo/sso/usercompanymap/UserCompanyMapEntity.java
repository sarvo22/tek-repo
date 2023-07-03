package com.tekfilo.sso.usercompanymap;

import com.tekfilo.sso.company.CompanyEntity;
import com.tekfilo.sso.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_user_company_map")
public class UserCompanyMapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usercompany_generator")
    @SequenceGenerator(name = "usercompany_generator", sequenceName = "tbl_user_company_map_seq", allocationSize = 1)
    @Column(name = "map_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false, nullable = true)
    private UserEntity user;

    @Column(name = "company_id")
    private Integer companyId;

    @OneToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false, nullable = true)
    private CompanyEntity company;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "is_default")
    private Integer isDefault;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "system_remarks")
    private String systemRemarks;

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
