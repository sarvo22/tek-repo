package com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.repository.PurchaseInvoiceMainRepository;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.repository.PurchaseReturnInvoiceChargesRepository;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.repository.PurchaseReturnInvoiceDetailRepository;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.repository.PurchaseReturnInvoiceMainRepository;
import com.tekfilo.jewellery.master.SupplierAddressEntity;
import com.tekfilo.jewellery.master.repository.SupplierAddressRepository;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import com.tekfilo.jewellery.product.repository.ProductRepository;
import com.tekfilo.jewellery.util.FilterClauseAppender;
import com.tekfilo.jewellery.util.InvoiceConstants;
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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class PurchaseReturnInvoiceService implements IPurchaseReturnInvoiceService {


    @Autowired
    PurchaseReturnInvoiceMainRepository purchaseReturnInvoiceMainRepository;

    @Autowired
    PurchaseReturnInvoiceDetailRepository purchaseReturnInvoiceDetailRepository;

    @Autowired
    PurchaseReturnInvoiceChargesRepository purchaseReturnInvoiceChargesRepository;

    @Autowired
    SupplierAddressRepository supplierAddressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    PurchaseInvoiceMainRepository purchaseInvoiceMainRepository;


    @Override
    public Page<PurchaseReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.purchaseReturnInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public PurchaseReturnInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception {
        return purchaseReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private PurchaseReturnInvoiceMainEntity convertToEntity(InvoiceMainDto invoiceMainDto) {
        PurchaseReturnInvoiceMainEntity entity = new PurchaseReturnInvoiceMainEntity();
        BeanUtils.copyProperties(invoiceMainDto, entity);
        entity.setTotalInvoiceAmount(new BigDecimal(0.00));
        entity.setTotalPaidAmount(new BigDecimal(0.00));
        entity.setPaymentStatus(InvoiceConstants.UNPAID);
        entity.setAccountingStatus(InvoiceConstants.UNPOSTED);

        if (Optional.ofNullable(invoiceMainDto.getBillingAddressId()).isPresent()) {
            if (invoiceMainDto.getBillingAddressId() > 0) {
                SupplierAddressEntity billingAddress = this.supplierAddressRepository.findById(invoiceMainDto.getBillingAddressId()).orElse(new SupplierAddressEntity());
                if (billingAddress.getAddressId() != null) {
                    entity.setBillingAddressId(invoiceMainDto.getBillingAddressId());
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


        if (Optional.ofNullable(invoiceMainDto.getShippingAddressId()).isPresent()) {
            if (invoiceMainDto.getShippingAddressId() > 0) {
                SupplierAddressEntity shippingAddress = this.supplierAddressRepository.findById(invoiceMainDto.getShippingAddressId()).orElse(new SupplierAddressEntity());
                if (shippingAddress.getAddressId() != null) {
                    entity.setShippingAddressId(invoiceMainDto.getShippingAddressId());
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

        entity.setSequence(invoiceMainDto.getSequence() == null ? 0 : invoiceMainDto.getSequence());
        entity.setIsLocked(invoiceMainDto.getIsLocked() == null ? 0 : invoiceMainDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(invoiceMainDto.getIsDeleted() == null ? 0 : invoiceMainDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(InvoiceMainDto invoiceMainDto) throws Exception {
        purchaseReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public PurchaseReturnInvoiceMainEntity findById(Integer id) {
        return purchaseReturnInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(PurchaseReturnInvoiceMainEntity entity) {
        purchaseReturnInvoiceMainRepository.save(entity);
    }

    @Override
    public List<PurchaseReturnInvoiceDetailEntity> findAllDetail(Integer id) {
        List<PurchaseReturnInvoiceDetailEntity> detailEntityList = purchaseReturnInvoiceDetailRepository.findAllDetail(id);

        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setPurchaseInvoiceMain(this.purchaseInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new PurchaseInvoiceMainEntity()));
                    }
                }
            });
        }
        return detailEntityList;
    }

    @Override
    public PurchaseReturnInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        return purchaseReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public void saveAllDetail(List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception {
        List<PurchaseReturnInvoiceDetailEntity> detailEntityList = new ArrayList<>();
        invoiceDetailDtoList.stream().forEachOrdered(e -> {
            detailEntityList.add(convertDetailToEntity(e));
        });
        purchaseReturnInvoiceDetailRepository.saveAll(detailEntityList);
    }

    private PurchaseReturnInvoiceDetailEntity convertDetailToEntity(InvoiceDetailDto invoiceDetailDto) {
        PurchaseReturnInvoiceDetailEntity entity = new PurchaseReturnInvoiceDetailEntity();
        BeanUtils.copyProperties(invoiceDetailDto, entity);
        entity.setInvQty(entity.getInvQty() == null ? new BigDecimal(0.00) : entity.getInvQty());
        entity.setInputRate(entity.getInputRate() == null ? new BigDecimal(0.00) : entity.getInputRate());
        entity.setInputAmount(new BigDecimal(entity.getInputRate().doubleValue() * entity.getInvQty().doubleValue()));
        entity.setCostPrice(entity.getCostPrice() == null ? new BigDecimal(0.00) : entity.getCostPrice());
        entity.setDiscountValue(entity.getDiscountValue() == null ? new BigDecimal(0.00) : entity.getDiscountValue());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(invoiceDetailDto.getIsDeleted() == null ? 0 : invoiceDetailDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        purchaseReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public PurchaseReturnInvoiceDetailEntity findDetailById(Integer id) {
        return purchaseReturnInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(PurchaseReturnInvoiceDetailEntity entity) {
        purchaseReturnInvoiceDetailRepository.save(entity);
    }

    @Override
    public List<PurchaseReturnInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<PurchaseReturnInvoiceChargesEntity> pagedList = purchaseReturnInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public PurchaseReturnInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        return purchaseReturnInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        purchaseReturnInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private PurchaseReturnInvoiceChargesEntity convertChargesToEntity(InvoiceChargesDto invoiceChargesDto) {
        PurchaseReturnInvoiceChargesEntity entity = new PurchaseReturnInvoiceChargesEntity();
        BeanUtils.copyProperties(invoiceChargesDto, entity);
        entity.setInputPctAmountValue(entity.getInputPctAmountValue() == null ? new BigDecimal(0.00) : entity.getInputPctAmountValue());
        entity.setInputAmount(entity.getInputAmount() == null ? new BigDecimal(0.00) : entity.getInputAmount());
        entity.setIsSupplierPayable(invoiceChargesDto.getIsSupplierPayable() == null ? 0 : invoiceChargesDto.getIsSupplierPayable());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(invoiceChargesDto.getIsDeleted() == null ? 0 : invoiceChargesDto.getIsDeleted());
        return entity;
    }

    @Override
    public PurchaseReturnInvoiceChargesEntity findChargesById(Integer id) {
        return purchaseReturnInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(PurchaseReturnInvoiceChargesEntity entity) {
        purchaseReturnInvoiceChargesRepository.save(entity);
    }

    @Override
    public List<PurchaseReturnInvoiceMainEntity> findMain() {
        return purchaseReturnInvoiceMainRepository.findMain();
    }

    @Override
    public PurchaseReturnInvoiceMainEntity createPurchaseInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception {
        PurchaseReturnInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        PurchaseReturnInvoiceMainEntity entity = purchaseReturnInvoiceMainRepository.save(createEntity);
        List<InvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<PurchaseReturnInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        purchaseReturnInvoiceDetailRepository.saveAll(entities);
        List<PurchaseReturnInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<InvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        purchaseReturnInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<InvoiceDetailDto> convertFindAllDetailByMainId(List<PurchaseReturnInvoiceDetailEntity> detailList) {
        List<InvoiceDetailDto> invoiceDetailDtoList = new ArrayList<>();
        detailList.stream().forEachOrdered(e -> {
            InvoiceDetailDto invoiceDetailDto = new InvoiceDetailDto();
            invoiceDetailDto.setRowId(e.getId());
            invoiceDetailDto.setId(e.getId());
            invoiceDetailDto.setInvId(e.getInvId());
            invoiceDetailDto.setProductId(e.getProductId());
            invoiceDetailDto.setProductDescription(e.getProductDescription());
            invoiceDetailDto.setInvQty(e.getInvQty());
            invoiceDetailDto.setUom(e.getUom());
            invoiceDetailDto.setInputRate(e.getInputRate());
            invoiceDetailDto.setInputAmount(e.getInputAmount());
            invoiceDetailDto.setDiscountType(e.getDiscountType());
            invoiceDetailDto.setDiscountValue(e.getDiscountValue());
            invoiceDetailDto.setProductNo(e.getProduct() != null ? e.getProduct().getProductNo() : null);
            invoiceDetailDto.setDescription(e.getProductDescription());
            invoiceDetailDto.setPicture1Path(e.getProduct() != null ? e.getProduct().getPicturePath() : null);
            invoiceDetailDtoList.add(invoiceDetailDto);
        });
        return invoiceDetailDtoList;
    }

    private List<InvoiceDetailDto> convert2InvoiceDetailDto(List<ProductEntity> productEntityList) {
        List<InvoiceDetailDto> invoiceDetailDtoList = new ArrayList<>();
        productEntityList.stream().forEachOrdered(e -> {
            InvoiceDetailDto invoiceDetailDto = new InvoiceDetailDto();
            invoiceDetailDto.setProductId(e.getId());
            invoiceDetailDto.setProductDescription(e.getDescription());
            invoiceDetailDto.setUom(e.getUnit());
            invoiceDetailDto.setProductNo(e.getProductNo());
            invoiceDetailDto.setDescription(e.getDescription());
            invoiceDetailDto.setPicture1Path(e.getPicturePath());
            invoiceDetailDtoList.add(invoiceDetailDto);
        });
        return invoiceDetailDtoList;
    }

    private List<InvoiceChargesDto> setInvoiceMainId2ChargesList(List<InvoiceChargesDto> charges, Integer invoiceMainId) {
        charges.stream().forEachOrdered(c -> {
            c.setInvId(invoiceMainId);
        });
        return charges;
    }

    private List<InvoiceDetailDto> setInvoiceMainId2DetailList(List<InvoiceDetailDto> detail, Integer invoiceMainId) {
        detail.stream().forEachOrdered(e -> {
            e.setInvId(invoiceMainId);
        });
        return detail;
    }

    @Override
    public void changeStatus(Integer id) throws Exception {
        PurchaseReturnInvoiceMainEntity mainEntity = this.purchaseReturnInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.purchaseReturnInvoiceMainRepository.save(mainEntity);
    }

    @Override
    public List<PurchaseReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<PurchaseReturnInvoiceDetailEntity> detailEntityList = this.purchaseReturnInvoiceDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setPurchaseInvoiceMain(this.purchaseInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new PurchaseInvoiceMainEntity()));
                    }
                }
                e.setPurchaseReturnInvoiceMain(this.purchaseReturnInvoiceMainRepository.findById(e.getInvId()).orElse(new PurchaseReturnInvoiceMainEntity()));
            });
        }
        return detailEntityList;
    }

    @Override
    public void removeAll(PurchaseReturnInvoiceMainEntity entity, List<PurchaseReturnInvoiceDetailEntity> detailEntities, List<PurchaseReturnInvoiceChargesEntity> chargesEntities) throws Exception {
        this.purchaseReturnInvoiceChargesRepository.saveAll(chargesEntities);
        this.purchaseReturnInvoiceDetailRepository.saveAll(detailEntities);
        this.purchaseReturnInvoiceMainRepository.save(entity);
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = java.sql.Timestamp.from(Instant.now());
        this.purchaseReturnInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.purchaseReturnInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.purchaseReturnInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<PurchaseReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.purchaseReturnInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<PurchaseReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.purchaseReturnInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<PurchaseReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.purchaseReturnInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<PurchaseReturnInvoiceMainEntity> entities) throws Exception {
        this.purchaseReturnInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<PurchaseReturnInvoiceMainEntity> entities) throws Exception {
        this.purchaseReturnInvoiceMainRepository.saveAll(entities);
    }
}
