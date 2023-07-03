package com.tekfilo.admin.employee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tekfilo.admin.department.DepartmentEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Table(name = "tbl_employee")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
public class EmployeeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_generator")
    @SequenceGenerator(name = "employee_generator", sequenceName = "tbl_employee_seq", allocationSize = 1)
    @Column(name = "employee_id", updatable = false, insertable = true, nullable = false)
    private Integer id;


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "contract_type")
    private String contractType;
    @Column(name = "department_id")
    private Integer departmentId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id",
            referencedColumnName = "department_id",
            nullable = true, insertable = false, updatable = false)
    private DepartmentEntity department;

    @Column(name = "designation")
    private String designation;
    @Column(name = "gender")
    private String gender;
    @Column(name = "marital_status")
    private String maritalStatus;
    @Column(name = "nationality")
    private String nationality;
    @Column(name = "identity_type1")
    private String identityType1;
    @Column(name = "identity_no1")
    private String identityNo1;
    @Column(name = "identity_type2")
    private String identityType2;
    @Column(name = "identity_no2")
    private String identityNo2;
    @Column(name = "identity_type3")
    private String identityType3;
    @Column(name = "identity_no3")
    private String identityNo3;
    @Column(name = "per_address1")
    private String perAddress1;
    @Column(name = "per_address2")
    private String perAddress2;
    @Column(name = "per_country")
    private Integer perCountry;
    @Column(name = "per_state")
    private String perState;
    @Column(name = "per_city")
    private String perCity;

    @Column(name = "per_pin_code")
    private String perPinCode;


    @Column(name = "cur_address1")
    private String curAddress1;
    @Column(name = "cur_address2")
    private String curAddress2;
    @Column(name = "cur_country")
    private Integer curCountry;
    @Column(name = "cur_state")
    private String curState;
    @Column(name = "cur_city")
    private String curCity;

    @Column(name = "cur_pin_code")
    private String curPinCode;


    @Column(name = "doj")
    private Date doj;
    @Column(name = "dob")
    private Date dob;
    @Column(name = "resign_dt")
    private Date resignDt;
    @Column(name = "email")
    private String email;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "sort_seq")
    private Integer sequence;
    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "picture_name")
    private String picName;

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
