package com.tekfilo.sso.auth;

import com.tekfilo.sso.base.SSOResponse;
import com.tekfilo.sso.company.CompanyEntity;
import com.tekfilo.sso.company.CompanyService;
import com.tekfilo.sso.country.CountryEntity;
import com.tekfilo.sso.country.CountryService;
import com.tekfilo.sso.country.DropDownDto;
import com.tekfilo.sso.inquiry.InqueryService;
import com.tekfilo.sso.inquiry.InquiryDto;
import com.tekfilo.sso.parameter.ParameterEntity;
import com.tekfilo.sso.parameter.ParameterService;
import com.tekfilo.sso.registration.RegistrationEntity;
import com.tekfilo.sso.registration.RegistrationForm;
import com.tekfilo.sso.registration.RegistrationService;
import com.tekfilo.sso.role.service.UserRoleService;
import com.tekfilo.sso.state.StateEntity;
import com.tekfilo.sso.state.StateService;
import com.tekfilo.sso.tenantdbconfig.TenantDBConfigService;
import com.tekfilo.sso.user.dto.UserDto;
import com.tekfilo.sso.user.entity.UserEntity;
import com.tekfilo.sso.user.service.UserService;
import com.tekfilo.sso.util.JwtUtil;
import com.tekfilo.sso.util.SSOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/sso")
public class AuthController {


    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    CountryService countryService;

    @Autowired
    CompanyService companyService;

    @Autowired
    StateService stateService;

    @Autowired
    ParameterService parameterService;

    @Autowired
    TenantDBConfigService tenantDBConfigService;

    @Autowired
    InqueryService inqueryService;

    @Autowired
    UserRoleService userRoleService;
    String SIGNUP_VERIFICATION_SUCCESS = "Signup Process verified successfully, You can able to login with Selected Credentials";
    String SIGNUP_VERIFICATION_FAILED = "Signup Process verified not successful, Found more than one Signup with same email address ";
    @Value("${spring.profiles.active}")
    private String profile;
    @Value("${tekfilo.email.service.verification-url}")
    private String verificationHostUrl;

    @GetMapping
    public String entrypoint() throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("Service entry point working");
        return "SSO Service up and Running on " + profile + " mode.";
    }


    @GetMapping("/getcountries")
    public ResponseEntity<List<DropDownDto>> findAllCountries() {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        List<CountryEntity> countryEntities = countryService.findAllCountries();
        countryEntities.stream().forEachOrdered(e -> {
            dropDownDtoList.add(new DropDownDto(e.getId().toString(), e.getCountryName()));
        });
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/getstates/{countryid}")
    public ResponseEntity<List<DropDownDto>> findAllStatesByCountry(@PathVariable("countryid") Integer countryId) {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        List<StateEntity> stateEntities = stateService.findAllStates(countryId);
        stateEntities.stream().forEachOrdered(e -> {
            dropDownDtoList.add(new DropDownDto(e.getStateCode(), e.getStateName()));
        });
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/getbusinesstypes")
    public ResponseEntity<List<DropDownDto>> findAllBusinessTypes() {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        List<ParameterEntity> stateEntities = parameterService.getBusinessTypeList();
        stateEntities.stream().forEachOrdered(e -> {
            dropDownDtoList.add(new DropDownDto(e.getParameterCode(), e.getParameterName()));
        });
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/getfiscalyear")
    public ResponseEntity<List<DropDownDto>> findFiscalYear() {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        List<ParameterEntity> stateEntities = parameterService.getFiscalYearList();
        stateEntities.stream().forEachOrdered(e -> {
            dropDownDtoList.add(new DropDownDto(e.getParameterCode(), e.getParameterName()));
        });
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/gettimezone")
    public ResponseEntity<List<DropDownDto>> findTimeZone() {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        List<ParameterEntity> stateEntities = parameterService.getTimeZoneList();
        stateEntities.stream().forEachOrdered(e -> {
            dropDownDtoList.add(new DropDownDto(e.getParameterCode(), e.getParameterName()));
        });
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/getdatetimeformat")
    public ResponseEntity<List<DropDownDto>> getDateTimeFormat() {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        List<ParameterEntity> stateEntities = parameterService.getDateTimeFormat();
        stateEntities.stream().forEachOrdered(e -> {
            dropDownDtoList.add(new DropDownDto(e.getParameterCode(), e.getParameterName()));
        });
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/getdatetimedivider")
    public ResponseEntity<List<DropDownDto>> getDateTimeDivider() {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        List<ParameterEntity> stateEntities = parameterService.getDateTimeDivider();
        stateEntities.stream().forEachOrdered(e -> {
            dropDownDtoList.add(new DropDownDto(e.getParameterCode(), e.getParameterName()));
        });
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/companyregistertype")
    public ResponseEntity<List<DropDownDto>> getCompanyRegisterTypes() {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        List<ParameterEntity> stateEntities = parameterService.getCompanyRegisterTypes();
        stateEntities.stream().forEachOrdered(e -> {
            dropDownDtoList.add(new DropDownDto(e.getParameterCode(), e.getParameterName()));
        });
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/companytaxtype")
    public ResponseEntity<List<DropDownDto>> getCompanyTaxTypes() {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        List<ParameterEntity> stateEntities = parameterService.getCompanyTaxTypes();
        stateEntities.stream().forEachOrdered(e -> {
            dropDownDtoList.add(new DropDownDto(e.getParameterCode(), e.getParameterName()));
        });
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    /**
     * Save the Signup form
     *
     * @param registrationForm
     * @return
     */

    @PostMapping("/signup")
    public ResponseEntity<SSOResponse> save(@RequestBody RegistrationForm registrationForm) {
        SSOResponse response = new SSOResponse();
        try {
            log.info("Signup process initiated");
            if (registrationForm.getSubscriptionId() == null) {
                response.setStatus(101);
                response.setMessage("Subscription not found for this registration, choose product for signup");
                return new ResponseEntity<SSOResponse>(response, HttpStatus.OK);
            }
            registrationForm.setTokenVerificationCode(jwtUtil.generate(registrationForm.getEmail()));
            RegistrationEntity registrationEntity = registrationService.createSignUpProcess(registrationForm);
            //RegistrationEntity registrationEntity = new RegistrationEntity();
            //registrationEntity.setSignupId(61);
            //registrationEntity.setTokenVerificationCode("eyJhbGciOiJIUzUxMiJ9.eyJpZCI6InNpdmFyYWouZGhhcnVtYW5AdGVrZmlsby5jb20iLCJzdWIiOiJzaXZhcmFqLmRoYXJ1bWFuQHRla2ZpbG8uY29tIiwiaWF0IjoxNjUyODcxODY5LCJleHAiOjE2NTI5NTgyNjl9.gu2vr_0g5Hv6bX2cDhxvkNqQSxI26L04MeZRB4umq1v3IYwQu-ob0-QCnLb2mXiGiVCT3nCySvAB3RmYVcT6Gg");
            response.setStatus(100);
            response.setMessage("Signup Completed Successfully, Please check your mail ID for verification");
            response.setEmail(registrationForm.getEmail());
            response.setUrl(SSOUtil.getSignupVerificationUrl(verificationHostUrl, registrationEntity.getSignupId(), registrationEntity.getTokenVerificationCode()));
            response.setLangStatus("signup_100");
            log.info("Signup Process completed , task pass over for sending email service");
        } catch (Exception ex) {
            response.setStatus(101);
            response.setMessage(ex.getCause().getMessage());
            log.error(ex.getMessage());
            return new ResponseEntity<SSOResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<SSOResponse>(response, HttpStatus.OK);
    }


    /**
     * Verify the signup token generated
     *
     * @param token
     * @return
     */
    @GetMapping("/signup/verify/{signupid}/{token}")
    public ResponseEntity<AuthResponse> verifySignupToken(
            @PathVariable("signupid") String signupId,
            @PathVariable("token") String token) {

        AuthResponse response = new AuthResponse();
        try {
            response.setStatus(100);
            if (token == null) {
                response.setMessage("Unable to verify the Signup Process, please re-try");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if (token != null) {
                if (jwtUtil.signupTokenValidation(token)) {
                    Integer count = registrationService.updateTokenStatus(signupId);
                    switch (count) {
                        case 101:
                        case 102:
                        case 103:
                            response.setMessage(SIGNUP_VERIFICATION_FAILED);
                            break;
                        case 104:
                            response.setMessage("Signup Process already verified");
                            break;
                        default:
                            response.setMessage(SIGNUP_VERIFICATION_SUCCESS);
                            break;
                    }
                } else {
                    response.setMessage("Unable to verify your Signup, expired Signup link Request for new one");
                }
            }
        } catch (Exception exception) {
            response.setStatus(101);
            response.setMessage("Technical exception raised while doing verification {} " + exception.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getsignupdetails/{id}")
    public ResponseEntity<RegistrationEntity> getSignupDetails(@PathVariable("id") Integer id) {
        return new ResponseEntity<RegistrationEntity>(registrationService.findById(id).get(), HttpStatus.OK);
    }

    /**
     * Login for Client
     *
     * @param form
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthForm form) {
        final String providedPassword = form.getPassword();
        form.setPassword(SSOUtil.decode(providedPassword));
        int status = userService.validateUser(form);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setStatus(status);
        authResponse.setLangStatus("login_" + status);
        switch (status) {
            case 101:
                authResponse.setMessage("Username not found or Username not matching");
                break;
            case 102:
                authResponse.setMessage("Password not matching");
                break;
            case 110:
                authResponse.setMessage("User is Deactivated");
                break;
            case 100:
                try {
                    String accessToken = jwtUtil.generate(form, "ACCESS");
                    authResponse.setMessage("Authentication Successful");
                    authResponse.setAccessToken(accessToken);
                    UserEntity userEntity = userService.findUserByName(form.getUsername());
                    final Integer userId = userEntity.getUserId();
                    UserDto userDto = new UserDto();
                    userDto.setFirstName(userEntity.getFirstName());
                    userDto.setLastName(userEntity.getLastName());
                    userDto.setContactNo(userEntity.getContactNo());
                    userDto.setUsername(userEntity.getUserName());
                    userDto.setEmail(userEntity.getEmail());
                    userDto.setId(userId);
                    userDto.setAvatar(userEntity.getImagePath());
                    List<String> roles = userService.findUserRolesByUserId(userId);
                    userDto.setRoles(roles.toArray(new String[0]));
                    userDto.setUserCompanies(userService.findAllUserMappedCompanies(userId));
                    userDto.setLoggedInCompany(userService.findDefaultCompany(userId));
                    authResponse.setData(userDto);
                    authResponse.setXTenantId(tenantDBConfigService.getTenantUIDByUserAndCompanyId(userEntity.getUserId(), userDto.getLoggedInCompany().getId()));
                } catch (Exception exception) {
                    log.error(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
                    authResponse.setStatus(101);
                    authResponse.setLangStatus("login_101");
                    authResponse.setAccessToken(null);
                    authResponse.setData(new UserDto());
                    authResponse.setMessage("Unable to get Tenant UID, Please contact admin@tekfilo.com");
                }
                break;
            default:
                break;
        }
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    @GetMapping("/switchcompany/{userid}/{companyid}")
    public ResponseEntity<SwitchCompanyResponse> switchCompanyGetTenant(@PathVariable("userid") Integer userId,
                                                                        @PathVariable("companyid") Integer companyId) {
        SwitchCompanyResponse switchCompanyResponse = new SwitchCompanyResponse();
        try {
            final String xTenantID = tenantDBConfigService.getTenantUIDByUserAndCompanyId(userId, companyId);
            switchCompanyResponse.setStatus(100);
            switchCompanyResponse.setXtenantId(xTenantID);
            switchCompanyResponse.setMessage("Company Changed");
        } catch (Exception exception) {
            switchCompanyResponse.setStatus(101);
            switchCompanyResponse.setXtenantId(null);
            switchCompanyResponse.setMessage("Exception raised while switching company {} " + exception.getMessage());
        }
        return new ResponseEntity<SwitchCompanyResponse>(switchCompanyResponse, HttpStatus.OK);
    }

    @PostMapping("/emailcheck")
    public ResponseEntity<?> emailCheck(@RequestBody Map<String, Object> inputData) {
        String email = (String) inputData.get("email");
        log.info("Checking validation for email : " + email);
        Boolean aBoolean = registrationService.checkEmailExistOrNot(email);
        return ResponseEntity.status(HttpStatus.OK).body(aBoolean);
    }

    @PostMapping("/usercheck")
    public ResponseEntity<?> userCheck(@RequestBody Map<String, Object> inputData) {
        String username = (String) inputData.get("username");
        log.info("Checking validation for username : " + username);
        Boolean aBoolean = userService.checkUserExistOrNot(username);
        return ResponseEntity.status(HttpStatus.OK).body(aBoolean);
    }

    @GetMapping("/companybyid/{companyid}")
    public ResponseEntity<CompanyEntity> getCompanyById(@PathVariable("companyid") Integer companyId) {
        return new ResponseEntity<CompanyEntity>(companyService.findById(companyId), HttpStatus.OK);
    }

    @PostMapping("/confirminvitation")
    public ResponseEntity<SSOResponse> acceptInvitation(@RequestBody Map<String, Object> data) {
        SSOResponse ssoResponse = new SSOResponse();
        final String url = (String) data.get("url");
        final String urlString = SSOUtil.decode(url);

        if (!Optional.ofNullable(urlString).isPresent()) {
            ssoResponse.setStatus(101);
            ssoResponse.setMessage("URL not found, Unable to confirm Invitation");
            return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
        }
        final String[] formData = urlString.split(SSOUtil.SPLITTER);
        if (formData.length > 0) {
            InviteForm inviteForm = new InviteForm();
            inviteForm.setEmail(formData[0]);
            inviteForm.setSelectedRoleId(formData[1]);
            inviteForm.setSenderEmail(formData[2]);
            inviteForm.setSenderName(formData[3]);
            inviteForm.setSenderCompanyId(Integer.parseInt(formData[4]));
            inviteForm.setSenderCompanyName(formData[5]);
            inviteForm.setUsername(inviteForm.getEmail());
            inviteForm.setPassword((String) data.get("password"));
            try {
                userService.createInviteUserProcess(inviteForm);
            } catch (Exception ex) {
                ssoResponse.setStatus(101);
                ssoResponse.setMessage("Exception raised while Invite confirm process {}" + ex.getMessage());
                return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
            }
        }
        ssoResponse.setStatus(100);
        ssoResponse.setMessage("User Confirmed");
        log.info("Received Confirmation Invitation Object {} ");
        return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
    }

    @PostMapping("/inquiry")
    public ResponseEntity<SSOResponse> inquiry(@RequestBody InquiryDto enquiryDto) {
        SSOResponse response = new SSOResponse();
        try {
            this.inqueryService.saveInquery(enquiryDto);

            this.inqueryService.sendEmail(enquiryDto);

            response.setStatus(100);
            response.setMessage("Success");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setMessage(exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(response, HttpStatus.OK);
    }
}
