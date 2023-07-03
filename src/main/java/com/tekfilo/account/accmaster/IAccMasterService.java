package com.tekfilo.account.accmaster;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.transaction.clone.ClonePayload;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAccMasterService {
    Page<AccMasterEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    AccMasterEntity save(AccMasterDto accMasterDto) throws Exception;

    void modify(AccMasterDto accMasterDto) throws Exception;

    AccMasterEntity findById(Integer id);

    void remove(AccMasterEntity entity) throws Exception;

    List<AccMasterEntity> getAccountList(String searchKey);

    List<AccMasterEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<AccMasterEntity> entities) throws Exception;

    void lock(List<AccMasterEntity> entities) throws Exception;

    void unlock(List<AccMasterEntity> entities) throws Exception;

    void cloneAccountMaster(ClonePayload clonePayload) throws Exception;
}
