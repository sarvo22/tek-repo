package com.tekfilo.inventory.master;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "tbl_employee")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Immutable
public class EmployeeEntity implements Serializable {

    @Id
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

}
