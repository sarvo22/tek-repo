package com.tekfilo.admin.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


    public Page<RoleEntity> findAll(int pageNo, int pageSize, String sortColumn, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ?
                PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).descending());
        Page<RoleEntity> pagedList = roleRepository.findAll(pageable);
        return pagedList;
    }
}
