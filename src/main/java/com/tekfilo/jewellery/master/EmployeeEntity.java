package com.tekfilo.jewellery.master;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Getter
@Setter
@Table(name = "tbl_employee")
@Entity
public class EmployeeEntity {

    @Id
    @Column(name = "employee_id", updatable = false, insertable = false, nullable = false)
    private Integer id;


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nick_name")
    private String nickName;

}
