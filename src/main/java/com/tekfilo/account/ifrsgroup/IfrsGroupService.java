package com.tekfilo.account.ifrsgroup;

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
public class IfrsGroupService implements IIfrsGroupService {

    @Autowired
    IfrsGroupRepository ifrsGroupRepository;

    @Override
    public Page<IfrsGroupEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.ifrsGroupRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }

    @Override
    public IfrsGroupEntity save(IfrsGroupDto ifrsGroupDto) throws Exception {
        return ifrsGroupRepository.save(convertToEntity(ifrsGroupDto));
    }

    private IfrsGroupEntity convertToEntity(IfrsGroupDto ifrsGroupDto) {
        IfrsGroupEntity entity = new IfrsGroupEntity();
        BeanUtils.copyProperties(ifrsGroupDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(ifrsGroupDto.getSequence() == null ? 0 : ifrsGroupDto.getSequence());
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modify(IfrsGroupDto ifrsGroupDto) throws Exception {
        ifrsGroupRepository.save(convertToEntity(ifrsGroupDto));
    }

    @Override
    public IfrsGroupEntity findById(Integer id) {
        return ifrsGroupRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(IfrsGroupEntity entity) throws Exception {
        ifrsGroupRepository.save(entity);
    }

    @Override
    public List<IfrsGroupEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.ifrsGroupRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<IfrsGroupEntity> entities) throws Exception {
        this.ifrsGroupRepository.saveAll(entities);
    }

    @Override
    public void lock(List<IfrsGroupEntity> entities) throws Exception {
        this.ifrsGroupRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<IfrsGroupEntity> entities) throws Exception {
        this.ifrsGroupRepository.saveAll(entities);
    }
}
