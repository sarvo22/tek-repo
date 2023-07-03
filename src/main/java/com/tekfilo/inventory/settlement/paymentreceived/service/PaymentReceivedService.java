package com.tekfilo.inventory.settlement.paymentreceived.service;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.salesinvoice.entity.SalesInvoiceMainEntity;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.settlement.common.PartyRegisterMainEntity;
import com.tekfilo.inventory.settlement.common.PartyRegisterMainRepository;
import com.tekfilo.inventory.settlement.paymentreceived.PaymentReceivedRequestPayload;
import com.tekfilo.inventory.settlement.paymentreceived.dto.PaymentMainDto;
import com.tekfilo.inventory.settlement.paymentreceived.dto.PendingInvoiceDto;
import com.tekfilo.inventory.settlement.paymentreceived.entity.PaymentReceivedDetailEntity;
import com.tekfilo.inventory.settlement.paymentreceived.entity.PaymentReceivedMainEntity;
import com.tekfilo.inventory.settlement.paymentreceived.repository.PaymentReceivedDetailRepository;
import com.tekfilo.inventory.settlement.paymentreceived.repository.PaymentReceivedMainRepository;
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
public class PaymentReceivedService implements IPaymentReceivedService {

    @Autowired
    PaymentReceivedMainRepository paymentReceivedMainRepository;

    @Autowired
    PaymentReceivedDetailRepository paymentReceivedDetailRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    PartyRegisterMainRepository partyRegisterMainRepository;

    @Override
    public Page<PaymentReceivedMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        Page<PaymentReceivedMainEntity> pagedList = this.paymentReceivedMainRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
        return pagedList;
    }

    @Override
    public Integer saveAll(PaymentReceivedRequestPayload paymentReceivedRequestPayload) throws Exception {

        PaymentReceivedMainEntity createEntity = convertMain2Entity(paymentReceivedRequestPayload.getMain());
        if (!Optional.ofNullable(paymentReceivedRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(paymentReceivedRequestPayload.getMain().getPaymentType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setPaymentNo(nextNumber);
        }
        PaymentReceivedMainEntity entity = paymentReceivedMainRepository.save(createEntity);

        paymentReceivedDetailRepository.saveAll(convert2DetailEntity(paymentReceivedRequestPayload.getDetail(), entity.getId()));
        return entity.getId();
    }

    @Override
    public Integer saveMain(PaymentMainDto paymentMainDto) throws Exception {
        PaymentReceivedMainEntity createEntity = convertMain2Entity(paymentMainDto);
        if (!Optional.ofNullable(paymentMainDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(paymentMainDto.getPaymentType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setPaymentNo(nextNumber);
        }
        PaymentReceivedMainEntity entity = paymentReceivedMainRepository.save(createEntity);
        return entity.getId();
    }

    @Override
    public void saveDetailList(List<PendingInvoiceDto> detailList) throws Exception {
        List<PaymentReceivedDetailEntity> entities = new ArrayList<>();
        detailList.stream().forEachOrdered(e -> {
            e.setPaymentId(e.getPaymentId());
            entities.add(getDetailEntity(e));
        });
        paymentReceivedDetailRepository.saveAll(entities);
    }

    @Override
    public PaymentReceivedMainEntity findById(Integer id) {
        return this.paymentReceivedMainRepository.findById(id).get();
    }

    @Override
    public List<PaymentReceivedDetailEntity> findDetailByMainId(Integer id) {
        List<PaymentReceivedDetailEntity> detailEntities = this.paymentReceivedDetailRepository.findAllByMainId(id);
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
    public PaymentReceivedDetailEntity findDetailById(Integer id) {
        return this.paymentReceivedDetailRepository.findById(id).get();
    }

    @Override
    public void deleteDetail(PaymentReceivedDetailEntity entity) throws Exception {
        this.paymentReceivedDetailRepository.save(entity);
    }

    @Override
    public void deleteMain(Integer id) throws Exception {
        List<PaymentReceivedDetailEntity> entities = this.paymentReceivedDetailRepository.findAllByMainId(id);
        entities.stream().forEachOrdered(e -> {
            e.setIsDeleted(1);
            e.setModifiedBy(UserContext.getLoggedInUser());
        });
        this.paymentReceivedDetailRepository.saveAll(entities);
        PaymentReceivedMainEntity entity = this.paymentReceivedMainRepository.findById(id).get();
        entity.setIsDeleted(1);
        entity.setModifiedBy(UserContext.getLoggedInUser());
        this.paymentReceivedMainRepository.save(entity);
    }


    private List<PaymentReceivedDetailEntity> convert2DetailEntity(List<PendingInvoiceDto> detail, Integer id) {
        List<PaymentReceivedDetailEntity> entities = new ArrayList<>();
        detail.stream().forEachOrdered(e -> {
            e.setPaymentId(id);
            entities.add(getDetailEntity(e));
        });
        return entities;
    }

    private PaymentReceivedDetailEntity getDetailEntity(PendingInvoiceDto dto) {
        PaymentReceivedDetailEntity entity = new PaymentReceivedDetailEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setPaymentId(dto.getPaymentId());
        entity.setIsLocked(0);
        entity.setSequence(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    private PaymentReceivedMainEntity convertMain2Entity(PaymentMainDto main) {
        PaymentReceivedMainEntity entity = new PaymentReceivedMainEntity();
        BeanUtils.copyProperties(main, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    private List<PendingInvoiceDto> convert2PendingInvoiceDto(List<SalesInvoiceMainEntity> pendingInvoiceList) {

        List<PendingInvoiceDto> pendingInvoiceDtoList = new ArrayList<>();
        pendingInvoiceList.stream().forEachOrdered(e -> {
            PendingInvoiceDto dto = new PendingInvoiceDto();
            dto.setInvId(e.getId());
            dto.setInvoiceTypeNo(e.getInvoiceType().concat("-").concat(e.getInvoiceNo()));
            dto.setInvoiceDate(Optional.ofNullable(e.getInvoiceDate()).isPresent() ? e.getInvoiceDate().toString() : null);
            dto.setInvoiceDueDate(e.getInvoiceDueDate() == null ? null : e.getInvoiceDueDate().toString());
            dto.setReferenceNo(e.getReferenceNo());
            dto.setCustomerId(e.getCustomerId());
            dto.setCustomerName(Optional.of(e.getCustomer()).isPresent() ? e.getCustomer().getCustomerName() : null);
            dto.setPaymentTypeName(Optional.ofNullable(e.getPaymentTypeId()).isPresent() ? e.getPaymentTerm().getPaymentTypeName() : null);
            dto.setTotalInvoiceAmount(e.getTotalInvoiceAmount());
            dto.setTotalPaidAmount(e.getTotalReceivedAmount());
            pendingInvoiceDtoList.add(dto);
        });
        return pendingInvoiceDtoList;
    }

    @Override
    public List<PaymentReceivedDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType) {
        List<PaymentReceivedDetailEntity> detailEntities = paymentReceivedDetailRepository.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType);
        detailEntities.stream().forEach(e -> {
            final String invoiceTypeId = e.getInvType().concat("-").concat(e.getInvId().toString());
            PartyRegisterMainEntity mainEntities = this.partyRegisterMainRepository.findById(invoiceTypeId).orElse(new PartyRegisterMainEntity());
            final Double pendingToReceive = (mainEntities.getInvoiceAmount() == null ? 0.00 : mainEntities.getInvoiceAmount()) -
                    (mainEntities.getSettlementAmount() == null ? 0.00 : mainEntities.getSettlementAmount());
            mainEntities.setReceiveNow(pendingToReceive);
            e.setInvoice(mainEntities);
            e.setPaymentReceivedMain(this.paymentReceivedMainRepository.findById(e.getPaymentId()).orElse(new PaymentReceivedMainEntity()));
        });
        return detailEntities;
    }


    @Override
    public List<PaymentReceivedMainEntity> findAllMainByMainIds(List<Integer> ids) {
        return this.paymentReceivedMainRepository.findAllMainByMainIds(ids);
    }

    @Override
    public List<PaymentReceivedDetailEntity> findAllDetailByMainIds(List<Integer> ids) {
        return this.paymentReceivedDetailRepository.findAllDetailByMainIds(ids);
    }

    @Override
    @Transactional
    public void removeAll(List<PaymentReceivedMainEntity> entityList, List<PaymentReceivedDetailEntity> detailEntities) throws Exception {
        this.paymentReceivedDetailRepository.deleteDetailById(detailEntities.stream().map(PaymentReceivedDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.paymentReceivedMainRepository.deleteMainByIdList(entityList.stream().map(PaymentReceivedMainEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    @Override
    public void lock(List<PaymentReceivedMainEntity> entities) throws SQLException {
        this.paymentReceivedMainRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<PaymentReceivedMainEntity> entities) throws SQLException {
        this.paymentReceivedMainRepository.saveAll(entities);
    }
}
