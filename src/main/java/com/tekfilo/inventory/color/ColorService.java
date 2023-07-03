package com.tekfilo.inventory.color;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.cut.CutEntity;
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
public class ColorService {

    @Autowired
    ColorRepository colorRepository;

    public ColorEntity save(ColorDto colorDto) throws Exception {
        return colorRepository.save(convertToEntity(colorDto));
    }

    private ColorEntity convertToEntity(ColorDto colorDto) {
        ColorEntity entity = new ColorEntity();
        BeanUtils.copyProperties(colorDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    public Page<ColorEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.colorRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public ColorEntity findById(Integer colorId) {
        return colorRepository.findById(colorId).get();
    }

    public void remove(ColorEntity entity) {
        colorRepository.save(entity);
    }

    public List<ColorEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.colorRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void removeAll(List<ColorEntity> entities) throws Exception {
        this.colorRepository.saveAll(entities);
    }

    public void lock(List<ColorEntity> entities) throws Exception {
        this.colorRepository.saveAll(entities);
    }

    public void unlock(List<ColorEntity> entities) throws Exception {
        this.colorRepository.saveAll(entities);
    }


    public List<ColorEntity> getColorList(String category) {
        return this.colorRepository.findAllColors(category, CompanyContext.getCurrentCompany());
    }

    public int checkNameExists(String colorName) {
        return this.colorRepository.checkNameExists(colorName.toLowerCase());
    }

    public List<ColorEntity> findListByName(String colorName){
        if(colorName == null)
            return new ArrayList<>();
        return this.colorRepository.findAllByName(colorName.toLowerCase().trim());
    }
}
