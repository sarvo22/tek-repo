package com.tekfilo.admin.process;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.multitenancy.CompanyContext;
import com.tekfilo.admin.multitenancy.UserContext;
import com.tekfilo.admin.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ProcessService {
    @Autowired
    ProcessRepository processRepository;


    public Page<ProcessEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.processRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public ProcessEntity save(ProcessEntity processEntity) throws Exception {
        processEntity.setSequence(processEntity.getSequence() == null ? 0 : processEntity.getSequence());
        processEntity.setIsLocked(processEntity.getIsLocked() == null ? 0 : processEntity.getIsLocked());
        processEntity.setCompanyId(CompanyContext.getCurrentCompany());
        processEntity.setCreatedBy(UserContext.getLoggedInUser());
        processEntity.setModifiedBy(UserContext.getLoggedInUser());
        processEntity.setIsDeleted(processEntity.getIsDeleted() == null ? 0 : processEntity.getIsDeleted());
        return processRepository.save(processEntity);
    }


    public ProcessEntity findById(Integer id) {
        return processRepository.findById(id).orElseThrow(()->new RuntimeException("Requested Process details not found for Id " + id));
    }

    public void remove(ProcessEntity entity) throws Exception {
        processRepository.save(entity);
    }


    public List<ProcessEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.processRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void removeAll(List<ProcessEntity> entities) throws Exception {
        this.processRepository.saveAll(entities);
    }

    public void lock(List<ProcessEntity> entities) throws Exception {
        this.processRepository.saveAll(entities);
    }

    public void unlock(List<ProcessEntity> entities) throws Exception {
        this.processRepository.saveAll(entities);
    }
}
