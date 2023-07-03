package com.tekfilo.admin.factorychart;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.multitenancy.CompanyContext;
import com.tekfilo.admin.multitenancy.UserContext;
import com.tekfilo.admin.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class FactoryChartService {

    @Autowired
    FactoryChartRepository factoryChartRepository;

    @Autowired
    FactoryChartDetailRepository factoryChartDetailRepository;

    public Page<FactoryChartEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.factoryChartRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public FactoryChartEntity save(FactoryChartDto factoryChartDto) throws Exception {
        return factoryChartRepository.save(convertToEntity(factoryChartDto));
    }

    private FactoryChartEntity convertToEntity(FactoryChartDto factoryChartDto) {
        FactoryChartEntity entity = new FactoryChartEntity();
        BeanUtils.copyProperties(factoryChartDto, entity);
        entity.setSortSequence(0);
        entity.setIsLocked(0);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    private FactoryChartDetailEntity convertToDetailEntity(FactoryChartDetailDto factoryChartDetailDto) {
        FactoryChartDetailEntity entity = new FactoryChartDetailEntity();
        BeanUtils.copyProperties(factoryChartDetailDto, entity);
        entity.setSortSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void modify(FactoryChartDto factoryChartDto) throws Exception {
        factoryChartRepository.save(convertToEntity(factoryChartDto));
    }


    public FactoryChartEntity findById(Integer id) {
        return factoryChartRepository.findById(id).orElse(null);
    }

    public void remove(FactoryChartEntity entity) throws Exception {
        factoryChartRepository.save(entity);
    }

    public FactoryChartDetailEntity saveDetail(FactoryChartDetailDto factoryChartDetailDto) throws Exception {
        return factoryChartDetailRepository.save(convertToDetailEntity(factoryChartDetailDto));
    }

    public FactoryChartDetailEntity findDetailById(Integer id) {
        return factoryChartDetailRepository.findById(id).orElse(null);
    }

    public List<FactoryChartDetailEntity> findDetailByMainId(Integer id) {
        return factoryChartDetailRepository.findDetailByMainId(id);
    }

    public void removeDetail(FactoryChartDetailEntity entity) throws Exception {
        factoryChartDetailRepository.save(entity);
    }

}
