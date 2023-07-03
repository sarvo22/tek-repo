package com.tekfilo.jewellery.design.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.design.dto.DesignComponentDto;
import com.tekfilo.jewellery.design.dto.DesignDto;
import com.tekfilo.jewellery.design.entity.DesignComponentEntity;
import com.tekfilo.jewellery.design.entity.DesignEntity;
import com.tekfilo.jewellery.design.repository.DesignComponentRepository;
import com.tekfilo.jewellery.design.repository.DesignRepository;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceChargesEntity;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.product.repository.ProductRepository;
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
import java.util.stream.IntStream;

@Service
@Transactional
public class DesignService {

    @Autowired
    DesignRepository designRepository;

    @Autowired
    DesignComponentRepository designComponentRepository;

    @Autowired
    ProductRepository productRepository;

    public Page<DesignEntity> findAllDesigns(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.designRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public DesignEntity save(DesignDto designDto) throws Exception {
        return designRepository.save(convertToDesignEntity(designDto));
    }

    private DesignEntity convertToDesignEntity(DesignDto designDto) {
        DesignEntity entity = new DesignEntity();
        BeanUtils.copyProperties(designDto, entity);
        entity.setMetalWeight(designDto.getMetalWeight() == null ? 0 : designDto.getMetalWeight());
        entity.setSequence(designDto.getSequence() == null ? 0 : designDto.getSequence());
        entity.setIsLocked(designDto.getIsLocked() == null ? 0 : designDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public void modify(DesignDto designDto) throws Exception {
        designRepository.save(convertToDesignEntity(designDto));
    }

    public DesignEntity findDesignById(Integer id) {
        return designRepository.findById(id).orElse(null);
    }

    public void removeDesign(DesignEntity entity) throws Exception {
        List<DesignComponentEntity> designComponentEntities = this.designComponentRepository.findAllByMainId(entity.getId());
        designComponentEntities.stream().forEach(e -> {
            e.setIsDeleted(1);
            e.setModifiedBy(UserContext.getLoggedInUser());
        });
        designComponentRepository.saveAll(designComponentEntities);
        designRepository.save(entity);
    }


    public List<DesignEntity> findAllDesignEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.designRepository.findAll(filterClauseAppender.getInClassFilter(ids,"id"));
    }

    @Transactional
    public void removeAllDesign(List<DesignEntity> entities) throws Exception {
        this.designComponentRepository.deleteAllByDesignIds(entities.stream().map(DesignEntity::getId).collect(Collectors.toList()),UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
        this.designRepository.deleteAllByIds(entities.stream().map(DesignEntity::getId).collect(Collectors.toList()),UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }

    public Specification getFilterClause(List<Integer> filterClause) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            predicates.add(builder.equal(root.get("designId"), filterClause));
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

    public void lockDesign(List<DesignEntity> entities) throws Exception {
        this.designRepository.saveAll(entities);
    }

    public void unlockDesign(List<DesignEntity> entities) throws Exception {
        this.designRepository.saveAll(entities);
    }

    public List<DesignComponentEntity> findDesignComponentList(Integer designId) {
        return this.designComponentRepository.findAllByMainId(designId);
    }

    public DesignComponentEntity saveDesignComponent(DesignComponentDto designComponentDto) throws Exception {
        return this.designComponentRepository.save(convertToDesignComponentEntity(designComponentDto));
    }

    private DesignComponentEntity convertToDesignComponentEntity(DesignComponentDto designComponentDto) {
        DesignComponentEntity designComponentEntity = new DesignComponentEntity();
        BeanUtils.copyProperties(designComponentDto, designComponentEntity);
        designComponentEntity.setSequence(0);
        designComponentEntity.setIsLocked(0);
        designComponentEntity.setCreatedBy(UserContext.getLoggedInUser());
        designComponentEntity.setModifiedBy(UserContext.getLoggedInUser());
        designComponentEntity.setIsDeleted(0);
        return designComponentEntity;
    }

    public void deleteDesignComponent(DesignComponentEntity entity) throws Exception {
        this.designComponentRepository.save(entity);
    }

    public DesignComponentEntity findDesignComponentById(Integer id) {
        return this.designComponentRepository.findById(id).orElseThrow(null);
    }

}
