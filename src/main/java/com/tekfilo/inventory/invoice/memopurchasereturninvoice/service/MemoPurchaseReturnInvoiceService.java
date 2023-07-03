package com.tekfilo.inventory.invoice.memopurchasereturninvoice.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.common.InvoiceChargesDto;
import com.tekfilo.inventory.invoice.common.InvoiceDetailDto;
import com.tekfilo.inventory.invoice.common.InvoiceMainDto;
import com.tekfilo.inventory.invoice.common.InvoiceRequestPayload;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceMainEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.repository.MemoPurchaseInvoiceMainRepository;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceMainEntity;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.repository.MemoPurchaseReturnInvoiceChargesRepository;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.repository.MemoPurchaseReturnInvoiceDetailRepository;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.repository.MemoPurchaseReturnInvoiceMainRepository;
import com.tekfilo.inventory.master.SupplierAddressEntity;
import com.tekfilo.inventory.master.repository.SupplierAddressRepository;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.product.ProductEntity;
import com.tekfilo.inventory.product.ProductRepository;
import com.tekfilo.inventory.util.FilterClauseAppender;
import com.tekfilo.inventory.util.InvoiceConstants;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MemoPurchaseReturnInvoiceService implements IMemoPurchaseReturnInvoiceService {

    @Autowired
    MemoPurchaseReturnInvoiceMainRepository memoPurchaseReturnInvoiceMainRepository;

    @Autowired
    MemoPurchaseReturnInvoiceDetailRepository memoPurchaseReturnInvoiceDetailRepository;

    @Autowired
    MemoPurchaseReturnInvoiceChargesRepository memoPurchaseReturnInvoiceChargesRepository;

    @Autowired
    SupplierAddressRepository supplierAddressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    MemoPurchaseInvoiceMainRepository memoPurchaseInvoiceMainRepository;

    @Override
    public Page<MemoPurchaseReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.memoPurchaseReturnInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public MemoPurchaseReturnInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception {
        return memoPurchaseReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private MemoPurchaseReturnInvoiceMainEntity convertToEntity(InvoiceMainDto invoiceMainDto) {
        MemoPurchaseReturnInvoiceMainEntity entity = new MemoPurchaseReturnInvoiceMainEntity();
        BeanUtils.copyProperties(invoiceMainDto, entity);
        entity.setTotalInvoiceAmount(new BigDecimal(0.00));
        entity.setTotalPaidAmount(new BigDecimal(0.00));
        entity.setPaymentStatus(InvoiceConstants.UNPAID);
        entity.setAccountingStatus(InvoiceConstants.UNPOSTED);
        if (Optional.ofNullable(invoiceMainDto.getBillingAddressId()).isPresent()) {
            if (invoiceMainDto.getBillingAddressId() > 0) {
                SupplierAddressEntity billingAddress = this.supplierAddressRepository.findById((invoiceMainDto.getBillingAddressId())).orElse(new SupplierAddressEntity());
                if (billingAddress.getAddressId() != null) {
                    entity.setBillingAddressId((invoiceMainDto.getBillingAddressId()));
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
                SupplierAddressEntity shippingAddress = this.supplierAddressRepository.findById((invoiceMainDto.getShippingAddressId())).orElse(new SupplierAddressEntity());
                if (shippingAddress.getAddressId() != null) {
                    entity.setShippingAddressId((invoiceMainDto.getShippingAddressId()));
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
        memoPurchaseReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public MemoPurchaseReturnInvoiceMainEntity findById(Integer id) {
        return memoPurchaseReturnInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(MemoPurchaseReturnInvoiceMainEntity entity) {
        memoPurchaseReturnInvoiceMainRepository.save(entity);
    }

    @Override
    public List<MemoPurchaseReturnInvoiceDetailEntity> findAllDetail(Integer id) {
        List<MemoPurchaseReturnInvoiceDetailEntity> detailEntityList = memoPurchaseReturnInvoiceDetailRepository.findAllDetail(id);
        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        MemoPurchaseInvoiceMainEntity purchaseInvoiceMain = this.memoPurchaseInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new MemoPurchaseInvoiceMainEntity());
                        e.setPurchaseInvoiceMain(purchaseInvoiceMain);
                        e.setMemoPurchaseInvoiceMain(purchaseInvoiceMain);
                    }
                }
            });
        }
        return (detailEntityList);
    }

    @Override
    public MemoPurchaseReturnInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        return memoPurchaseReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    private MemoPurchaseReturnInvoiceDetailEntity convertDetailToEntity(InvoiceDetailDto invoiceDetailDto) {
        MemoPurchaseReturnInvoiceDetailEntity entity = new MemoPurchaseReturnInvoiceDetailEntity();
        BeanUtils.copyProperties(invoiceDetailDto, entity);
        entity.setInvQty1(entity.getInvQty1() == null ? new BigDecimal(0.00) : entity.getInvQty1());
        entity.setInvQty2(entity.getInvQty2() == null ? new BigDecimal(0.00) : entity.getInvQty2());
        entity.setInputRate(entity.getInputRate() == null ? new BigDecimal(0.00) : entity.getInputRate());
        entity.setInputAmount(new BigDecimal(entity.getInputRate().doubleValue() * entity.getInvQty1().doubleValue()));
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
        memoPurchaseReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public MemoPurchaseReturnInvoiceDetailEntity findDetailById(Integer id) {
        return memoPurchaseReturnInvoiceDetailRepository.findById(id).orElse(null);
    }


    @Override
    @Transactional
    public void removeDetail(MemoPurchaseReturnInvoiceDetailEntity entity) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        ids.add(entity.getId());
        this.memoPurchaseReturnInvoiceDetailRepository.deleteDetailById(ids, UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    public List<MemoPurchaseReturnInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<MemoPurchaseReturnInvoiceChargesEntity> pagedList = memoPurchaseReturnInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public MemoPurchaseReturnInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        return memoPurchaseReturnInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        memoPurchaseReturnInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private MemoPurchaseReturnInvoiceChargesEntity convertChargesToEntity(InvoiceChargesDto invoiceChargesDto) {
        MemoPurchaseReturnInvoiceChargesEntity entity = new MemoPurchaseReturnInvoiceChargesEntity();
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
    public MemoPurchaseReturnInvoiceChargesEntity findChargesById(Integer id) {
        return memoPurchaseReturnInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(MemoPurchaseReturnInvoiceChargesEntity entity) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        ids.add(entity.getId());
        this.memoPurchaseReturnInvoiceChargesRepository.deleteChargesById(ids, UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    public List<MemoPurchaseReturnInvoiceMainEntity> findMain() {
        return memoPurchaseReturnInvoiceMainRepository.findMain();
    }

    @Override
    public MemoPurchaseReturnInvoiceMainEntity createMemoPurchaseReturnInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception {
        MemoPurchaseReturnInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        MemoPurchaseReturnInvoiceMainEntity entity = memoPurchaseReturnInvoiceMainRepository.save(createEntity);
        List<InvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<MemoPurchaseReturnInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        memoPurchaseReturnInvoiceDetailRepository.saveAll(entities);
        List<MemoPurchaseReturnInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<InvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        memoPurchaseReturnInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<InvoiceDetailDto> convertFindAllDetailByMainId(List<MemoPurchaseReturnInvoiceDetailEntity> detailList) {
        List<InvoiceDetailDto> invoiceDetailDtoList = new ArrayList<>();
        detailList.stream().forEachOrdered(e -> {
            InvoiceDetailDto invoiceDetailDto = new InvoiceDetailDto();
            invoiceDetailDto.setRowId(e.getId());
            invoiceDetailDto.setId(e.getId());
            invoiceDetailDto.setInvId(e.getInvId());
            invoiceDetailDto.setProductId(e.getProductId());
            invoiceDetailDto.setProductDescription(e.getProductDescription());
            invoiceDetailDto.setInvQty1(e.getInvQty1());
            invoiceDetailDto.setInvQty2(e.getInvQty2());
            invoiceDetailDto.setUom1(e.getUom1());
            invoiceDetailDto.setUom2(e.getUom2());
            invoiceDetailDto.setInputRate(e.getInputRate());
            invoiceDetailDto.setInputAmount(e.getInputAmount());
            invoiceDetailDto.setDiscountType(e.getDiscountType());
            invoiceDetailDto.setDiscountValue(e.getDiscountValue());
            invoiceDetailDto.setProductNo(e.getProduct() != null ? e.getProduct().getProductNo() : null);
            invoiceDetailDto.setDescription(e.getProductDescription());
            invoiceDetailDto.setPicture1Path(e.getProduct() != null ? e.getProduct().getPicture1Path() : null);
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
            invoiceDetailDto.setUom1(e.getUnit1());
            invoiceDetailDto.setUom2(e.getUnit2());
            invoiceDetailDto.setProductNo(e.getProductNo());
            invoiceDetailDto.setDescription(e.getDescription());
            invoiceDetailDto.setPicture1Path(e.getPicture1Path());
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
    public void saveAllDetail(List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception {
        List<MemoPurchaseReturnInvoiceDetailEntity> detailEntityList = new ArrayList<>();
        invoiceDetailDtoList.stream().forEachOrdered(e -> {
            detailEntityList.add(convertDetailToEntity(e));
        });
        memoPurchaseReturnInvoiceDetailRepository.saveAll(detailEntityList);
    }


    @Override
    public void changeStatus(Integer id) throws Exception {
        MemoPurchaseReturnInvoiceMainEntity mainEntity = this.memoPurchaseReturnInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.memoPurchaseReturnInvoiceMainRepository.save(mainEntity);
    }

    @Override
    @Transactional
    public void removeAll(MemoPurchaseReturnInvoiceMainEntity entity, List<MemoPurchaseReturnInvoiceDetailEntity> detailEntities, List<MemoPurchaseReturnInvoiceChargesEntity> chargesEntities) throws Exception {
        this.memoPurchaseReturnInvoiceDetailRepository.deleteDetailById(detailEntities.stream().map(MemoPurchaseReturnInvoiceDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.memoPurchaseReturnInvoiceChargesRepository.deleteChargesById(chargesEntities.stream().map(MemoPurchaseReturnInvoiceChargesEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.memoPurchaseReturnInvoiceMainRepository.deleteMainById(entity.getId(),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = java.sql.Timestamp.from(Instant.now());
        this.memoPurchaseReturnInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.memoPurchaseReturnInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.memoPurchaseReturnInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<MemoPurchaseReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<MemoPurchaseReturnInvoiceDetailEntity> detailEntityList = this.memoPurchaseReturnInvoiceDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setMemoPurchaseInvoiceMain(this.memoPurchaseInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new MemoPurchaseInvoiceMainEntity()));
                    }
                }
                e.setMemoPurchaseReturnInvoiceMain(this.memoPurchaseReturnInvoiceMainRepository.findById(e.getInvId()).orElse(new MemoPurchaseReturnInvoiceMainEntity()));
            });
        }
        return detailEntityList;
    }

    @Override
    public List<MemoPurchaseReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.memoPurchaseReturnInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<MemoPurchaseReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.memoPurchaseReturnInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<MemoPurchaseReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.memoPurchaseReturnInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<MemoPurchaseReturnInvoiceMainEntity> entities) throws Exception {
        this.memoPurchaseReturnInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<MemoPurchaseReturnInvoiceMainEntity> entities) throws Exception {
        this.memoPurchaseReturnInvoiceMainRepository.saveAll(entities);
    }
}
