package com.tekfilo.account.transaction.jv.service;

import com.tekfilo.account.autonumber.AutoNumberGeneratorService;
import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.CompanyContext;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.clone.ClonePayload;
import com.tekfilo.account.transaction.jv.dto.JVBreakupDto;
import com.tekfilo.account.transaction.jv.dto.JVDetailDto;
import com.tekfilo.account.transaction.jv.dto.JVMainDto;
import com.tekfilo.account.transaction.jv.dto.JVRequestPayload;
import com.tekfilo.account.transaction.jv.entity.JVBreakupEntity;
import com.tekfilo.account.transaction.jv.entity.JVDetailEntity;
import com.tekfilo.account.transaction.jv.entity.JVMainEntity;
import com.tekfilo.account.transaction.jv.repository.JVBreakupRepository;
import com.tekfilo.account.transaction.jv.repository.JVDetailRepository;
import com.tekfilo.account.transaction.jv.repository.JVMainRepository;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class JVService {

    @Autowired
    JVMainRepository jvMainRepository;

    @Autowired
    JVDetailRepository jvDetailRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    JVBreakupRepository jvBreakupRepository;


    public Page<JVMainEntity> findAllMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.jvMainRepository.findAll(filterClauseAppender.getFilterClause(filterClauses), pageable);
    }

    public List<JVDetailEntity> findAllDetailByMainId(Integer id) {
        List<JVDetailEntity> detailEntities = this.jvDetailRepository.findAllByMainId(id);
        detailEntities.stream().forEach(e->{
            FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
            List<Integer> ids = new ArrayList<>();
            ids.add(e.getId());
            e.setBreakupList(jvBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("jvDetailId", ids)));
        });
        return detailEntities;
    }


    public JVMainEntity saveMain(JVMainDto jvMainDto) throws Exception {
        JVMainEntity entity  = this.convertToMainEntity(jvMainDto);
        if (!Optional.ofNullable(jvMainDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(jvMainDto.getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            entity.setInvoiceNo(nextNumber);
        }
        return jvMainRepository.save(entity);
    }


    public void saveDetail(List<JVDetailDto> jvDetailDtoList) throws Exception {
        List<JVDetailEntity> entityList = convertToDetailEntity(jvDetailDtoList);
        this.jvDetailRepository.saveAll(entityList);
    }

    public JVDetailEntity saveDetail(JVDetailDto jvDetailDto) throws Exception {
        return jvDetailRepository.save(convert2DetailEntity(jvDetailDto));
    }

    private JVDetailEntity convert2DetailEntity(JVDetailDto jvDetailDto) {
        JVDetailEntity entity = new JVDetailEntity();
        BeanUtils.copyProperties(jvDetailDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    private List<JVDetailEntity> convertToDetailEntity(List<JVDetailDto> jvDetailDtoList) {
        List<JVDetailEntity> entityList = new ArrayList<>();
        jvDetailDtoList.stream().forEach(e -> {
            entityList.add(convert2DetailEntity(e));
        });
        return entityList;
    }

    private JVMainEntity convertToMainEntity(JVMainDto jvMainDto) {
        JVMainEntity entity = new JVMainEntity();
        BeanUtils.copyProperties(jvMainDto, entity);
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void modifyMain(JVMainDto jvMainDto) throws Exception {
        jvMainRepository.save(convertToMainEntity(jvMainDto));
    }


    public JVMainEntity findMainById(Integer id) {
        return jvMainRepository.findById(id).orElse(null);
    }

    public void removeMain(JVMainEntity entity) throws Exception {
        jvMainRepository.save(entity);
    }

    public JVMainEntity saveAll(JVRequestPayload jvRequestPayload) throws Exception {
        JVMainEntity entity  = this.convertToMainEntity(jvRequestPayload.getMain());
        if (!Optional.ofNullable(jvRequestPayload.getMain().getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(jvRequestPayload.getMain().getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            entity.setInvoiceNo(nextNumber);
        }
        JVMainEntity jvMainEntity = this.jvMainRepository.save(entity);
        if(jvRequestPayload.getDetail() != null){
            jvRequestPayload.getDetail().stream().forEach(e -> {
                e.setId(setNullIfInvalidId(e.getId()));
                e.setJvMainId(jvMainEntity.getId());
            });
        }
        this.jvDetailRepository.saveAll(this.convertToDetailEntity(jvRequestPayload.getDetail()));
        return jvMainEntity;
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
    public JVDetailEntity findDetailById(Integer id) {
        return this.jvDetailRepository.findById(id).get();
    }

    public void removeDetail(JVDetailEntity detailEntity) throws Exception {
        this.jvDetailRepository.save(detailEntity);
    }

    public List<JVDetailEntity> findAllDetailByMainIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.jvDetailRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    public List<JVMainEntity> findJVsByIds(List<Integer> ids){
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.jvMainRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    public List<JVBreakupEntity> findJVsBreakupByDetailIds(List<Integer> ids){
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.jvBreakupRepository.findAll(filterClauseAppender.getCustomInClassFilter("jvDetailId", ids));
    }

    public void removeAll(List<JVMainEntity> mainEntities, List<JVDetailEntity> detailEntities, List<JVBreakupEntity> breakupEntities) throws Exception{
        this.jvBreakupRepository.saveAll(breakupEntities);
        this.jvDetailRepository.saveAll(detailEntities);
        this.jvMainRepository.saveAll(mainEntities);
    }

    public void lock(List<JVMainEntity> entities) throws Exception{
        this.jvMainRepository.saveAll(entities);
    }

    public void unlock(List<JVMainEntity> entities) throws Exception{
        this.jvMainRepository.saveAll(entities);
    }

    public JVBreakupEntity saveBreakup(JVBreakupDto jvBreakupDto) {
        return this.jvBreakupRepository.save(convertToBreakupEntity(jvBreakupDto));
    }

    private JVBreakupEntity convertToBreakupEntity(JVBreakupDto jvBreakupDto) {
        JVBreakupEntity entity = new JVBreakupEntity();
        BeanUtils.copyProperties(jvBreakupDto,entity);
        entity.setDebitAmount(jvBreakupDto.getDebitAmount() == null ? 0 : jvBreakupDto.getDebitAmount());
        entity.setCreditAmount(jvBreakupDto.getCreditAmount() == null ? 0 : jvBreakupDto.getCreditAmount());
        entity.setTaxPct(jvBreakupDto.getTaxPct() == null ? 0 : jvBreakupDto.getTaxPct());
        entity.setDebitTaxAmount(jvBreakupDto.getDebitAmount() == null ? 0 : jvBreakupDto.getDebitTaxAmount());
        entity.setCreditTaxAmount(jvBreakupDto.getCreditTaxAmount() == null ? 0 : jvBreakupDto.getCreditTaxAmount());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public JVBreakupEntity findBreakupById(Integer id) {
        return jvBreakupRepository.findById(id).orElse(new JVBreakupEntity());
    }

    public void saveBreaup(JVBreakupEntity jvBreakupEntity) throws Exception{
        this.jvBreakupRepository.save(jvBreakupEntity);
    }

    public void clone(ClonePayload clonePayload) throws Exception {
        if (clonePayload.getIds() != null) {
            if (clonePayload.getIds().size() > 0) {
                clonePayload.getIds().stream().forEach(id -> {
                    JVMainEntity entity = jvMainRepository.findById(id).get();
                    JVMainDto dto = new JVMainDto();
                    BeanUtils.copyProperties(entity, dto);
                    dto.setId(null);
                    dto.setAccountingStatus(AccountConstants.DRAFT);
                    dto.setPaymentStatus(AccountConstants.UNPAID);
                    dto.setReferenceNo("Clone_" + dto.getReferenceNo());
                    JVRequestPayload jvRequestPayload = new JVRequestPayload();
                    jvRequestPayload.setMain(dto);
                    jvRequestPayload.setDetail(new ArrayList<>());
                    try {
                        JVMainEntity createdEntity = this.saveAll(jvRequestPayload);
                        List<JVDetailEntity> detailEntityList = this.findAllDetailByMainId(id);
                        detailEntityList.stream().forEach(e -> {
                            JVDetailDto detailDto = new JVDetailDto();
                            BeanUtils.copyProperties(e, detailDto);
                            detailDto.setId(null);
                            detailDto.setJvMainId(createdEntity.getId());
                            try {
                                JVDetailEntity createdDetailEntity = this.saveDetail(detailDto);
                                e.getBreakupList().stream().forEach(br -> {
                                    JVBreakupDto breakupDto = new JVBreakupDto();
                                    BeanUtils.copyProperties(br, breakupDto);
                                    breakupDto.setId(null);
                                    breakupDto.setJvDetailId(createdDetailEntity.getId());
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

    public void reversal(List<Integer> ids) throws Exception {
        if (ids != null) {
            if (ids.size() > 0) {
                ids.stream().forEach(id -> {
                    JVMainEntity entity = jvMainRepository.findById(id).get();
                    JVMainDto dto = new JVMainDto();
                    BeanUtils.copyProperties(entity, dto);
                    dto.setId(null);
                    dto.setAccountingStatus(AccountConstants.DRAFT);
                    dto.setPaymentStatus(AccountConstants.UNPAID);
                    dto.setReferenceNo("Reversal_" + dto.getReferenceNo());
                    JVRequestPayload jvRequestPayload = new JVRequestPayload();
                    jvRequestPayload.setMain(dto);
                    jvRequestPayload.setDetail(new ArrayList<>());
                    try {
                        JVMainEntity createdEntity = this.saveAll(jvRequestPayload);
                        List<JVDetailEntity> detailEntityList = this.findAllDetailByMainId(id);
                        detailEntityList.stream().forEach(e -> {
                            JVDetailDto detailDto = new JVDetailDto();
                            BeanUtils.copyProperties(e, detailDto);
                            detailDto.setId(null);
                            final BigDecimal debitAmount = detailDto.getDebitAmount();
                            final BigDecimal creditAmount = detailDto.getCreditAmount();
                            final BigDecimal debitTaxAmount = detailDto.getDebitTaxAmount();
                            final BigDecimal creditTaxAmount = detailDto.getCreditTaxAmount();
                            detailDto.setDebitAmount(creditAmount);
                            detailDto.setCreditAmount(debitAmount);
                            detailDto.setDebitTaxAmount(creditTaxAmount);
                            detailDto.setCreditTaxAmount(debitTaxAmount);
                            detailDto.setJvMainId(createdEntity.getId());
                            try {
                                JVDetailEntity createdDetailEntity = this.saveDetail(detailDto);
                                e.getBreakupList().stream().forEach(br -> {
                                    JVBreakupDto breakupDto = new JVBreakupDto();
                                    BeanUtils.copyProperties(br, breakupDto);
                                    final Double debitBreakupAmount = breakupDto.getDebitAmount();
                                    final Double creditBreakupAmount = breakupDto.getCreditAmount();
                                    final Double debitBreakupTaxAmount = breakupDto.getDebitTaxAmount();
                                    final Double creditBreakupTaxAmount = breakupDto.getCreditTaxAmount();
                                    breakupDto.setId(null);
                                    breakupDto.setDebitAmount(debitBreakupAmount);
                                    breakupDto.setCreditAmount(creditBreakupAmount);
                                    breakupDto.setDebitTaxAmount(debitBreakupTaxAmount);
                                    breakupDto.setCreditTaxAmount(creditBreakupTaxAmount);
                                    breakupDto.setJvDetailId(createdDetailEntity.getId());
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
