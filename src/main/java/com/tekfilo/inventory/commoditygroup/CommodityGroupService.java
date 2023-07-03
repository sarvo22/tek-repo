package com.tekfilo.inventory.commoditygroup;

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

import java.util.List;

@Service
@Transactional
public class CommodityGroupService implements ICommodityGroupService {

    @Autowired
    CommodityGroupRepository commodityGroupRepository;


    @Override
    public Page<CommodityGroupEntity> findAll(int pageNo, int pageSize,
                                              String sortName, String sortDirection, List<FilterClause> filterClause) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.commodityGroupRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);

    }

    @Override
    public CommodityGroupEntity save(CommodityGroupDto commodityGroupDto) throws Exception {
        return commodityGroupRepository.save(convertToEntity(commodityGroupDto));
    }

    @Override
    public void modify(CommodityGroupDto commodityGroupDto) throws Exception {
        commodityGroupRepository.save(convertToEntity(commodityGroupDto));
    }

    @Override
    public CommodityGroupEntity findById(Integer id) {
        return commodityGroupRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(CommodityGroupEntity entity) {
        commodityGroupRepository.save(entity);
    }

    private CommodityGroupEntity convertToEntity(CommodityGroupDto commodityGroupDto) {
        CommodityGroupEntity entity = new CommodityGroupEntity();
        entity.setId(commodityGroupDto.getId());
        entity.setGroupName(commodityGroupDto.getGroupName());
        entity.setGroupType(commodityGroupDto.getGroupType());
        entity.setDescription(commodityGroupDto.getDescription());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setSequence(commodityGroupDto.getSequence() == null ? 0 : commodityGroupDto.getSequence());
        entity.setIsLocked(commodityGroupDto.getIsLocked() == null ? 0 : commodityGroupDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(commodityGroupDto.getIsDeleted() == null ? 0 : commodityGroupDto.getIsDeleted());
        return entity;
    }

    @Override
    public List<CommodityGroupEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.commodityGroupRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<CommodityGroupEntity> entities) throws Exception {
        this.commodityGroupRepository.saveAll(entities);
    }

    @Override
    public void lock(List<CommodityGroupEntity> entities) throws Exception {
        this.commodityGroupRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<CommodityGroupEntity> entities) throws Exception {
        this.commodityGroupRepository.saveAll(entities);
    }

    @Override
    public int checkNameExists(String commodityGroupName) {
        return this.commodityGroupRepository.checkNameExists(commodityGroupName.toLowerCase());
    }
}
