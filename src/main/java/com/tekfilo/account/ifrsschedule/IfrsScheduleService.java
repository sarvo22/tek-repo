package com.tekfilo.account.ifrsschedule;

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
public class IfrsScheduleService implements IIfrsScheduleService {

    @Autowired
    IfrsScheduleRepository ifrsScheduleRepository;

    @Override
    public Page<IfrsScheduleEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.ifrsScheduleRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }

    @Override
    public IfrsScheduleEntity save(IfrsScheduleDto ifrsScheduleDto) throws Exception {
        return ifrsScheduleRepository.save(convertToEntity(ifrsScheduleDto));
    }

    private IfrsScheduleEntity convertToEntity(IfrsScheduleDto ifrsScheduleDto) {
        IfrsScheduleEntity entity = new IfrsScheduleEntity();
        BeanUtils.copyProperties(ifrsScheduleDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(ifrsScheduleDto.getSequence() == null ? 0 : ifrsScheduleDto.getSequence());
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modify(IfrsScheduleDto ifrsScheduleDto) throws Exception {
        ifrsScheduleRepository.save(convertToEntity(ifrsScheduleDto));
    }

    @Override
    public IfrsScheduleEntity findById(Integer id) {
        return ifrsScheduleRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(IfrsScheduleEntity entity) throws Exception {
        ifrsScheduleRepository.save(entity);
    }

    @Override
    public List<IfrsScheduleEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.ifrsScheduleRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<IfrsScheduleEntity> entities) throws Exception {
        this.ifrsScheduleRepository.saveAll(entities);
    }

    @Override
    public void lock(List<IfrsScheduleEntity> entities) throws Exception {
        this.ifrsScheduleRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<IfrsScheduleEntity> entities) throws Exception {
        this.ifrsScheduleRepository.saveAll(entities);
    }
}
