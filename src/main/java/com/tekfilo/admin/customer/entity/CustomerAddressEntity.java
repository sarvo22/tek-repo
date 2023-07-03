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
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tbl_customer_address")
public class CustomerAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customeraddress_generator")
    @SequenceGenerator(name = "customeraddress_generator", sequenceName = "tbl_customer_address_seq", allocationSize = 1)
    @Column(name = "address_id", updatable = false, insertable = true, nullable = false)
    private Integer id;
    @Column(name = "customer_id", insertable = true, updatable = false)
    private Integer customerId;

    @Column(name = "address_type")
    private String addressType;

    @Column(name = "address1")
    private String address1;
    @Column(name = "address2")
    private String address2;
    @Column(name = "country")
    private String country;
    @Column(name = "state")
    private String state;
    @Column(name = "city")
    private String city;
    @Column(name = "area")
    private String area;
    @Column(name = "street")
    private String street;
    @Column(name = "pin_code")
    private String pinCode;
    @Column(name = "default_address")
    private Integer defaultAddress;

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
