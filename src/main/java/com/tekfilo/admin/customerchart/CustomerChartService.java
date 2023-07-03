package com.tekfilo.admin.customerchart;

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
public class CustomerChartService {

    @Autowired
    CustomerChartRepository customerChartRepository;

    @Autowired
    CustomerChartDetailRepository customerChartDetailRepository;

    public Page<CustomerChartEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.customerChartRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public CustomerChartEntity save(CustomerChartDto customerChartDto) throws Exception {
        return customerChartRepository.save(convertToEntity(customerChartDto));
    }

    private CustomerChartEntity convertToEntity(CustomerChartDto customerChartDto) {
        CustomerChartEntity entity = new CustomerChartEntity();
        BeanUtils.copyProperties(customerChartDto, entity);
        entity.setSortSequence(0);
        entity.setIsLocked(0);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    private CustomerChartDetailEntity convertToDetailEntity(CustomerChartDetailDto customerChartDetailDto) {
        CustomerChartDetailEntity entity = new CustomerChartDetailEntity();
        BeanUtils.copyProperties(customerChartDetailDto, entity);
        entity.setSortSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void modify(CustomerChartDto customerChartDto) throws Exception {
        customerChartRepository.save(convertToEntity(customerChartDto));
    }


    public CustomerChartEntity findById(Integer id) {
        return customerChartRepository.findById(id).orElse(null);
    }

    public void remove(CustomerChartEntity entity) throws Exception {
        customerChartRepository.save(entity);
    }

    public CustomerChartDetailEntity saveDetail(CustomerChartDetailDto customerChartDetailDto) throws Exception {
        return customerChartDetailRepository.save(convertToDetailEntity(customerChartDetailDto));
    }

    public CustomerChartDetailEntity findDetailById(Integer id) {
        return customerChartDetailRepository.findById(id).orElse(null);
    }

    public List<CustomerChartDetailEntity> findDetailByMainId(Integer id) {
        return customerChartDetailRepository.findDetailByMainId(id);
    }

    public void removeDetail(CustomerChartDetailEntity entity) throws Exception {
        customerChartDetailRepository.save(entity);
    }

}
