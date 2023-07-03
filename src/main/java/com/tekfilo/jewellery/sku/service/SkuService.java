package com.tekfilo.jewellery.sku.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.design.entity.DesignEntity;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.product.repository.ProductRepository;
import com.tekfilo.jewellery.sku.dto.*;
import com.tekfilo.jewellery.sku.entity.*;
import com.tekfilo.jewellery.sku.repository.*;
import com.tekfilo.jewellery.util.FilterClauseAppender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SkuService {

    @Autowired
    SkuRepository skuRepository;

    @Autowired
    SkuComponentRepository skuComponentRepository;

    @Autowired
    SkuLabourRepository skuLabourRepository;

    @Autowired
    SkuFindingRepository skuFindingRepository;

    @Autowired
    SkuMouldPartRepository skuMouldPartRepository;

    @Autowired
    SkuWaxRepository skuWaxRepository;

    public Page<SkuEntity> findAllSkus(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.skuRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public SkuEntity save(SkuDto skuDto) throws Exception {
        return skuRepository.save(convertToSkuEntity(skuDto));
    }

    private SkuEntity convertToSkuEntity(SkuDto skuDto) {
        SkuEntity entity = new SkuEntity();
        BeanUtils.copyProperties(skuDto, entity);
        entity.setSequence(skuDto.getSequence() == null ? 0 : skuDto.getSequence());
        entity.setIsLocked(skuDto.getIsLocked() == null ? 0 : skuDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void modify(SkuDto skuDto) throws Exception {
        skuRepository.save(convertToSkuEntity(skuDto));
    }

    public SkuEntity findSkuById(Integer id) {
        return skuRepository.findById(id).orElse(null);
    }

    public void removeSku(SkuEntity entity) throws Exception {
        List<SkuComponentEntity> skuComponentEntities = this.skuComponentRepository.findAllByMainId(entity.getId());
        skuComponentEntities.stream().forEach(e -> {
            e.setIsDeleted(1);
            e.setModifiedBy(UserContext.getLoggedInUser());
        });
        skuComponentRepository.saveAll(skuComponentEntities);
        skuRepository.save(entity);
    }


    public List<SkuEntity> findAllSkuEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.skuRepository.findAll(filterClauseAppender.getInClassFilter(ids,"id"));
    }

    @Transactional
    public void removeAllSku(List<SkuEntity> entities) throws Exception {
        this.skuComponentRepository.deleteAllBySkuIds(entities.stream().map(SkuEntity::getId).collect(Collectors.toList()),UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
        this.skuRepository.deleteAllByIds(entities.stream().map(SkuEntity::getId).collect(Collectors.toList()),UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    public Specification getFilterClause(List<Integer> filterClause) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            predicates.add(builder.equal(root.get("skuId"), filterClause));
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public Specification getFilterClause(List<Integer> filterClause, String column) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            predicates.add(builder.equal(root.get(column), filterClause));
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public void lockSku(List<SkuEntity> entities) throws Exception {
        this.skuRepository.saveAll(entities);
    }

    public void unlockSku(List<SkuEntity> entities) throws Exception {
        this.skuRepository.saveAll(entities);
    }

    public List<SkuComponentEntity> findSkuComponentList(Integer skuId) {
        return this.skuComponentRepository.findAllByMainId(skuId);
    }

    public SkuComponentEntity saveSkuComponent(SkuComponentDto skuComponentDto) throws Exception {
        return this.skuComponentRepository.save(convertToSkuComponentEntity(skuComponentDto));
    }

    private SkuComponentEntity convertToSkuComponentEntity(SkuComponentDto skuComponentDto) {
        SkuComponentEntity skuComponentEntity = new SkuComponentEntity();
        BeanUtils.copyProperties(skuComponentDto, skuComponentEntity);
        skuComponentEntity.setSequence(0);
        skuComponentEntity.setIsLocked(0);
        skuComponentEntity.setCreatedBy(UserContext.getLoggedInUser());
        skuComponentEntity.setModifiedBy(UserContext.getLoggedInUser());
        skuComponentEntity.setIsDeleted(0);
        return skuComponentEntity;
    }

    public void deleteSkuComponent(SkuComponentEntity entity) throws Exception {
        this.skuComponentRepository.save(entity);
    }

    public SkuComponentEntity findSkuComponentById(Integer id) {
        return this.skuComponentRepository.findById(id).orElseThrow(null);
    }

    public List<SkuLabourEntity> findSkuLabourList(Integer skuId) {
        return this.skuLabourRepository.findAllByMainId(skuId);
    }

    public SkuLabourEntity saveSkuLabour(SkuLabourDto skuLabourDto) throws Exception {
        return this.skuLabourRepository.save(convertToSkuLabourEntity(skuLabourDto));
    }

    private SkuLabourEntity convertToSkuLabourEntity(SkuLabourDto skuLabourDto) {
        SkuLabourEntity skuLabourEntity = new SkuLabourEntity();
        BeanUtils.copyProperties(skuLabourDto, skuLabourEntity);
        skuLabourEntity.setSequence(0);
        skuLabourEntity.setIsLocked(0);
        skuLabourEntity.setCreatedBy(UserContext.getLoggedInUser());
        skuLabourEntity.setModifiedBy(UserContext.getLoggedInUser());
        skuLabourEntity.setIsDeleted(0);
        return skuLabourEntity;
    }

    public void deleteSkuLabour(SkuLabourEntity entity) throws Exception {
        this.skuLabourRepository.save(entity);
    }

    public SkuLabourEntity findSkuLabourById(Integer id) {
        return this.skuLabourRepository.findById(id).orElseThrow(null);
    }

    public List<SkuFindingEntity> findSkuFindingList(Integer skuId) {
        return this.skuFindingRepository.findAllByMainId(skuId);
    }

    public SkuFindingEntity saveSkuFinding(SkuFindingDto skuFindingDto) throws Exception {
        return this.skuFindingRepository.save(convertToSkuFindingEntity(skuFindingDto));
    }

    private SkuFindingEntity convertToSkuFindingEntity(SkuFindingDto skuFindingDto) {
        SkuFindingEntity skuFindingEntity = new SkuFindingEntity();
        BeanUtils.copyProperties(skuFindingDto, skuFindingEntity);
        skuFindingEntity.setSequence(0);
        skuFindingEntity.setIsLocked(0);
        skuFindingEntity.setCreatedBy(UserContext.getLoggedInUser());
        skuFindingEntity.setModifiedBy(UserContext.getLoggedInUser());
        skuFindingEntity.setIsDeleted(0);
        return skuFindingEntity;
    }

    public void deleteSkuFinding(SkuFindingEntity entity) throws Exception {
        this.skuFindingRepository.save(entity);
    }

    public SkuFindingEntity findSkuFindingById(Integer id) {
        return this.skuFindingRepository.findById(id).orElseThrow(null);
    }

    public List<SkuMouldPartEntity> findSkuMouldPartList(Integer skuId) {
        return this.skuMouldPartRepository.findAllByMainId(skuId);
    }

    public SkuMouldPartEntity saveSkuMouldPart(SkuMouldPartDto skuMouldPartDto) throws Exception {
        return this.skuMouldPartRepository.save(convertToSkuMouldPartEntity(skuMouldPartDto));
    }

    private SkuMouldPartEntity convertToSkuMouldPartEntity(SkuMouldPartDto skuMouldPartDto) {
        SkuMouldPartEntity skuMouldPartEntity = new SkuMouldPartEntity();
        BeanUtils.copyProperties(skuMouldPartDto, skuMouldPartEntity);
        skuMouldPartEntity.setSequence(0);
        skuMouldPartEntity.setIsLocked(0);
        skuMouldPartEntity.setCreatedBy(UserContext.getLoggedInUser());
        skuMouldPartEntity.setModifiedBy(UserContext.getLoggedInUser());
        skuMouldPartEntity.setIsDeleted(0);
        return skuMouldPartEntity;
    }

    public void deleteSkuMouldPart(SkuMouldPartEntity entity) throws Exception {
        this.skuMouldPartRepository.save(entity);
    }

    public SkuMouldPartEntity findSkuMouldPartById(Integer id) {
        return this.skuMouldPartRepository.findById(id).orElseThrow(null);
    }

    public List<SkuWaxEntity> findSkuWaxList(Integer skuId) {
        return this.skuWaxRepository.findAllByMainId(skuId);
    }

    public SkuWaxEntity saveSkuWax(SkuWaxDto skuWaxDto) throws Exception {
        return this.skuWaxRepository.save(convertToSkuWaxEntity(skuWaxDto));
    }

    private SkuWaxEntity convertToSkuWaxEntity(SkuWaxDto skuWaxDto) {
        SkuWaxEntity skuWaxEntity = new SkuWaxEntity();
        BeanUtils.copyProperties(skuWaxDto, skuWaxEntity);
        skuWaxEntity.setSequence(0);
        skuWaxEntity.setIsLocked(0);
        skuWaxEntity.setCreatedBy(UserContext.getLoggedInUser());
        skuWaxEntity.setModifiedBy(UserContext.getLoggedInUser());
        skuWaxEntity.setIsDeleted(0);
        return skuWaxEntity;
    }

    public void deleteSkuWax(SkuWaxEntity entity) throws Exception {
        this.skuWaxRepository.save(entity);
    }

    public SkuWaxEntity findSkuWaxById(Integer id) {
        return this.skuWaxRepository.findById(id).orElseThrow(null);
    }

}
