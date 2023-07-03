package com.tekfilo.account.ifrsgroup;

import com.tekfilo.account.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IIfrsGroupService {
    Page<IfrsGroupEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    IfrsGroupEntity save(IfrsGroupDto ifrsGroupDto) throws Exception;

    void modify(IfrsGroupDto ifrsGroupDto) throws Exception;

    IfrsGroupEntity findById(Integer id);

    void remove(IfrsGroupEntity entity) throws Exception;

    List<IfrsGroupEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<IfrsGroupEntity> entities) throws Exception;

    void lock(List<IfrsGroupEntity> entities) throws Exception;

    void unlock(List<IfrsGroupEntity> entities) throws Exception;
}
