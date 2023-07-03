package com.tekfilo.jewellery.goodsoutward.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardChargesDto;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardDetailDto;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardMainDto;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardRequestPayload;
import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardChargesEntity;
import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardDetailEntity;
import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardMainEntity;
import com.tekfilo.jewellery.goodsoutward.repository.GoodsOutwardChargesRepository;
import com.tekfilo.jewellery.goodsoutward.repository.GoodsOutwardDetailRepository;
import com.tekfilo.jewellery.goodsoutward.repository.GoodsOutwardMainRepository;
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
public class GoodsOutwardService implements IGoodsOutwardService {


    private static final String CUSTOMER = "CUSTOMER";
    private static final String SUPPLIER = "SUPPLIER";
    @Autowired
    GoodsOutwardMainRepository goodsOutwardMainRepository;

    @Autowired
    GoodsOutwardDetailRepository goodsOutwardDetailRepository;

    @Autowired
    GoodsOutwardChargesRepository goodsOutwardChargesRepository;

    @Autowired
    SupplierAddressRepository supplierAddressRepository;


    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;


    @Override
    public Page<GoodsOutwardMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.goodsOutwardMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public GoodsOutwardMainEntity save(GoodsOutwardMainDto goodsOutwardMainDto) throws Exception {
        return goodsOutwardMainRepository.save(convertToEntity(goodsOutwardMainDto));
    }

    private GoodsOutwardMainEntity convertToEntity(GoodsOutwardMainDto goodsOutwardMainDto) {
        GoodsOutwardMainEntity entity = new GoodsOutwardMainEntity();
        BeanUtils.copyProperties(goodsOutwardMainDto, entity);
        entity.setTotalInvoiceAmount(new BigDecimal(0.00));
        entity.setTotalPaidAmount(new BigDecimal(0.00));
        entity.setPaymentStatus(InvoiceConstants.UNPAID);
        entity.setAccountingStatus(InvoiceConstants.UNPOSTED);

        if (Optional.ofNullable(goodsOutwardMainDto.getBillingAddressId()).isPresent()) {
            if (goodsOutwardMainDto.getBillingAddressId() > 0) {
                if (goodsOutwardMainDto.getPartyType().equalsIgnoreCase(CUSTOMER)) {
                    CustomerAddressEntity billingAddress = this.customerAddressRepository.findById(goodsOutwardMainDto.getBillingAddressId()).orElse(new CustomerAddressEntity());
                    if (billingAddress.getAddressId() != null) {
                        entity.setBillingAddressId(goodsOutwardMainDto.getBillingAddressId());
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
                    SupplierAddressEntity billingAddress = this.supplierAddressRepository.findById(goodsOutwardMainDto.getBillingAddressId()).orElse(new SupplierAddressEntity());
                    if (billingAddress.getAddressId() != null) {
                        entity.setBillingAddressId(goodsOutwardMainDto.getBillingAddressId());
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


        if (Optional.ofNullable(goodsOutwardMainDto.getShippingAddressId()).isPresent()) {
            if (goodsOutwardMainDto.getShippingAddressId() > 0) {
                if (goodsOutwardMainDto.getPartyType().equalsIgnoreCase(CUSTOMER)) {
                    CustomerAddressEntity shippingAddress = this.customerAddressRepository.findById(goodsOutwardMainDto.getShippingAddressId()).orElse(new CustomerAddressEntity());
                    if (shippingAddress.getAddressId() != null) {
                        entity.setShippingAddressId(goodsOutwardMainDto.getShippingAddressId());
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
                    SupplierAddressEntity shippingAddress = this.supplierAddressRepository.findById(goodsOutwardMainDto.getShippingAddressId()).orElse(new SupplierAddressEntity());
                    if (shippingAddress.getAddressId() != null) {
                        entity.setShippingAddressId(goodsOutwardMainDto.getShippingAddressId());
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
        entity.setSequence(goodsOutwardMainDto.getSequence() == null ? 0 : goodsOutwardMainDto.getSequence());
        entity.setIsLocked(goodsOutwardMainDto.getIsLocked() == null ? 0 : goodsOutwardMainDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(goodsOutwardMainDto.getIsDeleted() == null ? 0 : goodsOutwardMainDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(GoodsOutwardMainDto goodsOutwardMainDto) throws Exception {
        goodsOutwardMainRepository.save(convertToEntity(goodsOutwardMainDto));
    }

    @Override
    public GoodsOutwardMainEntity findById(Integer id) {
        return goodsOutwardMainRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(GoodsOutwardMainEntity entity) {
        goodsOutwardMainRepository.save(entity);
    }

    @Override
    public List<GoodsOutwardDetailEntity> findAllDetail(Integer id) {
        List<GoodsOutwardDetailEntity> pagedList = goodsOutwardDetailRepository.findAllDetail(id);
        return (pagedList);
    }

    @Override
    public GoodsOutwardDetailEntity saveDetail(GoodsOutwardDetailDto goodsOutwardDetailDto) throws Exception {
        return goodsOutwardDetailRepository.save(convertDetailToEntity(goodsOutwardDetailDto));
    }

    private GoodsOutwardDetailEntity convertDetailToEntity(GoodsOutwardDetailDto goodsOutwardDetailDto) {
        GoodsOutwardDetailEntity entity = new GoodsOutwardDetailEntity();
        BeanUtils.copyProperties(goodsOutwardDetailDto, entity);
        entity.setInvQty(entity.getInvQty() == null ? new BigDecimal(0.00) : entity.getInvQty());
        entity.setInputRate(entity.getInputRate() == null ? new BigDecimal(0.00) : entity.getInputRate());
        entity.setInputAmount(new BigDecimal(entity.getInputRate().doubleValue() * entity.getInvQty().doubleValue()));
        entity.setCostPrice(entity.getCostPrice() == null ? new BigDecimal(0.00) : entity.getCostPrice());
        entity.setDiscountValue(entity.getDiscountValue() == null ? new BigDecimal(0.00) : entity.getDiscountValue());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(goodsOutwardDetailDto.getIsDeleted() == null ? 0 : goodsOutwardDetailDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyDetail(GoodsOutwardDetailDto goodsOutwardDetailDto) throws Exception {
        goodsOutwardDetailRepository.save(convertDetailToEntity(goodsOutwardDetailDto));
    }

    @Override
    public GoodsOutwardDetailEntity findDetailById(Integer id) {
        return goodsOutwardDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void removeDetail(GoodsOutwardDetailEntity entity) {
        goodsOutwardDetailRepository.save(entity);
    }

    @Override
    public List<GoodsOutwardChargesEntity> findAllICharges(Integer invId) {
        List<GoodsOutwardChargesEntity> pagedList = goodsOutwardChargesRepository.findAllCharges(invId);
        return pagedList;
    }

    @Override
    public GoodsOutwardChargesEntity saveCharges(GoodsOutwardChargesDto goodsOutwardChargesDto) throws Exception {
        return goodsOutwardChargesRepository.save(convertChargesToEntity(goodsOutwardChargesDto));
    }

    @Override
    public void modifyCharges(GoodsOutwardChargesDto goodsOutwardChargesDto) throws Exception {
        goodsOutwardChargesRepository.save(convertChargesToEntity(goodsOutwardChargesDto));
    }

    private GoodsOutwardChargesEntity convertChargesToEntity(GoodsOutwardChargesDto goodsOutwardChargesDto) {
        GoodsOutwardChargesEntity entity = new GoodsOutwardChargesEntity();
        BeanUtils.copyProperties(goodsOutwardChargesDto, entity);
        entity.setInputPctAmountValue(entity.getInputPctAmountValue() == null ? new BigDecimal(0.00) : entity.getInputPctAmountValue());
        entity.setInputAmount(entity.getInputAmount() == null ? new BigDecimal(0.00) : entity.getInputAmount());
        entity.setIsPartyPayable(goodsOutwardChargesDto.getIsPartyPayable() == null ? 0 : goodsOutwardChargesDto.getIsPartyPayable());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(goodsOutwardChargesDto.getIsDeleted() == null ? 0 : goodsOutwardChargesDto.getIsDeleted());
        return entity;
    }

    @Override
    public GoodsOutwardChargesEntity findChargesById(Integer id) {
        return goodsOutwardChargesRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCharges(GoodsOutwardChargesEntity entity) {
        goodsOutwardChargesRepository.save(entity);
    }

    @Override
    public List<GoodsOutwardMainEntity> findMain() {
        return goodsOutwardMainRepository.findMain();
    }

    @Override
    public GoodsOutwardMainEntity createInvoice(GoodsOutwardRequestPayload invoiceRequestPayload) throws Exception {
        GoodsOutwardMainEntity createEntity = this.convertToEntity(invoiceRequestPayload.getMain());
        if (!Optional.ofNullable(invoiceRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        GoodsOutwardMainEntity entity = goodsOutwardMainRepository.save(createEntity);
        List<GoodsOutwardDetailDto> goodsOutwardDetailDtoList = setInvoiceMainId2DetailList(invoiceRequestPayload.getDetail(), entity.getId());
        List<GoodsOutwardDetailEntity> entities = new ArrayList<>();

        goodsOutwardDetailDtoList.stream().forEachOrdered(de -> {
            entities.add(convertDetailToEntity(de));
        });
        goodsOutwardDetailRepository.saveAll(entities);
        List<GoodsOutwardChargesEntity> chargesEntities = new ArrayList<>();
        List<GoodsOutwardChargesDto> chargesDtoList = setInvoiceMainId2ChargesList(invoiceRequestPayload.getCharges(), entity.getId());
        chargesDtoList.stream().forEachOrdered(ce -> {
            chargesEntities.add(convertChargesToEntity(ce));
        });
        goodsOutwardChargesRepository.saveAll(chargesEntities);
        return entity;
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<GoodsOutwardDetailDto> convertFindAllDetailByMainId(List<GoodsOutwardDetailEntity> detailList) {
        List<GoodsOutwardDetailDto> goodsOutwardDetailDtoList = new ArrayList<>();
        detailList.stream().forEachOrdered(e -> {
            GoodsOutwardDetailDto goodsOutwardDetailDto = new GoodsOutwardDetailDto();
            goodsOutwardDetailDto.setRowId(e.getId());
            goodsOutwardDetailDto.setId(e.getId());
            goodsOutwardDetailDto.setInvId(e.getInvId());
            goodsOutwardDetailDto.setProductId(e.getProductId());
            goodsOutwardDetailDto.setProductDescription(e.getProductDescription());
            goodsOutwardDetailDto.setInvQty(e.getInvQty());
            goodsOutwardDetailDto.setUom(e.getUom());
            goodsOutwardDetailDto.setInputRate(e.getInputRate());
            goodsOutwardDetailDto.setInputAmount(e.getInputAmount());
            goodsOutwardDetailDto.setDiscountType(e.getDiscountType());
            goodsOutwardDetailDto.setDiscountValue(e.getDiscountValue());
            goodsOutwardDetailDto.setProductNo(e.getProduct() != null ? e.getProduct().getProductNo() : null);
            goodsOutwardDetailDto.setDescription(e.getProductDescription());
            goodsOutwardDetailDto.setPicture1Path(e.getProduct() != null ? e.getProduct().getPicturePath() : null);
            goodsOutwardDetailDtoList.add(goodsOutwardDetailDto);
        });
        return goodsOutwardDetailDtoList;
    }

    private List<GoodsOutwardDetailDto> convert2GoodsOutwardDetailDto(List<ProductEntity> productEntityList) {
        List<GoodsOutwardDetailDto> goodsOutwardDetailDtoList = new ArrayList<>();
        productEntityList.stream().forEachOrdered(e -> {
            GoodsOutwardDetailDto goodsOutwardDetailDto = new GoodsOutwardDetailDto();
            goodsOutwardDetailDto.setProductId(e.getId());
            goodsOutwardDetailDto.setProductDescription(e.getDescription());
            goodsOutwardDetailDto.setUom(e.getUnit());
            goodsOutwardDetailDto.setProductNo(e.getProductNo());
            goodsOutwardDetailDto.setDescription(e.getDescription());
            goodsOutwardDetailDto.setPicture1Path(e.getPicturePath());
            goodsOutwardDetailDtoList.add(goodsOutwardDetailDto);
        });
        return goodsOutwardDetailDtoList;
    }

    private List<GoodsOutwardChargesDto> setInvoiceMainId2ChargesList(List<GoodsOutwardChargesDto> charges, Integer invoiceMainId) {
        charges.stream().forEachOrdered(c -> {
            c.setInvId(invoiceMainId);
        });
        return charges;
    }

    private List<GoodsOutwardDetailDto> setInvoiceMainId2DetailList(List<GoodsOutwardDetailDto> detail, Integer invoiceMainId) {
        detail.stream().forEachOrdered(e -> {
            e.setInvId(invoiceMainId);
        });
        return detail;
    }

    @Override
    public List<GoodsOutwardMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.goodsOutwardMainRepository.findAllMainByIds(mainIds);
    }

    @Override
    public List<GoodsOutwardDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.goodsOutwardDetailRepository.findAllDetailMainByIds(mainIds);
    }

    @Override
    public List<GoodsOutwardChargesEntity> findAllChargesByMainIds(List<Integer> mainIds) {
        return this.goodsOutwardChargesRepository.findAllChargesMainByIds(mainIds);
    }

    @Override
    @Transactional
    public void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception {
        final Integer userId = UserContext.getLoggedInUser();
        final Timestamp timestamp = java.sql.Timestamp.from(Instant.now());
        this.goodsOutwardDetailRepository.deleteDetailById(detailIds, userId, timestamp);
        this.goodsOutwardChargesRepository.deleteChargesById(chargesIds, userId, timestamp);
        this.goodsOutwardMainRepository.deleteMainByIds(mainIds, userId, timestamp);
    }

    @Override
    public void lock(List<GoodsOutwardMainEntity> entities) throws Exception {
        this.goodsOutwardMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<GoodsOutwardMainEntity> entities) throws Exception {
        this.goodsOutwardMainRepository.saveAll(entities);
    }
}
