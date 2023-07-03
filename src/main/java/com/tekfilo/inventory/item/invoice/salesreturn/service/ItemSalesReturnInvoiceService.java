package com.tekfilo.inventory.item.invoice.salesreturn.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.ItemRepository;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceChargesDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceDetailDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceMainDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceRequestPayload;
import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.salesinvoice.repository.ItemSalesInvoiceMainRepository;
import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceChargesEntity;
import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceDetailEntity;
import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.salesreturn.repository.ItemSalesReturnInvoiceChargesRepository;
import com.tekfilo.inventory.item.invoice.salesreturn.repository.ItemSalesReturnInvoiceDetailRepository;
import com.tekfilo.inventory.item.invoice.salesreturn.repository.ItemSalesReturnInvoiceMainRepository;
import com.tekfilo.inventory.master.CustomerAddressEntity;
import com.tekfilo.inventory.master.repository.CustomerAddressRepository;
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
public class ItemSalesReturnInvoiceService implements IItemSalesReturnInvoiceService {

    @Autowired
    ItemSalesReturnInvoiceMainRepository itemSalesReturnInvoiceMainRepository;

    @Autowired
    ItemSalesReturnInvoiceDetailRepository itemSalesReturnInvoiceDetailRepository;

    @Autowired
    ItemSalesReturnInvoiceChargesRepository itemSalesReturnInvoiceChargesRepository;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    ItemSalesInvoiceMainRepository itemSalesInvoiceMainRepository;


    @Override
    public Page<ItemSalesReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.itemSalesReturnInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public ItemSalesReturnInvoiceMainEntity save(ItemInvoiceMainDto invoiceMainDto) throws Exception {
        return itemSalesReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private ItemSalesReturnInvoiceMainEntity convertToEntity(ItemInvoiceMainDto invoiceMainDto) {
        ItemSalesReturnInvoiceMainEntity entity = new ItemSalesReturnInvoiceMainEntity();
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
        itemSalesReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public ItemSalesReturnInvoiceMainEntity findById(Integer id) {
        return itemSalesReturnInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(ItemSalesReturnInvoiceMainEntity entity) {
        itemSalesReturnInvoiceMainRepository.save(entity);
    }

    @Override
    public List<ItemSalesReturnInvoiceDetailEntity> findAllDetail(Integer id) {
        List<ItemSalesReturnInvoiceDetailEntity> detailEntityList = itemSalesReturnInvoiceDetailRepository.findAllDetail(id);
        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceType()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setSalesInvoiceMain(this.itemSalesInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new ItemSalesInvoiceMainEntity()));
                    }
                }
            });
        }
        return detailEntityList;
    }

    @Override
    public ItemSalesReturnInvoiceDetailEntity saveDetail(ItemInvoiceDetailDto invoiceDetailDto) throws Exception {
        return itemSalesReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public void saveAllDetail(List<ItemInvoiceDetailDto> invoiceDetailDtoList) throws Exception {
        List<ItemSalesReturnInvoiceDetailEntity> detailEntityList = new ArrayList<>();
        invoiceDetailDtoList.stream().forEachOrdered(e -> {
            detailEntityList.add(convertDetailToEntity(e));
        });
        itemSalesReturnInvoiceDetailRepository.saveAll(detailEntityList);
    }

    private ItemSalesReturnInvoiceDetailEntity convertDetailToEntity(ItemInvoiceDetailDto invoiceDetailDto) {
        ItemSalesReturnInvoiceDetailEntity entity = new ItemSalesReturnInvoiceDetailEntity();
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
        itemSalesReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public ItemSalesReturnInvoiceDetailEntity findDetailById(Integer id) {
        return itemSalesReturnInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(ItemSalesReturnInvoiceDetailEntity entity) {
        itemSalesReturnInvoiceDetailRepository.save(entity);
    }

    @Override
    public List<ItemSalesReturnInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<ItemSalesReturnInvoiceChargesEntity> pagedList = itemSalesReturnInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public ItemSalesReturnInvoiceChargesEntity saveCharges(ItemInvoiceChargesDto invoiceChargesDto) throws Exception {
        return itemSalesReturnInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(ItemInvoiceChargesDto invoiceChargesDto) throws Exception {
        itemSalesReturnInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private ItemSalesReturnInvoiceChargesEntity convertChargesToEntity(ItemInvoiceChargesDto invoiceChargesDto) {
        ItemSalesReturnInvoiceChargesEntity entity = new ItemSalesReturnInvoiceChargesEntity();
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
    public ItemSalesReturnInvoiceChargesEntity findChargesById(Integer id) {
        return itemSalesReturnInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(ItemSalesReturnInvoiceChargesEntity entity) {
        itemSalesReturnInvoiceChargesRepository.save(entity);
    }

    @Override
    public List<ItemSalesReturnInvoiceMainEntity> findMain() {
        return itemSalesReturnInvoiceMainRepository.findMain();
    }

    @Override
    public ItemSalesReturnInvoiceMainEntity createSalesInvoice(ItemInvoiceRequestPayload invoiceRequestPayload) throws Exception {
        ItemSalesReturnInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        ItemSalesReturnInvoiceMainEntity entity = itemSalesReturnInvoiceMainRepository.save(createEntity);
        List<ItemInvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<ItemSalesReturnInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        itemSalesReturnInvoiceDetailRepository.saveAll(entities);
        List<ItemSalesReturnInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<ItemInvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        itemSalesReturnInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ItemEntity> getProductList(String searchKey) {
        List<ItemEntity> productEntityList = this.itemRepository.findByItemName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<ItemInvoiceDetailDto> convertFindAllDetailByMainId(List<ItemSalesReturnInvoiceDetailEntity> detailList) {
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
        ItemSalesReturnInvoiceMainEntity mainEntity = this.itemSalesReturnInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.itemSalesReturnInvoiceMainRepository.save(mainEntity);
    }

    @Override
    public List<ItemSalesReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<ItemSalesReturnInvoiceDetailEntity> detailEntityList = this.itemSalesReturnInvoiceDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setSalesInvoiceMain(this.itemSalesInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new ItemSalesInvoiceMainEntity()));
                    }
                }
                e.setSalesReturnInvoiceMain(this.itemSalesReturnInvoiceMainRepository.findById(e.getInvId()).orElse(new ItemSalesReturnInvoiceMainEntity()));
            });
        }
        return detailEntityList;
    }

    @Override
    public void removeAll(ItemSalesReturnInvoiceMainEntity entity, List<ItemSalesReturnInvoiceDetailEntity> detailEntities, List<ItemSalesReturnInvoiceChargesEntity> chargesEntities) throws Exception {
        this.itemSalesReturnInvoiceChargesRepository.saveAll(chargesEntities);
        this.itemSalesReturnInvoiceDetailRepository.saveAll(detailEntities);
        this.itemSalesReturnInvoiceMainRepository.save(entity);
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = Timestamp.from(Instant.now());
        this.itemSalesReturnInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.itemSalesReturnInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.itemSalesReturnInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<ItemSalesReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.itemSalesReturnInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<ItemSalesReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.itemSalesReturnInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<ItemSalesReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.itemSalesReturnInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<ItemSalesReturnInvoiceMainEntity> entities) throws Exception {
        this.itemSalesReturnInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<ItemSalesReturnInvoiceMainEntity> entities) throws Exception {
        this.itemSalesReturnInvoiceMainRepository.saveAll(entities);
    }
}
