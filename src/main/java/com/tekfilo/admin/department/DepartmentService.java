package com.tekfilo.admin.department;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.multitenancy.CompanyContext;
import com.tekfilo.admin.multitenancy.UserContext;
import com.tekfilo.admin.util.FilterClauseAppender;
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
public class DepartmentService implements IDepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public Page<DepartmentEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.departmentRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public DepartmentEntity save(DepartmentDto departmentDto) throws Exception {
        return departmentRepository.save(convertToEntity(departmentDto));
    }

    private DepartmentEntity convertToEntity(DepartmentDto departmentDto) {
        DepartmentEntity entity = new DepartmentEntity();
        BeanUtils.copyProperties(departmentDto, entity);
        entity.setSequence(departmentDto.getSequence() == null ? 0 : departmentDto.getSequence());
        entity.setIsLocked(departmentDto.getIsLocked() == null ? 0 : departmentDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(departmentDto.getIsDeleted() == null ? 0 : departmentDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(DepartmentDto departmentDto) throws Exception {
        departmentRepository.save(convertToEntity(departmentDto));
    }

    @Override
    public DepartmentEntity findById(Integer id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(DepartmentEntity entity) throws Exception {
        departmentRepository.save(entity);
    }


    @Override
    public List<DepartmentEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.departmentRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<DepartmentEntity> entities) throws Exception {
        this.departmentRepository.saveAll(entities);
    }

    @Override
    public void lock(List<DepartmentEntity> entities) throws Exception {
        this.departmentRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<DepartmentEntity> entities) throws Exception {
        this.departmentRepository.saveAll(entities);
    }
}
