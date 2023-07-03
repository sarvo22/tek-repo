package com.tekfilo.admin.department;

import com.tekfilo.admin.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * {@link IDepartmentService} used for maintaining department master in Multitenant application
 *
 * @author sivaraj.d
 * @version 1.0.0
 */
public interface IDepartmentService {

    /**
     * Find All department list as @{@link org.springframework.data.domain.Page}
     *
     * @param pageNo
     * @param pageSize
     * @param sortName
     * @param sortDirection
     * @return
     */
    Page<DepartmentEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    DepartmentEntity save(DepartmentDto departmentDto) throws Exception;

    void modify(DepartmentDto departmentDto) throws Exception;

    DepartmentEntity findById(Integer id);

    void remove(DepartmentEntity entity) throws Exception;


    List<DepartmentEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<DepartmentEntity> entities) throws Exception;

    void lock(List<DepartmentEntity> entities) throws Exception;

    void unlock(List<DepartmentEntity> entities) throws Exception;

}
