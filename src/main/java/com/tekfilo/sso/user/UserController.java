package com.tekfilo.sso.user;

import com.tekfilo.sso.base.SSOResponse;
import com.tekfilo.sso.role.entity.UserRoleEntity;
import com.tekfilo.sso.user.dto.UserDto;
import com.tekfilo.sso.user.dto.UserRightsRequest;
import com.tekfilo.sso.user.dto.UserRightsResponse;
import com.tekfilo.sso.user.entity.UserEntity;
import com.tekfilo.sso.user.entity.UserMenus;
import com.tekfilo.sso.user.service.UserService;
import com.tekfilo.sso.usercompanymap.UserCompanyMapEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/sso/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/getusermenus/{id}")
    public ResponseEntity<List<UserMenus>> getUserMenus(@PathVariable("id") Integer userId) {
        return new ResponseEntity<List<UserMenus>>(userService.getUserMenus(userId), HttpStatus.OK);
    }

    @GetMapping("/getallusers")
    public ResponseEntity<List<UserEntity>> getUsersList() {
        return new ResponseEntity<List<UserEntity>>(userService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/getcompanyusers/{companyId}")
    public ResponseEntity<List<UserCompanyMapEntity>> getCompanyUserList(@PathVariable("companyId") Integer companyId) {
        return new ResponseEntity<List<UserCompanyMapEntity>>(userService.findAllUserMappedCompanyList(companyId), HttpStatus.OK);
    }

    @GetMapping("/getuserroles/{userid}")
    public ResponseEntity<List<UserRoleEntity>> getUserRoles(@PathVariable("userid") Integer userId) {
        return new ResponseEntity<List<UserRoleEntity>>(userService.findUserRolesListByUserId(userId), HttpStatus.OK);
    }

    @PostMapping("/saveuserrole")
    public ResponseEntity<SSOResponse> saveUserRole(@RequestBody Map<String, Object> data) {
        SSOResponse ssoResponse = new SSOResponse();
        try {
            final String userId = (String) data.get("userId");
            final String roleId = (String) data.get("roleId");
            final String operateBy = (String) data.get("operateBy");
            final String companyId = (String) data.get("companyId");
            this.userService.saveUserRole(userId, roleId, operateBy, companyId);
            ssoResponse.setStatus(100);
            ssoResponse.setLangStatus("save_100");
            ssoResponse.setMessage("Saved Successfully");
        } catch (Exception exception) {
            log.error(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
            ssoResponse.setStatus(101);
            ssoResponse.setLangStatus("error_101");
            ssoResponse.setMessage(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
    }


    @PutMapping("/savepicture/{imagename}/{userid}")
    public ResponseEntity<SSOResponse> updateUserProfileImage(@PathVariable("imagename") String imagename,
                                                              @PathVariable("userid") Integer userid) {
        SSOResponse ssoResponse = new SSOResponse();
        try {
            this.userService.updateImagePath(imagename, userid);
            ssoResponse.setStatus(100);
            ssoResponse.setLangStatus("save_100");
            ssoResponse.setMessage("Saved Successfully");
        } catch (Exception exception) {
            log.error(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
            ssoResponse.setStatus(101);
            ssoResponse.setLangStatus("error_101");
            ssoResponse.setMessage(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
    }

    @PostMapping("/getloggedinuserrights")
    public ResponseEntity<UserRightsResponse> getLoggedInUserRights(@RequestBody UserRightsRequest request) {
        UserRightsResponse response = new UserRightsResponse();
        response.setStatus(true);
        UserRightsResponse xTenantResponse = this.userService.getLoggedInUserXTenantID(request);
        if (xTenantResponse.isStatus()) {
            response.setXtenantId(xTenantResponse.getXtenantId());
            response.setRoles(this.userService.getLoggedInUserRoles(request));
            response.setSubscribedPrivileges(this.userService.getLoggedInUserCompanySubscriptionPrivilegeList(request));
            this.userService.getLoggedInUserRights(request, response);
        } else {
            response.setStatus(false);
        }
        return new ResponseEntity<UserRightsResponse>(response, HttpStatus.OK);
    }

    @PatchMapping("/updateprofile")
    public ResponseEntity<SSOResponse> updateUserProfileDetails(@RequestBody UserDto userDto) {
        SSOResponse ssoResponse = new SSOResponse();
        try {
            this.userService.updateProfile(userDto);
            ssoResponse.setStatus(100);
            ssoResponse.setLangStatus("save_100");
            ssoResponse.setMessage("Saved Successfully");
        } catch (Exception exception) {
            log.error(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
            ssoResponse.setStatus(101);
            ssoResponse.setLangStatus("error_101");
            ssoResponse.setMessage(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
    }

    @PatchMapping("/deactivate/{userid}")
    public ResponseEntity<SSOResponse> inactiveUser(@PathVariable("userid") Integer userId) {
        SSOResponse ssoResponse = new SSOResponse();
        try {
            this.userService.changeUserStatus(userId, 1);
            ssoResponse.setStatus(100);
            ssoResponse.setLangStatus("save_100");
            ssoResponse.setMessage("User Deactivated");
        } catch (Exception exception) {
            log.error(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
            ssoResponse.setStatus(101);
            ssoResponse.setLangStatus("error_101");
            ssoResponse.setMessage(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
    }

    @PatchMapping("/activate/{userid}")
    public ResponseEntity<SSOResponse> activateUser(@PathVariable("userid") Integer userId) {
        SSOResponse ssoResponse = new SSOResponse();
        try {
            this.userService.changeUserStatus(userId, 0);
            ssoResponse.setStatus(100);
            ssoResponse.setLangStatus("save_100");
            ssoResponse.setMessage("User Activated");
        } catch (Exception exception) {
            log.error(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
            ssoResponse.setStatus(101);
            ssoResponse.setLangStatus("error_101");
            ssoResponse.setMessage(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
    }
}
