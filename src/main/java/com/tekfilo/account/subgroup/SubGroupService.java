package com.tekfilo.account.subgroup;

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
public class SubGroupService implements ISubGroupService {

    @Autowired
    SubGroupRepository subGroupRepository;

    @Override
    public Page<SubGroupEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.subGroupRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);

    }

    @Override
    public SubGroupEntity save(SubGroupDto groupDto) throws Exception {
        return subGroupRepository.save(convertToEntity(groupDto));
    }

    private SubGroupEntity convertToEntity(SubGroupDto groupDto) {
        SubGroupEntity entity = new SubGroupEntity();
        BeanUtils.copyProperties(groupDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(groupDto.getSequence() == null ? 0 : groupDto.getSequence());
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modify(SubGroupDto groupDto) throws Exception {
        subGroupRepository.save(convertToEntity(groupDto));
    }

    @Override
    public SubGroupEntity findById(Integer id) {
        return subGroupRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(SubGroupEntity entity) throws Exception {
        subGroupRepository.save(entity);
    }

    @Override
    public List<SubGroupEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.subGroupRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<SubGroupEntity> entities) throws Exception {
        this.subGroupRepository.saveAll(entities);
    }

    @Override
    public void lock(List<SubGroupEntity> entities) throws Exception {
        this.subGroupRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<SubGroupEntity> entities) throws Exception {
        this.subGroupRepository.saveAll(entities);
    }
}
