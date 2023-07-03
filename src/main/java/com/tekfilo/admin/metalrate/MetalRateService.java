package com.tekfilo.admin.metalrate;

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
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@Transactional
public class MetalRateService implements IMetalService {

    @Autowired
    MetalRateRepository metalRateRepository;

    @Override
    public Page<MetalRateEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, @RequestBody List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return metalRateRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public MetalRateEntity save(MetalRateDto metalRateDto) throws Exception {
        return metalRateRepository.save(convertToEntity(metalRateDto));
    }

    private MetalRateEntity convertToEntity(MetalRateDto metalRateDto) {
        MetalRateEntity entity = new MetalRateEntity();
        BeanUtils.copyProperties(metalRateDto, entity);
        entity.setSequence(metalRateDto.getSequence() == null ? 0 : metalRateDto.getSequence());
        entity.setIsLocked(metalRateDto.getIsLocked() == null ? 0 : metalRateDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(metalRateDto.getIsDeleted() == null ? 0 : metalRateDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(MetalRateDto metalRateDto) throws Exception {
        metalRateRepository.save(convertToEntity(metalRateDto));
    }

    @Override
    public MetalRateEntity findById(Integer id) {
        return metalRateRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(MetalRateEntity entity) throws Exception {
        metalRateRepository.save(entity);
    }


    @Override
    public List<MetalRateEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.metalRateRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<MetalRateEntity> entities) throws Exception {
        this.metalRateRepository.saveAll(entities);
    }

    @Override
    public void lock(List<MetalRateEntity> entities) throws Exception {
        this.metalRateRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<MetalRateEntity> entities) throws Exception {
        this.metalRateRepository.saveAll(entities);
    }
}
