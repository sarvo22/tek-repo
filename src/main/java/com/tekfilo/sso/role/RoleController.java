package com.tekfilo.sso.role;

import com.tekfilo.sso.base.SSOResponse;
import com.tekfilo.sso.plan.entity.SubscriptionPrivilege;
import com.tekfilo.sso.plan.service.impl.PlanService;
import com.tekfilo.sso.role.dto.RolePrivilegeDto;
import com.tekfilo.sso.role.entity.Privilege;
import com.tekfilo.sso.role.entity.RoleEntity;
import com.tekfilo.sso.role.entity.RolePrivilegeEntity;
import com.tekfilo.sso.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/sso/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    PlanService planService;

    @GetMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<RoleEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<Page<RoleEntity>>(roleService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }

    @GetMapping("/getcompanyroles/{companyId}")
    public ResponseEntity<List<RoleEntity>> getCompanyRoles(@PathVariable("companyId") Integer companyId) {
        return new ResponseEntity<List<RoleEntity>>(roleService.findAllRoles(companyId), HttpStatus.OK);
    }

    @GetMapping("/rolebyid/{id}")
    public ResponseEntity<RoleEntity> getRoleById(@PathVariable("id") Integer id) {
        return new ResponseEntity<RoleEntity>(roleService.findRoleById(id), HttpStatus.OK);
    }


    @GetMapping("/privileges")
    public ResponseEntity<List<Privilege>> getAvailablePrivilegesList() {
        return new ResponseEntity<List<Privilege>>(this.roleService.findAllPrivileges(), HttpStatus.OK);
    }

    @GetMapping("/privilegesbygroup")
    public ResponseEntity<Map<String, List<Privilege>>> getAvailablePrivilegesListGrouped() {
        return new ResponseEntity<Map<String, List<Privilege>>>(
                this.roleService.findAllPrivileges().stream().collect(
                        Collectors.groupingBy(mp -> mp.getPrivilegeGroup())
                ),
                HttpStatus.OK);
    }


    @GetMapping("/subscriptionprivilegesbygroup/{subscribeid}")
    public ResponseEntity<Map<String, List<SubscriptionPrivilege>>> getSubscribedAvailablePrivilegesListGrouped(@PathVariable("subscribeid") Integer subscribeid) {
        return new ResponseEntity<Map<String, List<SubscriptionPrivilege>>>(
                this.planService.getSubscriptionPrivileges(subscribeid).stream().collect(
                        Collectors.groupingBy(mp -> mp.getPrivilegeGroup())
                ),
                HttpStatus.OK);
    }

    @GetMapping("/roleprivilegebyroleid/{id}")
    public ResponseEntity<Map<String, List<RolePrivilegeEntity>>> getRolePrivilegeByRoleId(@PathVariable("id") Integer id) {
        return new ResponseEntity<Map<String, List<RolePrivilegeEntity>>>(
                this.roleService.findRolePrivilegeByRoleId(id).stream().collect(Collectors.groupingBy(m -> m.getPrivilegeGroup()))
                , HttpStatus.OK);
    }

    @PostMapping("/saveroleprivilege")
    public ResponseEntity<SSOResponse> saveRolePrivielge(@RequestBody RolePrivilegeDto rolePrivilegeDto) {
        SSOResponse ssoResponse = new SSOResponse();
        try {
            Integer roleId = this.roleService.saveRolePrivilegeMap(rolePrivilegeDto);
            ssoResponse.setId(roleId);
            ssoResponse.setStatus(100);
            ssoResponse.setMessage("Saved Successfully");
            ssoResponse.setLangStatus("save_100");
        } catch (Exception exception) {
            ssoResponse.setMessage("Exception raised while saving Role privilege map");
            ssoResponse.setStatus(101);
            ssoResponse.setLangStatus("save_101");
            log.error(exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
    }
}