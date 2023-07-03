package com.tekfilo.account.schedule;

import com.tekfilo.account.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IScheduleService {
    Page<ScheduleEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    ScheduleEntity save(ScheduleDto scheduleDto) throws Exception;

    void modify(ScheduleDto scheduleDto) throws Exception;

    ScheduleEntity findById(Integer id);

    void remove(ScheduleEntity entity) throws Exception;

    List<ScheduleEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<ScheduleEntity> entities) throws Exception;

    void lock(List<ScheduleEntity> entities) throws Exception;

    void unlock(List<ScheduleEntity> entities) throws Exception;
}
