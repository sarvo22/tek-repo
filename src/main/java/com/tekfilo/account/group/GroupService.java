package com.tekfilo.account.group;

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
public class GroupService implements IGroupService {

    @Autowired
    GroupRepository groupRepository;

    @Override
    public Page<GroupEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.groupRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }

    @Override
    public GroupEntity save(GroupDto groupDto) throws Exception {
        return groupRepository.save(convertToEntity(groupDto));
    }

    private GroupEntity convertToEntity(GroupDto groupDto) {
        GroupEntity entity = new GroupEntity();
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
    public void modify(GroupDto groupDto) throws Exception {
        groupRepository.save(convertToEntity(groupDto));
    }

    @Override
    public GroupEntity findById(Integer id) {
        return groupRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(GroupEntity entity) throws Exception {
        groupRepository.save(entity);
    }

    @Override
    public List<GroupEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.groupRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<GroupEntity> entities) throws Exception {
        this.groupRepository.saveAll(entities);
    }

    @Override
    public void lock(List<GroupEntity> entities) throws Exception {
        this.groupRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<GroupEntity> entities) throws Exception {
        this.groupRepository.saveAll(entities);
    }
}
