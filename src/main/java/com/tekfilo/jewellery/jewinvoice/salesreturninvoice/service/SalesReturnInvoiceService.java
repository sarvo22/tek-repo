package com.tekfilo.jewellery.jewinvoice.salesreturninvoice.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.repository.SalesInvoiceMainRepository;
import com.tekfilo.jewellery.jewinvoice.salesreturninvoice.entity.SalesReturnInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.salesreturninvoice.entity.SalesReturnInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.salesreturninvoice.entity.SalesReturnInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.salesreturninvoice.repository.SalesReturnInvoiceChargesRepository;
import com.tekfilo.jewellery.jewinvoice.salesreturninvoice.repository.SalesReturnInvoiceDetailRepository;
import com.tekfilo.jewellery.jewinvoice.salesreturninvoice.repository.SalesReturnInvoiceMainRepository;
import com.tekfilo.jewellery.master.CustomerAddressEntity;
import com.tekfilo.jewellery.master.repository.CustomerAddressRepository;
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
public class SalesReturnInvoiceService implements ISalesReturnInvoiceService {

    @Autowired
    SalesReturnInvoiceMainRepository salesReturnInvoiceMainRepository;

    @Autowired
    SalesReturnInvoiceDetailRepository salesReturnInvoiceDetailRepository;

    @Autowired
    SalesReturnInvoiceChargesRepository salesReturnInvoiceChargesRepository;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    SalesInvoiceMainRepository salesInvoiceMainRepository;


    @Override
    public Page<SalesReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.salesReturnInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public SalesReturnInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception {
        return salesReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private SalesReturnInvoiceMainEntity convertToEntity(InvoiceMainDto invoiceMainDto) {
        SalesReturnInvoiceMainEntity entity = new SalesReturnInvoiceMainEntity();
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
        salesReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public SalesReturnInvoiceMainEntity findById(Integer id) {
        return salesReturnInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(SalesReturnInvoiceMainEntity entity) {
        salesReturnInvoiceMainRepository.save(entity);
    }

    @Override
    public List<SalesReturnInvoiceDetailEntity> findAllDetail(Integer id) {
        List<SalesReturnInvoiceDetailEntity> detailEntityList = salesReturnInvoiceDetailRepository.findAllDetail(id);
        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceType()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setSalesInvoiceMain(this.salesInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new SalesInvoiceMainEntity()));
                    }
                }
            });
        }
        return detailEntityList;
    }

    @Override
    public SalesReturnInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        return salesReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public void saveAllDetail(List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception {
        List<SalesReturnInvoiceDetailEntity> detailEntityList = new ArrayList<>();
        invoiceDetailDtoList.stream().forEachOrdered(e -> {
            detailEntityList.add(convertDetailToEntity(e));
        });
        salesReturnInvoiceDetailRepository.saveAll(detailEntityList);
    }

    private SalesReturnInvoiceDetailEntity convertDetailToEntity(InvoiceDetailDto invoiceDetailDto) {
        SalesReturnInvoiceDetailEntity entity = new SalesReturnInvoiceDetailEntity();
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
        salesReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public SalesReturnInvoiceDetailEntity findDetailById(Integer id) {
        return salesReturnInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(SalesReturnInvoiceDetailEntity entity) {
        salesReturnInvoiceDetailRepository.save(entity);
    }

    @Override
    public List<SalesReturnInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<SalesReturnInvoiceChargesEntity> pagedList = salesReturnInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public SalesReturnInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        return salesReturnInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        salesReturnInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private SalesReturnInvoiceChargesEntity convertChargesToEntity(InvoiceChargesDto invoiceChargesDto) {
        SalesReturnInvoiceChargesEntity entity = new SalesReturnInvoiceChargesEntity();
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
    public SalesReturnInvoiceChargesEntity findChargesById(Integer id) {
        return salesReturnInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(SalesReturnInvoiceChargesEntity entity) {
        salesReturnInvoiceChargesRepository.save(entity);
    }

    @Override
    public List<SalesReturnInvoiceMainEntity> findMain() {
        return salesReturnInvoiceMainRepository.findMain();
    }

    @Override
    public SalesReturnInvoiceMainEntity createSalesInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception {
        SalesReturnInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        SalesReturnInvoiceMainEntity entity = salesReturnInvoiceMainRepository.save(createEntity);
        List<InvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<SalesReturnInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        salesReturnInvoiceDetailRepository.saveAll(entities);
        List<SalesReturnInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<InvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        salesReturnInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<InvoiceDetailDto> convertFindAllDetailByMainId(List<SalesReturnInvoiceDetailEntity> detailList) {
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
        SalesReturnInvoiceMainEntity mainEntity = this.salesReturnInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.salesReturnInvoiceMainRepository.save(mainEntity);
    }

    @Override
    public List<SalesReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<SalesReturnInvoiceDetailEntity> detailEntityList = this.salesReturnInvoiceDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setSalesInvoiceMain(this.salesInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new SalesInvoiceMainEntity()));
                    }
                }
                e.setSalesReturnInvoiceMain(this.salesReturnInvoiceMainRepository.findById(e.getInvId()).orElse(new SalesReturnInvoiceMainEntity()));
            });
        }
        return detailEntityList;
    }

    @Override
    public void removeAll(SalesReturnInvoiceMainEntity entity, List<SalesReturnInvoiceDetailEntity> detailEntities, List<SalesReturnInvoiceChargesEntity> chargesEntities) throws Exception {
        this.salesReturnInvoiceChargesRepository.saveAll(chargesEntities);
        this.salesReturnInvoiceDetailRepository.saveAll(detailEntities);
        this.salesReturnInvoiceMainRepository.save(entity);
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = java.sql.Timestamp.from(Instant.now());
        this.salesReturnInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.salesReturnInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.salesReturnInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<SalesReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.salesReturnInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<SalesReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.salesReturnInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<SalesReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.salesReturnInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<SalesReturnInvoiceMainEntity> entities) throws Exception {
        this.salesReturnInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<SalesReturnInvoiceMainEntity> entities) throws Exception {
        this.salesReturnInvoiceMainRepository.saveAll(entities);
    }
}
