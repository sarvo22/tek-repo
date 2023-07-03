package com.tekfilo.inventory.invoice.memopurchaseinvoice.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.common.InvoiceChargesDto;
import com.tekfilo.inventory.invoice.common.InvoiceDetailDto;
import com.tekfilo.inventory.invoice.common.InvoiceMainDto;
import com.tekfilo.inventory.invoice.common.InvoiceRequestPayload;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceMainEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.repository.MemoPurchaseInvoiceChargesRepository;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.repository.MemoPurchaseInvoiceDetailRepository;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.repository.MemoPurchaseInvoiceMainRepository;
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
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MemoPurchaseInvoiceService implements IMemoPurchaseInvoiceService {

    @Autowired
    MemoPurchaseInvoiceMainRepository memoPurchaseInvoiceMainRepository;

    @Autowired
    MemoPurchaseInvoiceDetailRepository memoPurchaseInvoiceDetailRepository;

    @Autowired
    MemoPurchaseInvoiceChargesRepository memoPurchaseInvoiceChargesRepository;

    @Autowired
    SupplierAddressRepository supplierAddressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    StockService stockService;

    @Override
    public Page<MemoPurchaseInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.memoPurchaseInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public MemoPurchaseInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception {
        return memoPurchaseInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private MemoPurchaseInvoiceMainEntity convertToEntity(InvoiceMainDto invoiceMainDto) {
        MemoPurchaseInvoiceMainEntity entity = new MemoPurchaseInvoiceMainEntity();
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
        memoPurchaseInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public MemoPurchaseInvoiceMainEntity findById(Integer id) {
        return memoPurchaseInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(MemoPurchaseInvoiceMainEntity entity) {
        memoPurchaseInvoiceMainRepository.save(entity);
    }

    @Override
    public List<MemoPurchaseInvoiceDetailEntity> findAllDetail(Integer id) {
        List<MemoPurchaseInvoiceDetailEntity> pagedList = memoPurchaseInvoiceDetailRepository.findAllDetail(id);
        return (pagedList);
    }

    @Override
    public MemoPurchaseInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        return memoPurchaseInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    private MemoPurchaseInvoiceDetailEntity convertDetailToEntity(InvoiceDetailDto invoiceDetailDto) {
        MemoPurchaseInvoiceDetailEntity entity = new MemoPurchaseInvoiceDetailEntity();
        BeanUtils.copyProperties(invoiceDetailDto, entity);
        entity.setInvQty1(entity.getInvQty1() == null ? new BigDecimal(0.00) : entity.getInvQty1());
        entity.setInvQty2(entity.getInvQty2() == null ? new BigDecimal(0.00) : entity.getInvQty2());
        entity.setInputRate(entity.getInputRate() == null ? new BigDecimal(0.00) : entity.getInputRate());
        entity.setDiscountValue(entity.getDiscountValue() == null ? new BigDecimal(0.00) : entity.getDiscountValue());
        entity.setConfQty1(invoiceDetailDto.getConfQty1() == null ? new BigDecimal(0.00) : invoiceDetailDto.getConfQty1());
        entity.setConfQty2(invoiceDetailDto.getConfQty2() == null ? new BigDecimal(0.00) : invoiceDetailDto.getConfQty2());
        entity.setRetQty1(invoiceDetailDto.getRetQty1() == null ? new BigDecimal(0.00) : invoiceDetailDto.getRetQty1());
        entity.setRetQty2(invoiceDetailDto.getRetQty2() == null ? new BigDecimal(0.00) : invoiceDetailDto.getRetQty2());
        entity.setConfInputRate(invoiceDetailDto.getConfInputRate() == null ? new BigDecimal(0.00) : invoiceDetailDto.getConfInputRate());
        entity.setConfInputAmount(new BigDecimal(entity.getConfQty1().doubleValue() * entity.getConfInputRate().doubleValue()));
        entity.setRetInputRate(invoiceDetailDto.getRetInputRate() == null ? new BigDecimal(0.00) : invoiceDetailDto.getRetInputRate());
        entity.setRetInputAmount(new BigDecimal(entity.getRetQty1().doubleValue() * entity.getRetInputRate().doubleValue()));
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
        memoPurchaseInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public MemoPurchaseInvoiceDetailEntity findDetailById(Integer id) {
        return memoPurchaseInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(MemoPurchaseInvoiceDetailEntity entity) throws SQLException {
        List<Integer> idList = new ArrayList<>();
        idList.add(entity.getId());
        this.memoPurchaseInvoiceDetailRepository.deleteDetailById(idList,
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    public List<MemoPurchaseInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<MemoPurchaseInvoiceChargesEntity> pagedList = memoPurchaseInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public MemoPurchaseInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        return memoPurchaseInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        memoPurchaseInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private MemoPurchaseInvoiceChargesEntity convertChargesToEntity(InvoiceChargesDto invoiceChargesDto) {
        MemoPurchaseInvoiceChargesEntity entity = new MemoPurchaseInvoiceChargesEntity();
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
    public MemoPurchaseInvoiceChargesEntity findChargesById(Integer id) {
        return memoPurchaseInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(MemoPurchaseInvoiceChargesEntity entity) throws SQLException {
        List<Integer> idList = new ArrayList<>();
        idList.add(entity.getId());
        this.memoPurchaseInvoiceChargesRepository.deleteChargesById(idList,
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    public List<MemoPurchaseInvoiceMainEntity> findMain() {
        return memoPurchaseInvoiceMainRepository.findMain();
    }

    @Override
    public MemoPurchaseInvoiceMainEntity createPurchaseInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception {
        MemoPurchaseInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        MemoPurchaseInvoiceMainEntity entity = memoPurchaseInvoiceMainRepository.save(createEntity);
        List<InvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<MemoPurchaseInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        memoPurchaseInvoiceDetailRepository.saveAll(entities);
        List<MemoPurchaseInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<InvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        memoPurchaseInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<InvoiceDetailDto> convertFindAllDetailByMainId(List<MemoPurchaseInvoiceDetailEntity> detailList) {
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
            invoiceDetailDto.setRetQty1(e.getRetQty1());
            invoiceDetailDto.setRetQty2(e.getRetQty2());
            invoiceDetailDto.setConfQty1(e.getConfQty1());
            invoiceDetailDto.setConfQty2(e.getConfQty2());
            invoiceDetailDto.setRetInputRate(e.getRetInputRate());
            invoiceDetailDto.setRetInputAmount(e.getRetInputAmount());
            invoiceDetailDto.setConfInputRate(e.getConfInputRate());
            invoiceDetailDto.setConfInputAmount(e.getConfInputAmount());
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
    public List<MemoPurchaseInvoiceDetailEntity> findDetailByIdWithStock(Integer purchaseInvoiceId) {
        List<MemoPurchaseInvoiceDetailEntity> entityList = this.memoPurchaseInvoiceDetailRepository.findAllDetail(purchaseInvoiceId);
        entityList.stream().forEach(e -> {
            StockEntity stock = this.stockService.getStock(e.getProductId(), e.getBinId(), CompanyContext.getCurrentCompany());
            e.setStock(stock);
        });
        return entityList;
    }


    @Override
    public List<MemoPurchaseInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency) {
        List<MemoPurchaseInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<MemoPurchaseInvoiceMainEntity> mainEntityList = this.memoPurchaseInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();
        List<MemoPurchaseInvoiceDetailEntity> detailEntityList = this.memoPurchaseInvoiceDetailRepository.findAll(appender.getInClassFilter(
                mainEntityList.stream().map(MemoPurchaseInvoiceMainEntity::getId).collect(Collectors.toList()), "invId"
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
    public List<MemoPurchaseInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId) {
        List<MemoPurchaseInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<MemoPurchaseInvoiceMainEntity> mainEntityList = this.memoPurchaseInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();

        List<MemoPurchaseInvoiceDetailEntity> detailEntityList = this.memoPurchaseInvoiceDetailRepository.findAll(
                appender.getReturnClassFilter(mainEntityList.stream().map(MemoPurchaseInvoiceMainEntity::getId).collect(Collectors.toList()),
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
    public void changeStatus(Integer id) throws Exception {
        MemoPurchaseInvoiceMainEntity mainEntity = this.memoPurchaseInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.memoPurchaseInvoiceMainRepository.save(mainEntity);
    }

    @Override
    @Transactional
    public void removeAll(MemoPurchaseInvoiceMainEntity entity, List<MemoPurchaseInvoiceDetailEntity> detailEntities, List<MemoPurchaseInvoiceChargesEntity> chargesEntities) throws Exception {
        this.memoPurchaseInvoiceDetailRepository.deleteDetailById(detailEntities.stream().map(MemoPurchaseInvoiceDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.memoPurchaseInvoiceChargesRepository.deleteChargesById(chargesEntities.stream().map(MemoPurchaseInvoiceChargesEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.memoPurchaseInvoiceMainRepository.deleteMainById(entity.getId(),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }


    @Override
    public List<MemoPurchaseInvoiceMainEntity> findAllMainByMainIds(List<Integer> ids) {
        return this.memoPurchaseInvoiceMainRepository.findAllMainByMainIds(ids);
    }

    @Override
    public List<MemoPurchaseInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> ids) {
        return this.memoPurchaseInvoiceDetailRepository.findAllDetailByMainIds(ids);
    }

    @Override
    public List<MemoPurchaseInvoiceChargesEntity> findAllIChargesByMainIds(List<Integer> ids) {
        return this.memoPurchaseInvoiceChargesRepository.findAllChargesByMainIds(ids);
    }

    @Override
    @Transactional
    public void removeAll(List<MemoPurchaseInvoiceMainEntity> entityList, List<MemoPurchaseInvoiceDetailEntity> detailEntities, List<MemoPurchaseInvoiceChargesEntity> chargesEntities) throws Exception {
        this.memoPurchaseInvoiceDetailRepository.deleteDetailById(detailEntities.stream().map(MemoPurchaseInvoiceDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.memoPurchaseInvoiceChargesRepository.deleteChargesById(chargesEntities.stream().map(MemoPurchaseInvoiceChargesEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.memoPurchaseInvoiceMainRepository.deleteMainByIdList(entityList.stream().map(MemoPurchaseInvoiceMainEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    public void lock(List<MemoPurchaseInvoiceMainEntity> entities) throws SQLException {
        this.memoPurchaseInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<MemoPurchaseInvoiceMainEntity> entities) throws SQLException {
        this.memoPurchaseInvoiceMainRepository.saveAll(entities);
    }
}
