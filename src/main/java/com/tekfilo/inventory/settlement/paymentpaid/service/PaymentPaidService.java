package com.tekfilo.inventory.settlement.paymentpaid.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.settlement.common.PartyRegisterMainEntity;
import com.tekfilo.inventory.settlement.common.PartyRegisterMainRepository;
import com.tekfilo.inventory.settlement.paymentpaid.PaymentPaidRequestPayload;
import com.tekfilo.inventory.settlement.paymentpaid.dto.PaymentPaidMainDto;
import com.tekfilo.inventory.settlement.paymentpaid.dto.PendingPaidInvoiceDto;
import com.tekfilo.inventory.settlement.paymentpaid.entity.PaymentPaidDetailEntity;
import com.tekfilo.inventory.settlement.paymentpaid.entity.PaymentPaidMainEntity;
import com.tekfilo.inventory.settlement.paymentpaid.repository.PaymentPaidDetailRepository;
import com.tekfilo.inventory.settlement.paymentpaid.repository.PaymentPaidMainRepository;
import com.tekfilo.inventory.settlement.paymentreceived.dto.PendingInvoiceDto;
import com.tekfilo.inventory.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PaymentPaidService implements IPaymentPaidService {

    @Autowired
    PaymentPaidMainRepository paymentPaidMainRepository;

    @Autowired
    PaymentPaidDetailRepository paymentPaidDetailRepository;

    @Autowired
    PartyRegisterMainRepository partyRegisterMainRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Override
    public Page<PaymentPaidMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.paymentPaidMainRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }


    @Override
    public Integer saveAll(PaymentPaidRequestPayload paymentPaidRequestPayload) throws Exception {
        PaymentPaidMainEntity createEntity = convertMain2Entity(paymentPaidRequestPayload.getMain());
        if (!Optional.ofNullable(paymentPaidRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(paymentPaidRequestPayload.getMain().getPaymentType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setPaymentNo(nextNumber);
        }

        PaymentPaidMainEntity entity = paymentPaidMainRepository.save(createEntity);
        paymentPaidDetailRepository.saveAll(convert2DetailEntity(paymentPaidRequestPayload.getDetail(), entity.getId()));
        return entity.getId();
    }


    @Override
    public PaymentPaidMainEntity findById(Integer id) {
        return this.paymentPaidMainRepository.findById(id).get();
    }

    @Override
    public List<PaymentPaidDetailEntity> findDetailByMainId(Integer id) {
        List<PaymentPaidDetailEntity> detailEntities = this.paymentPaidDetailRepository.findAllByMainId(id);
        detailEntities.stream().forEach(e -> {
            final String invoiceTypeId = e.getInvType().concat("-").concat(e.getInvId().toString());
            PartyRegisterMainEntity mainEntities = this.partyRegisterMainRepository.findById(invoiceTypeId).orElse(new PartyRegisterMainEntity());
            final Double pendingToReceive = (mainEntities.getInvoiceAmount() == null ? 0.00 : mainEntities.getInvoiceAmount()) -
                    (mainEntities.getSettlementAmount() == null ? 0.00 : mainEntities.getSettlementAmount());
            mainEntities.setReceiveNow(pendingToReceive);
            e.setInvoice(mainEntities);
        });
        return detailEntities;

    }

    @Override
    public PaymentPaidDetailEntity findDetailById(Integer id) {
        return this.paymentPaidDetailRepository.findById(id).get();
    }

    @Override
    public void deleteDetail(PaymentPaidDetailEntity entity) throws Exception {
        this.paymentPaidDetailRepository.save(entity);
    }

    @Override
    public void deleteMain(Integer id) throws Exception {
        List<PaymentPaidDetailEntity> entities = this.paymentPaidDetailRepository.findAllByMainId(id);
        entities.stream().forEachOrdered(e -> {
            e.setIsDeleted(1);
            e.setModifiedBy(UserContext.getLoggedInUser());
        });
        this.paymentPaidDetailRepository.saveAll(entities);
        PaymentPaidMainEntity entity = this.paymentPaidMainRepository.findById(id).get();
        entity.setIsDeleted(1);
        entity.setModifiedBy(1); // later change into login user
        this.paymentPaidMainRepository.save(entity);
    }

    @Override
    public Integer saveMain(PaymentPaidMainDto paymentPaidMainDto) throws Exception {
        PaymentPaidMainEntity createEntity = convertMain2Entity(paymentPaidMainDto);
        if (!Optional.ofNullable(paymentPaidMainDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(paymentPaidMainDto.getPaymentType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setPaymentNo(nextNumber);
        }

        PaymentPaidMainEntity entity = paymentPaidMainRepository.save(createEntity);

        return entity.getId();
    }

    @Override
    public void saveDetailList(List<PendingInvoiceDto> detailList) throws Exception {
        List<PaymentPaidDetailEntity> entities = new ArrayList<>();
        detailList.stream().forEachOrdered(e -> {
            entities.add(getDetailEntity(e));
        });
        paymentPaidDetailRepository.saveAll(entities);
    }

    @Override
    public List<PaymentPaidDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<PaymentPaidDetailEntity> detailEntities = paymentPaidDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        detailEntities.stream().forEach(e -> {
            final String invoiceTypeId = e.getInvType().concat("-").concat(e.getInvId().toString());
            PartyRegisterMainEntity mainEntities = this.partyRegisterMainRepository.findById(invoiceTypeId).orElse(new PartyRegisterMainEntity());
            final Double pendingToReceive = (mainEntities.getInvoiceAmount() == null ? 0.00 : mainEntities.getInvoiceAmount()) -
                    (mainEntities.getSettlementAmount() == null ? 0.00 : mainEntities.getSettlementAmount());
            mainEntities.setReceiveNow(pendingToReceive);
            e.setInvoice(mainEntities);
            e.setPaymentPaidMain(this.paymentPaidMainRepository.findById(e.getPaymentId()).orElse(new PaymentPaidMainEntity()));
        });
        return detailEntities;
    }


    private PaymentPaidDetailEntity getDetailEntity(PendingInvoiceDto dto) {
        PaymentPaidDetailEntity entity = new PaymentPaidDetailEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setPaymentId(dto.getPaymentId());
        entity.setIsLocked(0);
        entity.setSequence(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    private List<PaymentPaidDetailEntity> convert2DetailEntity(List<PendingPaidInvoiceDto> detail, Integer id) {
        List<PaymentPaidDetailEntity> entities = new ArrayList<>();
        detail.stream().forEachOrdered(e -> {
            PaymentPaidDetailEntity entity = new PaymentPaidDetailEntity();
            BeanUtils.copyProperties(e, entity);
            entity.setPaymentId(id);
            entity.setIsLocked(0);
            entity.setSequence(0);
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(0);
            entities.add(entity);
        });
        return entities;
    }

    private PaymentPaidMainEntity convertMain2Entity(PaymentPaidMainDto main) {
        PaymentPaidMainEntity entity = new PaymentPaidMainEntity();
        BeanUtils.copyProperties(main, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    private List<PendingPaidInvoiceDto> convert2PendingInvoiceDto(List<PurchaseInvoiceMainEntity> pendingInvoiceList) {

        List<PendingPaidInvoiceDto> pendingInvoiceDtoList = new ArrayList<>();
        pendingInvoiceList.stream().forEachOrdered(e -> {
            PendingPaidInvoiceDto dto = new PendingPaidInvoiceDto();
            dto.setInvId(e.getId());
            dto.setInvoiceType(e.getInvoiceType());
            dto.setInvoiceNo(e.getInvoiceNo());
            dto.setInvoiceTypeNo(e.getInvoiceType().concat("-").concat(e.getInvoiceNo()));
            dto.setInvoiceDate(Optional.ofNullable(e.getInvoiceDate()).isPresent() ? e.getInvoiceDate().toString() : null);
            dto.setInvoiceDueDate(e.getInvoiceDueDate() == null ? null : e.getInvoiceDueDate().toString());
            dto.setReferenceNo(e.getReferenceNo());
            dto.setSupplierId(e.getSupplierId());
            dto.setSupplierName(Optional.of(e.getSupplier()).isPresent() ? e.getSupplier().getSupplierName() : null);
            dto.setPaymentTypeName(Optional.ofNullable(e.getPaymentTypeId()).isPresent() ? e.getPaymentTerm().getPaymentTypeName() : null);
            dto.setTotalInvoiceAmount(e.getTotalInvoiceAmount());
            dto.setTotalPaidAmount(e.getTotalPaidAmount());
            pendingInvoiceDtoList.add(dto);
        });
        return pendingInvoiceDtoList;
    }

    @Override
    public List<PaymentPaidDetailEntity> findAllDetailBySupplierId(Integer supplierId) {
        return new ArrayList<>();
    }

    @Override
    public List<PaymentPaidMainEntity> findAllMainByMainIds(List<Integer> ids) {
        return this.paymentPaidMainRepository.findAllMainByMainIds(ids);
    }

    @Override
    public List<PaymentPaidDetailEntity> findAllDetailByMainIds(List<Integer> ids) {
        return this.paymentPaidDetailRepository.findAllDetailByMainIds(ids);
    }

    @Override
    @Transactional
    public void removeAll(List<PaymentPaidMainEntity> entityList, List<PaymentPaidDetailEntity> detailEntities) throws Exception {
        this.paymentPaidDetailRepository.deleteDetailById(detailEntities.stream().map(PaymentPaidDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.paymentPaidMainRepository.deleteMainByIdList(entityList.stream().map(PaymentPaidMainEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    public void lock(List<PaymentPaidMainEntity> entities) throws SQLException {
        this.paymentPaidMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<PaymentPaidMainEntity> entities) throws SQLException {
        this.paymentPaidMainRepository.saveAll(entities);
    }
}
