package com.tekfilo.sso.inquiry;

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
@Table(name = "tbl_inquery")
public class InqueryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inquery_generator")
    @SequenceGenerator(name = "inquery_generator", sequenceName = "tbl_inquery_seq", allocationSize = 1)
    @Column(name = "inquery_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "email_id")
    private String email_id;

    @Column(name = "message")
    private String message;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
