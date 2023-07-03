package com.tekfilo.jewellery.production.studinvoice.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.master.FactoryAddressEntity;
import com.tekfilo.jewellery.master.repository.FactoryAddressRepository;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.production.common.ProductionInvoiceComponentDto;
import com.tekfilo.jewellery.production.common.ProductionInvoiceDetailDto;
import com.tekfilo.jewellery.production.common.ProductionInvoiceMainDto;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceComponentEntity;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceDetailEntity;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceMainEntity;
import com.tekfilo.jewellery.production.studinvoice.repository.StudInvoiceComponentRepository;
import com.tekfilo.jewellery.production.studinvoice.repository.StudInvoiceDetailRepository;
import com.tekfilo.jewellery.production.studinvoice.repository.StudInvoiceMainRepository;
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
public class StudInvoiceService {

    @Autowired
    StudInvoiceMainRepository studInvoiceMainRepository;

    @Autowired
    StudInvoiceDetailRepository studInvoiceDetailRepository;

    @Autowired
    StudInvoiceComponentRepository studInvoiceComponentRepository;

    @Autowired
    FactoryAddressRepository studAddressRepository;
    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;


    public Page<StudInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.studInvoiceMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public StudInvoiceMainEntity save(ProductionInvoiceMainDto invoiceMainDto) throws Exception {
        return studInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    private StudInvoiceMainEntity convertToEntity(ProductionInvoiceMainDto invoiceMainDto) throws Exception {
        StudInvoiceMainEntity entity = new StudInvoiceMainEntity();
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
                FactoryAddressEntity billingAddress = this.studAddressRepository.findById(invoiceMainDto.getBillingAddressId()).orElse(new FactoryAddressEntity());
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
                FactoryAddressEntity shippingAddress = this.studAddressRepository.findById(invoiceMainDto.getShippingAddressId()).orElse(new FactoryAddressEntity());
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
        studInvoiceMainRepository.save(convertToEntity(invoiceMainDto));
    }

    public StudInvoiceMainEntity findById(Integer id) {
        return studInvoiceMainRepository.findById(id).orElse(null);
    }

    public void remove(StudInvoiceMainEntity entity) {
        studInvoiceMainRepository.save(entity);
    }

    public StudInvoiceDetailEntity saveDetail(ProductionInvoiceDetailDto productionInvoiceDetailDto) throws Exception {
        return studInvoiceDetailRepository.save(convertToDetailEntity(productionInvoiceDetailDto));
    }

    private StudInvoiceDetailEntity convertToDetailEntity(ProductionInvoiceDetailDto productionInvoiceDetailDto) {
        StudInvoiceDetailEntity entity = new StudInvoiceDetailEntity();
        BeanUtils.copyProperties(productionInvoiceDetailDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void saveDetailList(List<ProductionInvoiceDetailDto> productionInvoiceDetailDto) throws Exception {
        List<StudInvoiceDetailEntity> detailEntities = new ArrayList<>();
        productionInvoiceDetailDto.stream().forEach(e -> {
            detailEntities.add(convertToDetailEntity(e));
        });
        studInvoiceDetailRepository.saveAll(detailEntities);
    }

    public List<StudInvoiceDetailEntity> findDetailByMainId(Integer id) {
        return studInvoiceDetailRepository.findAllDetail(id);
    }

    public List<StudInvoiceComponentEntity> findComponentByDetailId(Integer id) {
        return studInvoiceComponentRepository.findComponentsByDetailId(id);
    }

    public StudInvoiceComponentEntity saveComponent(ProductionInvoiceComponentDto productionInvoiceComponentDto) {
        return studInvoiceComponentRepository.save(convertToComponentEntity(productionInvoiceComponentDto));
    }

    private StudInvoiceComponentEntity convertToComponentEntity(ProductionInvoiceComponentDto productionInvoiceComponentDto) {
        StudInvoiceComponentEntity entity = new StudInvoiceComponentEntity();
        BeanUtils.copyProperties(productionInvoiceComponentDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void deleteDetail(Integer id) throws Exception {
        List<StudInvoiceComponentEntity> componentEntityList = studInvoiceComponentRepository.findComponentsByDetailId(id);
        componentEntityList.stream().forEach(e -> {
            e.setModifiedBy(UserContext.getLoggedInUser());
            e.setIsDeleted(1);
        });
        StudInvoiceDetailEntity detailEntity = studInvoiceDetailRepository.findById(id).orElse(null);
        detailEntity.setModifiedBy(UserContext.getLoggedInUser());
        detailEntity.setIsDeleted(1);
        studInvoiceComponentRepository.saveAll(componentEntityList);
        studInvoiceDetailRepository.save(detailEntity);
    }

    public void deleteComponent(Integer id) throws Exception {
        StudInvoiceComponentEntity entity = studInvoiceComponentRepository.findById(id).orElse(null);
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(1);
        studInvoiceComponentRepository.save(entity);
    }

    public  List<StudInvoiceMainEntity> findMainListByIds(List<Integer> ids){
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.studInvoiceMainRepository.findAll(filterClauseAppender.getInClassFilter(ids,"id"));
    }

    public  void lock(List<StudInvoiceMainEntity> entities) throws Exception{
        this.studInvoiceMainRepository.saveAll(entities);
    }

    public void unlock(List<StudInvoiceMainEntity> entities) throws Exception{
        this.studInvoiceMainRepository.saveAll(entities);
    }

    public List<StudInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds) {
        return this.studInvoiceMainRepository.findAllMainByIds(mainIds);
    }

    public List<StudInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds) {
        return this.studInvoiceDetailRepository.findAllDetailMainByIds(mainIds);
    }

    public List<StudInvoiceComponentEntity> findAllComponentByMainIds(List<Integer> mainIds) {
        return this.studInvoiceComponentRepository.findAllComponentMainByIds(mainIds);
    }

    @Transactional
    public void removeAll(List<StudInvoiceMainEntity> mainEntities, List<StudInvoiceDetailEntity> detailEntities, List<StudInvoiceComponentEntity> componentEntities) throws Exception {
        this.studInvoiceComponentRepository.deleteComponentById(componentEntities.stream().map(StudInvoiceComponentEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.studInvoiceDetailRepository.deleteDetailById(detailEntities.stream().map(StudInvoiceDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.studInvoiceMainRepository.deleteMainById(mainEntities.stream().map(StudInvoiceMainEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }
}
