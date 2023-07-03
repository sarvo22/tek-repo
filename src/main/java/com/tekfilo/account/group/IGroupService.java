package com.tekfilo.account.group;

import com.tekfilo.account.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IGroupService {
    Page<GroupEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    GroupEntity save(GroupDto groupDto) throws Exception;

    void modify(GroupDto groupDto) throws Exception;

    GroupEntity findById(Integer id);

    void remove(GroupEntity entity) throws Exception;

    List<GroupEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<GroupEntity> entities) throws Exception;

    void lock(List<GroupEntity> entities) throws Exception;

    void unlock(List<GroupEntity> entities) throws Exception;

}
