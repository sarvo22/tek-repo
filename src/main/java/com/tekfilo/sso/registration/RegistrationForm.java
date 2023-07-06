package com.tekfilo.sso.registration;

import com.tekfilo.sso.base.BaseForm;
import lombok.Data;

@Data
public class RegistrationForm extends BaseForm {
    private Integer signupId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Integer countryId;
    private String companyName;
    private String mobileNo;
    private String tokenVerificationCode;
    private String jobTitle;
    private String employeeCount;
    private Integer subscriptionId;
}
