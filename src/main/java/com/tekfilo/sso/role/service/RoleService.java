package com.tekfilo.sso.role.service;

import com.tekfilo.sso.role.dto.PrivilegeDto;
import com.tekfilo.sso.role.dto.RolePrivilegeDto;
import com.tekfilo.sso.role.entity.Privilege;
import com.tekfilo.sso.role.entity.RoleEntity;
import com.tekfilo.sso.role.entity.RolePrivilegeEntity;
import com.tekfilo.sso.role.repository.PrivilegeRepository;
import com.tekfilo.sso.role.repository.RolePrivilegeRepository;
import com.tekfilo.sso.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Autowired
    RolePrivilegeRepository rolePrivilegeRepository;

    public Page<RoleEntity> findAll(int pageNo, int pageSize, String sortColumn, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ?
                PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).descending());
        Page<RoleEntity> pagedList = roleRepository.findAll(pageable);
        return pagedList;
    }

    public List<RoleEntity> findAllRoles(Integer companyId) {
        return roleRepository.findAllRoles(companyId);
    }

    public List<Privilege> findAllPrivileges() {
        return this.privilegeRepository.findAll();
    }

    public Integer saveRolePrivilegeMap(RolePrivilegeDto rolePrivilegeDto) throws Exception {
        RoleEntity entity = this.roleRepository.save(convertToRoleEntity(rolePrivilegeDto.getRole(),
                rolePrivilegeDto.getOperateBy(),
                rolePrivilegeDto.getCompanyId()));

        rolePrivilegeRepository.saveAll(convert2RolePrivilegeMap(
                entity.getRoleId(),
                rolePrivilegeDto.getCompanyId(),
                rolePrivilegeDto.getOperateBy(),
                rolePrivilegeDto.getPrivilege()
        ));
        return entity.getRoleId();
    }

    private List<RolePrivilegeEntity> convert2RolePrivilegeMap(Integer roleId, Integer companyId, Integer operateBy, Map<String, List<PrivilegeDto>> privilege) {
        List<RolePrivilegeEntity> entities = new ArrayList<>();
        privilege.entrySet().stream().forEach(e -> {
            e.getValue().stream().forEach(mp -> {
                RolePrivilegeEntity entity = new RolePrivilegeEntity();
                entity.setMapId(mp.getMapId());
                entity.setRoleId(roleId);
                entity.setPrivilegeId(mp.getPrivilegeId());
                entity.setPrivilegeName(mp.getPrivilegeName());
                entity.setPrivilegeGroup(mp.getPrivilegeGroup());
                entity.setMenuId(mp.getMenuId());
                entity.setFullaccess(mp.getFullaccess() != null && mp.getFullaccess());
                entity.setView(mp.getView() != null && mp.getView());
                entity.setAdd(mp.getAdd() != null && mp.getAdd());
                entity.setEdit(mp.getEdit() != null && mp.getEdit());
                entity.setDelete(mp.getDelete() != null && mp.getDelete());
                entity.setPrint(mp.getPrint() != null && mp.getPrint());
                entity.setShare(mp.getShare() != null && mp.getShare());
                entity.setEmail(mp.getEmail() != null && mp.getEmail());
                entity.setDetailview(mp.getDetailview() != null && mp.getDetailview());
                entity.setDetailadd(mp.getDetailadd() != null && mp.getDetailadd());
                entity.setDetaildelete(mp.getDetaildelete() != null && mp.getDetaildelete());
                entity.setMainlock(mp.getMainlock() != null && mp.getMainlock());
                entity.setMainunlock(mp.getMainunlock() != null && mp.getMainunlock());
                entity.setDetaillock(mp.getDetaillock() != null && mp.getDetaillock());
                entity.setDetailunlock(mp.getDetailunlock() != null && mp.getDetailunlock());
                entity.setQuickpayment(mp.getQuickpayment() != null && mp.getQuickpayment());
                entity.setChangestatus(mp.getChangestatus() != null && mp.getChangestatus());
                entity.setUpload(mp.getUpload() != null && mp.getUpload());
                entity.setAddcharges(mp.getAddcharges() != null && mp.getAddcharges());
                entity.setDeletecharges(mp.getDeletecharges() != null && mp.getDeletecharges());
                entity.setPostaccount(mp.getPostaccount() != null && mp.getPostaccount());
                entity.setSubmit(mp.getSubmit() != null && mp.getSubmit());
                entity.setCreatedBy(operateBy);
                entity.setModifiedBy(operateBy);
                entity.setIsDeleted(0);
                entities.add(entity);
            });
        });
        return entities;
    }

    private RoleEntity convertToRoleEntity(RoleEntity roleEntity,
                                           Integer userId,
                                           Integer companyId) {
        RoleEntity entity = new RoleEntity();
        entity.setRoleId(roleEntity.getRoleId());
        entity.setRoleName(roleEntity.getRoleName());
        entity.setSubscriptionId(roleEntity.getSubscriptionId());
        entity.setRemarks(roleEntity.getRemarks());
        entity.setModifiedBy(userId);
        entity.setCreatedBy(userId);
        entity.setCompanyId(companyId);
        entity.setIsDeleted(0);
        return entity;
    }

    public RoleEntity findRoleById(Integer id) {
        return this.roleRepository.findById(id).get();
    }

    public List<RolePrivilegeEntity> findRolePrivilegeByRoleId(Integer id) {
        return this.rolePrivilegeRepository.finAllByRoleId(id);
    }
}
