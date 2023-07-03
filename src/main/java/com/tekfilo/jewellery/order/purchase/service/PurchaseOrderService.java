package com.tekfilo.jewellery.order.purchase.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.master.SupplierAddressEntity;
import com.tekfilo.jewellery.master.repository.SupplierAddressRepository;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.order.purchase.dto.*;
import com.tekfilo.jewellery.order.purchase.entity.*;
import com.tekfilo.jewellery.order.purchase.repository.*;
import com.tekfilo.jewellery.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
    PurchaseOrderComponentRepository purchaseOrderComponentRepository;

    @Autowired
    PurchaseOrderDetailMouldPartRepository purchaseOrderDetailMouldPartRepository;

    @Autowired
    PurchaseOrderDetailLabourRepository purchaseOrderDetailLabourRepository;

    @Autowired
    PurchaseOrderDetailFindingRepository purchaseOrderDetailFindingRepository;

    @Autowired
    PurchaseOrderDetailWaxRepository purchaseOrderDetailWaxRepository;


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
    public List<PurchaseOrderMainEntity> findMainListByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.purchaseOrderMainRepository.findAll(filterClauseAppender.getInClassFilter(ids, "id"));
    }


    @Override
    public void lock(List<PurchaseOrderMainEntity> entities) throws Exception {
        this.purchaseOrderMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<PurchaseOrderMainEntity> entities) throws Exception {
        this.purchaseOrderMainRepository.saveAll(entities);
    }

    @Override
    public List<PurchaseOrderDetailLabourEntity> findPurchaseOrderDetailLabourList(Integer purchaseOrderDetailId) {
        return this.purchaseOrderDetailLabourRepository.findAllByDetailId(purchaseOrderDetailId);
    }

    @Override
    public PurchaseOrderDetailLabourEntity savePurchaseOrderDetailLabour(PurchaseOrderDetailLabourDto purchaseOrderDetailLabourDto) throws Exception {
        return this.purchaseOrderDetailLabourRepository.save(convertToPurchaseOrderDetailLabourEntity(purchaseOrderDetailLabourDto));
    }

    private PurchaseOrderDetailLabourEntity convertToPurchaseOrderDetailLabourEntity(PurchaseOrderDetailLabourDto purchaseOrderDetailLabourDto) {
        PurchaseOrderDetailLabourEntity purchaseOrderDetailLabourEntity = new PurchaseOrderDetailLabourEntity();
        BeanUtils.copyProperties(purchaseOrderDetailLabourDto, purchaseOrderDetailLabourEntity);
        purchaseOrderDetailLabourEntity.setSequence(0);
        purchaseOrderDetailLabourEntity.setIsLocked(0);
        purchaseOrderDetailLabourEntity.setCreatedBy(UserContext.getLoggedInUser());
        purchaseOrderDetailLabourEntity.setModifiedBy(UserContext.getLoggedInUser());
        purchaseOrderDetailLabourEntity.setIsDeleted(0);
        return purchaseOrderDetailLabourEntity;
    }

    @Override
    public void deletePurchaseOrderDetailLabour(PurchaseOrderDetailLabourEntity entity) throws Exception {
        this.purchaseOrderDetailLabourRepository.save(entity);
    }

    @Override
    public PurchaseOrderDetailLabourEntity findPurchaseOrderDetailLabourById(Integer id) {
        return this.purchaseOrderDetailLabourRepository.findById(id).orElseThrow(null);
    }

    @Override
    public List<PurchaseOrderDetailFindingEntity> findPurchaseOrderDetailFindingList(Integer purchaseOrderDetailId) {
        return this.purchaseOrderDetailFindingRepository.findAllByDetailId(purchaseOrderDetailId);
    }

    @Override
    public PurchaseOrderDetailFindingEntity savePurchaseOrderDetailFinding(PurchaseOrderDetailFindingDto purchaseOrderDetailFindingDto) throws Exception {
        return this.purchaseOrderDetailFindingRepository.save(convertToPurchaseOrderDetailFindingEntity(purchaseOrderDetailFindingDto));
    }

    private PurchaseOrderDetailFindingEntity convertToPurchaseOrderDetailFindingEntity(PurchaseOrderDetailFindingDto purchaseOrderDetailFindingDto) {
        PurchaseOrderDetailFindingEntity purchaseOrderDetailFindingEntity = new PurchaseOrderDetailFindingEntity();
        BeanUtils.copyProperties(purchaseOrderDetailFindingDto, purchaseOrderDetailFindingEntity);
        purchaseOrderDetailFindingEntity.setSequence(0);
        purchaseOrderDetailFindingEntity.setIsLocked(0);
        purchaseOrderDetailFindingEntity.setCreatedBy(UserContext.getLoggedInUser());
        purchaseOrderDetailFindingEntity.setModifiedBy(UserContext.getLoggedInUser());
        purchaseOrderDetailFindingEntity.setIsDeleted(0);
        return purchaseOrderDetailFindingEntity;
    }

    @Override
    public void deletePurchaseOrderDetailFinding(PurchaseOrderDetailFindingEntity entity) throws Exception {
        this.purchaseOrderDetailFindingRepository.save(entity);
    }

    @Override
    public PurchaseOrderDetailFindingEntity findPurchaseOrderDetailFindingById(Integer id) {
        return this.purchaseOrderDetailFindingRepository.findById(id).orElseThrow(null);
    }

    @Override
    public List<PurchaseOrderDetailMouldPartEntity> findPurchaseOrderDetailMouldPartList(Integer purchaseOrderDetailId) {
        return this.purchaseOrderDetailMouldPartRepository.findAllByDetailId(purchaseOrderDetailId);
    }

    @Override
    public PurchaseOrderDetailMouldPartEntity savePurchaseOrderDetailMouldPart(PurchaseOrderDetailMouldPartDto purchaseOrderDetailMouldPartDto) throws Exception {
        return this.purchaseOrderDetailMouldPartRepository.save(convertToPurchaseOrderDetailMouldPartEntity(purchaseOrderDetailMouldPartDto));
    }

    private PurchaseOrderDetailMouldPartEntity convertToPurchaseOrderDetailMouldPartEntity(PurchaseOrderDetailMouldPartDto purchaseOrderDetailMouldPartDto) {
        PurchaseOrderDetailMouldPartEntity purchaseOrderDetailMouldPartEntity = new PurchaseOrderDetailMouldPartEntity();
        BeanUtils.copyProperties(purchaseOrderDetailMouldPartDto, purchaseOrderDetailMouldPartEntity);
        purchaseOrderDetailMouldPartEntity.setSequence(0);
        purchaseOrderDetailMouldPartEntity.setIsLocked(0);
        purchaseOrderDetailMouldPartEntity.setCreatedBy(UserContext.getLoggedInUser());
        purchaseOrderDetailMouldPartEntity.setModifiedBy(UserContext.getLoggedInUser());
        purchaseOrderDetailMouldPartEntity.setIsDeleted(0);
        return purchaseOrderDetailMouldPartEntity;
    }

    @Override
    public void deletePurchaseOrderDetailMouldPart(PurchaseOrderDetailMouldPartEntity entity) throws Exception {
        this.purchaseOrderDetailMouldPartRepository.save(entity);
    }

    @Override
    public PurchaseOrderDetailMouldPartEntity findPurchaseOrderDetailMouldPartById(Integer id) {
        return this.purchaseOrderDetailMouldPartRepository.findById(id).orElseThrow(null);
    }

    @Override
    public List<PurchaseOrderDetailWaxEntity> findPurchaseOrderDetailWaxList(Integer purchaseOrderDetailId) {
        return this.purchaseOrderDetailWaxRepository.findAllByDetailId(purchaseOrderDetailId);
    }

    @Override
    public PurchaseOrderDetailWaxEntity savePurchaseOrderDetailWax(PurchaseOrderDetailWaxDto purchaseOrderDetailWaxDto) throws Exception {
        return this.purchaseOrderDetailWaxRepository.save(convertToPurchaseOrderDetailWaxEntity(purchaseOrderDetailWaxDto));
    }


    private PurchaseOrderDetailWaxEntity convertToPurchaseOrderDetailWaxEntity(PurchaseOrderDetailWaxDto purchaseOrderDetailWaxDto) {
        PurchaseOrderDetailWaxEntity purchaseOrderDetailWaxEntity = new PurchaseOrderDetailWaxEntity();
        BeanUtils.copyProperties(purchaseOrderDetailWaxDto, purchaseOrderDetailWaxEntity);
        purchaseOrderDetailWaxEntity.setSequence(0);
        purchaseOrderDetailWaxEntity.setIsLocked(0);
        purchaseOrderDetailWaxEntity.setCreatedBy(UserContext.getLoggedInUser());
        purchaseOrderDetailWaxEntity.setModifiedBy(UserContext.getLoggedInUser());
        purchaseOrderDetailWaxEntity.setIsDeleted(0);
        return purchaseOrderDetailWaxEntity;
    }

    @Override
    public void deletePurchaseOrderDetailWax(PurchaseOrderDetailWaxEntity entity) throws Exception {
        this.purchaseOrderDetailWaxRepository.save(entity);
    }

    @Override
    public PurchaseOrderDetailWaxEntity findPurchaseOrderDetailWaxById(Integer id) {
        return this.purchaseOrderDetailWaxRepository.findById(id).orElseThrow(null);
    }

    @Override
    public PurchaseOrderComponentEntity savePurchaseOrderComponent(PurchaseOrderComponentDto purchaseOrderComponentDto) {
        PurchaseOrderComponentEntity entity = new PurchaseOrderComponentEntity();
        BeanUtils.copyProperties(purchaseOrderComponentDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return purchaseOrderComponentRepository.save(entity);
    }

    @Override
    public List<PurchaseOrderComponentEntity> findPurchaseOrderComponentByDetailId(Integer id) {
        return purchaseOrderComponentRepository.findAllByDetailId(id);
    }

    @Override
    public void deleteAll(List<Integer> ids) throws DataIntegrityViolationException {
        this.purchaseOrderComponentRepository.deleteAllByMainIds(ids, UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.purchaseOrderDetailRepository.deleteDetailByMainIds(ids,
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.purchaseOrderMainRepository.deleteAllMainById(ids,
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

}
