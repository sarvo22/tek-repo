package com.tekfilo.inventory.item.invoice.purchase.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.ItemRepository;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceChargesDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceDetailDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceMainDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceRequestPayload;
import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceChargesEntity;
import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceDetailEntity;
import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.purchase.repository.ItemPurchaseInvoiceChargesRepository;
import com.tekfilo.inventory.item.invoice.purchase.repository.ItemPurchaseInvoiceDetailRepository;
import com.tekfilo.inventory.item.invoice.purchase.repository.ItemPurchaseInvoiceMainRepository;
import com.tekfilo.inventory.master.SupplierAddressEntity;
import com.tekfilo.inventory.master.repository.SupplierAddressRepository;
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
public class ItemPurchaseInvoiceService implements IItemPurchaseInvoiceService {


    @Autowired
    ItemPurchaseInvoiceMainRepository itemPurchaseInvoiceMainRepository;

    @Autowired
    ItemPurchaseInvoiceDetailRepository itemPurchaseInvoiceDetailRepository;

    @Autowired
    ItemPurchaseInvoiceChargesRepository itemPurchaseInvoiceChargesRepository;

    @Autowired
    SupplierAddressRepository supplierAddressRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    StockService stockService;


    @Override
    public Page<ItemPurchaseInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.itemPurchaseInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public ItemPurchaseInvoiceMainEntity save(ItemInvoiceMainDto itemInvoiceMainDto) throws Exception {
        return itemPurchaseInvoiceMainRepository.save(convertToEntity(itemInvoiceMainDto));
    }

    private ItemPurchaseInvoiceMainEntity convertToEntity(ItemInvoiceMainDto itemInvoiceMainDto) {
        ItemPurchaseInvoiceMainEntity entity = new ItemPurchaseInvoiceMainEntity();
        BeanUtils.copyProperties(itemInvoiceMainDto, entity);
        entity.setTotalInvoiceAmount(new BigDecimal(0.00));
        entity.setTotalPaidAmount(new BigDecimal(0.00));
        entity.setPaymentStatus(InvoiceConstants.UNPAID);
        entity.setAccountingStatus(InvoiceConstants.UNPOSTED);

        if (Optional.ofNullable(itemInvoiceMainDto.getBillingAddressId()).isPresent()) {
            if (itemInvoiceMainDto.getBillingAddressId() > 0) {
                SupplierAddressEntity billingAddress = this.supplierAddressRepository.findById(itemInvoiceMainDto.getBillingAddressId()).orElse(new SupplierAddressEntity());
                if (billingAddress.getAddressId() != null) {
                    entity.setBillingAddressId(itemInvoiceMainDto.getBillingAddressId());
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


        if (Optional.ofNullable(itemInvoiceMainDto.getShippingAddressId()).isPresent()) {
            if (itemInvoiceMainDto.getShippingAddressId() > 0) {
                SupplierAddressEntity shippingAddress = this.supplierAddressRepository.findById(itemInvoiceMainDto.getShippingAddressId()).orElse(new SupplierAddressEntity());
                if (shippingAddress.getAddressId() != null) {
                    entity.setShippingAddressId(itemInvoiceMainDto.getShippingAddressId());
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

        entity.setSequence(itemInvoiceMainDto.getSequence() == null ? 0 : itemInvoiceMainDto.getSequence());
        entity.setIsLocked(itemInvoiceMainDto.getIsLocked() == null ? 0 : itemInvoiceMainDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(itemInvoiceMainDto.getIsDeleted() == null ? 0 : itemInvoiceMainDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(ItemInvoiceMainDto itemInvoiceMainDto) throws Exception {
        itemPurchaseInvoiceMainRepository.save(convertToEntity(itemInvoiceMainDto));
    }

    @Override
    public ItemPurchaseInvoiceMainEntity findById(Integer id) {
        return itemPurchaseInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(ItemPurchaseInvoiceMainEntity entity) {
        itemPurchaseInvoiceMainRepository.save(entity);
    }

    @Override
    public List<ItemPurchaseInvoiceDetailEntity> findAllDetail(Integer id) {
        List<ItemPurchaseInvoiceDetailEntity> pagedList = itemPurchaseInvoiceDetailRepository.findAllDetail(id);
        return (pagedList);
    }

    @Override
    public ItemPurchaseInvoiceDetailEntity saveDetail(ItemInvoiceDetailDto itemInvoiceDetailDto) throws Exception {
        return itemPurchaseInvoiceDetailRepository.save(convertDetailToEntity(itemInvoiceDetailDto));
    }

    private ItemPurchaseInvoiceDetailEntity convertDetailToEntity(ItemInvoiceDetailDto itemInvoiceDetailDto) {
        ItemPurchaseInvoiceDetailEntity entity = new ItemPurchaseInvoiceDetailEntity();
        BeanUtils.copyProperties(itemInvoiceDetailDto, entity);
        entity.setInvQty(entity.getInvQty() == null ? new BigDecimal(0.00) : entity.getInvQty());
        entity.setInputRate(entity.getInputRate() == null ? new BigDecimal(0.00) : entity.getInputRate());
        entity.setInputAmount(new BigDecimal(entity.getInputRate().doubleValue() * entity.getInvQty().doubleValue()));
        entity.setCostPrice(entity.getCostPrice() == null ? new BigDecimal(0.00) : entity.getCostPrice());
        entity.setDiscountValue(entity.getDiscountValue() == null ? new BigDecimal(0.00) : entity.getDiscountValue());
        entity.setConfQty(itemInvoiceDetailDto.getConfQty() == null ? new BigDecimal(0.00) : itemInvoiceDetailDto.getConfQty());
        entity.setRetQty(itemInvoiceDetailDto.getRetQty() == null ? new BigDecimal(0.00) : itemInvoiceDetailDto.getRetQty());
        entity.setConfInputRate(itemInvoiceDetailDto.getConfInputRate() == null ? new BigDecimal(0.00) : itemInvoiceDetailDto.getConfInputRate());
        entity.setConfInputAmount(new BigDecimal(entity.getConfQty().doubleValue() * entity.getConfInputRate().doubleValue()));
        entity.setRetInputRate(itemInvoiceDetailDto.getRetInputRate() == null ? new BigDecimal(0.00) : itemInvoiceDetailDto.getRetInputRate());
        entity.setRetInputAmount(new BigDecimal(entity.getRetQty().doubleValue() * entity.getRetInputRate().doubleValue()));
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(itemInvoiceDetailDto.getIsDeleted() == null ? 0 : itemInvoiceDetailDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyDetail(ItemInvoiceDetailDto itemInvoiceDetailDto) throws Exception {
        itemPurchaseInvoiceDetailRepository.save(convertDetailToEntity(itemInvoiceDetailDto));
    }

    @Override
    public ItemPurchaseInvoiceDetailEntity findDetailById(Integer id) {
        return itemPurchaseInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(ItemPurchaseInvoiceDetailEntity entity) {
        itemPurchaseInvoiceDetailRepository.save(entity);
    }

    @Override
    public List<ItemPurchaseInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<ItemPurchaseInvoiceChargesEntity> pagedList = itemPurchaseInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public ItemPurchaseInvoiceChargesEntity saveCharges(ItemInvoiceChargesDto invoiceChargesDto) throws Exception {
        return itemPurchaseInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(ItemInvoiceChargesDto invoiceChargesDto) throws Exception {
        itemPurchaseInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private ItemPurchaseInvoiceChargesEntity convertChargesToEntity(ItemInvoiceChargesDto invoiceChargesDto) {
        ItemPurchaseInvoiceChargesEntity entity = new ItemPurchaseInvoiceChargesEntity();
        BeanUtils.copyProperties(invoiceChargesDto, entity);
        entity.setInputPctAmountValue(entity.getInputPctAmountValue() == null ? new BigDecimal(0.00) : entity.getInputPctAmountValue());
        entity.setInputAmount(entity.getInputAmount() == null ? new BigDecimal(0.00) : entity.getInputAmount());
        entity.setIsSupplierPayable(invoiceChargesDto.getIsSupplierPayable() == null ? 0 : invoiceChargesDto.getIsSupplierPayable());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public ItemPurchaseInvoiceChargesEntity findChargesById(Integer id) {
        return itemPurchaseInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(ItemPurchaseInvoiceChargesEntity entity) {
        itemPurchaseInvoiceChargesRepository.save(entity);
    }

    @Override
    public List<ItemPurchaseInvoiceMainEntity> findMain() {
        return itemPurchaseInvoiceMainRepository.findMain();
    }

    @Override
    public ItemPurchaseInvoiceMainEntity createPurchaseInvoice(ItemInvoiceRequestPayload itemInvoiceRequestPayload) throws Exception {
        ItemPurchaseInvoiceMainEntity createEntity = this.convertToEntity(itemInvoiceRequestPayload.getMain());
        if (!Optional.ofNullable(itemInvoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(itemInvoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        ItemPurchaseInvoiceMainEntity entity = itemPurchaseInvoiceMainRepository.save(createEntity);
        List<ItemInvoiceDetailDto> itemInvoiceDetailDtoList = setInvoiceMainId2DetailList(itemInvoiceRequestPayload.getDetail(), entity.getId());
        List<ItemPurchaseInvoiceDetailEntity> entities = new ArrayList<>();

        itemInvoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        itemPurchaseInvoiceDetailRepository.saveAll(entities);
        List<ItemPurchaseInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<ItemInvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(itemInvoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        itemPurchaseInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ItemEntity> getProductList(String searchKey) {
        List<ItemEntity> productEntityList = this.itemRepository.findByItemName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    @Override
    public List<ItemPurchaseInvoiceDetailEntity> findDetailByProductId(Integer productId) {
        return this.itemPurchaseInvoiceDetailRepository.findAllDetailByProductId(productId);
    }

    private List<ItemInvoiceDetailDto> convertFindAllDetailByMainId(List<ItemPurchaseInvoiceDetailEntity> detailList) {
        List<ItemInvoiceDetailDto> itemInvoiceDetailDtoList = new ArrayList<>();
        detailList.stream().forEachOrdered(e -> {
            ItemInvoiceDetailDto itemInvoiceDetailDto = new ItemInvoiceDetailDto();
            itemInvoiceDetailDto.setRowId(e.getId());
            itemInvoiceDetailDto.setId(e.getId());
            itemInvoiceDetailDto.setInvId(e.getInvId());
            itemInvoiceDetailDto.setProductId(e.getProductId());
            itemInvoiceDetailDto.setProductDescription(e.getProductDescription());
            itemInvoiceDetailDto.setInvQty(e.getInvQty());
            itemInvoiceDetailDto.setUom(e.getUom());
            itemInvoiceDetailDto.setInputRate(e.getInputRate());
            itemInvoiceDetailDto.setInputAmount(e.getInputAmount());
            itemInvoiceDetailDto.setDiscountType(e.getDiscountType());
            itemInvoiceDetailDto.setDiscountValue(e.getDiscountValue());
            itemInvoiceDetailDto.setProductNo(e.getProduct() != null ? e.getProduct().getItemName() : null);
            itemInvoiceDetailDto.setDescription(e.getProductDescription());
            itemInvoiceDetailDto.setPicture1Path(e.getProduct() != null ? e.getProduct().getImageUrl() : null);
            itemInvoiceDetailDtoList.add(itemInvoiceDetailDto);
        });
        return itemInvoiceDetailDtoList;
    }

    private List<ItemInvoiceDetailDto> convert2ItemItemInvoiceDetailDto(List<ItemEntity> productEntityList) {
        List<ItemInvoiceDetailDto> itemInvoiceDetailDtoList = new ArrayList<>();
        productEntityList.stream().forEachOrdered(e -> {
            ItemInvoiceDetailDto itemInvoiceDetailDto = new ItemInvoiceDetailDto();
            itemInvoiceDetailDto.setProductId(e.getId());
            itemInvoiceDetailDto.setProductDescription(e.getDescription());
            itemInvoiceDetailDto.setUom(e.getUnit());
            itemInvoiceDetailDto.setProductNo(e.getItemName());
            itemInvoiceDetailDto.setDescription(e.getDescription());
            itemInvoiceDetailDto.setPicture1Path(e.getImageUrl());
            itemInvoiceDetailDtoList.add(itemInvoiceDetailDto);
        });
        return itemInvoiceDetailDtoList;
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
    public void changeStatus(Integer id) throws Exception {
        ItemPurchaseInvoiceMainEntity mainEntity = this.itemPurchaseInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.itemPurchaseInvoiceMainRepository.save(mainEntity);
    }


    @Override
    public List<ItemPurchaseInvoiceDetailEntity> findDetailByIdWithStock(Integer purchaseInvoiceId) {
        List<ItemPurchaseInvoiceDetailEntity> entityList = this.itemPurchaseInvoiceDetailRepository.findAllDetail(purchaseInvoiceId);
        entityList.stream().forEach(e -> {
            StockEntity stock = this.stockService.getStock(e.getProductId(), e.getBinId(), CompanyContext.getCurrentCompany());
            e.setStock(stock);
        });
        return entityList;
    }

    @Override
    public List<ItemPurchaseInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency) {
        List<ItemPurchaseInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<ItemPurchaseInvoiceMainEntity> mainEntityList = this.itemPurchaseInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();
        List<ItemPurchaseInvoiceDetailEntity> detailEntityList = this.itemPurchaseInvoiceDetailRepository.findAll(appender.getInClassFilter(
                mainEntityList.stream().map(ItemPurchaseInvoiceMainEntity::getId).collect(Collectors.toList()), "invId"
        ));

        detailEntityList.removeIf(e ->
                e.getInvQty().doubleValue() - e.getRetQty().doubleValue() - e.getConfQty().doubleValue() == 0
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
    public List<ItemPurchaseInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId) {
        List<ItemPurchaseInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<ItemPurchaseInvoiceMainEntity> mainEntityList = this.itemPurchaseInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();

        List<ItemPurchaseInvoiceDetailEntity> detailEntityList = this.itemPurchaseInvoiceDetailRepository.findAll(
                appender.getReturnClassFilter(mainEntityList.stream().map(ItemPurchaseInvoiceMainEntity::getId).collect(Collectors.toList()),
                        productId)
        );

        detailEntityList.removeIf(e ->
                e.getInvQty().doubleValue() - e.getRetQty().doubleValue() - e.getConfQty().doubleValue() == 0
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
    public void removeAll(ItemPurchaseInvoiceMainEntity entity, List<ItemPurchaseInvoiceDetailEntity> detailEntities, List<ItemPurchaseInvoiceChargesEntity> chargesEntities) throws Exception {
        this.itemPurchaseInvoiceDetailRepository.deleteDetailById(detailEntities.stream().map(ItemPurchaseInvoiceDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), Timestamp.from(Instant.now()));

        this.itemPurchaseInvoiceChargesRepository.deleteChargesById(chargesEntities.stream().map(ItemPurchaseInvoiceChargesEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), Timestamp.from(Instant.now()));

        this.itemPurchaseInvoiceMainRepository.deleteMainById(entity.getId(),
                UserContext.getLoggedInUser(), Timestamp.from(Instant.now()));
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = Timestamp.from(Instant.now());
        this.itemPurchaseInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.itemPurchaseInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.itemPurchaseInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<ItemPurchaseInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.itemPurchaseInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<ItemPurchaseInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.itemPurchaseInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<ItemPurchaseInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.itemPurchaseInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<ItemPurchaseInvoiceMainEntity> entities) throws Exception {
        this.itemPurchaseInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<ItemPurchaseInvoiceMainEntity> entities) throws Exception {
        this.itemPurchaseInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public List<ItemPurchaseInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<ItemPurchaseInvoiceDetailEntity> detailEntityList = this.itemPurchaseInvoiceDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        return detailEntityList;
    }
}
