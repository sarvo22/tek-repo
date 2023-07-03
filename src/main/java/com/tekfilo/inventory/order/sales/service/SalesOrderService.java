package com.tekfilo.inventory.order.sales.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.master.CustomerAddressEntity;
import com.tekfilo.inventory.master.repository.CustomerAddressRepository;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.order.sales.dto.SalesOrderDetailDto;
import com.tekfilo.inventory.order.sales.dto.SalesOrderMainDto;
import com.tekfilo.inventory.order.sales.entity.SalesOrderDetailEntity;
import com.tekfilo.inventory.order.sales.entity.SalesOrderMainEntity;
import com.tekfilo.inventory.order.sales.repository.SalesOrderDetailRepository;
import com.tekfilo.inventory.order.sales.repository.SalesOrderMainRepository;
import com.tekfilo.inventory.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    CustomerAddressRepository customerAddressRepository;

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
        if (Optional.ofNullable(salesOrderMainDto.getBillingAddressId()).isPresent()) {
            if (salesOrderMainDto.getBillingAddressId() > 0) {
                CustomerAddressEntity billingAddress = this.customerAddressRepository.findById(salesOrderMainDto.getBillingAddressId()).orElse(new CustomerAddressEntity());
                if (billingAddress.getAddressId() != null) {
                    entity.setBillingAddressId(salesOrderMainDto.getBillingAddressId());
                    entity.setBillingAddress1(billingAddress.getAddress1());
                    entity.setBillingAddress2(billingAddress.getAddress2());
                    entity.setBillingCountry(billingAddress.getCountry());
                    entity.setBillingState(billingAddress.getState());
                    entity.setBillingCity(billingAddress.getCity());
                    entity.setBillingStreet(billingAddress.getStreet());
                    entity.setBillingPinCode(billingAddress.getPinCode());
                    entity.setBillingWebsite(null);
                    entity.setBillingPhoneNo(null);
                    entity.setBillingFaxNo(null);
                    entity.setBillingMobileNo(null);
                }
            }
        }


        if (Optional.ofNullable(salesOrderMainDto.getShippingAddressId()).isPresent()) {
            if (salesOrderMainDto.getShippingAddressId() > 0) {
                CustomerAddressEntity shippingAddress = this.customerAddressRepository.findById(salesOrderMainDto.getShippingAddressId()).orElse(new CustomerAddressEntity());
                if (shippingAddress.getAddressId() != null) {
                    entity.setShippingAddressId(salesOrderMainDto.getShippingAddressId());
                    entity.setShipingAddress1(shippingAddress.getAddress1());
                    entity.setShipingAddress2(shippingAddress.getAddress2());
                    entity.setShipingCountry(shippingAddress.getCountry());
                    entity.setShipingState(shippingAddress.getState());
                    entity.setShipingCity(shippingAddress.getCity());
                    entity.setShipingStreet(shippingAddress.getStreet());
                    entity.setShipingPinCode(shippingAddress.getPinCode());
                    entity.setShipingWebsite(null);
                    entity.setShipingPhoneNo(null);
                    entity.setShipingFaxNo(null);
                    entity.setShipingMobileNo(null);
                }
            }
        }
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
    public SalesOrderDetailEntity saveSalesOrderDetail(SalesOrderDetailDto salesOrderDetailDto) throws Exception {
        return salesOrderDetailRepository.save(convertDetailToEntity(salesOrderDetailDto));
    }

    private SalesOrderDetailEntity convertDetailToEntity(SalesOrderDetailDto salesOrderDetailDto) {
        SalesOrderDetailEntity entity = new SalesOrderDetailEntity();
        BeanUtils.copyProperties(salesOrderDetailDto, entity);
        final double qty1 = salesOrderDetailDto.getQty1() == null ? 0.00 : salesOrderDetailDto.getQty1().doubleValue();
        final double spRate = salesOrderDetailDto.getSpRate() == null ? 0.00 : salesOrderDetailDto.getSpRate().doubleValue();
        entity.setSpAmount(new BigDecimal(qty1 * spRate));

        entity.setFinalSpAmount(salesOrderDetailDto.getFinalSpAmount() == null ? new BigDecimal("0.00") : salesOrderDetailDto.getFinalSpAmount());
        entity.setIsTreated(salesOrderDetailDto.getIsTreated() == null ? 0 : salesOrderDetailDto.getIsTreated());
        entity.setIsCertified(salesOrderDetailDto.getIsCertified() == null ? 0 : salesOrderDetailDto.getIsCertified());

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
    public SalesOrderDetailEntity findSalesOrderDetailById(Integer salesOrderDetailId) {
        return salesOrderDetailRepository.findById(salesOrderDetailId).orElse(new SalesOrderDetailEntity());
    }

    @Override
    public List<SalesOrderMainEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.salesOrderMainRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    @Transactional
    public void removeAll(List<SalesOrderMainEntity> entities) throws Exception {
        this.salesOrderDetailRepository.deleteDetailBySalesOrderId(entities.stream().map(SalesOrderMainEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.salesOrderMainRepository.deleteMainById(entities.stream().map(SalesOrderMainEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    public void lock(List<SalesOrderMainEntity> entities) throws Exception {
        this.salesOrderMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<SalesOrderMainEntity> entities) throws Exception {
        this.salesOrderMainRepository.saveAll(entities);
    }
}
