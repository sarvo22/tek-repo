package com.tekfilo.account.ifrsschedule;

import com.tekfilo.account.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IIfrsScheduleService {
    Page<IfrsScheduleEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    IfrsScheduleEntity save(IfrsScheduleDto scheduleDto) throws Exception;

    void modify(IfrsScheduleDto scheduleDto) throws Exception;

    IfrsScheduleEntity findById(Integer id);

    void remove(IfrsScheduleEntity entity) throws Exception;

    List<IfrsScheduleEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<IfrsScheduleEntity> entities) throws Exception;

    void lock(List<IfrsScheduleEntity> entities) throws Exception;

    void unlock(List<IfrsScheduleEntity> entities) throws Exception;
}
