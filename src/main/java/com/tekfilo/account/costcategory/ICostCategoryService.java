package com.tekfilo.account.costcategory;

import com.tekfilo.account.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICostCategoryService {
    Page<CostCategoryEntity> findAllMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    CostCategoryEntity save(CostCategoryDto costCategoryDto) throws Exception;

    void modify(CostCategoryDto costCategoryDto) throws Exception;

    CostCategoryEntity findById(Integer id);

    void remove(CostCategoryEntity entity) throws Exception;
}
