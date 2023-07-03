package com.tekfilo.admin.metalrate;

import com.tekfilo.admin.base.FilterClause;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IMetalService {
    Page<MetalRateEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, @RequestBody List<FilterClause> filterClause);

    MetalRateEntity save(MetalRateDto metalRateDto) throws Exception;

    void modify(MetalRateDto metalRateDto) throws Exception;

    MetalRateEntity findById(Integer id);

    void remove(MetalRateEntity entity) throws Exception;

    List<MetalRateEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<MetalRateEntity> entities) throws Exception;

    void lock(List<MetalRateEntity> entities) throws Exception;

    void unlock(List<MetalRateEntity> entities) throws Exception;
}
