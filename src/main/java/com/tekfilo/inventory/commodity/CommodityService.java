package com.tekfilo.inventory.commodity;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.util.FilterClauseAppender;
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
public class CommodityService implements ICommodityService {

    @Autowired
    CommodityRepository commodityRepository;


    @Override
    public Page<CommodityEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.commodityRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public CommodityEntity save(CommodityDto commodityDto) throws Exception {
        return commodityRepository.save(convertToEntity(commodityDto));
    }

    @Override
    public void modify(CommodityDto commodityDto) throws Exception {
        commodityRepository.save(convertToEntity(commodityDto));
    }

    @Override
    public CommodityEntity findById(Integer id) {
        return commodityRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(CommodityEntity entity) {
        commodityRepository.save(entity);
    }

    private CommodityEntity convertToEntity(CommodityDto commodityDto) {
        CommodityEntity entity = new CommodityEntity();
        entity.setId(commodityDto.getId());
        entity.setName(commodityDto.getName());
        entity.setGroupId(commodityDto.getGroupId());
        entity.setDescription(commodityDto.getDescription());
        entity.setSequence(commodityDto.getSequence() == null ? 0 : commodityDto.getSequence());
        entity.setIsLocked(commodityDto.getIsLocked() == null ? 0 : commodityDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(commodityDto.getIsDeleted() == null ? 0 : commodityDto.getIsDeleted());
        return entity;
    }

    @Override
    public List<CommodityEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.commodityRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<CommodityEntity> entities) throws Exception {
        this.commodityRepository.saveAll(entities);
    }

    @Override
    public void lock(List<CommodityEntity> entities) throws Exception {
        this.commodityRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<CommodityEntity> entities) throws Exception {
        this.commodityRepository.saveAll(entities);
    }

    @Override
    public int checkNameExists(String commodityName) {
        return this.commodityRepository.checkNameExists(commodityName.toLowerCase());
    }

    @Override
    public boolean isCommodityExists(String commodityName) {
        if(commodityName == null)
            return false;
        int count = this.commodityRepository.checkNameExists(commodityName.toLowerCase().trim());
        return count == 0 ? false : true;
    }

    @Override
    public List<CommodityEntity> findListByName(String commodityName){
        if(commodityName == null)
            return new ArrayList<>();
        return this.commodityRepository.findAllByCommodityName(commodityName.toLowerCase().trim());
    }
}
