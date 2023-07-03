package com.tekfilo.account.transaction.bankpaymentreceipt.service;

import com.tekfilo.account.autonumber.AutoNumberGeneratorService;
import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.CompanyContext;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptBreakupDto;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptDetailDto;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptMainDto;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankRequestPayload;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptBreakupEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptDetailEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptMainEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.repository.BankPaymentReceiptBreakupRepository;
import com.tekfilo.account.transaction.bankpaymentreceipt.repository.BankPaymentReceiptDetailRepository;
import com.tekfilo.account.transaction.bankpaymentreceipt.repository.BankPaymentReceiptMainRepository;
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
public class BankPaymentReceiptService {

    @Autowired
    BankPaymentReceiptMainRepository bankPaymentReceiptMainRepository;

    @Autowired
    BankPaymentReceiptDetailRepository bankPaymentReceiptDetailRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    BankPaymentReceiptBreakupRepository bankPaymentReceiptBreakupRepository;

    public Page<BankPaymentReceiptMainEntity> findAllMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.bankPaymentReceiptMainRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }

    public List<BankPaymentReceiptDetailEntity> findAllDetailByMainId(Integer id) {
        List<BankPaymentReceiptDetailEntity> detailEntities = this.bankPaymentReceiptDetailRepository.findAllByMainId(id);
        detailEntities.stream().forEach(e -> {
            FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
            List<Integer> ids = new ArrayList<>();
            ids.add(e.getId());
            e.setBreakupList(bankPaymentReceiptBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("invoiceDetailId", ids)));
        });
        return detailEntities;
    }

    public BankPaymentReceiptMainEntity saveMain(BankPaymentReceiptMainDto bankPaymentReceiptMainDto) throws Exception {
        return bankPaymentReceiptMainRepository.save(convertToMainEntity(bankPaymentReceiptMainDto));
    }

    public void saveDetail(List<BankPaymentReceiptDetailDto> bankPaymentReceiptDetailDtoList) throws Exception {
        List<BankPaymentReceiptDetailEntity> entityList = convertToDetailEntity(bankPaymentReceiptDetailDtoList);
        this.bankPaymentReceiptDetailRepository.saveAll(entityList);
    }

    public BankPaymentReceiptDetailEntity saveDetail(BankPaymentReceiptDetailDto bankPaymentReceiptDetailDtoList) throws Exception {
        return bankPaymentReceiptDetailRepository.save(convertToDetailEntity(bankPaymentReceiptDetailDtoList));
    }

    private BankPaymentReceiptDetailEntity convertToDetailEntity(BankPaymentReceiptDetailDto detailDto) {
        BankPaymentReceiptDetailEntity entity = new BankPaymentReceiptDetailEntity();
        BeanUtils.copyProperties(detailDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    private List<BankPaymentReceiptDetailEntity> convertToDetailEntity(List<BankPaymentReceiptDetailDto> bankPaymentReceiptDetailDtoList) {
        List<BankPaymentReceiptDetailEntity> entityList = new ArrayList<>();
        bankPaymentReceiptDetailDtoList.stream().forEach(e -> {
            entityList.add(convertToDetailEntity(e));
        });
        return entityList;
    }

    private BankPaymentReceiptMainEntity convertToMainEntity(BankPaymentReceiptMainDto bankPaymentReceiptMainDto) {
        BankPaymentReceiptMainEntity entity = new BankPaymentReceiptMainEntity();
        BeanUtils.copyProperties(bankPaymentReceiptMainDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void modifyMain(BankPaymentReceiptMainDto bankPaymentReceiptMainDto) throws Exception {
        bankPaymentReceiptMainRepository.save(convertToMainEntity(bankPaymentReceiptMainDto));
    }


    public BankPaymentReceiptMainEntity findMainById(Integer id) {
        return bankPaymentReceiptMainRepository.findById(id).orElse(null);
    }

    public void removeMain(BankPaymentReceiptMainEntity entity) throws Exception {
        bankPaymentReceiptMainRepository.save(entity);
    }

    public BankPaymentReceiptMainEntity saveAll(BankRequestPayload bankRequestPayload) throws Exception {
        BankPaymentReceiptMainEntity entity = this.convertToMainEntity(bankRequestPayload.getMain());
        if (!Optional.ofNullable(bankRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(bankRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            entity.setInvoiceNo(nextNumber);
        }
        BankPaymentReceiptMainEntity bankPaymentReceiptMainEntity = this.bankPaymentReceiptMainRepository.save(entity);

        bankRequestPayload.getDetail().stream().forEach(e -> {
            e.setId(setNullIfInvalidId(e.getId()));
            e.setInvId(bankPaymentReceiptMainEntity.getId());
        });

        this.bankPaymentReceiptDetailRepository.saveAll(this.convertToDetailEntity(bankRequestPayload.getDetail()));
        return bankPaymentReceiptMainEntity;
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

    public BankPaymentReceiptDetailEntity findDetailById(Integer id) {
        return this.bankPaymentReceiptDetailRepository.findById(id).get();
    }

    public void removeDetail(BankPaymentReceiptDetailEntity detailEntity, List<BankPaymentReceiptBreakupEntity> breakupEntities) throws Exception {
        this.bankPaymentReceiptBreakupRepository.saveAll(breakupEntities);
        this.bankPaymentReceiptDetailRepository.save(detailEntity);
    }

    public List<BankPaymentReceiptDetailEntity> findAllDetailByMainIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.bankPaymentReceiptDetailRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    public List<BankPaymentReceiptMainEntity> findBankPaymentReceiptsByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.bankPaymentReceiptMainRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    public List<BankPaymentReceiptBreakupEntity> findBankPaymentReceiptsBreakupByDetailIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.bankPaymentReceiptBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("invoiceDetailId", ids));
    }

    public void removeAll(List<BankPaymentReceiptMainEntity> mainEntities, List<BankPaymentReceiptDetailEntity> detailEntities, List<BankPaymentReceiptBreakupEntity> breakupEntities) throws Exception {
        this.bankPaymentReceiptBreakupRepository.saveAll(breakupEntities);
        this.bankPaymentReceiptDetailRepository.saveAll(detailEntities);
        this.bankPaymentReceiptMainRepository.saveAll(mainEntities);
    }

    public void lock(List<BankPaymentReceiptMainEntity> entities) throws Exception {
        this.bankPaymentReceiptMainRepository.saveAll(entities);
    }

    public void unlock(List<BankPaymentReceiptMainEntity> entities) throws Exception {
        this.bankPaymentReceiptMainRepository.saveAll(entities);
    }

    public BankPaymentReceiptBreakupEntity saveBreakup(BankPaymentReceiptBreakupDto bankPaymentReceiptBreakupDto) {
        return this.bankPaymentReceiptBreakupRepository.save(convertToBreakupEntity(bankPaymentReceiptBreakupDto));
    }

    private BankPaymentReceiptBreakupEntity convertToBreakupEntity(BankPaymentReceiptBreakupDto bankPaymentReceiptBreakupDto) {
        BankPaymentReceiptBreakupEntity entity = new BankPaymentReceiptBreakupEntity();
        BeanUtils.copyProperties(bankPaymentReceiptBreakupDto, entity);
        entity.setGrossAmount(bankPaymentReceiptBreakupDto.getGrossAmount() == null ? 0 : bankPaymentReceiptBreakupDto.getGrossAmount());
        entity.setTaxPct(bankPaymentReceiptBreakupDto.getTaxPct() == null ? 0 : bankPaymentReceiptBreakupDto.getTaxPct());
        entity.setTaxAmount(bankPaymentReceiptBreakupDto.getTaxAmount() == null ? 0 : bankPaymentReceiptBreakupDto.getTaxAmount());
        entity.setNetAmount(bankPaymentReceiptBreakupDto.getNetAmount() == null ? 0 : bankPaymentReceiptBreakupDto.getNetAmount());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public BankPaymentReceiptBreakupEntity findBreakupById(Integer id) {
        return bankPaymentReceiptBreakupRepository.findById(id).orElse(new BankPaymentReceiptBreakupEntity());
    }

    public void saveBreaup(BankPaymentReceiptBreakupEntity bankPaymentReceiptBreakupEntity) throws Exception {
        this.bankPaymentReceiptBreakupRepository.save(bankPaymentReceiptBreakupEntity);
    }

    public void clone(ClonePayload clonePayload) throws Exception {
        if (clonePayload.getIds() != null) {
            if (clonePayload.getIds().size() > 0) {
                clonePayload.getIds().stream().forEach(id -> {
                    BankPaymentReceiptMainEntity entity = bankPaymentReceiptMainRepository.findById(id).get();
                    BankPaymentReceiptMainDto dto = new BankPaymentReceiptMainDto();
                    BeanUtils.copyProperties(entity, dto);
                    dto.setId(null);
                    dto.setAccountingStatus(AccountConstants.DRAFT);
                    dto.setPaymentStatus(AccountConstants.UNPAID);
                    dto.setReferenceNo("Clone_" + dto.getReferenceNo());
                    BankRequestPayload bankRequestPayload = new BankRequestPayload();
                    bankRequestPayload.setMain(dto);
                    bankRequestPayload.setDetail(new ArrayList<>());
                    try {
                        BankPaymentReceiptMainEntity createdEntity = this.saveAll(bankRequestPayload);
                        List<BankPaymentReceiptDetailEntity> detailEntityList = this.findAllDetailByMainId(id);
                        detailEntityList.stream().forEach(e -> {
                            BankPaymentReceiptDetailDto detailDto = new BankPaymentReceiptDetailDto();
                            BeanUtils.copyProperties(e, detailDto);
                            detailDto.setId(null);
                            detailDto.setInvId(createdEntity.getId());

                            try {
                                BankPaymentReceiptDetailEntity createdDetailEntity = this.saveDetail(detailDto);
                                e.getBreakupList().stream().forEach(br -> {
                                    BankPaymentReceiptBreakupDto breakupDto = new BankPaymentReceiptBreakupDto();
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
