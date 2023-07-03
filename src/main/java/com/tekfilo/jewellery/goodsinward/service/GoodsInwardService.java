package com.tekfilo.jewellery.goodsinward.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardChargesDto;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardDetailDto;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardMainDto;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardRequestPayload;
import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardChargesEntity;
import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardDetailEntity;
import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardMainEntity;
import com.tekfilo.jewellery.goodsinward.repository.GoodsInwardChargesRepository;
import com.tekfilo.jewellery.goodsinward.repository.GoodsInwardDetailRepository;
import com.tekfilo.jewellery.goodsinward.repository.GoodsInwardMainRepository;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
import com.tekfilo.jewellery.master.CustomerAddressEntity;
import com.tekfilo.jewellery.master.SupplierAddressEntity;
import com.tekfilo.jewellery.master.repository.CustomerAddressRepository;
import com.tekfilo.jewellery.master.repository.SupplierAddressRepository;
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
public class GoodsInwardService implements IGoodsInwardService {


    private static final String CUSTOMER = "CUSTOMER";
    private static final String SUPPLIER = "SUPPLIER";
    @Autowired
    GoodsInwardMainRepository goodsInwardMainRepository;

    @Autowired
    GoodsInwardDetailRepository goodsInwardDetailRepository;

    @Autowired
    GoodsInwardChargesRepository goodsInwardChargesRepository;

    @Autowired
    SupplierAddressRepository supplierAddressRepository;


    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;


    @Override
    public Page<GoodsInwardMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.goodsInwardMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public GoodsInwardMainEntity save(GoodsInwardMainDto goodsInwardMainDto) throws Exception {
        return goodsInwardMainRepository.save(convertToEntity(goodsInwardMainDto));
    }

    private GoodsInwardMainEntity convertToEntity(GoodsInwardMainDto goodsInwardMainDto) {
        GoodsInwardMainEntity entity = new GoodsInwardMainEntity();
        BeanUtils.copyProperties(goodsInwardMainDto, entity);
        entity.setTotalInvoiceAmount(new BigDecimal(0.00));
        entity.setTotalPaidAmount(new BigDecimal(0.00));
        entity.setPaymentStatus(InvoiceConstants.UNPAID);
        entity.setAccountingStatus(InvoiceConstants.UNPOSTED);

        if (Optional.ofNullable(goodsInwardMainDto.getBillingAddressId()).isPresent()) {
            if (goodsInwardMainDto.getBillingAddressId() > 0) {
                if (goodsInwardMainDto.getPartyType().equalsIgnoreCase(CUSTOMER)) {
                    CustomerAddressEntity billingAddress = this.customerAddressRepository.findById(goodsInwardMainDto.getBillingAddressId()).orElse(new CustomerAddressEntity());
                    if (billingAddress.getAddressId() != null) {
                        entity.setBillingAddressId(goodsInwardMainDto.getBillingAddressId());
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
                } else {
                    SupplierAddressEntity billingAddress = this.supplierAddressRepository.findById(goodsInwardMainDto.getBillingAddressId()).orElse(new SupplierAddressEntity());
                    if (billingAddress.getAddressId() != null) {
                        entity.setBillingAddressId(goodsInwardMainDto.getBillingAddressId());
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
        }


        if (Optional.ofNullable(goodsInwardMainDto.getShippingAddressId()).isPresent()) {
            if (goodsInwardMainDto.getShippingAddressId() > 0) {
                if (goodsInwardMainDto.getPartyType().equalsIgnoreCase(CUSTOMER)) {
                    CustomerAddressEntity shippingAddress = this.customerAddressRepository.findById(goodsInwardMainDto.getShippingAddressId()).orElse(new CustomerAddressEntity());
                    if (shippingAddress.getAddressId() != null) {
                        entity.setShippingAddressId(goodsInwardMainDto.getShippingAddressId());
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
                } else {
                    SupplierAddressEntity shippingAddress = this.supplierAddressRepository.findById(goodsInwardMainDto.getShippingAddressId()).orElse(new SupplierAddressEntity());
                    if (shippingAddress.getAddressId() != null) {
                        entity.setShippingAddressId(goodsInwardMainDto.getShippingAddressId());
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
        }
        entity.setSequence(goodsInwardMainDto.getSequence() == null ? 0 : goodsInwardMainDto.getSequence());
        entity.setIsLocked(goodsInwardMainDto.getIsLocked() == null ? 0 : goodsInwardMainDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(goodsInwardMainDto.getIsDeleted() == null ? 0 : goodsInwardMainDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(GoodsInwardMainDto goodsInwardMainDto) throws Exception {
        goodsInwardMainRepository.save(convertToEntity(goodsInwardMainDto));
    }

    @Override
    public GoodsInwardMainEntity findById(Integer id) {
        return goodsInwardMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(GoodsInwardMainEntity entity) {
        goodsInwardMainRepository.save(entity);
    }

    @Override
    public List<GoodsInwardDetailEntity> findAllDetail(Integer id) {
        List<GoodsInwardDetailEntity> pagedList = goodsInwardDetailRepository.findAllDetail(id);
        return (pagedList);
    }

    @Override
    public GoodsInwardDetailEntity saveDetail(GoodsInwardDetailDto goodsInwardDetailDto) throws Exception {
        return goodsInwardDetailRepository.save(convertDetailToEntity(goodsInwardDetailDto));
    }

    private GoodsInwardDetailEntity convertDetailToEntity(GoodsInwardDetailDto goodsInwardDetailDto) {
        GoodsInwardDetailEntity entity = new GoodsInwardDetailEntity();
        BeanUtils.copyProperties(goodsInwardDetailDto, entity);
        entity.setInvQty(entity.getInvQty() == null ? new BigDecimal(0.00) : entity.getInvQty());
        entity.setInputRate(entity.getInputRate() == null ? new BigDecimal(0.00) : entity.getInputRate());
        entity.setInputAmount(new BigDecimal(entity.getInputRate().doubleValue() * entity.getInvQty().doubleValue()));
        entity.setCostPrice(entity.getCostPrice() == null ? new BigDecimal(0.00) : entity.getCostPrice());
        entity.setDiscountValue(entity.getDiscountValue() == null ? new BigDecimal(0.00) : entity.getDiscountValue());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(goodsInwardDetailDto.getIsDeleted() == null ? 0 : goodsInwardDetailDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyDetail(GoodsInwardDetailDto goodsInwardDetailDto) throws Exception {
        goodsInwardDetailRepository.save(convertDetailToEntity(goodsInwardDetailDto));
    }

    @Override
    public GoodsInwardDetailEntity findDetailById(Integer id) {
        return goodsInwardDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(GoodsInwardDetailEntity entity) {
        goodsInwardDetailRepository.save(entity);
    }

    @Override
    public List<GoodsInwardChargesEntity> findAllICharges(Integer invId) {
        List<GoodsInwardChargesEntity> pagedList = goodsInwardChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public GoodsInwardChargesEntity saveCharges(GoodsInwardChargesDto goodsInwardChargesDto) throws Exception {
        return goodsInwardChargesRepository.save(convertChargesToEntity(goodsInwardChargesDto));
    }

    @Override
    public void modifyCharges(GoodsInwardChargesDto goodsInwardChargesDto) throws Exception {
        goodsInwardChargesRepository.save(convertChargesToEntity(goodsInwardChargesDto));
    }

    private GoodsInwardChargesEntity convertChargesToEntity(GoodsInwardChargesDto goodsInwardChargesDto) {
        GoodsInwardChargesEntity entity = new GoodsInwardChargesEntity();
        BeanUtils.copyProperties(goodsInwardChargesDto, entity);
        entity.setInputPctAmountValue(entity.getInputPctAmountValue() == null ? new BigDecimal(0.00) : entity.getInputPctAmountValue());
        entity.setInputAmount(entity.getInputAmount() == null ? new BigDecimal(0.00) : entity.getInputAmount());
        entity.setIsPartyPayable(goodsInwardChargesDto.getIsPartyPayable() == null ? 0 : goodsInwardChargesDto.getIsPartyPayable());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(goodsInwardChargesDto.getIsDeleted() == null ? 0 : goodsInwardChargesDto.getIsDeleted());
        return entity;
    }

    @Override
    public GoodsInwardChargesEntity findChargesById(Integer id) {
        return goodsInwardChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(GoodsInwardChargesEntity entity) {
        goodsInwardChargesRepository.save(entity);
    }

    @Override
    public List<GoodsInwardMainEntity> findMain() {
        return goodsInwardMainRepository.findMain();
    }

    @Override
    public GoodsInwardMainEntity createInvoice(GoodsInwardRequestPayload invoiceRequestPayload) throws Exception {
        GoodsInwardMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        GoodsInwardMainEntity entity = goodsInwardMainRepository.save(createEntity);
        List<GoodsInwardDetailDto> goodsInwardDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<GoodsInwardDetailEntity> entities = new ArrayList<>();

        goodsInwardDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        goodsInwardDetailRepository.saveAll(entities);
        List<GoodsInwardChargesEntity> chargesEntities = new ArrayList<>();
        List<GoodsInwardChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        goodsInwardChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<GoodsInwardDetailDto> convertFindAllDetailByMainId(List<GoodsInwardDetailEntity> detailList) {
        List<GoodsInwardDetailDto> goodsInwardDetailDtoList = new ArrayList<>();
        detailList.stream().forEachOrdered(e -> {
            GoodsInwardDetailDto goodsInwardDetailDto = new GoodsInwardDetailDto();
            goodsInwardDetailDto.setRowId(e.getId());
            goodsInwardDetailDto.setId(e.getId());
            goodsInwardDetailDto.setInvId(e.getInvId());
            goodsInwardDetailDto.setProductId(e.getProductId());
            goodsInwardDetailDto.setProductDescription(e.getProductDescription());
            goodsInwardDetailDto.setInvQty(e.getInvQty());
            goodsInwardDetailDto.setUom(e.getUom());
            goodsInwardDetailDto.setInputRate(e.getInputRate());
            goodsInwardDetailDto.setInputAmount(e.getInputAmount());
            goodsInwardDetailDto.setDiscountType(e.getDiscountType());
            goodsInwardDetailDto.setDiscountValue(e.getDiscountValue());
            goodsInwardDetailDto.setProductNo(e.getProduct() != null ? e.getProduct().getProductNo() : null);
            goodsInwardDetailDto.setDescription(e.getProductDescription());
            goodsInwardDetailDto.setPicture1Path(e.getProduct() != null ? e.getProduct().getPicturePath() : null);
            goodsInwardDetailDtoList.add(goodsInwardDetailDto);
        });
        return goodsInwardDetailDtoList;
    }

    private List<GoodsInwardDetailDto> convert2GoodsInwardDetailDto(List<ProductEntity> productEntityList) {
        List<GoodsInwardDetailDto> goodsInwardDetailDtoList = new ArrayList<>();
        productEntityList.stream().forEachOrdered(e -> {
            GoodsInwardDetailDto goodsInwardDetailDto = new GoodsInwardDetailDto();
            goodsInwardDetailDto.setProductId(e.getId());
            goodsInwardDetailDto.setProductDescription(e.getDescription());
            goodsInwardDetailDto.setUom(e.getUnit());
            goodsInwardDetailDto.setProductNo(e.getProductNo());
            goodsInwardDetailDto.setDescription(e.getDescription());
            goodsInwardDetailDto.setPicture1Path(e.getPicturePath());
            goodsInwardDetailDtoList.add(goodsInwardDetailDto);
        });
        return goodsInwardDetailDtoList;
    }

    private List<GoodsInwardChargesDto> setInvoiceMainId2ChargesList(List<GoodsInwardChargesDto> charges, Integer invoiceMainId) {
        charges.stream().forEachOrdered(c -> {
            c.setInvId(invoiceMainId);
        });
        return charges;
    }

    private List<GoodsInwardDetailDto> setInvoiceMainId2DetailList(List<GoodsInwardDetailDto> detail, Integer invoiceMainId) {
        detail.stream().forEachOrdered(e -> {
            e.setInvId(invoiceMainId);
        });
        return detail;
    }

    @Override
    public List<GoodsInwardMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.goodsInwardMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<GoodsInwardDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.goodsInwardDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<GoodsInwardChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.goodsInwardChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = java.sql.Timestamp.from(Instant.now());
        this.goodsInwardDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.goodsInwardChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.goodsInwardMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public void lock(List<GoodsInwardMainEntity> entities) throws Exception {
        this.goodsInwardMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<GoodsInwardMainEntity> entities) throws Exception {
        this.goodsInwardMainRepository.saveAll(entities);
    }
}
