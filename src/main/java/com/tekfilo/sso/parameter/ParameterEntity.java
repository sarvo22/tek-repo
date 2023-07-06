package com.tekfilo.sso.parameter;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_global_parameter")
public class ParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parameter_generator")
    @SequenceGenerator(name = "parameter_generator", sequenceName = "tbl_global_parameter_seq", allocationSize = 1)
    @Column(name = "parameter_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "parameter_code")
    private String parameterCode;

    @Column(name = "parameter_name")
    private String parameterName;

    @Column(name = "parameter_group")
    private String parameterGroup;

    @Column(name = "sort_seq")
    private Integer sequence;


    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "system_remarks")
    private String systemRemarks;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;


}
