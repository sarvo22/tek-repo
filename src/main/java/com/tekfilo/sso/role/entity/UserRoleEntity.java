package com.tekfilo.sso.role.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_user_roles")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userrole_generator")
    @SequenceGenerator(name = "userrole_generator", sequenceName = "tbl_user_roles_seq", allocationSize = 1)
    @Column(name = "role_map_id", updatable = false, insertable = true, nullable = false)
    private Integer roleMapId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private RoleEntity role;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "remarks")
    private Integer remarks;

    @Column(name = "system_remarks")
    private Integer systemRemarks;

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
