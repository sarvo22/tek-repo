package com.tekfilo.account.costcenter;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.costcategory.CostCategoryEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICostCenterService {
    Page<CostCenterEntity> findAllMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    CostCenterEntity save(CostCenterDto costCenterDto) throws Exception;

    void modify(CostCenterDto costCenterDto) throws Exception;

    CostCenterEntity findById(Integer id);

    void remove(CostCenterEntity entity) throws Exception;
}
