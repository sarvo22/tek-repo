package com.tekfilo.inventory.invoice.purchaseinvoice.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.common.InvoiceChargesDto;
import com.tekfilo.inventory.invoice.common.InvoiceDetailDto;
import com.tekfilo.inventory.invoice.common.InvoiceMainDto;
import com.tekfilo.inventory.invoice.common.InvoiceRequestPayload;
import com.tekfilo.inventory.invoice.purchaseinvoice.entity.PurchaseInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.purchaseinvoice.entity.PurchaseInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
import com.tekfilo.inventory.invoice.purchaseinvoice.repository.PurchaseInvoiceChargesRepository;
import com.tekfilo.inventory.invoice.purchaseinvoice.repository.PurchaseInvoiceDetailRepository;
import com.tekfilo.inventory.invoice.purchaseinvoice.repository.PurchaseInvoiceMainRepository;
import com.tekfilo.inventory.master.SupplierAddressEntity;
import com.tekfilo.inventory.master.repository.SupplierAddressRepository;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.product.ProductEntity;
import com.tekfilo.inventory.product.ProductRepository;
import com.tekfilo.inventory.stock.StockEntity;
import com.tekfilo.inventory.stock.StockService;
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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PurchaseInvoiceService implements IPurchaseInvoiceService {


    @Autowired
    PurchaseInvoiceMainRepository purchaseInvoiceMainRepository;

    @Autowired
    PurchaseInvoiceDetailRepository purchaseInvoiceDetailRepository;

    @Autowired
    PurchaseInvoiceChargesRepository purchaseInvoiceChargesRepository;

    @Autowired
    SupplierAddressRepository supplierAddressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    StockService stockService;


    @Override
    public Page<PurchaseInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.purchaseInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public PurchaseInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception {
        return purchaseInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private PurchaseInvoiceMainEntity convertToEntity(InvoiceMainDto invoiceMainDto) {
        PurchaseInvoiceMainEntity entity = new PurchaseInvoiceMainEntity();
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
        purchaseInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public PurchaseInvoiceMainEntity findById(Integer id) {
        return purchaseInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(PurchaseInvoiceMainEntity entity) {
        purchaseInvoiceMainRepository.save(entity);
    }

    @Override
    public List<PurchaseInvoiceDetailEntity> findAllDetail(Integer id) {
        List<PurchaseInvoiceDetailEntity> pagedList = purchaseInvoiceDetailRepository.findAllDetail(id);
        return (pagedList);
    }

    @Override
    public PurchaseInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        return purchaseInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    private PurchaseInvoiceDetailEntity convertDetailToEntity(InvoiceDetailDto invoiceDetailDto) {
        PurchaseInvoiceDetailEntity entity = new PurchaseInvoiceDetailEntity();
        BeanUtils.copyProperties(invoiceDetailDto, entity);
        entity.setInvQty1(entity.getInvQty1() == null ? new BigDecimal(0.00) : entity.getInvQty1());
        entity.setInvQty2(entity.getInvQty2() == null ? new BigDecimal(0.00) : entity.getInvQty2());
        entity.setInputRate(entity.getInputRate() == null ? new BigDecimal(0.00) : entity.getInputRate());
        entity.setInputAmount(new BigDecimal(entity.getInputRate().doubleValue() * entity.getInvQty1().doubleValue()));
        entity.setCostPrice(entity.getCostPrice() == null ? new BigDecimal(0.00) : entity.getCostPrice());
        entity.setDiscountValue(entity.getDiscountValue() == null ? new BigDecimal(0.00) : entity.getDiscountValue());
        entity.setConfQty1(invoiceDetailDto.getConfQty1() == null ? new BigDecimal(0.00) : invoiceDetailDto.getConfQty1());
        entity.setConfQty2(invoiceDetailDto.getConfQty2() == null ? new BigDecimal(0.00) : invoiceDetailDto.getConfQty2());
        entity.setRetQty1(invoiceDetailDto.getRetQty1() == null ? new BigDecimal(0.00) : invoiceDetailDto.getRetQty1());
        entity.setRetQty2(invoiceDetailDto.getRetQty2() == null ? new BigDecimal(0.00) : invoiceDetailDto.getRetQty2());
        entity.setConfInputRate(invoiceDetailDto.getConfInputRate() == null ? new BigDecimal(0.00) : invoiceDetailDto.getConfInputRate());
        entity.setConfInputAmount(new BigDecimal(entity.getConfQty1().doubleValue() * entity.getConfInputRate().doubleValue()));
        entity.setRetInputRate(invoiceDetailDto.getRetInputRate() == null ? new BigDecimal(0.00) : invoiceDetailDto.getRetInputRate());
        entity.setRetInputAmount(new BigDecimal(entity.getRetQty1().doubleValue() * entity.getRetInputRate().doubleValue()));
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(invoiceDetailDto.getIsDeleted() == null ? 0 : invoiceDetailDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        purchaseInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public PurchaseInvoiceDetailEntity findDetailById(Integer id) {
        return purchaseInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(PurchaseInvoiceDetailEntity entity) {
        purchaseInvoiceDetailRepository.save(entity);
    }

    @Override
    public List<PurchaseInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<PurchaseInvoiceChargesEntity> pagedList = purchaseInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public PurchaseInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        return purchaseInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        purchaseInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private PurchaseInvoiceChargesEntity convertChargesToEntity(InvoiceChargesDto invoiceChargesDto) {
        PurchaseInvoiceChargesEntity entity = new PurchaseInvoiceChargesEntity();
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
    public PurchaseInvoiceChargesEntity findChargesById(Integer id) {
        return purchaseInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(PurchaseInvoiceChargesEntity entity) {
        purchaseInvoiceChargesRepository.save(entity);
    }

    @Override
    public List<PurchaseInvoiceMainEntity> findMain() {
        return purchaseInvoiceMainRepository.findMain();
    }

    @Override
    public PurchaseInvoiceMainEntity createPurchaseInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception {
        PurchaseInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        PurchaseInvoiceMainEntity entity = purchaseInvoiceMainRepository.save(createEntity);
        List<InvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<PurchaseInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        purchaseInvoiceDetailRepository.saveAll(entities);
        List<PurchaseInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<InvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        purchaseInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    @Override
    public List<PurchaseInvoiceDetailEntity> findDetailByProductId(Integer productId) {
        return this.purchaseInvoiceDetailRepository.findAllDetailByProductId(productId);
    }

    private List<InvoiceDetailDto> convertFindAllDetailByMainId(List<PurchaseInvoiceDetailEntity> detailList) {
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
    public void changeStatus(Integer id) throws Exception {
        PurchaseInvoiceMainEntity mainEntity = this.purchaseInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.purchaseInvoiceMainRepository.save(mainEntity);
    }


    @Override
    public List<PurchaseInvoiceDetailEntity> findDetailByIdWithStock(Integer purchaseInvoiceId) {
        List<PurchaseInvoiceDetailEntity> entityList = this.purchaseInvoiceDetailRepository.findAllDetail(purchaseInvoiceId);
        entityList.stream().forEach(e -> {
            StockEntity stock = this.stockService.getStock(e.getProductId(), e.getBinId(), CompanyContext.getCurrentCompany());
            e.setStock(stock);
        });
        return entityList;
    }

    @Override
    public List<PurchaseInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency) {
        List<PurchaseInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<PurchaseInvoiceMainEntity> mainEntityList = this.purchaseInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();
        List<PurchaseInvoiceDetailEntity> detailEntityList = this.purchaseInvoiceDetailRepository.findAll(appender.getInClassFilter(
                mainEntityList.stream().map(PurchaseInvoiceMainEntity::getId).collect(Collectors.toList()), "invId"
        ));

        detailEntityList.removeIf(e ->
                e.getInvQty1().doubleValue() - e.getRetQty1().doubleValue() - e.getConfQty1().doubleValue() == 0
        );

        if (detailEntityList.size() == 0) {
            return pendingInvoiceList;
        }

        detailEntityList.stream().forEach(e -> {
            StockEntity stock = this.stockService.getStock(e.getProductId(), e.getBinId(), CompanyContext.getCurrentCompany());
            if (Optional.ofNullable(stock).isPresent()) {
                if (stock.getBalanceQty1().doubleValue() > 0) {
                    e.setStock(stock);
                    pendingInvoiceList.add(e);
                }
            }
        });

        return pendingInvoiceList;
    }

    @Override
    public List<PurchaseInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId) {
        List<PurchaseInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<PurchaseInvoiceMainEntity> mainEntityList = this.purchaseInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();

        List<PurchaseInvoiceDetailEntity> detailEntityList = this.purchaseInvoiceDetailRepository.findAll(
                appender.getReturnClassFilter(mainEntityList.stream().map(PurchaseInvoiceMainEntity::getId).collect(Collectors.toList()),
                        productId)
        );

        detailEntityList.removeIf(e ->
                e.getInvQty1().doubleValue() - e.getRetQty1().doubleValue() - e.getConfQty1().doubleValue() == 0
        );

        if (detailEntityList.size() == 0) {
            return pendingInvoiceList;
        }

        detailEntityList.stream().forEach(e -> {
            StockEntity stock = this.stockService.getStock(e.getProductId(), e.getBinId(), CompanyContext.getCurrentCompany());
            if (Optional.ofNullable(stock).isPresent()) {
                if (stock.getBalanceQty1().doubleValue() > 0) {
                    e.setStock(stock);
                    pendingInvoiceList.add(e);
                }
            }
        });
        return pendingInvoiceList;
    }

    @Override
    @Transactional
    public void removeAll(PurchaseInvoiceMainEntity entity, List<PurchaseInvoiceDetailEntity> detailEntities, List<PurchaseInvoiceChargesEntity> chargesEntities) throws Exception {
        this.purchaseInvoiceDetailRepository.deleteDetailById(detailEntities.stream().map(PurchaseInvoiceDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.purchaseInvoiceChargesRepository.deleteChargesById(chargesEntities.stream().map(PurchaseInvoiceChargesEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.purchaseInvoiceMainRepository.deleteMainById(entity.getId(),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = java.sql.Timestamp.from(Instant.now());
        this.purchaseInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.purchaseInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.purchaseInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<PurchaseInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.purchaseInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<PurchaseInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.purchaseInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<PurchaseInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.purchaseInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<PurchaseInvoiceMainEntity> entities) throws Exception {
        this.purchaseInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<PurchaseInvoiceMainEntity> entities) throws Exception {
        this.purchaseInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public List<PurchaseInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<PurchaseInvoiceDetailEntity> detailEntityList = this.purchaseInvoiceDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        return detailEntityList;
    }
}
