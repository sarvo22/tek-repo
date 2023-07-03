package com.tekfilo.account.schedule;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.CompanyContext;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.util.FilterClauseAppender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScheduleService implements IScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Override
    public Page<ScheduleEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.scheduleRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);

    }

    @Override
    public ScheduleEntity save(ScheduleDto scheduleDto) throws Exception {
        return scheduleRepository.save(convertToEntity(scheduleDto));
    }

    private ScheduleEntity convertToEntity(ScheduleDto scheduleDto) {
        ScheduleEntity entity = new ScheduleEntity();
        BeanUtils.copyProperties(scheduleDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(scheduleDto.getSequence() == null ? 0 : scheduleDto.getSequence());
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modify(ScheduleDto scheduleDto) throws Exception {
        scheduleRepository.save(convertToEntity(scheduleDto));
    }

    @Override
    public ScheduleEntity findById(Integer id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(ScheduleEntity entity) throws Exception {
        scheduleRepository.save(entity);
    }

    @Override
    public List<ScheduleEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.scheduleRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<ScheduleEntity> entities) throws Exception {
        this.scheduleRepository.saveAll(entities);
    }

    @Override
    public void lock(List<ScheduleEntity> entities) throws Exception {
        this.scheduleRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<ScheduleEntity> entities) throws Exception {
        this.scheduleRepository.saveAll(entities);
    }
}
