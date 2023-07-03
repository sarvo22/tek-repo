package com.tekfilo.admin.employee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto {

    private Integer id;
    private String code;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nickName;
    private String contractType;
    private Integer departmentId;
    private String designation;
    private String gender;
    private String maritalStatus;
    private String nationality;
    private String identityType1;
    private String identityNo1;
    private String identityType2;
    private String identityNo2;
    private String identityType3;
    private String identityNo3;
    private String perAddress1;
    private String perAddress2;
    private Integer perCountry;
    private String perState;
    private String perCity;
    private String perPinCode;
    private String curAddress1;
    private String curAddress2;
    private Integer curCountry;
    private String curState;
    private String curCity;
    private String curPinCode;
    private Date doj;
    private Date dob;
    private Date resignDt;
    private String email;
    private String picName;
    private Integer sequence;
    private Integer isLocked;
    private Integer isDeleted;
    private Integer operateBy;
    private String dateFormat;
}
