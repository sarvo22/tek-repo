package com.tekfilo.account.ifrssubgroup;

import com.tekfilo.account.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IfrsISubGroupService {
    Page<IfrsSubGroupEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    IfrsSubGroupEntity save(IfrsSubGroupDto groupDto) throws Exception;

    void modify(IfrsSubGroupDto groupDto) throws Exception;

    IfrsSubGroupEntity findById(Integer id);

    void remove(IfrsSubGroupEntity entity) throws Exception;


    List<IfrsSubGroupEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<IfrsSubGroupEntity> entities) throws Exception;

    void lock(List<IfrsSubGroupEntity> entities) throws Exception;

    void unlock(List<IfrsSubGroupEntity> entities) throws Exception;
}
