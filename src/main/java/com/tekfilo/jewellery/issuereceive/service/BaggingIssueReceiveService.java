package com.tekfilo.jewellery.issuereceive.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.issuereceive.dto.BaggingIssueReceiveDetailDto;
import com.tekfilo.jewellery.issuereceive.dto.BaggingIssueReceiveMainDto;
import com.tekfilo.jewellery.issuereceive.entity.*;
import com.tekfilo.jewellery.issuereceive.repository.*;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BaggingIssueReceiveService {

    @Autowired
    BaggingIssueRespectiveMainRepository baggingIssueRespectiveMainRepository;

    @Autowired
    BaggingIssueRespectiveDetailRepository baggingIssueRespectiveDetailRepository;

    @Autowired
    BaggingIssueIrRespectiveMainRepository baggingIssueIrRespectiveMainRepository;

    @Autowired
    BaggingIssueIrRespectiveDetailRepository baggingIssueIrRespectiveDetailRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    BaggingReturnRespectiveMainRepository baggingReturnRespectiveMainRepository;

    @Autowired
    BaggingReturnRespectiveDetailRepository baggingReturnRespectiveDetailRepository;

    @Autowired
    BaggingReturnIrRespectiveMainRepository baggingReturnIrRespectiveMainRepository;

    @Autowired
    BaggingReturnIrRespectiveDetailRepository baggingReturnIrRespectiveDetailRepository;

    public Page<BaggingIssueRespectiveMainEntity> findAllBaggingIssueRespectiveMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.baggingIssueRespectiveMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public BaggingIssueRespectiveMainEntity findBaggingIssueRespectiveById(Integer id) {
        return this.baggingIssueRespectiveMainRepository.findById(id).orElse(new BaggingIssueRespectiveMainEntity());
    }

    public List<BaggingIssueRespectiveDetailEntity> findBaggingIssueRespectiveDetailByMainId(Integer invId) {
        return baggingIssueRespectiveDetailRepository.findAllByMainId(invId);
    }

    public List<BaggingIssueRespectiveDetailEntity> findBaggingIssueRespectiveDetailByMainIdList(List<Integer> invId) {
        return baggingIssueRespectiveDetailRepository.findAllByDetailByMainIds(invId);
    }

    public BaggingIssueRespectiveMainEntity saveIssueRespectiveMain(BaggingIssueReceiveMainDto issueReceiveMainDto) throws Exception {
        BaggingIssueRespectiveMainEntity createEntity = this.convertToIssueRespectiveMainEntity(issueReceiveMainDto);
        if (!Optional.ofNullable(issueReceiveMainDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(issueReceiveMainDto.getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        BaggingIssueRespectiveMainEntity entity = baggingIssueRespectiveMainRepository.save(createEntity);
        return entity;
    }

    private BaggingIssueRespectiveMainEntity convertToIssueRespectiveMainEntity(BaggingIssueReceiveMainDto issueReceiveMainDto) {
        BaggingIssueRespectiveMainEntity entity = new BaggingIssueRespectiveMainEntity();
        BeanUtils.copyProperties(issueReceiveMainDto, entity);
        entity.setSequence(issueReceiveMainDto.getSequence() == null ? 0 : issueReceiveMainDto.getSequence());
        entity.setIsLocked(issueReceiveMainDto.getIsLocked() == null ? 0 : issueReceiveMainDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(issueReceiveMainDto.getIsDeleted() == null ? 0 : issueReceiveMainDto.getIsDeleted());
        return entity;
    }


    public void removeIssueRespectiveMain(BaggingIssueRespectiveMainEntity entity, List<BaggingIssueRespectiveDetailEntity> detailEntities) throws Exception {
        baggingIssueRespectiveDetailRepository.saveAll(detailEntities);
        baggingIssueRespectiveMainRepository.save(entity);
    }

    public BaggingIssueRespectiveDetailEntity saveIssueRespectiveDetail(BaggingIssueReceiveDetailDto baggingIssueReceiveDetailDto) throws Exception {
        return baggingIssueRespectiveDetailRepository.save(convertToIssueRespectiveDetailEntity(baggingIssueReceiveDetailDto));
    }

    private BaggingIssueRespectiveDetailEntity convertToIssueRespectiveDetailEntity(BaggingIssueReceiveDetailDto issueReceiveDetailDto) {
        BaggingIssueRespectiveDetailEntity entity = new BaggingIssueRespectiveDetailEntity();
        BeanUtils.copyProperties(issueReceiveDetailDto, entity);
        entity.setInvQty(issueReceiveDetailDto.getInvQty() == null ? 0 : issueReceiveDetailDto.getInvQty());
        entity.setInvQty1(issueReceiveDetailDto.getInvQty1() == null ? 0 : issueReceiveDetailDto.getInvQty1());
        entity.setInvQty2(issueReceiveDetailDto.getInvQty2() == null ? 0 : issueReceiveDetailDto.getInvQty2());
        entity.setSequence(issueReceiveDetailDto.getSequence() == null ? 0 : issueReceiveDetailDto.getSequence());
        entity.setIsLocked(issueReceiveDetailDto.getIsLocked() == null ? 0 : issueReceiveDetailDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(issueReceiveDetailDto.getIsDeleted() == null ? 0 : issueReceiveDetailDto.getIsDeleted());
        return entity;
    }

    public BaggingIssueRespectiveDetailEntity findBaggingIssueRespectiveDetailById(Integer id) {
        return baggingIssueRespectiveDetailRepository.findById(id).orElse(new BaggingIssueRespectiveDetailEntity());
    }

    public void removeBaggingIssueRespectiveDetail(BaggingIssueRespectiveDetailEntity entity) throws Exception {
        baggingIssueRespectiveDetailRepository.save(entity);
    }

    public List<BaggingIssueRespectiveMainEntity> findAllBaggingIssueRespectiveMainByIds(List<Integer> ids) {
        return baggingIssueRespectiveMainRepository.findAllMainByMainIds(ids);
    }

    public void removeAllBaggingIssueRespectiveMain(List<BaggingIssueRespectiveMainEntity> baggingIssueRespectiveMainEntities, List<BaggingIssueRespectiveDetailEntity> detailEntities) throws Exception {
        baggingIssueRespectiveDetailRepository.saveAll(detailEntities);
        baggingIssueRespectiveMainRepository.saveAll(baggingIssueRespectiveMainEntities);
    }

    public void lockBaggingIssueRespective(List<BaggingIssueRespectiveMainEntity> entities) {
        baggingIssueRespectiveMainRepository.saveAll(entities);
    }

    public void unlockBaggingIssueRespective(List<BaggingIssueRespectiveMainEntity> entities) {
        baggingIssueRespectiveMainRepository.saveAll(entities);
    }

    public Page<BaggingIssueIrrespectiveMainEntity> findAllBaggingIssueIrRespectiveMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.baggingIssueIrRespectiveMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public BaggingIssueIrrespectiveMainEntity findBaggingIssueIrRespectiveById(Integer id) {
        return this.baggingIssueIrRespectiveMainRepository.findById(id).orElse(new BaggingIssueIrrespectiveMainEntity());
    }

    public List<BaggingIssueIrrespectiveDetailEntity> findBaggingIssueIrRespectiveDetailByMainId(Integer invId) {
        return baggingIssueIrRespectiveDetailRepository.findAllByMainId(invId);
    }

    public List<BaggingIssueIrrespectiveDetailEntity> findBaggingIssueIrRespectiveDetailByMainIdList(List<Integer> invId) {
        return baggingIssueIrRespectiveDetailRepository.findAllByDetailByMainIds(invId);
    }

    public BaggingIssueIrrespectiveMainEntity saveIssueIrRespectiveMain(BaggingIssueReceiveMainDto issueReceiveMainDto) throws Exception {
        BaggingIssueIrrespectiveMainEntity createEntity = this.convertToIssueIrRespectiveMainEntity(issueReceiveMainDto);
        if (!Optional.ofNullable(issueReceiveMainDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(issueReceiveMainDto.getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        BaggingIssueIrrespectiveMainEntity entity = baggingIssueIrRespectiveMainRepository.save(createEntity);
        return entity;
    }

    private BaggingIssueIrrespectiveMainEntity convertToIssueIrRespectiveMainEntity(BaggingIssueReceiveMainDto issueReceiveMainDto) {
        BaggingIssueIrrespectiveMainEntity entity = new BaggingIssueIrrespectiveMainEntity();
        BeanUtils.copyProperties(issueReceiveMainDto, entity);
        entity.setSequence(issueReceiveMainDto.getSequence() == null ? 0 : issueReceiveMainDto.getSequence());
        entity.setIsLocked(issueReceiveMainDto.getIsLocked() == null ? 0 : issueReceiveMainDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(issueReceiveMainDto.getIsDeleted() == null ? 0 : issueReceiveMainDto.getIsDeleted());
        return entity;
    }


    public void removeIssueIrRespectiveMain(BaggingIssueIrrespectiveMainEntity entity, List<BaggingIssueIrrespectiveDetailEntity> detailEntities) throws Exception {
        baggingIssueIrRespectiveDetailRepository.saveAll(detailEntities);
        baggingIssueIrRespectiveMainRepository.save(entity);
    }

    public BaggingIssueIrrespectiveDetailEntity saveIssueIrRespectiveDetail(BaggingIssueReceiveDetailDto baggingIssueReceiveDetailDto) throws Exception {
        return baggingIssueIrRespectiveDetailRepository.save(convertToIssueIrRespectiveDetailEntity(baggingIssueReceiveDetailDto));
    }

    private BaggingIssueIrrespectiveDetailEntity convertToIssueIrRespectiveDetailEntity(BaggingIssueReceiveDetailDto issueReceiveDetailDto) {
        BaggingIssueIrrespectiveDetailEntity entity = new BaggingIssueIrrespectiveDetailEntity();
        BeanUtils.copyProperties(issueReceiveDetailDto, entity);
        entity.setInvQty1(issueReceiveDetailDto.getInvQty1() == null ? 0 : issueReceiveDetailDto.getInvQty1());
        entity.setInvQty2(issueReceiveDetailDto.getInvQty2() == null ? 0 : issueReceiveDetailDto.getInvQty2());
        entity.setSequence(issueReceiveDetailDto.getSequence() == null ? 0 : issueReceiveDetailDto.getSequence());
        entity.setIsLocked(issueReceiveDetailDto.getIsLocked() == null ? 0 : issueReceiveDetailDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(issueReceiveDetailDto.getIsDeleted() == null ? 0 : issueReceiveDetailDto.getIsDeleted());
        return entity;
    }

    public BaggingIssueIrrespectiveDetailEntity findBaggingIssueIrRespectiveDetailById(Integer id) {
        return baggingIssueIrRespectiveDetailRepository.findById(id).orElse(new BaggingIssueIrrespectiveDetailEntity());
    }

    public void removeBaggingIssueIrRespectiveDetail(BaggingIssueIrrespectiveDetailEntity entity) throws Exception {
        baggingIssueIrRespectiveDetailRepository.save(entity);
    }

    public List<BaggingIssueIrrespectiveMainEntity> findAllBaggingIssueIrRespectiveMainByIds(List<Integer> ids) {
        return baggingIssueIrRespectiveMainRepository.findAllMainByMainIds(ids);
    }

    public void removeAllBaggingIssueIrRespectiveMain(List<BaggingIssueIrrespectiveMainEntity> baggingIssueIrRespectiveMainEntities, List<BaggingIssueIrrespectiveDetailEntity> detailEntities) throws Exception {
        baggingIssueIrRespectiveDetailRepository.saveAll(detailEntities);
        baggingIssueIrRespectiveMainRepository.saveAll(baggingIssueIrRespectiveMainEntities);
    }

    public void lockBaggingIssueIrRespective(List<BaggingIssueIrrespectiveMainEntity> entities) {
        baggingIssueIrRespectiveMainRepository.saveAll(entities);
    }

    public void unlockBaggingIssueIrRespective(List<BaggingIssueIrrespectiveMainEntity> entities) {
        baggingIssueIrRespectiveMainRepository.saveAll(entities);
    }


    public Page<BaggingReturnRespectiveMainEntity> findAllBaggingReturnRespectiveMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.baggingReturnRespectiveMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public BaggingReturnRespectiveMainEntity findBaggingReturnRespectiveById(Integer id) {
        return this.baggingReturnRespectiveMainRepository.findById(id).orElse(new BaggingReturnRespectiveMainEntity());
    }

    public List<BaggingReturnRespectiveDetailEntity> findBaggingReturnRespectiveDetailByMainId(Integer invId) {
        return baggingReturnRespectiveDetailRepository.findAllByMainId(invId);
    }

    public List<BaggingReturnRespectiveDetailEntity> findBaggingReturnRespectiveDetailByMainIdList(List<Integer> invId) {
        return baggingReturnRespectiveDetailRepository.findAllByDetailByMainIds(invId);
    }

    public BaggingReturnRespectiveMainEntity saveReturnRespectiveMain(BaggingIssueReceiveMainDto issueReceiveMainDto) throws Exception {
        BaggingReturnRespectiveMainEntity createEntity = this.convertToReturnRespectiveMainEntity(issueReceiveMainDto);
        if (!Optional.ofNullable(issueReceiveMainDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(issueReceiveMainDto.getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        BaggingReturnRespectiveMainEntity entity = baggingReturnRespectiveMainRepository.save(createEntity);
        return entity;
    }

    private BaggingReturnRespectiveMainEntity convertToReturnRespectiveMainEntity(BaggingIssueReceiveMainDto issueReceiveMainDto) {
        BaggingReturnRespectiveMainEntity entity = new BaggingReturnRespectiveMainEntity();
        BeanUtils.copyProperties(issueReceiveMainDto, entity);
        entity.setSequence(issueReceiveMainDto.getSequence() == null ? 0 : issueReceiveMainDto.getSequence());
        entity.setIsLocked(issueReceiveMainDto.getIsLocked() == null ? 0 : issueReceiveMainDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(issueReceiveMainDto.getIsDeleted() == null ? 0 : issueReceiveMainDto.getIsDeleted());
        return entity;
    }


    public void removeReturnRespectiveMain(BaggingReturnRespectiveMainEntity entity, List<BaggingReturnRespectiveDetailEntity> detailEntities) throws Exception {
        baggingReturnRespectiveDetailRepository.saveAll(detailEntities);
        baggingReturnRespectiveMainRepository.save(entity);
    }

    public BaggingReturnRespectiveDetailEntity saveReturnRespectiveDetail(BaggingIssueReceiveDetailDto baggingReturnReceiveDetailDto) throws Exception {
        return baggingReturnRespectiveDetailRepository.save(convertToReturnRespectiveDetailEntity(baggingReturnReceiveDetailDto));
    }

    private BaggingReturnRespectiveDetailEntity convertToReturnRespectiveDetailEntity(BaggingIssueReceiveDetailDto issueReceiveDetailDto) {
        BaggingReturnRespectiveDetailEntity entity = new BaggingReturnRespectiveDetailEntity();
        BeanUtils.copyProperties(issueReceiveDetailDto, entity);
        entity.setInvQty(issueReceiveDetailDto.getInvQty() == null ? 0 : issueReceiveDetailDto.getInvQty());
        entity.setInvQty1(issueReceiveDetailDto.getInvQty1() == null ? 0 : issueReceiveDetailDto.getInvQty1());
        entity.setInvQty2(issueReceiveDetailDto.getInvQty2() == null ? 0 : issueReceiveDetailDto.getInvQty2());
        entity.setSequence(issueReceiveDetailDto.getSequence() == null ? 0 : issueReceiveDetailDto.getSequence());
        entity.setIsLocked(issueReceiveDetailDto.getIsLocked() == null ? 0 : issueReceiveDetailDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(issueReceiveDetailDto.getIsDeleted() == null ? 0 : issueReceiveDetailDto.getIsDeleted());
        return entity;
    }

    public BaggingReturnRespectiveDetailEntity findBaggingReturnRespectiveDetailById(Integer id) {
        return baggingReturnRespectiveDetailRepository.findById(id).orElse(new BaggingReturnRespectiveDetailEntity());
    }

    public void removeBaggingReturnRespectiveDetail(BaggingReturnRespectiveDetailEntity entity) throws Exception {
        baggingReturnRespectiveDetailRepository.save(entity);
    }

    public List<BaggingReturnRespectiveMainEntity> findAllBaggingReturnRespectiveMainByIds(List<Integer> ids) {
        return baggingReturnRespectiveMainRepository.findAllMainByMainIds(ids);
    }

    public void removeAllBaggingReturnRespectiveMain(List<BaggingReturnRespectiveMainEntity> baggingReturnRespectiveMainEntities, List<BaggingReturnRespectiveDetailEntity> detailEntities) throws Exception {
        baggingReturnRespectiveDetailRepository.saveAll(detailEntities);
        baggingReturnRespectiveMainRepository.saveAll(baggingReturnRespectiveMainEntities);
    }

    public void lockBaggingReturnRespective(List<BaggingReturnRespectiveMainEntity> entities) {
        baggingReturnRespectiveMainRepository.saveAll(entities);
    }

    public void unlockBaggingReturnRespective(List<BaggingReturnRespectiveMainEntity> entities) {
        baggingReturnRespectiveMainRepository.saveAll(entities);
    }

    public Page<BaggingReturnIrrespectiveMainEntity> findAllBaggingReturnIrRespectiveMain(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.baggingReturnIrRespectiveMainRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public BaggingReturnIrrespectiveMainEntity findBaggingReturnIrRespectiveById(Integer id) {
        return this.baggingReturnIrRespectiveMainRepository.findById(id).orElse(new BaggingReturnIrrespectiveMainEntity());
    }

    public List<BaggingReturnIrrespectiveDetailEntity> findBaggingReturnIrRespectiveDetailByMainId(Integer invId) {
        return baggingReturnIrRespectiveDetailRepository.findAllByMainId(invId);
    }

    public List<BaggingReturnIrrespectiveDetailEntity> findBaggingReturnIrRespectiveDetailByMainIdList(List<Integer> invId) {
        return baggingReturnIrRespectiveDetailRepository.findAllByDetailByMainIds(invId);
    }

    public BaggingReturnIrrespectiveMainEntity saveReturnIrRespectiveMain(BaggingIssueReceiveMainDto issueReceiveMainDto) throws Exception {
        BaggingReturnIrrespectiveMainEntity createEntity = this.convertToReturnIrRespectiveMainEntity(issueReceiveMainDto);
        if (!Optional.ofNullable(issueReceiveMainDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(issueReceiveMainDto.getInvoiceType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setInvoiceNo(nextNumber);
        }
        BaggingReturnIrrespectiveMainEntity entity = baggingReturnIrRespectiveMainRepository.save(createEntity);
        return entity;
    }

    private BaggingReturnIrrespectiveMainEntity convertToReturnIrRespectiveMainEntity(BaggingIssueReceiveMainDto issueReceiveMainDto) {
        BaggingReturnIrrespectiveMainEntity entity = new BaggingReturnIrrespectiveMainEntity();
        BeanUtils.copyProperties(issueReceiveMainDto, entity);
        entity.setSequence(issueReceiveMainDto.getSequence() == null ? 0 : issueReceiveMainDto.getSequence());
        entity.setIsLocked(issueReceiveMainDto.getIsLocked() == null ? 0 : issueReceiveMainDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(issueReceiveMainDto.getIsDeleted() == null ? 0 : issueReceiveMainDto.getIsDeleted());
        return entity;
    }


    public void removeReturnIrRespectiveMain(BaggingReturnIrrespectiveMainEntity entity, List<BaggingReturnIrrespectiveDetailEntity> detailEntities) throws Exception {
        baggingReturnIrRespectiveDetailRepository.saveAll(detailEntities);
        baggingReturnIrRespectiveMainRepository.save(entity);
    }

    public BaggingReturnIrrespectiveDetailEntity saveReturnIrRespectiveDetail(BaggingIssueReceiveDetailDto baggingReturnReceiveDetailDto) throws Exception {
        return baggingReturnIrRespectiveDetailRepository.save(convertToReturnIrRespectiveDetailEntity(baggingReturnReceiveDetailDto));
    }

    private BaggingReturnIrrespectiveDetailEntity convertToReturnIrRespectiveDetailEntity(BaggingIssueReceiveDetailDto issueReceiveDetailDto) {
        BaggingReturnIrrespectiveDetailEntity entity = new BaggingReturnIrrespectiveDetailEntity();
        BeanUtils.copyProperties(issueReceiveDetailDto, entity);
        entity.setInvQty1(issueReceiveDetailDto.getInvQty1() == null ? 0 : issueReceiveDetailDto.getInvQty1());
        entity.setInvQty2(issueReceiveDetailDto.getInvQty2() == null ? 0 : issueReceiveDetailDto.getInvQty2());
        entity.setSequence(issueReceiveDetailDto.getSequence() == null ? 0 : issueReceiveDetailDto.getSequence());
        entity.setIsLocked(issueReceiveDetailDto.getIsLocked() == null ? 0 : issueReceiveDetailDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(issueReceiveDetailDto.getIsDeleted() == null ? 0 : issueReceiveDetailDto.getIsDeleted());
        return entity;
    }

    public BaggingReturnIrrespectiveDetailEntity findBaggingReturnIrRespectiveDetailById(Integer id) {
        return baggingReturnIrRespectiveDetailRepository.findById(id).orElse(new BaggingReturnIrrespectiveDetailEntity());
    }

    public void removeBaggingReturnIrRespectiveDetail(BaggingReturnIrrespectiveDetailEntity entity) throws Exception {
        baggingReturnIrRespectiveDetailRepository.save(entity);
    }

    public List<BaggingReturnIrrespectiveMainEntity> findAllBaggingReturnIrRespectiveMainByIds(List<Integer> ids) {
        return baggingReturnIrRespectiveMainRepository.findAllMainByMainIds(ids);
    }

    public void removeAllBaggingReturnIrRespectiveMain(List<BaggingReturnIrrespectiveMainEntity> baggingReturnIrRespectiveMainEntities, List<BaggingReturnIrrespectiveDetailEntity> detailEntities) throws Exception {
        baggingReturnIrRespectiveDetailRepository.saveAll(detailEntities);
        baggingReturnIrRespectiveMainRepository.saveAll(baggingReturnIrRespectiveMainEntities);
    }

    public void lockBaggingReturnIrRespective(List<BaggingReturnIrrespectiveMainEntity> entities) {
        baggingReturnIrRespectiveMainRepository.saveAll(entities);
    }

    public void unlockBaggingReturnIrRespective(List<BaggingReturnIrrespectiveMainEntity> entities) {
        baggingReturnIrRespectiveMainRepository.saveAll(entities);
    }
}
