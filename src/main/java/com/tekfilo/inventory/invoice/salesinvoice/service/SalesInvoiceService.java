package com.tekfilo.inventory.invoice.salesinvoice.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.common.InvoiceChargesDto;
import com.tekfilo.inventory.invoice.common.InvoiceDetailDto;
import com.tekfilo.inventory.invoice.common.InvoiceMainDto;
import com.tekfilo.inventory.invoice.common.InvoiceRequestPayload;
import com.tekfilo.inventory.invoice.salesinvoice.entity.SalesInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.salesinvoice.entity.SalesInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.salesinvoice.entity.SalesInvoiceMainEntity;
import com.tekfilo.inventory.invoice.salesinvoice.repository.SalesInvoiceChargesRepository;
import com.tekfilo.inventory.invoice.salesinvoice.repository.SalesInvoiceDetailRepository;
import com.tekfilo.inventory.invoice.salesinvoice.repository.SalesInvoiceMainRepository;
import com.tekfilo.inventory.master.CustomerAddressEntity;
import com.tekfilo.inventory.master.repository.CustomerAddressRepository;
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
public class SalesInvoiceService implements ISalesInvoiceService {

    @Autowired
    SalesInvoiceMainRepository salesInvoiceMainRepository;

    @Autowired
    SalesInvoiceDetailRepository salesInvoiceDetailRepository;

    @Autowired
    SalesInvoiceChargesRepository salesInvoiceChargesRepository;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    StockService stockService;


    @Override
    public Page<SalesInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.salesInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public SalesInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception {
        return salesInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private SalesInvoiceMainEntity convertToEntity(InvoiceMainDto invoiceMainDto) {
        SalesInvoiceMainEntity entity = new SalesInvoiceMainEntity();
        BeanUtils.copyProperties(invoiceMainDto, entity);
        entity.setTotalInvoiceAmount(new BigDecimal(0.00));
        entity.setTotalReceivedAmount(new BigDecimal(0.00));
        entity.setPaymentStatus(InvoiceConstants.UNPAID);
        entity.setAccountingStatus(InvoiceConstants.UNPOSTED);

        if (Optional.ofNullable(invoiceMainDto.getBillingAddressId()).isPresent()) {
            if (invoiceMainDto.getBillingAddressId() > 0) {
                CustomerAddressEntity billingAddress = this.customerAddressRepository.findById(invoiceMainDto.getBillingAddressId()).orElse(new CustomerAddressEntity());
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
                CustomerAddressEntity shippingAddress = this.customerAddressRepository.findById(invoiceMainDto.getShippingAddressId()).orElse(new CustomerAddressEntity());
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
        salesInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public SalesInvoiceMainEntity findById(Integer id) {
        return salesInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(SalesInvoiceMainEntity entity) {
        salesInvoiceMainRepository.save(entity);
    }

    @Override
    public List<SalesInvoiceDetailEntity> findAllDetail(Integer id) {
        List<SalesInvoiceDetailEntity> pagedList = salesInvoiceDetailRepository.findAllDetail(id);
        return (pagedList);
    }

    @Override
    public SalesInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        return salesInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    private SalesInvoiceDetailEntity convertDetailToEntity(InvoiceDetailDto invoiceDetailDto) {
        SalesInvoiceDetailEntity entity = new SalesInvoiceDetailEntity();
        BeanUtils.copyProperties(invoiceDetailDto, entity);
        entity.setInvQty1(entity.getInvQty1() == null ? new BigDecimal(0.00) : entity.getInvQty1());
        entity.setInvQty2(entity.getInvQty2() == null ? new BigDecimal(0.00) : entity.getInvQty2());
        entity.setInputRate(entity.getInputRate() == null ? new BigDecimal(0.00) : entity.getInputRate());
        entity.setInputAmount(new BigDecimal(entity.getInputRate().doubleValue() * entity.getInvQty1().doubleValue()));
        entity.setCostPrice(entity.getCostPrice() == null ? new BigDecimal(0.00) : entity.getCostPrice());
        entity.setDiscountValue(entity.getDiscountValue() == null ? new BigDecimal(0.00) : entity.getDiscountValue());
        entity.setConfQty1(invoiceDetailDto.getConfQty1() == null ? new BigDecimal(0.00) : entity.getConfQty1());
        entity.setConfQty2(invoiceDetailDto.getConfQty2() == null ? new BigDecimal(0.00) : entity.getConfQty2());
        entity.setRetQty1(invoiceDetailDto.getRetQty1() == null ? new BigDecimal(0.00) : entity.getRetQty1());
        entity.setRetQty2(invoiceDetailDto.getRetQty2() == null ? new BigDecimal(0.00) : entity.getRetQty2());
        entity.setConfInputRate(invoiceDetailDto.getConfInputRate() == null ? new BigDecimal(0.00) : entity.getConfInputRate());
        entity.setConfInputAmount(invoiceDetailDto.getConfInputAmount() == null ? new BigDecimal(0.00) : entity.getConfInputAmount());
        entity.setRetInputRate(invoiceDetailDto.getRetInputRate() == null ? new BigDecimal(0.00) : entity.getRetInputRate());
        entity.setRetInputAmount(invoiceDetailDto.getRetInputAmount() == null ? new BigDecimal(0.00) : entity.getRetInputAmount());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(invoiceDetailDto.getIsDeleted() == null ? 0 : invoiceDetailDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        salesInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public SalesInvoiceDetailEntity findDetailById(Integer id) {
        return salesInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(SalesInvoiceDetailEntity entity) {
        salesInvoiceDetailRepository.save(entity);
    }

    @Override
    public List<SalesInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<SalesInvoiceChargesEntity> pagedList = salesInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public SalesInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        return salesInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        salesInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private SalesInvoiceChargesEntity convertChargesToEntity(InvoiceChargesDto invoiceChargesDto) {
        SalesInvoiceChargesEntity entity = new SalesInvoiceChargesEntity();
        BeanUtils.copyProperties(invoiceChargesDto, entity);
        entity.setInputPctAmountValue(entity.getInputPctAmountValue() == null ? new BigDecimal(0.00) : entity.getInputPctAmountValue());
        entity.setInputAmount(entity.getInputAmount() == null ? new BigDecimal(0.00) : entity.getInputAmount());
        entity.setIsCustomerPayable(invoiceChargesDto.getIsCustomerPayable() == null ? 0 : invoiceChargesDto.getIsCustomerPayable());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(invoiceChargesDto.getIsDeleted() == null ? 0 : invoiceChargesDto.getIsDeleted());
        return entity;
    }

    @Override
    public SalesInvoiceChargesEntity findChargesById(Integer id) {
        return salesInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(SalesInvoiceChargesEntity entity) {
        salesInvoiceChargesRepository.save(entity);
    }

    @Override
    public List<SalesInvoiceMainEntity> findMain() {
        return salesInvoiceMainRepository.findMain();
    }

    @Override
    public SalesInvoiceMainEntity createSalesInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception {
        SalesInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        SalesInvoiceMainEntity entity = salesInvoiceMainRepository.save(createEntity);
        List<InvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<SalesInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        salesInvoiceDetailRepository.saveAll(entities);
        List<SalesInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<InvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        salesInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<SalesInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<SalesInvoiceDetailEntity> detailEntityList = this.salesInvoiceDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        return detailEntityList;
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<InvoiceDetailDto> convertFindAllDetailByMainId(List<SalesInvoiceDetailEntity> detailList) {
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
    public List<SalesInvoiceDetailEntity> findDetailByProductId(Integer productId) {
        return this.salesInvoiceDetailRepository.findAllDetailByProductId(productId);
    }

    @Override
    public void changeStatus(Integer id) throws Exception {
        SalesInvoiceMainEntity mainEntity = this.salesInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.salesInvoiceMainRepository.save(mainEntity);
    }

    @Override
    public List<SalesInvoiceDetailEntity> findDetailByIdWithStock(Integer salesInvoiceId) {
        List<SalesInvoiceDetailEntity> entityList = this.salesInvoiceDetailRepository.findAllDetail(salesInvoiceId);
        entityList.stream().forEach(e -> {
            StockEntity stock = this.stockService.getStock(e.getProductId(), e.getBinId(), CompanyContext.getCurrentCompany());
            e.setStock(stock);
        });
        return entityList;
    }

    @Override
    public List<SalesInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency) {
        List<SalesInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<SalesInvoiceMainEntity> mainEntityList = this.salesInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();
        List<SalesInvoiceDetailEntity> detailEntityList = this.salesInvoiceDetailRepository.findAll(appender.getInClassFilter(
                mainEntityList.stream().map(SalesInvoiceMainEntity::getId).collect(Collectors.toList()), "invId"
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
    public List<SalesInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId) {
        List<SalesInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<SalesInvoiceMainEntity> mainEntityList = this.salesInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();

        List<SalesInvoiceDetailEntity> detailEntityList = this.salesInvoiceDetailRepository.findAll(
                appender.getReturnClassFilter(mainEntityList.stream().map(SalesInvoiceMainEntity::getId).collect(Collectors.toList()),
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
    public void removeAll(SalesInvoiceMainEntity entity, List<SalesInvoiceDetailEntity> detailEntities, List<SalesInvoiceChargesEntity> chargesEntities) throws Exception {
        this.salesInvoiceChargesRepository.saveAll(chargesEntities);
        this.salesInvoiceDetailRepository.saveAll(detailEntities);
        this.salesInvoiceMainRepository.save(entity);
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = java.sql.Timestamp.from(Instant.now());
        this.salesInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.salesInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.salesInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<SalesInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.salesInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<SalesInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.salesInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<SalesInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.salesInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<SalesInvoiceMainEntity> entities) throws Exception {
        this.salesInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<SalesInvoiceMainEntity> entities) throws Exception {
        this.salesInvoiceMainRepository.saveAll(entities);
    }
}
