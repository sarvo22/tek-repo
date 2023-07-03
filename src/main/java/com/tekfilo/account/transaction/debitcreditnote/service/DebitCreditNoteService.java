package com.tekfilo.account.transaction.debitcreditnote.service;

import com.tekfilo.account.autonumber.AutoNumberGeneratorService;
import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.CompanyContext;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.clone.ClonePayload;
import com.tekfilo.account.transaction.debitcreditnote.dto.DebitCreditNoteBreakupDto;
import com.tekfilo.account.transaction.debitcreditnote.dto.DebitCreditNoteDetailDto;
import com.tekfilo.account.transaction.debitcreditnote.dto.DebitCreditNoteMainDto;
import com.tekfilo.account.transaction.debitcreditnote.dto.DebitCreditNoteRequestPayload;
import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteBreakupEntity;
import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteDetailEntity;
import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteMainEntity;
import com.tekfilo.account.transaction.debitcreditnote.repository.DebitCreditNoteBreakupRepository;
import com.tekfilo.account.transaction.debitcreditnote.repository.DebitCreditNoteDetailRepository;
import com.tekfilo.account.transaction.debitcreditnote.repository.DebitCreditNoteMainRepository;
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
public class DebitCreditNoteService {
    
    @Autowired
    DebitCreditNoteMainRepository debitCreditNoteMainRepository;
    
    @Autowired
    DebitCreditNoteDetailRepository debitCreditNoteDetailRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    DebitCreditNoteBreakupRepository debitCreditNoteBreakupRepository;

    public Page<DebitCreditNoteMainEntity> findAllMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.debitCreditNoteMainRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }

    public List<DebitCreditNoteDetailEntity> findAllDetailByMainId(Integer id) {
        List<DebitCreditNoteDetailEntity> detailEntities = this.debitCreditNoteDetailRepository.findAllByMainId(id);
        detailEntities.stream().forEach(e->{
            FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
            List<Integer> ids = new ArrayList<>();
            ids.add(e.getId());
            e.setBreakupList(debitCreditNoteBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("invoiceDetailId", ids)));
        });
        return detailEntities;
    }


    public DebitCreditNoteMainEntity saveMain(DebitCreditNoteMainDto debitCreditNoteMainDto) throws Exception {
        return debitCreditNoteMainRepository.save(convertToMainEntity(debitCreditNoteMainDto));
    }
    public void saveDetail(List<DebitCreditNoteDetailDto> debitCreditNoteDetailDtos) throws Exception {
        List<DebitCreditNoteDetailEntity> entityList = convertToDetailEntity(debitCreditNoteDetailDtos);
        this.debitCreditNoteDetailRepository.saveAll(entityList);
    }
    public DebitCreditNoteDetailEntity saveDetail(DebitCreditNoteDetailDto debitCreditNoteDetailDtoList) throws Exception {
        return debitCreditNoteDetailRepository.save(convertToDetailEntity(debitCreditNoteDetailDtoList));
    }
    private DebitCreditNoteDetailEntity convertToDetailEntity(DebitCreditNoteDetailDto detailDto){
        DebitCreditNoteDetailEntity entity = new DebitCreditNoteDetailEntity();
        BeanUtils.copyProperties(detailDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }
    private List<DebitCreditNoteDetailEntity> convertToDetailEntity(List<DebitCreditNoteDetailDto> debitCreditNoteDetailDtoList) {
        List<DebitCreditNoteDetailEntity> entityList = new ArrayList<>();
        debitCreditNoteDetailDtoList.stream().forEach(e->{
            DebitCreditNoteDetailEntity entity = new DebitCreditNoteDetailEntity();
            BeanUtils.copyProperties(e, entity);
            entity.setSequence(0);
            entity.setIsLocked(0);
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(0);
            entityList.add(entity);
        });
        return entityList;
    }

    private DebitCreditNoteMainEntity convertToMainEntity(DebitCreditNoteMainDto debitCreditNoteMainDto) {
        DebitCreditNoteMainEntity entity = new DebitCreditNoteMainEntity();
        BeanUtils.copyProperties(debitCreditNoteMainDto,entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void modifyMain(DebitCreditNoteMainDto debitCreditNoteMainDto) throws Exception {
        debitCreditNoteMainRepository.save(convertToMainEntity(debitCreditNoteMainDto));
    }


    public DebitCreditNoteMainEntity findMainById(Integer id) {
        return debitCreditNoteMainRepository.findById(id).orElse(null);
    }

    public void removeMain(DebitCreditNoteMainEntity entity) throws Exception {
        debitCreditNoteMainRepository.save(entity);
    }

    public DebitCreditNoteMainEntity saveAll(DebitCreditNoteRequestPayload debitCreditNoteRequestPayload) throws  Exception{
        DebitCreditNoteMainEntity entity = this.convertToMainEntity(debitCreditNoteRequestPayload.getMain());
        if (!Optional.ofNullable(debitCreditNoteRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(debitCreditNoteRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            entity.setInvoiceNo(nextNumber);
        }
        DebitCreditNoteMainEntity debitCreditNoteMainEntity = this.debitCreditNoteMainRepository.save(entity);

        debitCreditNoteRequestPayload.getDetail().stream().forEach(e->{
            e.setId(setNullIfInvalidId(e.getId()));
            e.setInvId(debitCreditNoteMainEntity.getId());
        });

        this.debitCreditNoteDetailRepository.saveAll(this.convertToDetailEntity(debitCreditNoteRequestPayload.getDetail()));
        return debitCreditNoteMainEntity;
    }

    private Integer setNullIfInvalidId(Integer id) {
        if(id == null){
            return null;
        }
        if(id == 0){
            return null;
        }
        if(id == -1){
            return null;
        }
        return null;
    }

    public DebitCreditNoteDetailEntity findDetailById(Integer id) {
        return this.debitCreditNoteDetailRepository.findById(id).get();
    }

    public void removeDetail(DebitCreditNoteDetailEntity detailEntity, List<DebitCreditNoteBreakupEntity> breakupEntities) throws  Exception {
        this.debitCreditNoteBreakupRepository.saveAll(breakupEntities);
        this.debitCreditNoteDetailRepository.save(detailEntity);
    }
    public List<DebitCreditNoteDetailEntity> findAllDetailByMainIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.debitCreditNoteDetailRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    public List<DebitCreditNoteMainEntity> findDebitCreditNotesByIds(List<Integer> ids){
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.debitCreditNoteMainRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    public List<DebitCreditNoteBreakupEntity> findDebitCreditNotesBreakupByDetailIds(List<Integer> ids){
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.debitCreditNoteBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("invoiceDetailId", ids));
    }

    public void removeAll(List<DebitCreditNoteMainEntity> mainEntities, List<DebitCreditNoteDetailEntity> detailEntities, List<DebitCreditNoteBreakupEntity> breakupEntities) throws Exception{
        this.debitCreditNoteBreakupRepository.saveAll(breakupEntities);
        this.debitCreditNoteDetailRepository.saveAll(detailEntities);
        this.debitCreditNoteMainRepository.saveAll(mainEntities);
    }

    public void lock(List<DebitCreditNoteMainEntity> entities) throws Exception{
        this.debitCreditNoteMainRepository.saveAll(entities);
    }

    public void unlock(List<DebitCreditNoteMainEntity> entities) throws Exception{
        this.debitCreditNoteMainRepository.saveAll(entities);
    }

    public DebitCreditNoteBreakupEntity saveBreakup(DebitCreditNoteBreakupDto debitCreditNoteBreakupDto) {
        return this.debitCreditNoteBreakupRepository.save(convertToBreakupEntity(debitCreditNoteBreakupDto));
    }

    private DebitCreditNoteBreakupEntity convertToBreakupEntity(DebitCreditNoteBreakupDto debitCreditNoteBreakupDto) {
        DebitCreditNoteBreakupEntity entity = new DebitCreditNoteBreakupEntity();
        BeanUtils.copyProperties(debitCreditNoteBreakupDto,entity);
        entity.setGrossAmount(debitCreditNoteBreakupDto.getGrossAmount() == null ? 0 : debitCreditNoteBreakupDto.getGrossAmount());
        entity.setTaxPct(debitCreditNoteBreakupDto.getTaxPct() == null ? 0 : debitCreditNoteBreakupDto.getTaxPct());
        entity.setTaxAmount(debitCreditNoteBreakupDto.getTaxAmount() == null ? 0 : debitCreditNoteBreakupDto.getTaxAmount());
        entity.setNetAmount(debitCreditNoteBreakupDto.getNetAmount() == null ? 0 : debitCreditNoteBreakupDto.getNetAmount());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public DebitCreditNoteBreakupEntity findBreakupById(Integer id) {
        return debitCreditNoteBreakupRepository.findById(id).orElse(new DebitCreditNoteBreakupEntity());
    }

    public void saveBreaup(DebitCreditNoteBreakupEntity debitCreditNoteBreakupEntity) throws Exception{
        this.debitCreditNoteBreakupRepository.save(debitCreditNoteBreakupEntity);
    }

    public void clone(ClonePayload clonePayload) throws Exception {
        if (clonePayload.getIds() != null) {
            if (clonePayload.getIds().size() > 0) {
                clonePayload.getIds().stream().forEach(id -> {
                    DebitCreditNoteMainEntity entity = debitCreditNoteMainRepository.findById(id).get();
                    DebitCreditNoteMainDto dto = new DebitCreditNoteMainDto();
                    BeanUtils.copyProperties(entity, dto);
                    dto.setId(null);
                    dto.setAccountingStatus(AccountConstants.DRAFT);
                    dto.setPaymentStatus(AccountConstants.UNPAID);
                    dto.setReferenceNo("Clone_" + dto.getReferenceNo());
                    DebitCreditNoteRequestPayload debitCreditNoteRequestPayloadRequestPayload = new DebitCreditNoteRequestPayload();
                    debitCreditNoteRequestPayloadRequestPayload.setMain(dto);
                    debitCreditNoteRequestPayloadRequestPayload.setDetail(new ArrayList<>());
                    try {
                        DebitCreditNoteMainEntity createdEntity = this.saveAll(debitCreditNoteRequestPayloadRequestPayload);
                        List<DebitCreditNoteDetailEntity> detailEntityList = this.findAllDetailByMainId(id);
                        detailEntityList.stream().forEach(e -> {
                            DebitCreditNoteDetailDto detailDto = new DebitCreditNoteDetailDto();
                            BeanUtils.copyProperties(e, detailDto);
                            detailDto.setId(null);
                            detailDto.setInvId(createdEntity.getId());

                            try {
                                DebitCreditNoteDetailEntity createdDetailEntity = this.saveDetail(detailDto);
                                e.getBreakupList().stream().forEach(br -> {
                                    DebitCreditNoteBreakupDto breakupDto = new DebitCreditNoteBreakupDto();
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
