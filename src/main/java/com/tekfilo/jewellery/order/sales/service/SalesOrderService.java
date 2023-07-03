package com.tekfilo.jewellery.order.sales.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderMainEntity;
import com.tekfilo.jewellery.order.sales.dto.SalesOrderDetailDto;
import com.tekfilo.jewellery.order.sales.dto.SalesOrderMainDto;
import com.tekfilo.jewellery.order.sales.entity.SalesOrderDetailEntity;
import com.tekfilo.jewellery.order.sales.entity.SalesOrderMainEntity;
import com.tekfilo.jewellery.order.sales.repository.SalesOrderDetailRepository;
import com.tekfilo.jewellery.order.sales.repository.SalesOrderMainRepository;
import com.tekfilo.jewellery.util.FilterClauseAppender;
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
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class SalesOrderService implements ISalesOrderService {

    @Autowired
    SalesOrderMainRepository salesOrderMainRepository;

    @Autowired
    SalesOrderDetailRepository salesOrderDetailRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Override
    public Page<SalesOrderMainEntity> findAllSalesOrders(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.salesOrderMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public SalesOrderMainEntity findSalesOrderById(Integer id) {
        return Optional.ofNullable(salesOrderMainRepository.findById(id)).get().orElse(null);
    }

    @Override
    public List<SalesOrderDetailEntity> findSalesOrderDetailListByMain(Integer salesOrderId) {
        return salesOrderDetailRepository.findAllByMain(salesOrderId);
    }

    @Override
    public Integer saveSalesOrderMain(SalesOrderMainDto salesOrderMainDto) throws Exception {
        SalesOrderMainEntity createEntity = convertMainDtoToEntity(salesOrderMainDto);
        if (!Optional.ofNullable(salesOrderMainDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(salesOrderMainDto.getSalesOrderType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setSalesOrderNo(nextNumber);
        }
        SalesOrderMainEntity entity = salesOrderMainRepository.save(createEntity);
        return entity.getId();
    }

    private SalesOrderMainEntity convertMainDtoToEntity(SalesOrderMainDto salesOrderMainDto) {
        SalesOrderMainEntity entity = new SalesOrderMainEntity();
        BeanUtils.copyProperties(salesOrderMainDto, entity);
        entity.setSalesOrderDate(salesOrderMainDto.getSalesOrderDate());
        entity.setDeliveryDate(salesOrderMainDto.getDeliveryDate());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modifySalesOrderMain(SalesOrderMainDto salesOrderMainDto) throws Exception {
        salesOrderMainRepository.save(convertMainDtoToEntity(salesOrderMainDto));
    }

    @Override
    public void removeSalesOrderMain(SalesOrderMainDto salesOrderMainDto) throws Exception {

    }

    @Override
    public void saveSalesOrderDetail(SalesOrderDetailDto salesOrderDetailDto) throws Exception {
        salesOrderDetailRepository.save(convertDetailToEntity(salesOrderDetailDto));
    }

    private SalesOrderDetailEntity convertDetailToEntity(SalesOrderDetailDto salesOrderDetailDto) {
        SalesOrderDetailEntity entity = new SalesOrderDetailEntity();
        BeanUtils.copyProperties(salesOrderDetailDto, entity);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modifySalesOrderDetail(SalesOrderDetailDto salesOrderDetailDto) throws Exception {
        salesOrderDetailRepository.save(convertDetailToEntity(salesOrderDetailDto));
    }

    @Override
    public void removeSalesOrderDetail(Integer salesOrderDetailId, Integer operateBy) throws Exception {
        salesOrderDetailRepository.remove(salesOrderDetailId, operateBy);
    }

    @Override
    public void removeSalesOrderDetailByMain(Integer salesOrderId, Integer operateBy) throws Exception {

    }

    @Override
    public void removeSalesOrderMainById(Integer salesOrderId, Integer operateBy) throws Exception {
        salesOrderMainRepository.removeByMain(salesOrderId, operateBy);
    }

    @Override
    public void removeSalesOrder(Integer salesOrderId, Integer operateBy) throws Exception {
        salesOrderDetailRepository.removeByMain(salesOrderId, operateBy);
        salesOrderMainRepository.removeByMain(salesOrderId, operateBy);
    }


    @Override
    public  List<SalesOrderMainEntity> findMainListByIds(List<Integer> ids){
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.salesOrderMainRepository.findAll(filterClauseAppender.getInClassFilter(ids,"id"));
    }


    @Override
    public  void lock(List<SalesOrderMainEntity> entities) throws Exception{
        this.salesOrderMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<SalesOrderMainEntity> entities) throws Exception{
        this.salesOrderMainRepository.saveAll(entities);
    }
}
