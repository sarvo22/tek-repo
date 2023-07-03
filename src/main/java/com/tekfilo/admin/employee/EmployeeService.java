package com.tekfilo.admin.employee;

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

import java.text.ParseException;
import java.util.List;

@Service
@Transactional
public class EmployeeService implements IEmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Page<EmployeeEntity> findAll(int pageNo, int pageSize,
                                        String sortName, String sortDirection,
                                        List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.employeeRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public EmployeeEntity save(EmployeeDto employeeDto) throws Exception {
        return employeeRepository.save(convertToEntity(employeeDto));
    }

    private EmployeeEntity convertToEntity(EmployeeDto employeeDto) throws ParseException {
        EmployeeEntity entity = new EmployeeEntity();
        BeanUtils.copyProperties(employeeDto, entity);
        entity.setSequence(employeeDto.getSequence() == null ? 0 : employeeDto.getSequence());
        entity.setIsLocked(employeeDto.getIsLocked() == null ? 0 : employeeDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(employeeDto.getIsDeleted() == null ? 0 : employeeDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(EmployeeDto employeeDto) throws Exception {
        employeeRepository.save(convertToEntity(employeeDto));
    }

    @Override
    public EmployeeEntity findById(Integer id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(EmployeeEntity entity) throws Exception {
        employeeRepository.save(entity);
    }

    @Override
    public List<EmployeeEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.employeeRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<EmployeeEntity> entities) throws Exception {
        this.employeeRepository.saveAll(entities);
    }

    @Override
    public void lock(List<EmployeeEntity> entities) throws Exception {
        this.employeeRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<EmployeeEntity> entities) throws Exception {
        this.employeeRepository.saveAll(entities);
    }

    @Override
    public void updatePicturePath(EmployeeEntity entity) throws Exception {
        this.employeeRepository.save(entity);
    }
}
