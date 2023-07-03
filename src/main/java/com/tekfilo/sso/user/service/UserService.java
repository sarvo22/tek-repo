package com.tekfilo.sso.user.service;

import com.tekfilo.sso.auth.AuthForm;
import com.tekfilo.sso.auth.InviteForm;
import com.tekfilo.sso.company.CompanyEntity;
import com.tekfilo.sso.plan.entity.SubscriptionPrivilege;
import com.tekfilo.sso.plan.repository.SubscriptionPrivilegeRepository;
import com.tekfilo.sso.role.dto.PrivilegeDto;
import com.tekfilo.sso.role.entity.RoleEntity;
import com.tekfilo.sso.role.entity.RolePrivilegeEntity;
import com.tekfilo.sso.role.entity.UserRoleEntity;
import com.tekfilo.sso.role.repository.RolePrivilegeRepository;
import com.tekfilo.sso.role.repository.UserRoleRepository;
import com.tekfilo.sso.tenantdbconfig.TenantDBConfigEntity;
import com.tekfilo.sso.tenantdbconfig.TenantDBConfigRepository;
import com.tekfilo.sso.tenantdbconfig.TenantDBConfigService;
import com.tekfilo.sso.user.dto.*;
import com.tekfilo.sso.user.entity.UserEntity;
import com.tekfilo.sso.user.entity.UserMenus;
import com.tekfilo.sso.user.repository.UserMainMenuRepository;
import com.tekfilo.sso.user.repository.UserRepository;
import com.tekfilo.sso.usercompanymap.UserCompanyMapEntity;
import com.tekfilo.sso.usercompanymap.UserCompanyMapRepository;
import com.tekfilo.sso.util.SSOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    UserCompanyMapRepository userCompanyMapRepository;

    @Autowired
    TenantDBConfigRepository tenantDBConfigRepository;

    @Autowired
    RolePrivilegeRepository rolePrivilegeRepository;

    @Autowired
    UserMainMenuRepository userMainMenuRepository;


    @Autowired
    SubscriptionPrivilegeRepository subscriptionPrivilegeRepository;

    @Autowired
    TenantDBConfigService tenantDBConfigService;

    public void createNewUser(UserDto userDto) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeySpecException {
        userRepository.save(convertToEntity(userDto));
    }

    private UserEntity convertToEntity(UserDto userDto) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserEntity entity = new UserEntity();
        entity.setUserName(userDto.getUsername());
        entity.setPassword(SSOUtil.generateStrongPasswordHash(userDto.getPassword()));
        entity.setEmail(userDto.getEmail());
        entity.setUID(SSOUtil.getUserUID());
        entity.setTotalLoginCount(0);
        entity.setIsActive(1);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    private UserEntity convertInviteToUserEntity(InviteForm inviteForm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserEntity entity = new UserEntity();
        entity.setUserName(inviteForm.getUsername());
        entity.setPassword(SSOUtil.generateStrongPasswordHash(inviteForm.getPassword()));
        entity.setEmail(inviteForm.getEmail());
        entity.setUID(SSOUtil.getUserUID());
        entity.setTotalLoginCount(0);
        entity.setIsActive(1);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    public int validateUser(AuthForm form) {

        if (StringUtils.isEmpty(form.getUsername())) {
            return 101;
        }

        if (StringUtils.isEmpty(form.getUsername().trim())) {
            return 101;
        }
        List<UserEntity> userList = userRepository.findUserByUserName(form.getUsername().trim());
        if (userList == null)
            return 101;
        if (userList.size() == 0)
            return 101;
        if (userList.size() > 1)
            return 101;
        UserEntity entity = userList.get(0);
        if (entity.getIsLocked() == 1) {
            return 110;
        }
        if (!form.getUsername().equalsIgnoreCase(entity.getUserName()))
            return 101;
        try {
            if (!SSOUtil.validatePassword(form.getPassword(), entity.getPassword()))
                return 102;
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return 102;
        } catch (InvalidKeySpecException invalidKeySpecException) {
            return 102;
        }

        return 100;
    }

    /**
     * Show List of Authorized menus for Logged in user
     */
    public List<UserMenus> getUserMenus(Integer userId) {
        return this.userMainMenuRepository.findAll();
    }

    public Boolean checkUserExistOrNot(String username) {
        List<UserEntity> userList = userRepository.checkUserExistOrNot(username);
        return userList.size() > 0;
    }


    public UserEntity findUserByName(String username) {
        return userRepository.findUserByUserName(username).get(0);
    }

    public UserEntity findUserByEmail(String email) {
        List<UserEntity> userEntityList = userRepository.findUserByEmail(email);
        return userEntityList.size() > 0 ? userEntityList.get(0) : new UserEntity();
    }

    public Boolean checkUserExistOrNotByMail(String email) throws Exception {
        List<UserEntity> userEntityList = userRepository.findUserByEmail(email);
        if (userEntityList.size() > 1) {
            throw new Exception("More users found");
        }
        return userEntityList.size() != 0;
    }


    public List<String> findUserRolesByUserId(Integer userId) {
        List<UserRoleEntity> userRoleEntityList = userRoleRepository.findUserRolesByUserId(userId);
        List<RoleEntity> roleEntityList = userRoleEntityList.stream().map(UserRoleEntity::getRole).collect(Collectors.toList());
        return roleEntityList.stream().map(RoleEntity::getRoleName).collect(Collectors.toList());
    }

    public List<UserRoleEntity> findUserRolesListByUserId(Integer userId) {
        return this.userRoleRepository.findUserRolesByUserId(userId);
    }

    public List<UserEntity> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public List<UserCompanyMapEntity> findAllUserMappedCompanies(Integer userId) {
        return userCompanyMapRepository.findUserCompaniesByUserId(userId);

    }


    public CompanyEntity findDefaultCompany(Integer userId) {
        List<UserCompanyMapEntity> entities = userCompanyMapRepository.findDefaultCompany(userId);
        if (entities.size() > 0)
            return entities.get(0).getCompany();
        return new CompanyEntity();
    }

    public List<UserCompanyMapEntity> findAllUserMappedCompanyList(Integer companyId) {
        return this.userCompanyMapRepository.findAllUserCompanyList(companyId);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void createInviteUserProcess(InviteForm inviteForm) throws Exception {
        boolean isUserAvailable = this.checkUserExistOrNotByMail(inviteForm.getEmail());
        UserEntity userEntity = isUserAvailable ? this.findUserByEmail(inviteForm.getEmail()) : userRepository.save(convertInviteToUserEntity(inviteForm));
        UserEntity senderEntity = this.findUserByEmail(inviteForm.getSenderEmail());
        if (userEntity.getUserId() == null) {
            throw new RuntimeException("Unable to complete Process due to User not found");
        }
        if (!isUserAvailable) {
            userRoleRepository.save(convert2UserRoleEntity(userEntity.getUserId(), Integer.parseInt(inviteForm.getSelectedRoleId()), inviteForm.getSenderCompanyId()));
            userCompanyMapRepository.save(createUserCompanyMap(true, userEntity.getUserId(), inviteForm.getSenderCompanyId()));
        } else {
            List<UserRoleEntity> roleEntityList = userRoleRepository.findUserRolesByRoleAndUserId(Integer.parseInt(inviteForm.getSelectedRoleId()), userEntity.getUserId());
            if (roleEntityList.size() == 0) {
                userRoleRepository.save(convert2UserRoleEntity(userEntity.getUserId(), Integer.parseInt(inviteForm.getSelectedRoleId()), inviteForm.getSenderCompanyId()));
            }

            List<UserCompanyMapEntity> userCompanyMapEntities = findAllUserMappedCompanies(userEntity.getUserId());
            Predicate<UserCompanyMapEntity> companyCheck = e -> e.getCompanyId() == inviteForm.getSenderCompanyId();
            if (!userCompanyMapEntities.stream().anyMatch(companyCheck)) {
                boolean setDefaultCompany = userCompanyMapEntities.size() <= 0;
                userCompanyMapRepository.save(createUserCompanyMap(setDefaultCompany, userEntity.getUserId(), inviteForm.getSenderCompanyId()));
            }
        }

        List<TenantDBConfigEntity> senderTenantList = tenantDBConfigRepository.getTenantUIDByCompanyId(inviteForm.getSenderCompanyId(), senderEntity.getUserId());
        if (senderTenantList.size() == 0) {
            throw new RuntimeException("Sender do not have Tenant DB Configuration, hence Invitation Process failed, Please contact Administrator or Tekfilo Support Team");
        }
        if (senderTenantList.size() > 1) {
            throw new RuntimeException("Sender has more than one Tenant DB Configuration, hence Invitation Process failed, Please contact Administrator or Tekfilo Support Team");
        }
        tenantDBConfigRepository.save(createNewTenantDBConfig(userEntity.getUID(), userEntity.getUserId(), senderTenantList.get(0).getTenantUID(), senderTenantList.get(0).getCompanyId()));
    }

    private UserCompanyMapEntity createUserCompanyMap(boolean setDefaultCompany,
                                                      Integer userId,
                                                      Integer companyId) {

        UserCompanyMapEntity entity = new UserCompanyMapEntity();
        entity.setId(null);
        entity.setUserId(userId);
        entity.setCompanyId(companyId);
        entity.setIsActive(1);
        entity.setIsDefault(setDefaultCompany ? 1 : 0);
        entity.setCreatedBy(userId);
        entity.setRemarks("created via invitation");
        entity.setIsDeleted(0);
        return entity;
    }

    private UserRoleEntity convert2UserRoleEntity(Integer userId, Integer roleId, Integer companyId) {
        UserRoleEntity entity = new UserRoleEntity();
        entity.setRoleMapId(null);
        entity.setUserId(userId);
        entity.setRoleId(roleId);
        entity.setCompanyId(companyId);
        entity.setCreatedBy(userId);
        entity.setIsDeleted(0);
        return entity;
    }

    private TenantDBConfigEntity createNewTenantDBConfig(String userUid, Integer userId, String uid, Integer companyId) {
        TenantDBConfigEntity entity = new TenantDBConfigEntity();
        entity.setUserUID(Optional.ofNullable(userUid).isPresent() ? userUid : String.valueOf(userId));
        entity.setTenantUID(uid);
        entity.setUserId(userId);
        entity.setCompanyId(companyId);
        entity.setIsLocked(0);
        entity.setCreatedBy(userId);
        entity.setIsDeleted(0);
        return entity;
    }

    public void saveUserRole(String userId, String roleId, String operateBy, String companyId) {
        UserRoleEntity entity = new UserRoleEntity();
        entity.setRoleId(Integer.parseInt(roleId));
        entity.setUserId(Integer.parseInt(userId));
        entity.setCreatedBy(Integer.parseInt(operateBy));
        entity.setModifiedBy(Integer.parseInt(operateBy));
        entity.setCompanyId(Integer.parseInt(companyId));
        entity.setIsDeleted(0);
        this.userRoleRepository.save(entity);
    }

    @Deprecated
    public void removeOldUserRoles(List<UserRoleEntity> userRoleEntityList) {
        this.userRoleRepository.deleteAll(userRoleEntityList);
    }

    @Deprecated
    public Map<String, PrivilegeDto> findUserRolePrivileges(Integer userId, List<UserCompanyMapEntity> userCompanyMapEntityList) {
        List<UserRoleEntity> userRoleEntityList = this.userRoleRepository.findUserRolesByUserId(userId);
        if (userRoleEntityList.size() == 0)
            return new HashMap<>();
        List<RolePrivilegeEntity> rolePrivilegeEntityList = this.rolePrivilegeRepository.findAllUserRolesByUserId(
                userRoleEntityList.stream().map(role -> role.getRoleId()).collect(Collectors.toList())
        );
        Map<String, PrivilegeDto> userRightsMap = new HashMap<>();
        Map<String, List<RolePrivilegeEntity>> uMap = rolePrivilegeEntityList.stream().collect(Collectors.groupingBy(a -> a.getPrivilegeName()));
        uMap.entrySet().stream().forEach(map -> {
            final String key = map.getKey(); // change into menuId on above group later if required to be changed
            final List<RolePrivilegeEntity> privilegeEntityList = map.getValue();
            if (privilegeEntityList.size() > 0) {
                PrivilegeDto dto = new PrivilegeDto();
                RolePrivilegeEntity entity = privilegeEntityList.get(0);
                // Copy values into DTO
                // here assuming that one menu has all the privileges , if more than one menu comes then values will be ignored
                dto.setMapId(entity.getMapId());
                dto.setPrivilegeName(entity.getPrivilegeName());
                dto.setPrivilegeGroup(entity.getPrivilegeGroup());
                dto.setMenuId(entity.getMenuId());
                dto.setFullaccess(entity.getFullaccess());
                dto.setView(entity.getView());
                dto.setAdd(entity.getAdd());
                dto.setEdit(entity.getEdit());
                dto.setDelete(entity.getDelete());
                dto.setPrint(entity.getPrint());
                dto.setShare(entity.getShare());
                dto.setEmail(entity.getEmail());
                dto.setDetailview(entity.getDetailview());
                dto.setDetailadd(entity.getDetailadd());
                dto.setDetaildelete(entity.getDetaildelete());
                dto.setMainlock(entity.getMainlock());
                dto.setMainunlock(entity.getMainunlock());
                dto.setDetaillock(entity.getDetaillock());
                dto.setDetailunlock(entity.getDetailunlock());
                userRightsMap.put(key, dto);
            }
        });
        return userRightsMap;
    }


    public List<PrivilegeDto> findUserRolePrivilegesByRoleList(Integer userId, List<UserRoleEntity> userRoleEntityList) {
        if (userRoleEntityList.size() == 0)
            return new ArrayList<>();

        List<RolePrivilegeEntity> rolePrivilegeEntityList = this.rolePrivilegeRepository.findAllUserRolesByUserId(userRoleEntityList.stream().map(role -> role.getRoleId()).collect(Collectors.toList()));

        List<PrivilegeDto> rolePrivilegeList = new ArrayList<>();

        if (rolePrivilegeEntityList.size() > 0) {
            rolePrivilegeEntityList.stream().forEach(entity -> {
                PrivilegeDto dto = new PrivilegeDto();
                dto.setMapId(entity.getMapId());
                dto.setPrivilegeName(entity.getPrivilegeName());
                dto.setPrivilegeGroup(entity.getPrivilegeGroup());
                dto.setMenuId(entity.getMenuId());
                dto.setFullaccess(entity.getFullaccess());
                dto.setView(entity.getView());
                dto.setAdd(entity.getAdd());
                dto.setEdit(entity.getEdit());
                dto.setDelete(entity.getDelete());
                dto.setPrint(entity.getPrint());
                dto.setShare(entity.getShare());
                dto.setEmail(entity.getEmail());
                dto.setDetailview(entity.getDetailview());
                dto.setDetailadd(entity.getDetailadd());
                dto.setDetaildelete(entity.getDetaildelete());
                dto.setMainlock(entity.getMainlock());
                dto.setMainunlock(entity.getMainunlock());
                dto.setDetaillock(entity.getDetaillock());
                dto.setDetailunlock(entity.getDetailunlock());
                rolePrivilegeList.add(dto);
            });
        }
        return rolePrivilegeList;
    }

    public void updateImagePath(String imagename, Integer userId) throws Exception {
        userRepository.updateImagePath(imagename, userId);
    }

    public UserRightsResponse getLoggedInUserXTenantID(UserRightsRequest request) {
        UserRightsResponse response = new UserRightsResponse();
        response.setStatus(true);
        try {
            final String xTenantID = tenantDBConfigService.getTenantUIDByUserAndCompanyId(request.getUserId(), request.getCompanyId());
            response.setXtenantId(xTenantID);
        } catch (Exception exception) {
            response.setStatus(false);
            return response;
        }
        return response;
    }

    public List<RoleEntity> getLoggedInUserRoles(UserRightsRequest request) {
        List<UserRoleEntity> userRoleEntityList = this.userRoleRepository.findUserRolesByUserIdAndCompanyId(request.getUserId(), request.getCompanyId());
        List<RoleEntity> roleEntityList = new ArrayList<>();
        userRoleEntityList.stream().forEach(role -> {
            int roleSubscriptionId = role.getRole().getSubscriptionId().intValue();
            int requestSubscriptionId = request.getSubscriptionId().intValue();
            if (roleSubscriptionId == requestSubscriptionId) {
                roleEntityList.add(role.getRole());
            }
        });
        return roleEntityList;
    }

    public List<SubscriptionPrivilege> getLoggedInUserCompanySubscriptionPrivilegeList(UserRightsRequest request) {
        List<SubscriptionPrivilege> subscriptionPrivilegeList = this.subscriptionPrivilegeRepository.findAll(new Specification() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
                predicates.add(criteriaBuilder.equal(root.get("subscriptionId"), request.getSubscriptionId()));
                return criteriaBuilder.and(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));
            }
        });
        return subscriptionPrivilegeList;
    }

    public void getLoggedInUserRights(UserRightsRequest request, UserRightsResponse response) {

        List<Integer> roleIds = response.getRoles().stream().map(role -> role.getRoleId()).collect(Collectors.toList());
        if (roleIds.size() == 0) {
            response.setStatus(false);
        }
        List<RolePrivilegeEntity> rolePrivilegeEntityList = this.rolePrivilegeRepository.findAll(new Specification() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
                predicates.add(
                        criteriaBuilder.in(root.get("roleId")).value(castToRequiredType(root.get("roleId").getJavaType(), roleIds))
                );
                return criteriaBuilder.and(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));
            }
        });
        List<RolePrivilegeEntity> filteredRolePrivilegeList = new ArrayList<>();
//        rolePrivilegeEntityList.stream().forEach(e -> {
//            SubscriptionPrivilege available = response.getSubscribedPrivileges().stream().filter(sp -> sp.getPrivilegeId().intValue() == e.getPrivilegeId().intValue()).findFirst().orElse(null);
//            if (available != null) {
//                e.setMapId(null);
//                e.setRoleId(null);
//                filteredRolePrivilegeList.add(e);
//            }
//        });

        response.getSubscribedPrivileges().stream().forEach(sub -> {
            List<RolePrivilegeEntity> rolePrivilegeEntities = rolePrivilegeEntityList.stream().filter(e -> e.getPrivilegeId().intValue() == sub.getPrivilegeId().intValue()).collect(Collectors.toList());
            if (rolePrivilegeEntities != null) {
                if (rolePrivilegeEntities.size() > 0) {
                    filteredRolePrivilegeList.add(rolePrivilegeEntities.get(0));
                }
            }
        });
        response.setRolePrivileges(filteredRolePrivilegeList.stream().distinct().collect(Collectors.toList()));
        response.setSubscribedMenus(getUserRolePrivilegeMenuList(filteredRolePrivilegeList, request.getSubscriptionId(), request.getAppId()));
        response.setSubscribedPrivileges(new ArrayList<>());
    }

    private Object castToRequiredType(Class fieldType, List<Integer> value) {
        List<Object> list = new ArrayList<>();
        for (Integer i : value) {
            list.add(castToRequiredType(fieldType, i));
        }
        return list;
    }

    private Object castToRequiredType(Class fieldType, Integer value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        }
        return null;
    }

    private List<UserMenusDto> getUserRolePrivilegeMenuList(List<RolePrivilegeEntity> rolePrivilegeEntityList, Integer subscriptionId, Integer appId) {
        List<UserMenusDto> userMenusDtoList = new ArrayList<>();

        Map<Integer, List<RolePrivilegeEntity>> subscriptionGroup = rolePrivilegeEntityList.stream().collect(
                Collectors.groupingBy(a -> a.getUserSubMenus().getMainMenuId())
        );

        subscriptionGroup.entrySet().stream().forEach(mainMenu -> {
            final Integer mainMenuId = mainMenu.getKey();
            final List<RolePrivilegeEntity> subscriptionPrivileges = mainMenu.getValue();
            UserMenusDto dto = new UserMenusDto();
            dto.setSubscriptionId(subscriptionId);
            dto.setAppId(appId);
            dto.setMainMenuId(mainMenuId);
            UserMenus userMenus = subscriptionPrivileges.get(0).getUserSubMenus().getUserMenus();
            dto.setKey(userMenus.getKey());
            dto.setLabel(userMenus.getLabel());
            dto.setIcon(userMenus.getIcon());
            dto.setLink(userMenus.getLink());
            dto.setCollapsed(userMenus.getCollapsed());
            dto.setIsTitle(userMenus.getIsTitle());
            dto.setBadge(userMenus.getBadge());
            dto.setParentKey(userMenus.getParentKey());
            dto.setShowInInner(userMenus.getShowInInner());
            dto.setChildren(getChildMenusByRolePrivilege(subscriptionPrivileges));
            userMenusDtoList.add(dto);
        });

        //List<UserMenusDto> distinctMenusDto = new ArrayList<>();
        //distinctMenusDto = userMenusDtoList.stream().distinct().collect(Collectors.toList());
        Collections.sort(userMenusDtoList, Comparator.comparingInt(UserMenusDto::getMainMenuId));
        return userMenusDtoList;
    }


    @Deprecated
    private List<UserMenusDto> getSubscribedMenuList(List<SubscriptionPrivilege> subscriptionPrivilegeList, List<RolePrivilegeEntity> rolePrivilegeEntityList, Integer subscriptionId, Integer appId) {
        List<UserMenusDto> userMenusDtoList = new ArrayList<>();

//        Map<Integer, List<SubscriptionPrivilege>> subscriptionGroup = subscriptionPrivilegeList.stream().collect(
//                Collectors.groupingBy(a -> a.getUserSubMenus().getMainMenuId())
//        );

        Map<Integer, List<SubscriptionPrivilege>> subscriptionGroup = new HashMap<>();
        Map<Integer, List<RolePrivilegeEntity>> rolePrivilegeMapGroup = rolePrivilegeEntityList.stream().collect(Collectors.groupingBy(a -> a.getUserSubMenus().getMainMenuId()));

        subscriptionGroup.entrySet().stream().forEach(mainMenu -> {
            final Integer mainMenuId = mainMenu.getKey();

            // check user has this privilege or not
            final boolean hasMainMenuPrivilege = rolePrivilegeMapGroup.containsKey(mainMenuId);
            if (hasMainMenuPrivilege) {
                final List<SubscriptionPrivilege> subscriptionPrivileges = mainMenu.getValue();
                UserMenusDto dto = new UserMenusDto();
                dto.setSubscriptionId(subscriptionId);
                dto.setAppId(appId);
                dto.setMainMenuId(mainMenuId);
                //UserMenus userMenus = subscriptionPrivileges.get(0).getUserSubMenus().getUserMenus();
                UserMenus userMenus = new UserMenus();
                dto.setKey(userMenus.getKey());
                dto.setLabel(userMenus.getLabel());
                dto.setIcon(userMenus.getIcon());
                dto.setLink(userMenus.getLink());
                dto.setCollapsed(userMenus.getCollapsed());
                dto.setIsTitle(userMenus.getIsTitle());
                dto.setBadge(userMenus.getBadge());
                dto.setParentKey(userMenus.getParentKey());
                dto.setShowInInner(userMenus.getShowInInner());
                //List<UserSubMenusDto> childMenus = getChildMenus(subscriptionPrivileges, rolePrivilegeMapGroup.get(mainMenuId));
                List<UserSubMenusDto> childMenus = new ArrayList<>();
                if (childMenus.size() > 0) {
                    userMenusDtoList.add(dto);
                }
            }
        });

        Collections.sort(userMenusDtoList, Comparator.comparingInt(UserMenusDto::getMainMenuId));
        return userMenusDtoList;
    }

    @Deprecated
    private List<UserSubMenusDto> getChildMenus(List<SubscriptionPrivilege> menuList, List<RolePrivilegeEntity> rolePrivilegeEntityList) {
        List<UserSubMenusDto> userSubMenusDtoList = new ArrayList<>();
        Map<Integer, Map<Integer, List<RolePrivilegeEntity>>> rolePrivilegeGroup = rolePrivilegeEntityList.stream().collect(
                Collectors.groupingBy(e -> e.getMenuId(), Collectors.groupingBy(b -> b.getPrivilegeId()))
        );
        menuList.stream().forEach(menu -> {
            if (rolePrivilegeGroup.containsKey(menu.getMenuId())) {
                if (rolePrivilegeGroup.get(menu.getMenuId()).containsKey(menu.getPrivilegeId())) {
                    UserSubMenusDto userSubMenusDto = new UserSubMenusDto();
                    userSubMenusDto.setMenuId(menu.getMenuId());
//                    userSubMenusDto.setMainMenuId(menu.getUserSubMenus().getMainMenuId());
//                    userSubMenusDto.setKey(menu.getUserSubMenus().getKey());
//                    userSubMenusDto.setLabel(menu.getUserSubMenus().getLabel());
//                    userSubMenusDto.setLink(menu.getUserSubMenus().getLink());
//                    userSubMenusDto.setAdd(menu.getUserSubMenus().getAdd());
//                    userSubMenusDto.setAddLink(menu.getUserSubMenus().getAddLink());
//                    userSubMenusDto.setParentKey(menu.getUserSubMenus().getParentKey());
                    userSubMenusDtoList.add(userSubMenusDto);
                }
            }
        });
        return userSubMenusDtoList;
    }

    private List<UserSubMenusDto> getChildMenusByRolePrivilege(List<RolePrivilegeEntity> menuList) {
        List<UserSubMenusDto> userSubMenusDtoList = new ArrayList<>();
        menuList.stream().forEach(menu -> {
            UserSubMenusDto userSubMenusDto = new UserSubMenusDto();
            userSubMenusDto.setMenuId(menu.getMenuId());
            userSubMenusDto.setMainMenuId(menu.getUserSubMenus().getMainMenuId());
            userSubMenusDto.setKey(menu.getUserSubMenus().getKey());
            userSubMenusDto.setLabel(menu.getUserSubMenus().getLabel());
            userSubMenusDto.setLink(menu.getUserSubMenus().getLink());
            userSubMenusDto.setAdd(menu.getUserSubMenus().getAdd());
            userSubMenusDto.setAddLink(menu.getUserSubMenus().getAddLink());
            userSubMenusDto.setParentKey(menu.getUserSubMenus().getParentKey());
            userSubMenusDtoList.add(userSubMenusDto);
        });
        Collections.sort(userSubMenusDtoList, Comparator.comparingInt(UserSubMenusDto::getMenuId));
        return userSubMenusDtoList;
    }

    @Transactional
    public void updateProfile(UserDto userDto) throws DataIntegrityViolationException {
        userRepository.updateUserProfile(userDto.getFirstName(), userDto.getLastName(), userDto.getContactNo(), userDto.getId());
    }

    @Transactional
    public void changeUserStatus(Integer userId, Integer locked) throws DataIntegrityViolationException {
        userRepository.updateUserStatus(userId, locked);
    }
}
