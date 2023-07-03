package com.tekfilo.inventory.invoice.memosalesreturninvoice.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.common.InvoiceChargesDto;
import com.tekfilo.inventory.invoice.common.InvoiceDetailDto;
import com.tekfilo.inventory.invoice.common.InvoiceMainDto;
import com.tekfilo.inventory.invoice.common.InvoiceRequestPayload;
import com.tekfilo.inventory.invoice.memosalesinvoice.entity.MemoSalesInvoiceMainEntity;
import com.tekfilo.inventory.invoice.memosalesinvoice.repository.MemoSalesInvoiceMainRepository;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceMainEntity;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.repository.MemoSalesReturnInvoiceChargesRepository;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.repository.MemoSalesReturnInvoiceDetailRepository;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.repository.MemoSalesReturnInvoiceMainRepository;
import com.tekfilo.inventory.invoice.salesinvoice.entity.SalesInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.salesinvoice.entity.SalesInvoiceMainEntity;
import com.tekfilo.inventory.invoice.salesinvoice.repository.SalesInvoiceDetailRepository;
import com.tekfilo.inventory.invoice.salesinvoice.repository.SalesInvoiceMainRepository;
import com.tekfilo.inventory.master.CustomerAddressEntity;
import com.tekfilo.inventory.master.repository.CustomerAddressRepository;
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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class MemoSalesReturnInvoiceService implements IMemoSalesReturnInvoiceService {

    @Autowired
    MemoSalesReturnInvoiceMainRepository memoSalesReturnInvoiceMainRepository;

    @Autowired
    MemoSalesReturnInvoiceDetailRepository memoSalesReturnInvoiceDetailRepository;

    @Autowired
    MemoSalesReturnInvoiceChargesRepository memoSalesReturnInvoiceChargesRepository;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SalesInvoiceMainRepository salesInvoiceMainRepository;

    @Autowired
    SalesInvoiceDetailRepository salesInvoiceDetailRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    MemoSalesInvoiceMainRepository memoSalesInvoiceMainRepository;

    @Override
    public Page<MemoSalesReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.memoSalesReturnInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public MemoSalesReturnInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception {
        return memoSalesReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private MemoSalesReturnInvoiceMainEntity convertToEntity(InvoiceMainDto invoiceMainDto) {
        MemoSalesReturnInvoiceMainEntity entity = new MemoSalesReturnInvoiceMainEntity();
        BeanUtils.copyProperties(invoiceMainDto, entity);
        entity.setTotalInvoiceAmount(new BigDecimal(0.00));
        entity.setTotalReceivedAmount(new BigDecimal(0.00));
        entity.setPaymentStatus(InvoiceConstants.UNPAID);
        entity.setAccountingStatus(InvoiceConstants.UNPOSTED);

        if (Optional.ofNullable(invoiceMainDto.getBillingAddressId()).isPresent()) {
            if (invoiceMainDto.getBillingAddressId() > 0) {
                CustomerAddressEntity billingAddress = this.customerAddressRepository.findById((invoiceMainDto.getBillingAddressId())).orElse(new CustomerAddressEntity());
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
                CustomerAddressEntity shippingAddress = this.customerAddressRepository.findById((invoiceMainDto.getShippingAddressId())).orElse(new CustomerAddressEntity());
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
        memoSalesReturnInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public MemoSalesReturnInvoiceMainEntity findById(Integer id) {
        return memoSalesReturnInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(MemoSalesReturnInvoiceMainEntity entity) {
        memoSalesReturnInvoiceMainRepository.save(entity);
    }

    @Override
    public List<MemoSalesReturnInvoiceDetailEntity> findAllDetail(Integer id) {
        List<MemoSalesReturnInvoiceDetailEntity> detailEntityList = memoSalesReturnInvoiceDetailRepository.findAllDetail(id);
        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setSalesInvoiceMain(this.memoSalesInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new MemoSalesInvoiceMainEntity()));
                    }
                }
            });
        }
        return (detailEntityList);
    }

    @Override
    public MemoSalesReturnInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        return memoSalesReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    private MemoSalesReturnInvoiceDetailEntity convertDetailToEntity(InvoiceDetailDto invoiceDetailDto) {
        MemoSalesReturnInvoiceDetailEntity entity = new MemoSalesReturnInvoiceDetailEntity();
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
        memoSalesReturnInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public MemoSalesReturnInvoiceDetailEntity findDetailById(Integer id) {
        return memoSalesReturnInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(MemoSalesReturnInvoiceDetailEntity entity) {
        memoSalesReturnInvoiceDetailRepository.save(entity);
    }

    @Override
    public List<MemoSalesReturnInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<MemoSalesReturnInvoiceChargesEntity> pagedList = memoSalesReturnInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public MemoSalesReturnInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        return memoSalesReturnInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        memoSalesReturnInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private MemoSalesReturnInvoiceChargesEntity convertChargesToEntity(InvoiceChargesDto invoiceChargesDto) {
        MemoSalesReturnInvoiceChargesEntity entity = new MemoSalesReturnInvoiceChargesEntity();
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
    public MemoSalesReturnInvoiceChargesEntity findChargesById(Integer id) {
        return memoSalesReturnInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(MemoSalesReturnInvoiceChargesEntity entity) {
        memoSalesReturnInvoiceChargesRepository.save(entity);
    }

    @Override
    public List<MemoSalesReturnInvoiceMainEntity> findMain() {
        return memoSalesReturnInvoiceMainRepository.findMain();
    }

    @Override
    public MemoSalesReturnInvoiceMainEntity createSalesInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception {
        MemoSalesReturnInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        MemoSalesReturnInvoiceMainEntity entity = memoSalesReturnInvoiceMainRepository.save(createEntity);
        List<InvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<MemoSalesReturnInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        memoSalesReturnInvoiceDetailRepository.saveAll(entities);
        List<MemoSalesReturnInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<InvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        memoSalesReturnInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }


    private List<InvoiceDetailDto> convertFindAllDetailByMainId(List<MemoSalesReturnInvoiceDetailEntity> detailList) {
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
    public String confirmSales(Integer memoInvoiceId) throws Exception {
        MemoSalesReturnInvoiceMainEntity memoSalesInvoiceMainEntity = memoSalesReturnInvoiceMainRepository.findById(memoInvoiceId).get();
        List<MemoSalesReturnInvoiceDetailEntity> memoSalesInvoiceDetailEntityList = memoSalesReturnInvoiceDetailRepository.findAllDetail(memoInvoiceId);
        SalesInvoiceMainEntity salesInvoiceMainEntity = this.salesInvoiceMainRepository.save(convertMemoSalesMainEntity2SalesEntity(memoSalesInvoiceMainEntity));
        this.salesInvoiceDetailRepository.saveAll(convertMemoSalesDetail2SalesDetail(memoSalesInvoiceDetailEntityList, salesInvoiceMainEntity.getId()));
        this.memoSalesReturnInvoiceDetailRepository.saveAll(updateMemoSalesDetailConfirmData(memoSalesInvoiceDetailEntityList));
        this.memoSalesReturnInvoiceMainRepository.save(updateMemoInvoiceStatus(memoSalesInvoiceMainEntity));
        return salesInvoiceMainEntity.getInvoiceType().concat("-").concat(salesInvoiceMainEntity.getInvoiceNo());
    }

    @Override
    public void returnMemoSales(Integer memoInvoiceId) throws Exception {
        MemoSalesReturnInvoiceMainEntity memoSalesInvoiceMainEntity = memoSalesReturnInvoiceMainRepository.findById(memoInvoiceId).get();
        List<MemoSalesReturnInvoiceDetailEntity> memoSalesInvoiceDetailEntityList = memoSalesReturnInvoiceDetailRepository.findAllDetail(memoInvoiceId);
        this.memoSalesReturnInvoiceDetailRepository.saveAll(updateMemoSalesDetailReturnData(memoSalesInvoiceDetailEntityList));
        this.memoSalesReturnInvoiceMainRepository.save(updateMemoInvoiceStatus2Return(memoSalesInvoiceMainEntity));
    }

    private List<MemoSalesReturnInvoiceDetailEntity> updateMemoSalesDetailReturnData(List<MemoSalesReturnInvoiceDetailEntity> memoSalesInvoiceDetailEntityList) {
        memoSalesInvoiceDetailEntityList.stream().forEachOrdered(memoSalesInvoiceDetailEntity -> {
            memoSalesInvoiceDetailEntity.setRetQty1(memoSalesInvoiceDetailEntity.getInvQty1());
            memoSalesInvoiceDetailEntity.setRetQty2(memoSalesInvoiceDetailEntity.getRetQty2());
            memoSalesInvoiceDetailEntity.setRetInputRate(memoSalesInvoiceDetailEntity.getInputRate());
            memoSalesInvoiceDetailEntity.setRetInputAmount(memoSalesInvoiceDetailEntity.getInputAmount());
            memoSalesInvoiceDetailEntity.setModifiedBy(UserContext.getLoggedInUser());
        });
        return memoSalesInvoiceDetailEntityList;
    }

    private MemoSalesReturnInvoiceMainEntity updateMemoInvoiceStatus2Return(MemoSalesReturnInvoiceMainEntity entity) {
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setInvoiceStatus("RETURNED");
        return entity;
    }

    private SalesInvoiceMainEntity convertMemoSalesMainEntity2SalesEntity(MemoSalesReturnInvoiceMainEntity memoSalesInvoiceMainEntity) {
        SalesInvoiceMainEntity entity = new SalesInvoiceMainEntity();
        BeanUtils.copyProperties(memoSalesInvoiceMainEntity, entity);
        entity.setId(null);
        entity.setReferenceNo(memoSalesInvoiceMainEntity.getInvoiceType().concat("-").concat(memoSalesInvoiceMainEntity.getInvoiceNo()));
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setIsDeleted(0);
        return entity;
    }

    private List<SalesInvoiceDetailEntity> convertMemoSalesDetail2SalesDetail(List<MemoSalesReturnInvoiceDetailEntity> memoSalesInvoiceDetailEntities,
                                                                              Integer salesInvoiceMainId) {
        List<SalesInvoiceDetailEntity> salesInvoiceDetailEntities = new ArrayList<>();
        memoSalesInvoiceDetailEntities.stream().forEachOrdered(memoSalesInvoiceDetailEntity -> {
            SalesInvoiceDetailEntity entity = new SalesInvoiceDetailEntity();
            BeanUtils.copyProperties(memoSalesInvoiceDetailEntity, entity);
            entity.setInvId(salesInvoiceMainId);
            entity.setId(null);
            entity.setProduct(null);
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setModifiedBy(UserContext.getLoggedInUser());
            salesInvoiceDetailEntities.add(entity);
        });
        return salesInvoiceDetailEntities;
    }

    private List<MemoSalesReturnInvoiceDetailEntity> updateMemoSalesDetailConfirmData(List<MemoSalesReturnInvoiceDetailEntity> memoSalesInvoiceDetailEntities) {
        memoSalesInvoiceDetailEntities.stream().forEachOrdered(memoSalesInvoiceDetailEntity -> {
            memoSalesInvoiceDetailEntity.setConfQty1(memoSalesInvoiceDetailEntity.getInvQty1());
            memoSalesInvoiceDetailEntity.setConfQty2(memoSalesInvoiceDetailEntity.getInvQty2());
            memoSalesInvoiceDetailEntity.setConfInputRate(memoSalesInvoiceDetailEntity.getInputRate());
            memoSalesInvoiceDetailEntity.setConfInputAmount(memoSalesInvoiceDetailEntity.getInputAmount());
            memoSalesInvoiceDetailEntity.setModifiedBy(UserContext.getLoggedInUser());
        });
        return memoSalesInvoiceDetailEntities;
    }

    private MemoSalesReturnInvoiceMainEntity updateMemoInvoiceStatus(MemoSalesReturnInvoiceMainEntity entity) {
        entity.setInvoiceStatus("INVOICED");
        entity.setModifiedBy(UserContext.getLoggedInUser());
        return entity;
    }


    @Override
    public String confirmPartialSales(Integer memoInvoiceMainId, List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception {
        MemoSalesReturnInvoiceMainEntity memoSalesInvoiceMainEntity = this.memoSalesReturnInvoiceMainRepository.findById(memoInvoiceMainId).get();
        SalesInvoiceMainEntity salesInvoiceMainEntity = this.salesInvoiceMainRepository.save(this.convertMemoSalesMainEntity2SalesEntity(memoSalesInvoiceMainEntity));
        List<SalesInvoiceDetailEntity> detailEntities = this.convertMemoSalesDetailDto2SalesDetail(salesInvoiceMainEntity.getId(), invoiceDetailDtoList);
        this.salesInvoiceDetailRepository.saveAll(detailEntities);
        return salesInvoiceMainEntity.getInvoiceNo();
    }

    private List<SalesInvoiceDetailEntity> convertMemoSalesDetailDto2SalesDetail(Integer mainId, List<InvoiceDetailDto> invoiceDetailDtoList) {
        List<SalesInvoiceDetailEntity> salesInvoiceDetailEntityList = new ArrayList<>();
        invoiceDetailDtoList.stream().forEachOrdered(e -> {
            SalesInvoiceDetailEntity entity = new SalesInvoiceDetailEntity();
            entity.setInvId(mainId);
            entity.setProductId(e.getProductId());
            entity.setProductDescription(e.getProductDescription());

            entity.setInvQty1(e.getConfQty1());
            entity.setInvQty2(e.getConfQty2());

            entity.setInputRate(e.getConfInputRate());
            entity.setInputAmount(e.getConfInputAmount());
            entity.setUom1(e.getUom1());
            entity.setUom2(e.getUom2());

            entity.setSequence(0);
            entity.setIsLocked(0);
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(0);
            salesInvoiceDetailEntityList.add(entity);
        });
        return salesInvoiceDetailEntityList;
    }

    @Override
    public void partialReturnMemoSales(Integer memoInvoiceId, List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception {

    }

    private List<MemoSalesReturnInvoiceDetailEntity> convertMemoSalesDetail2PartialReturnDetail(List<MemoSalesReturnInvoiceDetailEntity> entities) {

        return entities;
    }

    @Override
    public List<MemoSalesReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<MemoSalesReturnInvoiceDetailEntity> detailEntityList = this.memoSalesReturnInvoiceDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        if (!detailEntityList.isEmpty()) {
            detailEntityList.stream().forEachOrdered(e -> {
                if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                    if (Optional.ofNullable(e.getReferenceInvoiceId()).isPresent()) {
                        e.setSalesInvoiceMain(this.memoSalesInvoiceMainRepository.findById(e.getReferenceInvoiceId()).orElse(new MemoSalesInvoiceMainEntity()));
                    }
                }
                e.setMemoSalesReturnInvoiceMain(this.memoSalesReturnInvoiceMainRepository.findById(e.getInvId()).orElse(new MemoSalesReturnInvoiceMainEntity()));
            });
        }
        return detailEntityList;
    }

    @Override
    public void saveAllDetail(List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception {
        List<MemoSalesReturnInvoiceDetailEntity> detailEntityList = new ArrayList<>();
        invoiceDetailDtoList.stream().forEachOrdered(e -> {
            detailEntityList.add(convertDetailToEntity(e));
        });
        memoSalesReturnInvoiceDetailRepository.saveAll(detailEntityList);
    }

    @Override
    public void changeStatus(Integer id) throws Exception {
        MemoSalesReturnInvoiceMainEntity mainEntity = this.memoSalesReturnInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.memoSalesReturnInvoiceMainRepository.save(mainEntity);
    }

    @Override
    public void removeAll(MemoSalesReturnInvoiceMainEntity entity, List<MemoSalesReturnInvoiceDetailEntity> detailEntities, List<MemoSalesReturnInvoiceChargesEntity> chargesEntities) throws Exception {
        this.memoSalesReturnInvoiceChargesRepository.saveAll(chargesEntities);
        this.memoSalesReturnInvoiceDetailRepository.saveAll(detailEntities);
        this.memoSalesReturnInvoiceMainRepository.save(entity);
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = java.sql.Timestamp.from(Instant.now());
        this.memoSalesReturnInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.memoSalesReturnInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.memoSalesReturnInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<MemoSalesReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.memoSalesReturnInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<MemoSalesReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.memoSalesReturnInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<MemoSalesReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.memoSalesReturnInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<MemoSalesReturnInvoiceMainEntity> entities) throws Exception {
        this.memoSalesReturnInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<MemoSalesReturnInvoiceMainEntity> entities) throws Exception {
        this.memoSalesReturnInvoiceMainRepository.saveAll(entities);
    }
}
