package com.tekfilo.account.subgroup;

import com.tekfilo.account.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISubGroupService {
    Page<SubGroupEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    SubGroupEntity save(SubGroupDto groupDto) throws Exception;

    void modify(SubGroupDto groupDto) throws Exception;

    SubGroupEntity findById(Integer id);

    void remove(SubGroupEntity entity) throws Exception;

    List<SubGroupEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<SubGroupEntity> entities) throws Exception;

    void lock(List<SubGroupEntity> entities) throws Exception;

    void unlock(List<SubGroupEntity> entities) throws Exception;
}

