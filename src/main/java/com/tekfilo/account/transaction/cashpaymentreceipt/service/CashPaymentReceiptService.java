package com.tekfilo.account.transaction.cashpaymentreceipt.service;

import com.tekfilo.account.autonumber.AutoNumberGeneratorService;
import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.CompanyContext;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.cashpaymentreceipt.dto.CashPaymentReceiptBreakupDto;
import com.tekfilo.account.transaction.cashpaymentreceipt.dto.CashPaymentReceiptDetailDto;
import com.tekfilo.account.transaction.cashpaymentreceipt.dto.CashPaymentReceiptMainDto;
import com.tekfilo.account.transaction.cashpaymentreceipt.dto.CashRequestPayload;
import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptBreakupEntity;
import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptDetailEntity;
import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptMainEntity;
import com.tekfilo.account.transaction.cashpaymentreceipt.repository.CashPaymentReceiptBreakupRepository;
import com.tekfilo.account.transaction.cashpaymentreceipt.repository.CashPaymentReceiptDetailRepository;
import com.tekfilo.account.transaction.cashpaymentreceipt.repository.CashPaymentReceiptMainRepository;
import com.tekfilo.account.transaction.clone.ClonePayload;
import com.tekfilo.account.util.AccountConstants;
import com.tekfilo.account.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CashPaymentReceiptService {

    @Autowired
    CashPaymentReceiptMainRepository cashPaymentReceiptMainRepository;

    @Autowired
    CashPaymentReceiptDetailRepository cashPaymentReceiptDetailRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    CashPaymentReceiptBreakupRepository cashPaymentReceiptBreakupRepository;

    public Page<CashPaymentReceiptMainEntity> findAllMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.cashPaymentReceiptMainRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }

    public List<CashPaymentReceiptDetailEntity> findAllDetailByMainId(Integer id) {
        List<CashPaymentReceiptDetailEntity> detailEntities = this.cashPaymentReceiptDetailRepository.findAllByMainId(id);
        detailEntities.stream().forEach(e->{
            FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
            List<Integer> ids = new ArrayList<>();
            ids.add(e.getId());
            e.setBreakupList(cashPaymentReceiptBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("invoiceDetailId", ids)));
        });
        return detailEntities;
    }


    public CashPaymentReceiptMainEntity saveMain(CashPaymentReceiptMainDto cashPaymentReceiptMainDto) throws Exception {
        return cashPaymentReceiptMainRepository.save(convertToMainEntity(cashPaymentReceiptMainDto));
    }

    public void saveDetail(List<CashPaymentReceiptDetailDto> cashPaymentReceiptDetailDtoList) throws Exception {
        List<CashPaymentReceiptDetailEntity> entityList = convertToDetailEntity(cashPaymentReceiptDetailDtoList);
        this.cashPaymentReceiptDetailRepository.saveAll(entityList);
    }

    public CashPaymentReceiptDetailEntity saveDetail(CashPaymentReceiptDetailDto cashPaymentReceiptDetailDtoList) throws Exception {
        return cashPaymentReceiptDetailRepository.save(convertToDetailEntity(cashPaymentReceiptDetailDtoList));
    }

    private CashPaymentReceiptDetailEntity convertToDetailEntity(CashPaymentReceiptDetailDto detailDto) {
        CashPaymentReceiptDetailEntity entity = new CashPaymentReceiptDetailEntity();
        BeanUtils.copyProperties(detailDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    private List<CashPaymentReceiptDetailEntity> convertToDetailEntity(List<CashPaymentReceiptDetailDto> cashPaymentReceiptDetailDtoList) {
        List<CashPaymentReceiptDetailEntity> entityList = new ArrayList<>();
        cashPaymentReceiptDetailDtoList.stream().forEach(e -> {
            entityList.add(convertToDetailEntity(e));
        });
        return entityList;
    }

    private CashPaymentReceiptMainEntity convertToMainEntity(CashPaymentReceiptMainDto cashPaymentReceiptMainDto) {
        CashPaymentReceiptMainEntity entity = new CashPaymentReceiptMainEntity();
        BeanUtils.copyProperties(cashPaymentReceiptMainDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void modifyMain(CashPaymentReceiptMainDto cashPaymentReceiptMainDto) throws Exception {
        cashPaymentReceiptMainRepository.save(convertToMainEntity(cashPaymentReceiptMainDto));
    }


    public CashPaymentReceiptMainEntity findMainById(Integer id) {
        return cashPaymentReceiptMainRepository.findById(id).orElse(null);
    }

    public void removeMain(CashPaymentReceiptMainEntity entity) throws Exception {
        cashPaymentReceiptMainRepository.save(entity);
    }

    public CashPaymentReceiptMainEntity saveAll(CashRequestPayload cashRequestPayload) throws Exception {
        CashPaymentReceiptMainEntity entity = this.convertToMainEntity(cashRequestPayload.getMain());
        if (!Optional.ofNullable(cashRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(cashRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            entity.setInvoiceNo(nextNumber);
        }
        CashPaymentReceiptMainEntity cashPaymentReceiptMainEntity = this.cashPaymentReceiptMainRepository.save(entity);

        cashRequestPayload.getDetail().stream().forEach(e -> {
            e.setId(setNullIfInvalidId(e.getId()));
            e.setInvId(cashPaymentReceiptMainEntity.getId());
        });

        this.cashPaymentReceiptDetailRepository.saveAll(this.convertToDetailEntity(cashRequestPayload.getDetail()));
        return cashPaymentReceiptMainEntity;
    }

    private Integer setNullIfInvalidId(Integer id) {
        if (id == null) {
            return null;
        }
        if (id == 0) {
            return null;
        }
        if (id == -1) {
            return null;
        }
        return null;
    }

    public CashPaymentReceiptDetailEntity findDetailById(Integer id) {
        return this.cashPaymentReceiptDetailRepository.findById(id).get();
    }

    public void removeDetail(CashPaymentReceiptDetailEntity detailEntity) throws Exception {
        this.cashPaymentReceiptDetailRepository.save(detailEntity);
    }

    public List<CashPaymentReceiptDetailEntity> findAllDetailByMainIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.cashPaymentReceiptDetailRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    public List<CashPaymentReceiptMainEntity> findCashPaymentReceiptsByIds(List<Integer> ids){
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.cashPaymentReceiptMainRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    public List<CashPaymentReceiptBreakupEntity> findCashPaymentReceiptsBreakupByDetailIds(List<Integer> ids){
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.cashPaymentReceiptBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("invoiceDetailId", ids));
    }

    public void removeAll(List<CashPaymentReceiptMainEntity> mainEntities, List<CashPaymentReceiptDetailEntity> detailEntities, List<CashPaymentReceiptBreakupEntity> breakupEntities) throws Exception{
        this.cashPaymentReceiptBreakupRepository.saveAll(breakupEntities);
        this.cashPaymentReceiptDetailRepository.saveAll(detailEntities);
        this.cashPaymentReceiptMainRepository.saveAll(mainEntities);
    }

    public void lock(List<CashPaymentReceiptMainEntity> entities) throws Exception{
        this.cashPaymentReceiptMainRepository.saveAll(entities);
    }

    public void unlock(List<CashPaymentReceiptMainEntity> entities) throws Exception{
        this.cashPaymentReceiptMainRepository.saveAll(entities);
    }

    public CashPaymentReceiptBreakupEntity saveBreakup(CashPaymentReceiptBreakupDto cashPaymentReceiptBreakupDto) {
        return this.cashPaymentReceiptBreakupRepository.save(convertToBreakupEntity(cashPaymentReceiptBreakupDto));
    }

    private CashPaymentReceiptBreakupEntity convertToBreakupEntity(CashPaymentReceiptBreakupDto cashPaymentReceiptBreakupDto) {
        CashPaymentReceiptBreakupEntity entity = new CashPaymentReceiptBreakupEntity();
        BeanUtils.copyProperties(cashPaymentReceiptBreakupDto,entity);
        entity.setGrossAmount(cashPaymentReceiptBreakupDto.getGrossAmount() == null ? 0 : cashPaymentReceiptBreakupDto.getGrossAmount());
        entity.setTaxPct(cashPaymentReceiptBreakupDto.getTaxPct() == null ? 0 : cashPaymentReceiptBreakupDto.getTaxPct());
        entity.setTaxAmount(cashPaymentReceiptBreakupDto.getTaxAmount() == null ? 0 : cashPaymentReceiptBreakupDto.getTaxAmount());
        entity.setNetAmount(cashPaymentReceiptBreakupDto.getNetAmount() == null ? 0 : cashPaymentReceiptBreakupDto.getNetAmount());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public CashPaymentReceiptBreakupEntity findBreakupById(Integer id) {
        return cashPaymentReceiptBreakupRepository.findById(id).orElse(new CashPaymentReceiptBreakupEntity());
    }

    public void saveBreaup(CashPaymentReceiptBreakupEntity cashPaymentReceiptBreakupEntity) throws Exception{
        this.cashPaymentReceiptBreakupRepository.save(cashPaymentReceiptBreakupEntity);
    }

    public void clone(ClonePayload clonePayload) throws Exception {
        if (clonePayload.getIds() != null) {
            if (clonePayload.getIds().size() > 0) {
                clonePayload.getIds().stream().forEach(id -> {
                    CashPaymentReceiptMainEntity entity = cashPaymentReceiptMainRepository.findById(id).get();
                    CashPaymentReceiptMainDto dto = new CashPaymentReceiptMainDto();
                    BeanUtils.copyProperties(entity, dto);
                    dto.setId(null);
                    dto.setAccountingStatus(AccountConstants.DRAFT);
                    dto.setPaymentStatus(AccountConstants.UNPAID);
                    dto.setReferenceNo("Clone_" + dto.getReferenceNo());
                    CashRequestPayload cashRequestPayload = new CashRequestPayload();
                    cashRequestPayload.setMain(dto);
                    cashRequestPayload.setDetail(new ArrayList<>());
                    try {
                        CashPaymentReceiptMainEntity createdEntity = this.saveAll(cashRequestPayload);
                        List<CashPaymentReceiptDetailEntity> detailEntityList = this.findAllDetailByMainId(id);
                        detailEntityList.stream().forEach(e -> {
                            CashPaymentReceiptDetailDto detailDto = new CashPaymentReceiptDetailDto();
                            BeanUtils.copyProperties(e, detailDto);
                            detailDto.setId(null);
                            detailDto.setInvId(createdEntity.getId());

                            try {
                                CashPaymentReceiptDetailEntity createdDetailEntity = this.saveDetail(detailDto);
                                e.getBreakupList().stream().forEach(br -> {
                                    CashPaymentReceiptBreakupDto breakupDto = new CashPaymentReceiptBreakupDto();
                                    BeanUtils.copyProperties(br, breakupDto);
                                    breakupDto.setId(null);
                                    breakupDto.setInvoiceDetailId(createdDetailEntity.getId());
                                    this.saveBreakup(breakupDto);
                                });
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}