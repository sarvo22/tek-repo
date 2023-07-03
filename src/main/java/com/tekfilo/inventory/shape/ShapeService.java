package com.tekfilo.inventory.shape;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.commodity.CommodityEntity;
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
public class ShapeService {

    @Autowired
    ShapeRepository shapeRepository;

    public ShapeService(ShapeRepository shapeRepository) {
        this.shapeRepository = shapeRepository;
    }

    public ShapeEntity save(ShapeDto shapeDto) throws Exception {
        return shapeRepository.save(convertToEntity(shapeDto));
    }

    private ShapeEntity convertToEntity(ShapeDto shapeDto) {
        ShapeEntity entity = new ShapeEntity();
        BeanUtils.copyProperties(shapeDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    public Page<ShapeEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.shapeRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public ShapeEntity findById(Integer shapeId) {
        return shapeRepository.findById(shapeId).get();
    }

    public void remove(ShapeEntity entity) {
        shapeRepository.save(entity);
    }

    public List<ShapeEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.shapeRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void removeAll(List<ShapeEntity> entities) throws Exception {
        this.shapeRepository.saveAll(entities);
    }

    public void lock(List<ShapeEntity> entities) throws Exception {
        this.shapeRepository.saveAll(entities);
    }

    public void unlock(List<ShapeEntity> entities) throws Exception {
        this.shapeRepository.saveAll(entities);
    }

    public List<ShapeEntity> getShapeList(String category) {
        return this.shapeRepository.findShapeList(category, CompanyContext.getCurrentCompany());

    }

    public List<ShapeEntity> findListByName(String shapeName){
        if(shapeName == null)
            return new ArrayList<>();
        return this.shapeRepository.findAllByName(shapeName.toLowerCase().trim());
    }
}
