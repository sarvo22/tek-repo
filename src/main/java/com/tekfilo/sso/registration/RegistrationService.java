package com.tekfilo.sso.registration;

import com.tekfilo.sso.company.CompanyEntity;
import com.tekfilo.sso.company.CompanyRepository;
import com.tekfilo.sso.country.CountryEntity;
import com.tekfilo.sso.country.CountryRepository;
import com.tekfilo.sso.database.DatabaseService;
import com.tekfilo.sso.plan.entity.CompanySubscription;
import com.tekfilo.sso.plan.entity.Subscription;
import com.tekfilo.sso.plan.entity.SubscriptionPrivilege;
import com.tekfilo.sso.plan.repository.CompanySubscriptionRepository;
import com.tekfilo.sso.plan.repository.SubscriptionPrivilegeRepository;
import com.tekfilo.sso.plan.repository.SubscriptionRepository;
import com.tekfilo.sso.role.entity.RoleEntity;
import com.tekfilo.sso.role.entity.RolePrivilegeEntity;
import com.tekfilo.sso.role.entity.UserRoleEntity;
import com.tekfilo.sso.role.repository.RolePrivilegeRepository;
import com.tekfilo.sso.role.repository.RoleRepository;
import com.tekfilo.sso.role.repository.UserRoleRepository;
import com.tekfilo.sso.tenantdbconfig.TenantDBConfigEntity;
import com.tekfilo.sso.tenantdbconfig.TenantDBConfigRepository;
import com.tekfilo.sso.user.entity.UserEntity;
import com.tekfilo.sso.user.entity.UserSubMenus;
import com.tekfilo.sso.user.repository.UserRepository;
import com.tekfilo.sso.usercompanymap.UserCompanyMapEntity;
import com.tekfilo.sso.usercompanymap.UserCompanyMapRepository;
import com.tekfilo.sso.util.SSOUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class RegistrationService {

    private static final String DEFAULT_SUPER_ADMIN_ROLE = "SuperAdmin";
    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    UserCompanyMapRepository userCompanyMapRepository;

    @Autowired
    TenantDBConfigRepository tenantDBConfigRepository;

    @Autowired
    DatabaseService databaseService;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    CompanySubscriptionRepository companySubscriptionRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    SubscriptionPrivilegeRepository subscriptionPrivilegeRepository;

    @Autowired
    RolePrivilegeRepository rolePrivilegeRepository;

    /**
     * Complete Registration Process goes here
     * 1. Signup
     * 2. Create User
     * 3. Create Company
     * 4. Create User Company
     * 5. Create User Role Mapping
     * 6. Create User Company Mapping
     * 7. Send verification email
     */
    public RegistrationEntity createSignUpProcess(RegistrationForm registrationForm) throws Exception {
        log.info("Signing up process - Creating Registration process");
        RegistrationEntity registrationEntity = registrationRepository.save(convertDtoEntity(registrationForm));
        log.info("Registration Completed moving to Next ==> Creating User");
        UserEntity userEntity = userRepository.save(createUserEntity(registrationEntity));
        log.info("User Creation Completed moving to Next ==> Creating Company");
        CompanyEntity companyEntity = companyRepository.save(createNewCompany(registrationEntity, userEntity.getUserId()));
        log.info("Company Creation Completed moving to Next ==> User Default Admin Role Mapping");
        RoleEntity defaultCompanyRole = roleRepository.save(createDefaultRoleOfCompany(userEntity.getUserId(), companyEntity.getId(), registrationForm.getSubscriptionId()));
        userRoleRepository.save(createNewUserRoleMapping(defaultCompanyRole.getRoleId(), userEntity.getUserId(), companyEntity.getId()));
        log.info("Role Mapping Completed moving to Next ==> User Company Mapping");
        userCompanyMapRepository.save(createNewUserCompanyMapping(userEntity.getUserId(), companyEntity.getId()));
        log.info("User Company Mapping Completed moving to Next ==> User Tenant Mapping");
        tenantDBConfigRepository.save(createNewTenantDBConfig(userEntity.getUserId(), userEntity.getUID(), companyEntity.getId()));
        //log.info("All registration process completed moving to Next ==> Execution of Schema Creation and table creation");
        //databaseService.initTenant(userEntity.getUserId(), userEntity.getUID());
        log.info("Create Company Subscription Mapping");
        companySubscriptionRepository.save(createCompanySubscription(companyEntity.getId(), registrationForm.getSubscriptionId()));
        log.info("Copy Subscription Privilege to Role Privilege");
        rolePrivilegeRepository.saveAll(getRolePrivilegeList(defaultCompanyRole.getRoleId(), registrationForm.getSubscriptionId(),userEntity.getUserId()));
        return registrationEntity;
    }

    private List<RolePrivilegeEntity> getRolePrivilegeList(Integer roleId,Integer subscriptionId, Integer userId) throws DataIntegrityViolationException{
        List<SubscriptionPrivilege> subscriptionPrivilegeList = subscriptionPrivilegeRepository.findAllBySubscriptionPrivilegeBySubscription(subscriptionId);
        if(subscriptionPrivilegeList == null)
            throw new RuntimeException("Subscription do not have any modules mapping");
        if(subscriptionPrivilegeList.size() == 0)
            throw new RuntimeException("Subscription do not have any modules mapping");
        List<RolePrivilegeEntity> rolePrivilegeEntities = new ArrayList<>();
        subscriptionPrivilegeList.stream().forEach(e->{
            RolePrivilegeEntity rolePrivilegeEntity = new RolePrivilegeEntity();
            rolePrivilegeEntity.setRoleId(roleId);
            rolePrivilegeEntity.setPrivilegeId(e.getPrivilegeId());
            rolePrivilegeEntity.setPrivilegeName(e.getPrivilegeName());
            rolePrivilegeEntity.setPrivilegeGroup(e.getPrivilegeGroup());
            rolePrivilegeEntity.setMenuId(e.getMenuId());
            rolePrivilegeEntity.setFullaccess(e.getFullaccess() == null ? false : e.getFullaccess());
            rolePrivilegeEntity.setView(e.getView() == null ? false : e.getView());
            rolePrivilegeEntity.setAdd(e.getAdd() == null ? false : e.getAdd());
            rolePrivilegeEntity.setEdit(e.getEdit() == null ? false : e.getEdit());
            rolePrivilegeEntity.setDelete(e.getDelete() == null ? false : e.getDelete());
            rolePrivilegeEntity.setPrint(e.getPrint() == null ? false : e.getPrint());
            rolePrivilegeEntity.setShare(e.getShare() == null ? false : e.getShare());
            rolePrivilegeEntity.setEmail(e.getEmail() == null ? false : e.getEmail());
            rolePrivilegeEntity.setDetailview(e.getDetailview() == null ? false : e.getDetailview());
            rolePrivilegeEntity.setDetailadd(e.getDetailadd() == null ? false : e.getDetailadd());
            rolePrivilegeEntity.setDetaildelete(e.getDetaildelete() == null ? false : e.getDetaildelete());
            rolePrivilegeEntity.setMainlock(e.getMainlock() ==null ? false : e.getMainlock());
            rolePrivilegeEntity.setMainunlock(e.getMainunlock() == null ? false : e.getMainunlock());
            rolePrivilegeEntity.setDetaillock(e.getDetaillock() == null ? false : e.getDetaillock());
            rolePrivilegeEntity.setDetailunlock(e.getDetailunlock() == null ? false : e.getDetailunlock());
            rolePrivilegeEntity.setQuickpayment(e.getQuickpayment() == null ? false : e.getQuickpayment());
            rolePrivilegeEntity.setChangestatus(e.getChangestatus() == null ? false : e.getChangestatus());
            rolePrivilegeEntity.setUpload(e.getUpload() == null ? false : e.getUpload());
            rolePrivilegeEntity.setAddcharges(e.getAddcharges() == null ? false : e.getAddcharges());
            rolePrivilegeEntity.setDeletecharges(e.getDeletecharges() == null ? false : e.getDeletecharges());
            rolePrivilegeEntity.setPostaccount(e.getPostaccount() == null ? false : e.getPostaccount());
            rolePrivilegeEntity.setSubmit(e.getSubmit() == null ? false : e.getSubmit());
            rolePrivilegeEntity.setRemarks("Default mapping from subscription");
            rolePrivilegeEntity.setCreatedBy(userId);
            rolePrivilegeEntity.setIsDeleted(0);
            rolePrivilegeEntities.add(rolePrivilegeEntity);
        });
        return rolePrivilegeEntities;
    }

    private CompanySubscription createCompanySubscription(Integer companyId, Integer subscriptionId) throws DataIntegrityViolationException {
        CompanySubscription companySubscription = new CompanySubscription();
        companySubscription.setCompanyId(companyId);
        companySubscription.setSubscriptionId(subscriptionId);
        companySubscription.setRemarks("Signup process mapped subscription against company");
        companySubscription.setIsDeleted(0);
        return companySubscription;
    }

    public void createUserInvitationProcess() {
        log.info("Make Registration Process via Invite, Tekfilo should have signup process gateway for getting user inside");

    }

    private RoleEntity createDefaultRoleOfCompany(Integer userId, Integer newCompanyId, Integer subscriptionId) throws DataIntegrityViolationException{
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(()->new RuntimeException("Unable to find Subscription Details for " + subscriptionId));
        RoleEntity defaultRole = new RoleEntity();
        defaultRole.setRoleName(subscription.getSubscriptionName().concat(" ").concat(DEFAULT_SUPER_ADMIN_ROLE));
        defaultRole.setSubscriptionId(subscriptionId);
        defaultRole.setCompanyId(newCompanyId);
        defaultRole.setCreatedBy(userId);
        defaultRole.setIsDeleted(0);
        return defaultRole;
    }

    private TenantDBConfigEntity createNewTenantDBConfig(Integer userId, String uid, Integer companyId) {
        TenantDBConfigEntity entity = new TenantDBConfigEntity();
        entity.setUserUID(uid.toLowerCase());
        entity.setTenantUID(uid.toLowerCase());
        entity.setUserId(userId);
        entity.setCompanyId(companyId);
        entity.setCreatedBy(userId);
        entity.setIsDeleted(0);
        entity.setIsLocked(1);
        return entity;
    }

    private UserCompanyMapEntity createNewUserCompanyMapping(Integer userId, Integer companyId) {
        UserCompanyMapEntity entity = new UserCompanyMapEntity();
        entity.setUserId(userId);
        entity.setCompanyId(companyId);
        entity.setIsActive(1);
        entity.setIsDefault(1);
        entity.setCreatedBy(userId);
        entity.setIsDeleted(0);
        return entity;
    }

    private UserRoleEntity createNewUserRoleMapping(Integer roleId, Integer userId, Integer companyId) {
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setRoleId(roleId);
        userRoleEntity.setUserId(userId);
        userRoleEntity.setCompanyId(companyId);
        userRoleEntity.setCreatedBy(userId);
        userRoleEntity.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        userRoleEntity.setIsDeleted(0);
        return userRoleEntity;
    }

    private CompanyEntity createNewCompany(RegistrationEntity registrationEntity, Integer userId) throws Exception {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setCompanyName(registrationEntity.getCompanyName());
        companyEntity.setDisplayName(registrationEntity.getCompanyName());
        companyEntity.setCompanyUID(SSOUtil.generateUniqueID());
        companyEntity.setCountryId(registrationEntity.getCountryId());
        CountryEntity country = getDefaultCurrency(registrationEntity.getCountryId());
        companyEntity.setDefaultCurrency(country.getDefaultCurrency());
        companyEntity.setDateFormat(country.getDateFormat());
        companyEntity.setUiDateFormat(country.getUiDateFormat());
        companyEntity.setStatus("ACTIVE");
        companyEntity.setCreatedBy(userId);
        companyEntity.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        companyEntity.setIsDeleted(0);
        return companyEntity;
    }

    private CountryEntity getDefaultCurrency(Integer countryId) throws Exception {
        CountryEntity country = countryRepository.findById(countryId).orElse(new CountryEntity());
        return country;
    }

    private UserEntity createUserEntity(RegistrationEntity registrationEntity) {
        UserEntity entity = new UserEntity();
        entity.setFirstName(registrationEntity.getFirstName());
        entity.setLastName(registrationEntity.getLastName());
        entity.setContactNo(registrationEntity.getMobileNo());
        entity.setUserName(registrationEntity.getUsername());
        entity.setPassword(registrationEntity.getPassword());
        entity.setEmail(registrationEntity.getEmail());
        entity.setUID(SSOUtil.getUserUID());
        entity.setTotalLoginCount(0);
        entity.setIsActive(1);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    /**
     * Convert user input Dto into Entity
     */
    private RegistrationEntity convertDtoEntity(RegistrationForm form) throws Exception {
        RegistrationEntity entity = new RegistrationEntity();
        entity.setSignupId(null);
        entity.setFirstName(form.getFirstName());
        entity.setLastName(form.getLastName());
        entity.setEmail(form.getEmail());
        entity.setUsername(form.getUsername());
        entity.setPassword(getHashPassword(form.getPassword()));
        entity.setCountryId(form.getCountryId());
        entity.setCompanyName(form.getCompanyName());
        entity.setMobileNo(form.getMobileNo());
        entity.setTokenVerificationCode(form.getTokenVerificationCode());
        entity.setIsTokenVerified(0);
        entity.setJobTitle(form.getJobTitle());
        entity.setEmployeeCount(form.getEmployeeCount());
        entity.setRemarks(null);
        entity.setSortSeq(0);
        entity.setLocked(0);
        entity.setDeleted(0);
        entity.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        return entity;
    }

    private String getHashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return SSOUtil.generateStrongPasswordHash(password);
    }

    public Optional<RegistrationEntity> findById(Integer id) {
        return registrationRepository.findById(id);
    }

    /**
     * Email link verification status
     *
     * @return
     */
    @Deprecated
    public Integer updateTokenStatusOld(String signupId) {
        RegistrationEntity signupEntity = registrationRepository.findById(Integer.valueOf(signupId)).get();
        List<UserEntity> userEntityList = userRepository.findUserByEmail(signupEntity.getEmail());

        if (signupEntity.getIsTokenVerified() == 1) {
            return 104;
        }

        if (userEntityList == null) {
            return 102;
        }
        if (userEntityList.size() > 1) {
            return 101;
        }
        if (userEntityList.size() == 0) {
            return 102;
        }
        UserEntity user = userEntityList.get(0);
        try {
            databaseService.initTenant(user.getUserId(), user.getUID());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return 103;
        }
        return registrationRepository.updateTokenStatus(Integer.valueOf(signupId), Timestamp.valueOf(LocalDateTime.now()));
    }

    public Integer updateTokenStatus(String signupId) {
        int status = 100;
        RegistrationEntity signupEntity = registrationRepository.findById(Integer.valueOf(signupId)).get();
        List<UserEntity> userEntityList = userRepository.findUserByEmail(signupEntity.getEmail());

        if (signupEntity.getIsTokenVerified() == 1) {
            status = 104;
        }

        if (userEntityList == null) {
            status = 102;
        }
        if (userEntityList.size() > 1) {
            status = 101;
        }
        if (userEntityList.size() == 0) {
            status = 102;
        }
        UserEntity user = userEntityList.get(0);
        try {
            List<TenantDBConfigEntity> tenantDBConfigEntityList = tenantDBConfigRepository.findAllLockedTenantsByUserID(user.getUserId());
            tenantDBConfigEntityList.stream().forEach(e -> {
                e.setIsLocked(0);
            });
            databaseService.createTenantDatabase(user.getUID().toLowerCase());
            databaseService.createTenantTriggers(user.getUID().toLowerCase());
            tenantDBConfigRepository.saveAll(tenantDBConfigEntityList);
            status = registrationRepository.updateTokenStatus(Integer.valueOf(signupId), Timestamp.valueOf(LocalDateTime.now()));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            status = 103;
        }
        return status;
    }

    public Boolean checkEmailExistOrNot(String email) {
        List<RegistrationEntity> registrationEntityList = registrationRepository.checkEmailExistOrNot(email);
        return registrationEntityList.size() > 0;
    }

}
