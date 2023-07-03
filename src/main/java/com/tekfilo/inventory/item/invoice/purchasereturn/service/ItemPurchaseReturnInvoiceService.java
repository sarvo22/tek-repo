package com.tekfilo.inventory.item.invoice.purchasereturn.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.ItemRepository;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceChargesDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceDetailDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceMainDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceRequestPayload;
import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.purchase.repository.ItemPurchaseInvoiceMainRepository;
import com.tekfilo.inventory.item.invoice.purchasereturn.entity.ItemPurchaseReturnInvoiceChargesEntity;
import com.tekfilo.inventory.item.invoice.purchasereturn.entity.ItemPurchaseReturnInvoiceDetailEntity;
import com.tekfilo.inventory.item.invoice.purchasereturn.entity.ItemPurchaseReturnInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.purchasereturn.repository.ItemPurchaseReturnInvoiceChargesRepository;
import com.tekfilo.inventory.item.invoice.purchasereturn.repository.ItemPurchaseReturnInvoiceDetailRepository;
import com.tekfilo.inventory.item.invoice.purchasereturn.repository.ItemPurchaseReturnInvoiceMainRepository;
import com.tekfilo.inventory.master.SupplierAddressEntity;
import com.tekfilo.inventory.master.repository.SupplierAddressRepository;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
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

@Slf4j
@Service
@Transactional
public class ItemPurchaseReturnInvoiceService implements IItemPurchaseReturnInvoiceService {


    @Autowired
    ItemPurchaseReturnInvoiceMainRepository itemPurchaseReturnInvoiceMainRepository;

    @Autowired
    ItemPurchaseReturnInvoiceDetailRepository itemPurchaseReturnInvoiceDetailRepository;

    @Autowired
    ItemPurchaseReturnInvoiceChargesRepository itemPurchaseReturnInvoiceChargesRepository;

    @Autowired
    SupplierAddressRepository supplierAddressRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    ItemPurchaseInvoiceMainRepository itemPurchaseInvoiceMainRepository;


    @Override
    public Page<ItemPurchaseReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.itemPurchaseReturnInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public ItemPurchaseReturnInvoiceMainEntity save(ItemInvoiceMainDto itemInvoiceMainDto) throws Exception {
        return itemPurchaseReturnInvoiceMainRepository.save(convertToEntity(itemInvoiceMainDto));
    }

    private ItemPurchaseReturnInvoiceMainEntity convertToEntity(ItemInvoiceMainDto invoiceMainDto) {
        ItemPurchaseReturnInvoiceMainEntity entity = new ItemPurchaseReturnInvoiceMainEntity();
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
    public void modify(ItemInvoiceMainDto invoiceMainDto) throws Exception {
        itemPurchaseReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public ItemPurchaseReturnInvoiceMainEntity findById(Integer id) {
        return itemPurchaseReturnInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(ItemPurchaseReturnInvoiceMainEntity entity) {
        itemPurchaseReturnInvoiceMainRepository.save(entity);
    }

    @Override
    public List<ItemPurchaseReturnInvoiceDetailEntity> findAllDetail(Integer id) {
        List<ItemPurchaseReturnInvoiceDetailEntity> detailEntityList = itemPurchaseReturnInvoiceDetailRepository.findAllDetail(id);

        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setPurchaseInvoiceMain(this.itemPurchaseInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new ItemPurchaseInvoiceMainEntity()));
                    }
                }
            });
        }
        return detailEntityList;
    }

    @Override
    public ItemPurchaseReturnInvoiceDetailEntity saveDetail(ItemInvoiceDetailDto invoiceDetailDto) throws Exception {
        return itemPurchaseReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public void saveAllDetail(List<ItemInvoiceDetailDto> invoiceDetailDtoList) throws Exception {
        List<ItemPurchaseReturnInvoiceDetailEntity> detailEntityList = new ArrayList<>();
        invoiceDetailDtoList.stream().forEachOrdered(e -> {
            detailEntityList.add(convertDetailToEntity(e));
        });
        itemPurchaseReturnInvoiceDetailRepository.saveAll(detailEntityList);
    }

    private ItemPurchaseReturnInvoiceDetailEntity convertDetailToEntity(ItemInvoiceDetailDto invoiceDetailDto) {
        ItemPurchaseReturnInvoiceDetailEntity entity = new ItemPurchaseReturnInvoiceDetailEntity();
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
    public void modifyDetail(ItemInvoiceDetailDto invoiceDetailDto) throws Exception {
        itemPurchaseReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public ItemPurchaseReturnInvoiceDetailEntity findDetailById(Integer id) {
        return itemPurchaseReturnInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(ItemPurchaseReturnInvoiceDetailEntity entity) {
        itemPurchaseReturnInvoiceDetailRepository.save(entity);
    }

    @Override
    public List<ItemPurchaseReturnInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<ItemPurchaseReturnInvoiceChargesEntity> pagedList = itemPurchaseReturnInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public ItemPurchaseReturnInvoiceChargesEntity saveCharges(ItemInvoiceChargesDto itemInvoiceChargesDto) throws Exception {
        return itemPurchaseReturnInvoiceChargesRepository.save(convertChargesToEntity(itemInvoiceChargesDto));
    }

    @Override
    public void modifyCharges(ItemInvoiceChargesDto itemInvoiceChargesDto) throws Exception {
        itemPurchaseReturnInvoiceChargesRepository.save(convertChargesToEntity(itemInvoiceChargesDto));
    }

    private ItemPurchaseReturnInvoiceChargesEntity convertChargesToEntity(ItemInvoiceChargesDto itemInvoiceChargesDto) {
        ItemPurchaseReturnInvoiceChargesEntity entity = new ItemPurchaseReturnInvoiceChargesEntity();
        BeanUtils.copyProperties(itemInvoiceChargesDto, entity);
        entity.setInputPctAmountValue(entity.getInputPctAmountValue() == null ? new BigDecimal(0.00) : entity.getInputPctAmountValue());
        entity.setInputAmount(entity.getInputAmount() == null ? new BigDecimal(0.00) : entity.getInputAmount());
        entity.setIsSupplierPayable(itemInvoiceChargesDto.getIsSupplierPayable() == null ? 0 : itemInvoiceChargesDto.getIsSupplierPayable());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public ItemPurchaseReturnInvoiceChargesEntity findChargesById(Integer id) {
        return itemPurchaseReturnInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(ItemPurchaseReturnInvoiceChargesEntity entity) {
        itemPurchaseReturnInvoiceChargesRepository.save(entity);
    }

    @Override
    public List<ItemPurchaseReturnInvoiceMainEntity> findMain() {
        return itemPurchaseReturnInvoiceMainRepository.findMain();
    }

    @Override
    public ItemPurchaseReturnInvoiceMainEntity createPurchaseInvoice(ItemInvoiceRequestPayload invoiceRequestPayload) throws Exception {
        ItemPurchaseReturnInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        ItemPurchaseReturnInvoiceMainEntity entity = itemPurchaseReturnInvoiceMainRepository.save(createEntity);
        List<ItemInvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<ItemPurchaseReturnInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        itemPurchaseReturnInvoiceDetailRepository.saveAll(entities);
        List<ItemPurchaseReturnInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<ItemInvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        itemPurchaseReturnInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ItemEntity> getProductList(String searchKey) {
        List<ItemEntity> productEntityList = this.itemRepository.findByItemName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<ItemInvoiceDetailDto> convertFindAllDetailByMainId(List<ItemPurchaseReturnInvoiceDetailEntity> detailList) {
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
    public void changeStatus(Integer id) throws Exception {
        ItemPurchaseReturnInvoiceMainEntity mainEntity = this.itemPurchaseReturnInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.itemPurchaseReturnInvoiceMainRepository.save(mainEntity);
    }

    @Override
    public List<ItemPurchaseReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<ItemPurchaseReturnInvoiceDetailEntity> detailEntityList = this.itemPurchaseReturnInvoiceDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setPurchaseInvoiceMain(this.itemPurchaseInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new ItemPurchaseInvoiceMainEntity()));
                    }
                }
                e.setPurchaseReturnInvoiceMain(this.itemPurchaseReturnInvoiceMainRepository.findById(e.getInvId()).orElse(new ItemPurchaseReturnInvoiceMainEntity()));
            });
        }
        return detailEntityList;
    }

    @Override
    public void removeAll(ItemPurchaseReturnInvoiceMainEntity entity, List<ItemPurchaseReturnInvoiceDetailEntity> detailEntities, List<ItemPurchaseReturnInvoiceChargesEntity> chargesEntities) throws Exception {
        this.itemPurchaseReturnInvoiceChargesRepository.saveAll(chargesEntities);
        this.itemPurchaseReturnInvoiceDetailRepository.saveAll(detailEntities);
        this.itemPurchaseReturnInvoiceMainRepository.save(entity);
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = Timestamp.from(Instant.now());
        this.itemPurchaseReturnInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.itemPurchaseReturnInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.itemPurchaseReturnInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<ItemPurchaseReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.itemPurchaseReturnInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<ItemPurchaseReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.itemPurchaseReturnInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<ItemPurchaseReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.itemPurchaseReturnInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<ItemPurchaseReturnInvoiceMainEntity> entities) throws Exception {
        this.itemPurchaseReturnInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<ItemPurchaseReturnInvoiceMainEntity> entities) throws Exception {
        this.itemPurchaseReturnInvoiceMainRepository.saveAll(entities);
    }
}
