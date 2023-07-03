package com.tekfilo.admin.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tbl_roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_generator")
    @SequenceGenerator(name = "role_generator", sequenceName = "tbl_roles_seq", allocationSize = 1)
    @Column(name = "role_id", updatable = false, insertable = true, nullable = false)
    private Integer roleId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "system_remarks")
    private String systemRemarks;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_dt")
    private Timestamp createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_dt")
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
