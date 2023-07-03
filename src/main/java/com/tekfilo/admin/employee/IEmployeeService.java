package com.tekfilo.admin.employee;

import com.tekfilo.admin.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IEmployeeService {

    Page<EmployeeEntity> findAll(int pageNo, int pageSize, String sortName,
                                 String sortDirection, List<FilterClause> filterClause);

    EmployeeEntity save(EmployeeDto employeeDto) throws Exception;

    void modify(EmployeeDto employeeDto) throws Exception;

    EmployeeEntity findById(Integer id);

    void remove(EmployeeEntity entity) throws Exception;

    List<EmployeeEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<EmployeeEntity> entities) throws Exception;

    void lock(List<EmployeeEntity> entities) throws Exception;

    void unlock(List<EmployeeEntity> entities) throws Exception;

    void updatePicturePath(EmployeeEntity entity) throws Exception;
}
