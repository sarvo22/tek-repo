package com.tekfilo.inventory.cut;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.shape.ShapeEntity;
import com.tekfilo.inventory.util.FilterClauseAppender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CutService {

    @Autowired
    CutRepository cutRepository;

    public CutEntity save(CutDto cutDto) throws Exception {
        return cutRepository.save(convertToEntity(cutDto));
    }

    private CutEntity convertToEntity(CutDto cutDto) {
        CutEntity entity = new CutEntity();
        BeanUtils.copyProperties(cutDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    public Page<CutEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.cutRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public CutEntity findById(Integer cutId) {
        return cutRepository.findById(cutId).get();
    }

    public void remove(CutEntity entity) {
        cutRepository.save(entity);
    }

    public List<CutEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.cutRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void removeAll(List<CutEntity> entities) throws Exception {
        this.cutRepository.saveAll(entities);
    }

    public void lock(List<CutEntity> entities) throws Exception {
        this.cutRepository.saveAll(entities);
    }

    public void unlock(List<CutEntity> entities) throws Exception {
        this.cutRepository.saveAll(entities);
    }

    public List<CutEntity> getCutList(String category) {
        return this.cutRepository.findAllCuts(category, CompanyContext.getCurrentCompany());
    }

    public int checkNameExists(String cutName) {
        return this.cutRepository.checkNameExists(cutName.toLowerCase());
    }

    public List<CutEntity> findListByName(String cutName){
        if(cutName == null)
            return new ArrayList<>();
        return this.cutRepository.findAllByName(cutName.toLowerCase().trim());
    }
}
