package com.tekfilo.inventory.clarity;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.color.ColorEntity;
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

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ClarityService {

    @Autowired
    ClarityRepository clarityRepository;

    public int checkNameExists(String colorName) {
        return this.clarityRepository.checkNameExists(colorName.toLowerCase());
    }

    public ClarityEntity save(ClarityDto clarityDto) throws Exception {
        return clarityRepository.save(convertToEntity(clarityDto));
    }

    private ClarityEntity convertToEntity(ClarityDto clarityDto) {
        ClarityEntity entity = new ClarityEntity();
        BeanUtils.copyProperties(clarityDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    public Page<ClarityEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection,
                                       List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.clarityRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);

    }

    public ClarityEntity findById(Integer clarityId) {
        return clarityRepository.findById(clarityId).get();
    }

    public void remove(ClarityEntity entity) {
        clarityRepository.save(entity);
    }

    public List<ClarityEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.clarityRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void removeAll(List<ClarityEntity> entities) throws Exception {
        this.clarityRepository.saveAll(entities);
    }

    public void lock(List<ClarityEntity> entities) throws Exception {
        this.clarityRepository.saveAll(entities);
    }

    public void unlock(List<ClarityEntity> entities) throws Exception {
        this.clarityRepository.saveAll(entities);
    }

    public List<ClarityEntity> getClarityList(String category) {
        return this.clarityRepository.findAllClarity(category, CompanyContext.getCurrentCompany());
    }

    public List<ClarityEntity> findListByName(String clarityName){
        if(clarityName == null)
            return new ArrayList<>();
        return this.clarityRepository.findAllByName(clarityName.toLowerCase().trim());
    }
}
