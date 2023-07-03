package com.tekfilo.jewellery.production.factoryinvoice.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.master.FactoryAddressEntity;
import com.tekfilo.jewellery.master.repository.FactoryAddressRepository;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.production.common.ProductionInvoiceComponentDto;
import com.tekfilo.jewellery.production.common.ProductionInvoiceDetailDto;
import com.tekfilo.jewellery.production.common.ProductionInvoiceMainDto;
import com.tekfilo.jewellery.production.factoryinvoice.entity.FactoryInvoiceComponentEntity;
import com.tekfilo.jewellery.production.factoryinvoice.entity.FactoryInvoiceDetailEntity;
import com.tekfilo.jewellery.production.factoryinvoice.entity.FactoryInvoiceMainEntity;
import com.tekfilo.jewellery.production.factoryinvoice.repository.FactoryInvoiceComponentRepository;
import com.tekfilo.jewellery.production.factoryinvoice.repository.FactoryInvoiceDetailRepository;
import com.tekfilo.jewellery.production.factoryinvoice.repository.FactoryInvoiceMainRepository;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class FactoryInvoiceService {

    @Autowired
    FactoryInvoiceMainRepository factoryInvoiceMainRepository;

    @Autowired
    FactoryInvoiceDetailRepository factoryInvoiceDetailRepository;

    @Autowired
    FactoryInvoiceComponentRepository factoryInvoiceComponentRepository;

    @Autowired
    FactoryAddressRepository factoryAddressRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;


    public Page<FactoryInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.factoryInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public FactoryInvoiceMainEntity save(ProductionInvoiceMainDto invoiceMainDto) throws Exception {
        return factoryInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private FactoryInvoiceMainEntity convertToEntity(ProductionInvoiceMainDto invoiceMainDto) throws Exception {
        FactoryInvoiceMainEntity entity = new FactoryInvoiceMainEntity();
        BeanUtils.copyProperties(invoiceMainDto, entity);

        if (!Optional.ofNullable(invoiceMainDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(invoiceMainDto.getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            entity.setInvoiceNo(nextNumber);
        }

        entity.setTotalInvoiceAmount(new BigDecimal(0.00));
        entity.setTotalPaidAmount(new BigDecimal(0.00));
        entity.setPaymentStatus(InvoiceConstants.UNPAID);
        entity.setAccountingStatus(InvoiceConstants.UNPOSTED);

        if (Optional.ofNullable(invoiceMainDto.getBillingAddressId()).isPresent()) {
            if (invoiceMainDto.getBillingAddressId() > 0) {
                FactoryAddressEntity billingAddress = this.factoryAddressRepository.findById(invoiceMainDto.getBillingAddressId()).orElse(new FactoryAddressEntity());
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
                FactoryAddressEntity shippingAddress = this.factoryAddressRepository.findById(invoiceMainDto.getShippingAddressId()).orElse(new FactoryAddressEntity());
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

        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void modify(ProductionInvoiceMainDto invoiceMainDto) throws Exception {
        factoryInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    public FactoryInvoiceMainEntity findById(Integer id) {
        return factoryInvoiceMainRepository.findById(id).orElse(null);
    }

    public void remove(FactoryInvoiceMainEntity entity) {
        factoryInvoiceMainRepository.save(entity);
    }

    public FactoryInvoiceDetailEntity saveDetail(ProductionInvoiceDetailDto productionInvoiceDetailDto) throws Exception {
        return factoryInvoiceDetailRepository.save(convertToDetailEntity(productionInvoiceDetailDto));
    }

    private FactoryInvoiceDetailEntity convertToDetailEntity(ProductionInvoiceDetailDto productionInvoiceDetailDto) {
        FactoryInvoiceDetailEntity entity = new FactoryInvoiceDetailEntity();
        BeanUtils.copyProperties(productionInvoiceDetailDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void saveDetailList(List<ProductionInvoiceDetailDto> productionInvoiceDetailDto) throws Exception {
        List<FactoryInvoiceDetailEntity> detailEntities = new ArrayList<>();
        productionInvoiceDetailDto.stream().forEach(e -> {
            detailEntities.add(convertToDetailEntity(e));
        });
        factoryInvoiceDetailRepository.saveAll(detailEntities);
    }

    public List<FactoryInvoiceDetailEntity> findDetailByMainId(Integer id) {
        return factoryInvoiceDetailRepository.findAllDetail(id);
    }

    public List<FactoryInvoiceComponentEntity> findComponentByDetailId(Integer id) {
        return factoryInvoiceComponentRepository.findComponentsByDetailId(id);
    }

    public FactoryInvoiceComponentEntity saveComponent(ProductionInvoiceComponentDto productionInvoiceComponentDto) {
        return factoryInvoiceComponentRepository.save(convertToComponentEntity(productionInvoiceComponentDto));
    }

    private FactoryInvoiceComponentEntity convertToComponentEntity(ProductionInvoiceComponentDto productionInvoiceComponentDto) {
        FactoryInvoiceComponentEntity entity = new FactoryInvoiceComponentEntity();
        BeanUtils.copyProperties(productionInvoiceComponentDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void deleteDetail(Integer id) throws Exception {
        List<FactoryInvoiceComponentEntity> componentEntityList = factoryInvoiceComponentRepository.findComponentsByDetailId(id);
        componentEntityList.stream().forEach(e -> {
            e.setModifiedBy(UserContext.getLoggedInUser());
            e.setIsDeleted(1);
        });
        FactoryInvoiceDetailEntity detailEntity = factoryInvoiceDetailRepository.findById(id).orElse(null);
        detailEntity.setModifiedBy(UserContext.getLoggedInUser());
        detailEntity.setIsDeleted(1);
        factoryInvoiceComponentRepository.saveAll(componentEntityList);
        factoryInvoiceDetailRepository.save(detailEntity);
    }

    public void deleteComponent(Integer id) throws Exception {
        FactoryInvoiceComponentEntity entity = factoryInvoiceComponentRepository.findById(id).orElse(null);
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(1);
        factoryInvoiceComponentRepository.save(entity);
    }

    public List<FactoryInvoiceMainEntity> findMainListByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.factoryInvoiceMainRepository.findAll(filterClauseAppender.getInClassFilter(ids, "id"));
    }

    public void lock(List<FactoryInvoiceMainEntity> entities) throws Exception {
        this.factoryInvoiceMainRepository.saveAll(entities);
    }

    public void unlock(List<FactoryInvoiceMainEntity> entities) throws Exception {
        this.factoryInvoiceMainRepository.saveAll(entities);
    }


    public List<FactoryInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.factoryInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    public List<FactoryInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.factoryInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    public List<FactoryInvoiceComponentEntity> findAllComponentByMainIds(List<Integer> mainIds) {
        return this.factoryInvoiceComponentRepository.findAllComponentMainByIds(mainIds);
    }

    @Transactional
    public void removeAll(List<FactoryInvoiceMainEntity> mainEntities, List<FactoryInvoiceDetailEntity> detailEntities, List<FactoryInvoiceComponentEntity> componentEntities) throws Exception {
        this.factoryInvoiceComponentRepository.deleteComponentById(componentEntities.stream().map(FactoryInvoiceComponentEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.factoryInvoiceDetailRepository.deleteDetailById(detailEntities.stream().map(FactoryInvoiceDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.factoryInvoiceMainRepository.deleteMainById(mainEntities.stream().map(FactoryInvoiceMainEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Transactional
    public void deleteMain(Integer id) {
        this.factoryInvoiceComponentRepository.deleteComponentByMainId(id, UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
        this.factoryInvoiceDetailRepository.deleteDetailByMainId(id, UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
        this.factoryInvoiceMainRepository.deleteByMainId(id, UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }
}
