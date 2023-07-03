package com.tekfilo.admin.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_customer_contact")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customercontact_generator")
    @SequenceGenerator(name = "customercontact_generator", sequenceName = "tbl_customer_contact_seq", allocationSize = 1)
    @Column(name = "contact_id", updatable = false, insertable = true, nullable = false)
    private Integer id;
    @Column(name = "customer_id", updatable = false, insertable = true)
    private Integer customerId;
    @Column(name = "contact_type")
    private String contactType;
    @Column(name = "name")
    private String name;
    @Column(name = "designation")
    private String designation;
    @Column(name = "phone_no")
    private String phoneNo;
    @Column(name = "mobile_no")
    private String mobileNo;
    @Column(name = "email")
    private String email;
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

}

