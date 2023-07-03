package com.tekfilo.inventory.order.purchase.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.master.SupplierAddressEntity;
import com.tekfilo.inventory.master.repository.SupplierAddressRepository;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.order.purchase.dto.PurchaseOrderDetailDto;
import com.tekfilo.inventory.order.purchase.dto.PurchaseOrderMainDto;
import com.tekfilo.inventory.order.purchase.entity.PurchaseOrderDetailEntity;
import com.tekfilo.inventory.order.purchase.entity.PurchaseOrderMainEntity;
import com.tekfilo.inventory.order.purchase.repository.PurchaseOrderDetailRepository;
import com.tekfilo.inventory.order.purchase.repository.PurchaseOrderMainRepository;
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
public class PurchaseOrderService implements IPurchaseOrderService {

    @Autowired
    PurchaseOrderMainRepository purchaseOrderMainRepository;

    @Autowired
    PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    @Autowired
    SupplierAddressRepository supplierAddressRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Override
    public Page<PurchaseOrderMainEntity> findAllPurchaseOrders(int pageNo, int pageSize, String sortName, String sortDirection,
                                                               List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.purchaseOrderMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public PurchaseOrderMainEntity findPurchaseOrderById(Integer id) {
        return Optional.ofNullable(purchaseOrderMainRepository.findById(id)).get().orElse(null);
    }

    @Override
    public List<PurchaseOrderDetailEntity> findPurchaseOrderDetailListByMain(Integer purchaseOrderId) {
        return purchaseOrderDetailRepository.findAllByMain(purchaseOrderId);
    }

    @Override
    public Integer savePurchaseOrderMain(PurchaseOrderMainDto purchaseOrderMainDto) throws Exception {
        PurchaseOrderMainEntity createEntity = convertMainDtoToEntity(purchaseOrderMainDto);
        if (!Optional.ofNullable(purchaseOrderMainDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(purchaseOrderMainDto.getPurchaseOrderType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setPurchaseOrderNo(nextNumber);
        }
        PurchaseOrderMainEntity entity = purchaseOrderMainRepository.save(createEntity);
        return entity.getId();
    }

    private PurchaseOrderMainEntity convertMainDtoToEntity(PurchaseOrderMainDto purchaseOrderMainDto) {
        PurchaseOrderMainEntity entity = new PurchaseOrderMainEntity();
        BeanUtils.copyProperties(purchaseOrderMainDto, entity);
        entity.setPurchaseOrderDate(purchaseOrderMainDto.getPurchaseOrderDate());
        entity.setDeliveryDate(purchaseOrderMainDto.getDeliveryDate());
        if (Optional.ofNullable(purchaseOrderMainDto.getBillingAddressId()).isPresent()) {
            if (purchaseOrderMainDto.getBillingAddressId() > 0) {
                SupplierAddressEntity billingAddress = this.supplierAddressRepository.findById(purchaseOrderMainDto.getBillingAddressId()).orElse(new SupplierAddressEntity());
                if (billingAddress.getAddressId() != null) {
                    entity.setBillingAddressId(purchaseOrderMainDto.getBillingAddressId());
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


        if (Optional.ofNullable(purchaseOrderMainDto.getShippingAddressId()).isPresent()) {
            if (purchaseOrderMainDto.getShippingAddressId() > 0) {
                SupplierAddressEntity shippingAddress = this.supplierAddressRepository.findById(purchaseOrderMainDto.getShippingAddressId()).orElse(new SupplierAddressEntity());
                if (shippingAddress.getAddressId() != null) {
                    entity.setShippingAddressId(purchaseOrderMainDto.getShippingAddressId());
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
    public void modifyPurchaseOrderMain(PurchaseOrderMainDto purchaseOrderMainDto) throws Exception {
        purchaseOrderMainRepository.save(convertMainDtoToEntity(purchaseOrderMainDto));
    }

    @Override
    public void removePurchaseOrderMain(PurchaseOrderMainDto purchaseOrderMainDto) throws Exception {

    }

    @Override
    public Integer savePurchaseOrderDetail(PurchaseOrderDetailDto purchaseOrderDetailDto) throws Exception {
        PurchaseOrderDetailEntity entity = purchaseOrderDetailRepository.save(convertDetailToEntity(purchaseOrderDetailDto));
        return entity.getId();
    }

    private PurchaseOrderDetailEntity convertDetailToEntity(PurchaseOrderDetailDto purchaseOrderDetailDto) {
        PurchaseOrderDetailEntity entity = new PurchaseOrderDetailEntity();
        BeanUtils.copyProperties(purchaseOrderDetailDto, entity);
        final double qty1 = purchaseOrderDetailDto.getQty1() == null ? 0.00 : purchaseOrderDetailDto.getQty1().doubleValue();
        final double ppRate = purchaseOrderDetailDto.getPpRate() == null ? 0.00 : purchaseOrderDetailDto.getPpRate().doubleValue();
        entity.setPpAmount(new BigDecimal(qty1 * ppRate));
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public void modifyPurchaseOrderDetail(PurchaseOrderDetailDto purchaseOrderDetailDto) throws Exception {
        purchaseOrderDetailRepository.save(convertDetailToEntity(purchaseOrderDetailDto));
    }

    @Override
    public void removePurchaseOrderDetail(Integer purchaseOrderDetailId, Integer operateBy) throws Exception {
        purchaseOrderDetailRepository.remove(purchaseOrderDetailId, operateBy);
    }

    @Override
    public void removePurchaseOrderDetailByMain(Integer purchaseOrderId, Integer operateBy) throws Exception {

    }

    @Override
    public void removePurchaseOrderMainById(Integer purchaseOrderId, Integer operateBy) throws Exception {
        purchaseOrderMainRepository.removeByMain(purchaseOrderId, operateBy);
    }

    @Override
    public void removePurchaseOrder(Integer purchaseOrderId, Integer operateBy) throws Exception {
        purchaseOrderDetailRepository.removeByMain(purchaseOrderId, operateBy);
        purchaseOrderMainRepository.removeByMain(purchaseOrderId, operateBy);
    }

    @Override
    public PurchaseOrderDetailEntity findPurchaseOrderDetailById(Integer purchaseOrderDetailId) {
        return this.purchaseOrderDetailRepository.findById(purchaseOrderDetailId).orElse(new PurchaseOrderDetailEntity());
    }


    @Override
    public List<PurchaseOrderMainEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.purchaseOrderMainRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    @Transactional
    public void removeAll(List<PurchaseOrderMainEntity> entities) throws Exception {
        this.purchaseOrderDetailRepository.deleteDetailByPurchaseOrderId(entities.stream().map(PurchaseOrderMainEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.purchaseOrderMainRepository.deleteMainById(entities.stream().map(PurchaseOrderMainEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    public void lock(List<PurchaseOrderMainEntity> entities) throws Exception {
        this.purchaseOrderMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<PurchaseOrderMainEntity> entities) throws Exception {
        this.purchaseOrderMainRepository.saveAll(entities);
    }
}
