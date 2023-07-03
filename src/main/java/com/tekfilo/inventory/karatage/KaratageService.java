package com.tekfilo.inventory.karatage;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.util.FilterClauseAppender;
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
public class KaratageService {

    @Autowired
    KaratageRepository karatageRepository;

    public KaratageEntity save(KaratageDto karatageDto) throws Exception {
        return karatageRepository.save(convertToEntity(karatageDto));
    }

    private KaratageEntity convertToEntity(KaratageDto karatageDto) {
        KaratageEntity entity = new KaratageEntity();
        BeanUtils.copyProperties(karatageDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    public int checkNameExists(String name) {
        return this.karatageRepository.checkNameExists(name.toLowerCase());
    }

    public Page<KaratageEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.karatageRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);

    }

    public KaratageEntity findById(Integer karatageId) {
        return karatageRepository.findById(karatageId).get();
    }

    public void remove(KaratageEntity entity) {
        karatageRepository.save(entity);
    }

    public List<KaratageEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.karatageRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void removeAll(List<KaratageEntity> entities) throws Exception {
        this.karatageRepository.saveAll(entities);
    }

    public void lock(List<KaratageEntity> entities) throws Exception {
        this.karatageRepository.saveAll(entities);
    }

    public void unlock(List<KaratageEntity> entities) throws Exception {
        this.karatageRepository.saveAll(entities);
    }
}
