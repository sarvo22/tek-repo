package com.tekfilo.sps.ibot.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_api")
public class ApiEntity {


    @Id
    @Column(name = "api_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "api_name")
    private String apiName;

    @Column(name = "description")
    private String description;

    @Column(name = "remarks")
    private String remarks;


    @Column(name = "action_code")
    private String actionCode;

    @Column(name = "api_endpoint")
    private String apiEndpoint;

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
