package com.tekfilo.sso.report;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_reports")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_generator")
    @SequenceGenerator(name = "report_generator", sequenceName = "tbl_reports_seq", allocationSize = 1)
    @Column(name = "report_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "subscription_id")
    private Integer subscriptionId;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "report_group")
    private String reportGroup;

    @Column(name = "report_category")
    private String reportCategory;

    @Column(name = "is_enabled")
    private Integer isEnabled;

    @Column(name = "group_sort_seq")
    private Integer groupSequence;

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
