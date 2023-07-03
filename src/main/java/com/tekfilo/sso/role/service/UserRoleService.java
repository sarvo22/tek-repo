package com.tekfilo.sso.role.service;

import com.tekfilo.sso.role.entity.UserRoleEntity;
import com.tekfilo.sso.role.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserRoleService {

    @Autowired
    UserRoleRepository userRoleRepository;

    public boolean findUserRoleExists(String roleId, Integer userId) {
        List<UserRoleEntity> userRoleEntityList = userRoleRepository.findUserRolesByRoleAndUserId(Integer.parseInt(roleId), userId);
        return userRoleEntityList.size() > 0;
    }
}
