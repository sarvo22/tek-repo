package com.tekfilo.account.costcenter;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.costcategory.CostCategoryEntity;
import com.tekfilo.account.multitenancy.CompanyContext;
import com.tekfilo.account.multitenancy.UserContext;
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
public class CostCenterService implements ICostCenterService {

    @Autowired
    CostCenterRepository costCenterRepository;

    @Override
    public Page<CostCenterEntity> findAllMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.costCenterRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }

    @Override
    public CostCenterEntity save(CostCenterDto costCenterDto) throws Exception {
        return costCenterRepository.save(convertToEntity(costCenterDto));
    }

    private CostCenterEntity convertToEntity(CostCenterDto costCenterDto) {
        CostCenterEntity entity = new CostCenterEntity();
        BeanUtils.copyProperties(costCenterDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(costCenterDto.getSequence() == null ? 0 : costCenterDto.getSequence());
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modify(CostCenterDto costCenterDto) throws Exception {
        costCenterRepository.save(convertToEntity(costCenterDto));
    }

    @Override
    public CostCenterEntity findById(Integer id) {
        return costCenterRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(CostCenterEntity entity) throws Exception {
        costCenterRepository.save(entity);
    }
}
