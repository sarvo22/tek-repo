package com.tekfilo.jewellery.jewinvoice.memosalesinvoice.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.entity.MemoSalesInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.entity.MemoSalesInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.entity.MemoSalesInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.repository.MemoSalesInvoiceChargesRepository;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.repository.MemoSalesInvoiceDetailRepository;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.repository.MemoSalesInvoiceMainRepository;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.repository.SalesInvoiceDetailRepository;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.repository.SalesInvoiceMainRepository;
import com.tekfilo.jewellery.master.CustomerAddressEntity;
import com.tekfilo.jewellery.master.repository.CustomerAddressRepository;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import com.tekfilo.jewellery.product.repository.ProductRepository;
import com.tekfilo.jewellery.stock.StockEntity;
import com.tekfilo.jewellery.stock.StockService;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MemoSalesInvoiceService implements IMemoSalesInvoiceService {

    @Autowired
    MemoSalesInvoiceMainRepository memoSalesInvoiceMainRepository;

    @Autowired
    MemoSalesInvoiceDetailRepository memoSalesInvoiceDetailRepository;

    @Autowired
    MemoSalesInvoiceChargesRepository memoSalesInvoiceChargesRepository;

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
    StockService stockService;

    @Override
    public Page<MemoSalesInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.memoSalesInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public MemoSalesInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception {
        return memoSalesInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private MemoSalesInvoiceMainEntity convertToEntity(InvoiceMainDto invoiceMainDto) {
        MemoSalesInvoiceMainEntity entity = new MemoSalesInvoiceMainEntity();
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
        memoSalesInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    @Override
    public MemoSalesInvoiceMainEntity findById(Integer id) {
        return memoSalesInvoiceMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(MemoSalesInvoiceMainEntity entity) {
        memoSalesInvoiceMainRepository.save(entity);
    }

    @Override
    public List<MemoSalesInvoiceDetailEntity> findAllDetail(Integer id) {
        List<MemoSalesInvoiceDetailEntity> pagedList = memoSalesInvoiceDetailRepository.findAllDetail(id);
        return pagedList;
    }

    @Override
    public MemoSalesInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        return memoSalesInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    private MemoSalesInvoiceDetailEntity convertDetailToEntity(InvoiceDetailDto invoiceDetailDto) {
        MemoSalesInvoiceDetailEntity entity = new MemoSalesInvoiceDetailEntity();
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
    public void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception {
        memoSalesInvoiceDetailRepository.save(convertDetailToEntity(invoiceDetailDto));
    }

    @Override
    public MemoSalesInvoiceDetailEntity findDetailById(Integer id) {
        return memoSalesInvoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(MemoSalesInvoiceDetailEntity entity) {
        memoSalesInvoiceDetailRepository.save(entity);
    }

    @Override
    public List<MemoSalesInvoiceChargesEntity> findAllICharges(Integer invId) {
        List<MemoSalesInvoiceChargesEntity> pagedList = memoSalesInvoiceChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public MemoSalesInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        return memoSalesInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    @Override
    public void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception {
        memoSalesInvoiceChargesRepository.save(convertChargesToEntity(invoiceChargesDto));
    }

    private MemoSalesInvoiceChargesEntity convertChargesToEntity(InvoiceChargesDto invoiceChargesDto) {
        MemoSalesInvoiceChargesEntity entity = new MemoSalesInvoiceChargesEntity();
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
    public MemoSalesInvoiceChargesEntity findChargesById(Integer id) {
        return memoSalesInvoiceChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(MemoSalesInvoiceChargesEntity entity) {
        memoSalesInvoiceChargesRepository.save(entity);
    }

    @Override
    public List<MemoSalesInvoiceMainEntity> findMain() {
        return memoSalesInvoiceMainRepository.findMain();
    }

    @Override
    public MemoSalesInvoiceMainEntity createSalesInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception {
        MemoSalesInvoiceMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        MemoSalesInvoiceMainEntity entity = memoSalesInvoiceMainRepository.save(createEntity);
        List<InvoiceDetailDto> invoiceDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<MemoSalesInvoiceDetailEntity> entities = new ArrayList<>();

        invoiceDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        memoSalesInvoiceDetailRepository.saveAll(entities);
        List<MemoSalesInvoiceChargesEntity> chargesEntities = new ArrayList<>();
        List<InvoiceChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        memoSalesInvoiceChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }


    private List<InvoiceDetailDto> convertFindAllDetailByMainId(List<MemoSalesInvoiceDetailEntity> detailList) {
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
    public String confirmSales(Integer memoInvoiceId) throws Exception {
        MemoSalesInvoiceMainEntity memoSalesInvoiceMainEntity = memoSalesInvoiceMainRepository.findById(memoInvoiceId).get();
        List<MemoSalesInvoiceDetailEntity> memoSalesInvoiceDetailEntityList = memoSalesInvoiceDetailRepository.findAllDetail(memoInvoiceId);
        SalesInvoiceMainEntity salesInvoiceMainEntity = this.salesInvoiceMainRepository.save(convertMemoSalesMainEntity2SalesEntity(memoSalesInvoiceMainEntity));
        this.salesInvoiceDetailRepository.saveAll(convertMemoSalesDetail2SalesDetail(memoSalesInvoiceDetailEntityList, salesInvoiceMainEntity.getId()));
        this.memoSalesInvoiceDetailRepository.saveAll(updateMemoSalesDetailConfirmData(memoSalesInvoiceDetailEntityList));
        this.memoSalesInvoiceMainRepository.save(updateMemoInvoiceStatus(memoSalesInvoiceMainEntity));
        return salesInvoiceMainEntity.getInvoiceType().concat("-").concat(salesInvoiceMainEntity.getInvoiceNo());
    }

    @Override
    public void returnMemoSales(Integer memoInvoiceId) throws Exception {
        MemoSalesInvoiceMainEntity memoSalesInvoiceMainEntity = memoSalesInvoiceMainRepository.findById(memoInvoiceId).get();
        List<MemoSalesInvoiceDetailEntity> memoSalesInvoiceDetailEntityList = memoSalesInvoiceDetailRepository.findAllDetail(memoInvoiceId);
        this.memoSalesInvoiceDetailRepository.saveAll(updateMemoSalesDetailReturnData(memoSalesInvoiceDetailEntityList));
        this.memoSalesInvoiceMainRepository.save(updateMemoInvoiceStatus2Return(memoSalesInvoiceMainEntity));
    }

    private List<MemoSalesInvoiceDetailEntity> updateMemoSalesDetailReturnData(List<MemoSalesInvoiceDetailEntity> memoSalesInvoiceDetailEntityList) {
        memoSalesInvoiceDetailEntityList.stream().forEachOrdered(memoSalesInvoiceDetailEntity -> {
            memoSalesInvoiceDetailEntity.setRetQty(memoSalesInvoiceDetailEntity.getInvQty());
            memoSalesInvoiceDetailEntity.setRetInputRate(memoSalesInvoiceDetailEntity.getInputRate());
            memoSalesInvoiceDetailEntity.setRetInputAmount(memoSalesInvoiceDetailEntity.getInputAmount());
            memoSalesInvoiceDetailEntity.setModifiedBy(UserContext.getLoggedInUser());
        });
        return memoSalesInvoiceDetailEntityList;
    }

    private MemoSalesInvoiceMainEntity updateMemoInvoiceStatus2Return(MemoSalesInvoiceMainEntity entity) {
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setInvoiceStatus("RETURNED");
        return entity;
    }

    private SalesInvoiceMainEntity convertMemoSalesMainEntity2SalesEntity(MemoSalesInvoiceMainEntity memoSalesInvoiceMainEntity) {
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

    private List<SalesInvoiceDetailEntity> convertMemoSalesDetail2SalesDetail(List<MemoSalesInvoiceDetailEntity> memoSalesInvoiceDetailEntities,
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

    private List<MemoSalesInvoiceDetailEntity> updateMemoSalesDetailConfirmData(List<MemoSalesInvoiceDetailEntity> memoSalesInvoiceDetailEntities) {
        memoSalesInvoiceDetailEntities.stream().forEachOrdered(memoSalesInvoiceDetailEntity -> {
            memoSalesInvoiceDetailEntity.setConfQty(memoSalesInvoiceDetailEntity.getInvQty());
            memoSalesInvoiceDetailEntity.setConfInputRate(memoSalesInvoiceDetailEntity.getInputRate());
            memoSalesInvoiceDetailEntity.setConfInputAmount(memoSalesInvoiceDetailEntity.getInputAmount());
            memoSalesInvoiceDetailEntity.setModifiedBy(UserContext.getLoggedInUser());
        });
        return memoSalesInvoiceDetailEntities;
    }

    private MemoSalesInvoiceMainEntity updateMemoInvoiceStatus(MemoSalesInvoiceMainEntity entity) {
        entity.setInvoiceStatus("INVOICED");
        entity.setModifiedBy(UserContext.getLoggedInUser());
        return entity;
    }


    @Override
    public String confirmPartialSales(Integer memoInvoiceMainId, List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception {
        MemoSalesInvoiceMainEntity memoSalesInvoiceMainEntity = this.memoSalesInvoiceMainRepository.findById(memoInvoiceMainId).get();
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

            entity.setInvQty(e.getConfQty());

            entity.setInputRate(e.getConfInputRate());
            entity.setInputAmount(e.getConfInputAmount());
            entity.setUom(e.getUom());

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

    private List<MemoSalesInvoiceDetailEntity> convertMemoSalesDetail2PartialReturnDetail(List<MemoSalesInvoiceDetailEntity> entities) {

        return entities;
    }

    @Override
    public List<MemoSalesInvoiceDetailEntity> findDetailByIdWithStock(Integer salesInvoiceId) {
        List<MemoSalesInvoiceDetailEntity> entityList = this.memoSalesInvoiceDetailRepository.findAllDetail(salesInvoiceId);
        entityList.stream().forEach(e -> {
            StockEntity stock = this.stockService.getStock(e.getProductId(), e.getBinId(), CompanyContext.getCurrentCompany());
            e.setStock(stock);
        });
        return entityList;
    }

    @Override
    public List<MemoSalesInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency) {
        List<MemoSalesInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<MemoSalesInvoiceMainEntity> mainEntityList = this.memoSalesInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();
        List<MemoSalesInvoiceDetailEntity> detailEntityList = this.memoSalesInvoiceDetailRepository.findAll(appender.getInClassFilter(
                mainEntityList.stream().map(MemoSalesInvoiceMainEntity::getId).collect(Collectors.toList()), "invId"
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
                if (stock.getBalanceQty().doubleValue() > 0) {
                    e.setStock(stock);
                    pendingInvoiceList.add(e);
                }
            }
        });

        return pendingInvoiceList;
    }

    @Override
    public List<MemoSalesInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId) {
        List<MemoSalesInvoiceDetailEntity> pendingInvoiceList = new ArrayList<>();
        // find all the invoices which are associated with this party
        List<MemoSalesInvoiceMainEntity> mainEntityList = this.memoSalesInvoiceMainRepository.findAllInvoiceByPartyAndCurrency(partyId, currency);
        // find all the details against the mainEntityList
        FilterClauseAppender appender = new FilterClauseAppender();

        List<MemoSalesInvoiceDetailEntity> detailEntityList = this.memoSalesInvoiceDetailRepository.findAll(
                appender.getReturnClassFilter(mainEntityList.stream().map(MemoSalesInvoiceMainEntity::getId).collect(Collectors.toList()),
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
                if (stock.getBalanceQty().doubleValue() > 0) {
                    e.setStock(stock);
                    pendingInvoiceList.add(e);
                }
            }
        });
        return pendingInvoiceList;
    }

    @Override
    public void changeStatus(Integer id) throws Exception {
        MemoSalesInvoiceMainEntity mainEntity = this.memoSalesInvoiceMainRepository.findById(id).get();
        mainEntity.setInvoiceStatus(InvoiceConstants.APPROVED);
        this.memoSalesInvoiceMainRepository.save(mainEntity);
    }

    @Override
    @Transactional
    public void removeAll(MemoSalesInvoiceMainEntity entity, List<MemoSalesInvoiceDetailEntity> detailEntities, List<MemoSalesInvoiceChargesEntity> chargesEntities) throws Exception {
        this.memoSalesInvoiceDetailRepository.deleteDetailById(detailEntities.stream().map(MemoSalesInvoiceDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.memoSalesInvoiceChargesRepository.deleteChargesById(chargesEntities.stream().map(MemoSalesInvoiceChargesEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.memoSalesInvoiceMainRepository.deleteMainById(entity.getId(),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));


    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = java.sql.Timestamp.from(Instant.now());
        this.memoSalesInvoiceDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.memoSalesInvoiceChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.memoSalesInvoiceMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public List<MemoSalesInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.memoSalesInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<MemoSalesInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.memoSalesInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<MemoSalesInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.memoSalesInvoiceChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    public void lock(List<MemoSalesInvoiceMainEntity> entities) throws Exception {
        this.memoSalesInvoiceMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<MemoSalesInvoiceMainEntity> entities) throws Exception {
        this.memoSalesInvoiceMainRepository.saveAll(entities);
    }
}
