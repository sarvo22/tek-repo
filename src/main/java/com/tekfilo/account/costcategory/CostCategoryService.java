package com.tekfilo.account.costcategory;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.CompanyContext;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.jv.entity.JVMainEntity;
import com.tekfilo.account.util.FilterClauseAppender;
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
public class CostCategoryService implements ICostCategoryService {

    @Autowired
    CostCategoryRepository costCategoryRepository;

    @Override
    public Page<CostCategoryEntity> findAllMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.costCategoryRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }

    @Override
    public CostCategoryEntity save(CostCategoryDto costCategoryDto) throws Exception {
        return costCategoryRepository.save(convertToEntity(costCategoryDto));
    }

    private CostCategoryEntity convertToEntity(CostCategoryDto costCategoryDto) {
        CostCategoryEntity entity = new CostCategoryEntity();
        BeanUtils.copyProperties(costCategoryDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(costCategoryDto.getSequence() == null ? 0 : costCategoryDto.getSequence());
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modify(CostCategoryDto costCategoryDto) throws Exception {
        costCategoryRepository.save(convertToEntity(costCategoryDto));
    }

    @Override
    public CostCategoryEntity findById(Integer id) {
        return costCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(CostCategoryEntity entity) throws Exception {
        costCategoryRepository.save(entity);
    }
}
