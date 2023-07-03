package com.tekfilo.inventory.item.invoice.salesinvoice.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.ItemRepository;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceChargesDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceDetailDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceMainDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceRequestPayload;
import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceChargesEntity;
import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceDetailEntity;
import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.salesinvoice.repository.ItemSalesInvoiceChargesRepository;
import com.tekfilo.inventory.item.invoice.salesinvoice.repository.ItemSalesInvoiceDetailRepository;
import com.tekfilo.inventory.item.invoice.salesinvoice.repository.ItemSalesInvoiceMainRepository;
import com.tekfilo.inventory.item.invoice.stock.entity.ItemStockEntity;
import com.tekfilo.inventory.item.invoice.stock.service.ItemStockService;
import com.tekfilo.inventory.master.CustomerAddressEntity;
import com.tekfilo.inventory.master.repository.CustomerAddressRepository;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
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
public class ItemSalesInvoiceService implements IItemSalesInvoiceService {

    @Autowired
    ItemSalesInvoiceMainRepository itemSalesInvoiceMainRepository;

    @Autowired
    ItemSalesInvoiceDetailRepository itemSalesInvoiceDetailRepository;

    @Autowired
    ItemSalesInvoiceChargesRepository itemSalesInvoiceChargesRepository;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    ItemStockService itemStockService;


    @Override
    public Page<ItemSalesInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.itemSalesInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public ItemSalesInvoiceMainEntity save(ItemInvoiceMainDto invoiceMainDto) throws Exception {
        return itemSalesInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private ItemSalesInvoiceMainEntity convertToEntity(ItemInvoiceMainDto invoiceMainDto) {
        ItemSalesInvoiceMainEntity entity = new ItemSalesInvoiceMainEntity();
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
    public void modify(ItemInvoiceMainDto invoiceMainDto) throws Exception {
        itemSalesInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public ItemSalesInvoiceMainEntity findById(Integer id) {
        return itemSalesInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(ItemSalesInvoiceMainEntity entity) {
        itemSalesInvoiceMainRepository.save(entity);
    }

    @Override
    public List<ItemSalesInvoiceDetailEntity> findAllDetail(Integer id) {
        List<ItemSalesInvoiceDetailEntity> pagedList = itemSalesInvoiceDetailRepository.findAllDetail(id);
        return (pagedList);
    }

    @Override
    public ItemSalesInvoiceDetailEntity saveDetail(ItemInvoiceDetailDto invoiceDetailDto) throws Exception {
        return itemSalesInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    private ItemSalesInvoiceDetailEntity convertDetailToEntity(ItemInvoiceDetailDto invoiceDetailDto) {
        ItemSalesInvoiceDetailEntity entity = new ItemSalesInvoiceDetailEntity();
        BeanUtils.copyProperties(invoiceDetailDto, entity);
        entity.setInvQty(entity.getInvQty() == null ? new BigDecimal(0.00) : entity.getInvQty());
        entity.setInputRate(entity.getInputRate() == null ? new BigDecimal(0.00) : entity.getInputRate());
        entity.setInputAmount(new BigDecimal(entity.getInputRate().doubleValue() * entity.getInvQty().doubleValue()));
        entity.setCostPrice(entity.getCostPrice() == null ? new BigDecimal(0.00) : entity.getCostPrice());
        entity.setDiscountValue(entity.getDiscountValue() == null ? new BigDecimal(0.00) : entity.getDiscountValue());
        entity.setConfQty(invoiceDetailDto.getConfQty() == null ? new BigDecimal(0.00) : entity.getConfQty());
        entity.setRetQty(invoiceDetailDto.getRetQty() == null ? new BigDecimal(0.00) : entity.getRetQty());
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
    public void modifyDetail(ItemInvoiceDetailDto invoiceDetailDto) throws Exception {
        itemSalesInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public ItemSalesInvoiceDetailEntity findDetailById(Integer id) {
        return itemSalesInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(ItemSalesInvoiceDetailEntity entity) {
        itemSalesInvoiceDetailRepository.save(entity);
    }

    @Override
    public List<ItemSalesInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<ItemSalesInvoiceChargesEntity> pagedList = itemSalesInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public ItemSalesInvoiceChargesEntity saveCharges(ItemInvoiceChargesDto invoiceChargesDto) throws Exception {
        return itemSalesInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(ItemInvoiceChargesDto invoiceChargesDto) throws Exception {
        itemSalesInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private ItemSalesInvoiceChargesEntity convertChargesToEntity(ItemInvoiceChargesDto invoiceChargesDto) {
        ItemSalesInvoiceChargesEntity entity = new ItemSalesInvoiceChargesEntity();
        BeanUtils.copyProperties(invoiceChargesDto, entity);
        entity.setInputPctAmountValue(entity.getInputPctAmountValue() == null ? new BigDecimal(0.00) : entity.getInputPctAmountValue());
        entity.setInputAmount(entity.getInputAmount() == null ? new BigDecimal(0.00) : entity.getInputAmount());
        entity.setIsCustomerPayable(invoiceChargesDto.getIsCustomerPayable() == null ? 0 : invoiceChargesDto.getIsCustomerPayable());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public ItemSalesInvoiceChargesEntity findChargesById(Integer id) {
        return itemSalesInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(ItemSalesInvoiceChargesEntity entity) {
        itemSalesInvoiceChargesRepository.save(entity);
    }

    @Override
    public List<ItemSalesInvoiceMainEntity> findMain() {
        return itemSalesInvoiceMainRepository.findMain();
    }

    @Override
    public ItemSalesInvoiceMainEntity createSalesInvoice(ItemInvoiceRequestPayload invoiceRequestPayload) throws Exception {
        ItemSalesInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        ItemSalesInvoiceMainEntity entity = itemSalesInvoiceMainRepository.save(createEntity);
        List<ItemInvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<ItemSalesInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        itemSalesInvoiceDetailRepository.saveAll(entities);
        List<ItemSalesInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<ItemInvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        itemSalesInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ItemSalesInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<ItemSalesInvoiceDetailEntity> detailEntityList = this.itemSalesInvoiceDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        return detailEntityList;
    }

    @Override
    public List<ItemEntity> getProductList(String searchKey) {
        List<ItemEntity> productEntityList = this.itemRepository.findByItemName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<ItemInvoiceDetailDto> convertFindAllDetailByMainId(List<ItemSalesInvoiceDetailEntity> detailList) {
        List<ItemInvoiceDetailDto> invoiceDetailDtoList = new ArrayList<>();
        detailList.stream().forEachOrdered(e -> {
            ItemInvoiceDetailDto invoiceDetailDto = new ItemInvoiceDetailDto();
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
            invoiceDetailDto.setProductNo(e.getProduct() != null ? e.getProduct().getItemName() : null);
            invoiceDetailDto.setDescription(e.getProductDescription());
            invoiceDetailDto.setPicture1Path(e.getProduct() != null ? e.getProduct().getImageUrl() : null);
            invoiceDetailDtoList.add(invoiceDetailDto);
        });
        return invoiceDetailDtoList;
    }

    private List<ItemInvoiceDetailDto> convert2InvoiceDetailDto(List<ItemEntity> productEntityList) {
        List<ItemInvoiceDetailDto> invoiceDetailDtoList = new ArrayList<>();
        productEntityList.stream().forEachOrdered(e -> {
            ItemInvoiceDetailDto invoiceDetailDto = new ItemInvoiceDetailDto();
            invoiceDetailDto.setProductId(e.getId());
            invoiceDetailDto.setProductDescription(e.getDescription());
            invoiceDetailDto.setUom(e.getUnit());
            invoiceDetailDto.setProductNo(e.getItemName());
            invoiceDetailDto.setDescription(e.getDescription());
            invoiceDetailDto.setPicture1Path(e.getImageUrl());
            invoiceDetailDtoList.add(invoiceDetailDto);
        });
        return invoiceDetailDtoList;
    }

    private List<ItemInvoiceChargesDto> setInvoiceMainId2ChargesList(List<ItemInvoiceChargesDto> charges, Integer invoiceMainId) {
        charges.stream().forEachOrdered(c -> {
            c.setInvId(invoiceMainId);
        });
        return charges;
    }

    private List<ItemInvoiceDetailDto> setInvoiceMainId2DetailList(List<ItemInvoiceDetailDto> detail, Integer invoiceMainId) {
        detail.stream().forEachOrdered(e -> {
            e.setInvId(invoiceMainId);
        });
        return detail;
    }


    @Override
    public List<ItemSalesInvoiceDetailEntity> findDetailByProductId(Integer productId) {
        return this.itemSalesInvoiceDetailRepository.findAllDetailByProductId(productId);
    }

    @Override
    public void changeStatus(Integer id) throws Exception {
        ItemSalesInvoiceMainEntity mainEntity = this.itemSalesInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.itemSalesInvoiceMainRepository.save(mainEntity);
    }

    @Override
    public List<ItemSalesInvoiceDetailEntity> findDetailByIdWithStock(Integer salesInvoiceId) {
        List<ItemSalesInvoiceDetailEntity> entityList = this.itemSalesInvoiceDetailRepository.findAllDetail(salesInvoiceId);
        entityList.stream().forEach(e -> {
            ItemStockEntity stock = this.itemStockService.getStock(e.getProductId(), e.getBinId(), CompanyContext.getCurrentCompany());
            e.setStock(stock);
        });
        return entityList;
    }

    @Override
    public List<ItemSalesInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency) {
        List<ItemSalesInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<ItemSalesInvoiceMainEntity> mainEntityList = this.itemSalesInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();
        List<ItemSalesInvoiceDetailEntity> detailEntityList = this.itemSalesInvoiceDetailRepository.findAll(appender.getInClassFilter(
                mainEntityList.stream().map(ItemSalesInvoiceMainEntity::getId).collect(Collectors.toList()), "invId"
        ));

        detailEntityList.removeIf(e ->
                e.getInvQty().doubleValue() - e.getRetQty().doubleValue() - e.getConfQty().doubleValue() == 0
        );

        if (detailEntityList.size() == 0) {
            return pendingInvoiceList;
        }

        detailEntityList.stream().forEach(e -> {
            ItemStockEntity stock = this.itemStockService.getStock(e.getProductId(), e.getBinId(), CompanyContext.getCurrentCompany());
            if (Optional.ofNullable(stock).isPresent()) {
                if (stock.getBalanceQty().doubleValue() > 0) {
                    e.setStock(stock);
                    pendingInvoiceList.add(e);
                }
            }
        });

        return pendingInvoiceList;
    }

    @Override
    public List<ItemSalesInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId) {
        List<ItemSalesInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<ItemSalesInvoiceMainEntity> mainEntityList = this.itemSalesInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();

        List<ItemSalesInvoiceDetailEntity> detailEntityList = this.itemSalesInvoiceDetailRepository.findAll(
                appender.getReturnClassFilter(mainEntityList.stream().map(ItemSalesInvoiceMainEntity::getId).collect(Collectors.toList()),
                        productId)
        );

        detailEntityList.removeIf(e ->
                e.getInvQty().doubleValue() - e.getRetQty().doubleValue() - e.getConfQty().doubleValue() == 0
        );

        if (detailEntityList.size() == 0) {
            return pendingInvoiceList;
        }

        detailEntityList.stream().forEach(e -> {
            ItemStockEntity stock = this.itemStockService.getStock(e.getProductId(), e.getBinId(), CompanyContext.getCurrentCompany());
            if (Optional.ofNullable(stock).isPresent()) {
                if (stock.getBalanceQty().doubleValue() > 0) {
                    e.setStock(stock);
                    pendingInvoiceList.add(e);
                }
            }
        });
        return pendingInvoiceList;
    }

    @Override
    public void removeAll(ItemSalesInvoiceMainEntity entity, List<ItemSalesInvoiceDetailEntity> detailEntities, List<ItemSalesInvoiceChargesEntity> chargesEntities) throws Exception {
        this.itemSalesInvoiceChargesRepository.saveAll(chargesEntities);
        this.itemSalesInvoiceDetailRepository.saveAll(detailEntities);
        this.itemSalesInvoiceMainRepository.save(entity);
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = Timestamp.from(Instant.now());
        this.itemSalesInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.itemSalesInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.itemSalesInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<ItemSalesInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.itemSalesInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<ItemSalesInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.itemSalesInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<ItemSalesInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.itemSalesInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<ItemSalesInvoiceMainEntity> entities) throws Exception {
        this.itemSalesInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<ItemSalesInvoiceMainEntity> entities) throws Exception {
        this.itemSalesInvoiceMainRepository.saveAll(entities);
    }
}
