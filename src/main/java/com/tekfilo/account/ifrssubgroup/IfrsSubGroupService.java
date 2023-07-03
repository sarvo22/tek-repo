package com.tekfilo.account.ifrssubgroup;

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
public class IfrsSubGroupService implements IfrsISubGroupService {

    @Autowired
    IfrsSubGroupRepository ifrsSubGroupRepository;

    @Override
    public Page<IfrsSubGroupEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.ifrsSubGroupRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }


    @Override
    public IfrsSubGroupEntity save(IfrsSubGroupDto ifrsSubGroupDto) throws Exception {
        return ifrsSubGroupRepository.save(convertToEntity(ifrsSubGroupDto));
    }

    private IfrsSubGroupEntity convertToEntity(IfrsSubGroupDto ifrsSubGroupDto) {
        IfrsSubGroupEntity entity = new IfrsSubGroupEntity();
        BeanUtils.copyProperties(ifrsSubGroupDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(ifrsSubGroupDto.getSequence() == null ? 0 : ifrsSubGroupDto.getSequence());
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modify(IfrsSubGroupDto ifrsSubGroupDto) throws Exception {
        ifrsSubGroupRepository.save(convertToEntity(ifrsSubGroupDto));
    }

    @Override
    public IfrsSubGroupEntity findById(Integer id) {
        return ifrsSubGroupRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(IfrsSubGroupEntity entity) throws Exception {
        ifrsSubGroupRepository.save(entity);
    }

    @Override
    public List<IfrsSubGroupEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.ifrsSubGroupRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<IfrsSubGroupEntity> entities) throws Exception {
        this.ifrsSubGroupRepository.saveAll(entities);
    }

    @Override
    public void lock(List<IfrsSubGroupEntity> entities) throws Exception {
        this.ifrsSubGroupRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<IfrsSubGroupEntity> entities) throws Exception {
        this.ifrsSubGroupRepository.saveAll(entities);
    }

}
