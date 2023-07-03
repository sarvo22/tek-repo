package com.tekfilo.admin.factory;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.factory.dto.*;
import com.tekfilo.admin.factory.entity.*;
import com.tekfilo.admin.factory.repository.*;
import com.tekfilo.admin.multitenancy.CompanyContext;
import com.tekfilo.admin.multitenancy.UserContext;
import com.tekfilo.admin.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class FactoryService implements IFactoryService {

    @Autowired
    FactoryRepository factoryRepository;

    @Autowired
    FactoryAddressRepository factoryAddressRepository;

    @Autowired
    FactoryContactRepository factoryContactRepository;

    @Autowired
    FactoryDocumentRepository factoryDocumentRepository;

    @Autowired
    FactoryLimitRepository factoryLimitRepository;

    @Override
    public List<FactoryEntity> getFactoryList(String searchKey) {
        log.info("Getting Factory List ");
        return factoryRepository.getFactoryList(searchKey);
    }

    @Override
    public Page<FactoryEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.factoryRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public FactoryEntity save(FactoryDto factoryDto) throws Exception {
        return factoryRepository.save(convertToEntity(factoryDto));
    }

    private FactoryEntity convertToEntity(FactoryDto factoryDto) {
        FactoryEntity entity = new FactoryEntity();
        BeanUtils.copyProperties(factoryDto, entity);
        entity.setFactoryCode(factoryDto.getFactoryCode() == null ? factoryDto.getFactoryName() : factoryDto.getFactoryCode());
        entity.setSortSequence(factoryDto.getSortSequence() == null ? 0 : factoryDto.getSortSequence());
        entity.setIsLocked(factoryDto.getIsLocked() == null ? 0 : factoryDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(factoryDto.getIsDeleted() == null ? 0 : factoryDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(FactoryDto factoryDto) throws Exception {
        factoryRepository.save(convertToEntity(factoryDto));
    }


    @Override
    public FactoryEntity findById(Integer id) {
        return factoryRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(FactoryEntity entity) throws Exception {
        factoryRepository.save(entity);
    }

    @Override
    public List<FactoryAddressEntity> findAllFactoryAddress(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<FactoryAddressEntity> pagedList = factoryAddressRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public FactoryAddressEntity saveFactoryAddress(FactoryAddressDto factoryAddressDto) throws Exception {
        return factoryAddressRepository.save(convertFactoryAddressToEntity(factoryAddressDto));
    }

    private FactoryAddressEntity convertFactoryAddressToEntity(FactoryAddressDto factoryAddressDto) {
        FactoryAddressEntity entity = new FactoryAddressEntity();
        BeanUtils.copyProperties(factoryAddressDto, entity);
        entity.setSortSequence(factoryAddressDto.getSortSequence() == null ? 0 : factoryAddressDto.getSortSequence());
        entity.setIsLocked(factoryAddressDto.getIsLocked() == null ? 0 : factoryAddressDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(factoryAddressDto.getIsDeleted() == null ? 0 : factoryAddressDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyFactoryAddress(FactoryAddressDto factoryAddressDto) throws Exception {
        factoryAddressRepository.save(convertFactoryAddressToEntity(factoryAddressDto));
    }

    @Override
    public FactoryAddressEntity findFactoryAddressById(Integer id) {
        return factoryAddressRepository.findById(id).orElse(null);
    }

    @Override
    public void removeFactoryAddress(FactoryAddressEntity entity) throws Exception {
        factoryAddressRepository.save(entity);
    }

    @Override
    public List<FactoryAddressEntity> findAddressByFactoryId(Integer factoryId) {
        return factoryAddressRepository.findAddressByFactoryId(factoryId);
    }

    @Override
    public List<FactoryContactEntity> findAllFactoryContact(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<FactoryContactEntity> pagedList = factoryContactRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public FactoryContactEntity saveFactoryContact(FactoryContactDto factoryContactDto) throws Exception {
        return factoryContactRepository.save(convertFactoryContactToEntity(factoryContactDto));
    }

    private FactoryContactEntity convertFactoryContactToEntity(FactoryContactDto factoryContactDto) {
        FactoryContactEntity entity = new FactoryContactEntity();
        BeanUtils.copyProperties(factoryContactDto, entity);
        entity.setSortSequence(factoryContactDto.getSortSequence() == null ? 0 : factoryContactDto.getSortSequence());
        entity.setIsLocked(factoryContactDto.getIsLocked() == null ? 0 : factoryContactDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(factoryContactDto.getIsDeleted() == null ? 0 : factoryContactDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyFactoryContact(FactoryContactDto factoryContactDto) throws Exception {
        factoryContactRepository.save(convertFactoryContactToEntity(factoryContactDto));
    }

    @Override
    public FactoryContactEntity findFactoryContactById(Integer id) {
        return factoryContactRepository.findById(id).orElse(null);
    }

    @Override
    public void removeFactoryContact(FactoryContactEntity entity) throws Exception {
        factoryContactRepository.save(entity);
    }

    @Override
    public List<FactoryContactEntity> findContactsByFactoryId(Integer factoryId) {
        return factoryContactRepository.findContactsByFactoryId(factoryId);
    }

    @Override
    public List<FactoryDocumentEntity> findAllFactoryDocument(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<FactoryDocumentEntity> pagedList = factoryDocumentRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public FactoryDocumentEntity saveFactoryDocument(FactoryDocumentDto factoryDocumentDto) throws Exception {
        return factoryDocumentRepository.save(convertFactoryDocumentToEntity(factoryDocumentDto));
    }

    private FactoryDocumentEntity convertFactoryDocumentToEntity(FactoryDocumentDto factoryDocumentDto) {
        FactoryDocumentEntity entity = new FactoryDocumentEntity();
        BeanUtils.copyProperties(factoryDocumentDto, entity);
        entity.setSortSequence(factoryDocumentDto.getSortSequence() == null ? 0 : factoryDocumentDto.getSortSequence());
        entity.setIsLocked(factoryDocumentDto.getIsLocked() == null ? 0 : factoryDocumentDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(factoryDocumentDto.getIsDeleted() == null ? 0 : factoryDocumentDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyFactoryDocument(FactoryDocumentDto factoryDocumentDto) throws Exception {
        factoryDocumentRepository.save(convertFactoryDocumentToEntity(factoryDocumentDto));
    }

    @Override
    public FactoryDocumentEntity findFactoryDocumentById(Integer id) {
        return factoryDocumentRepository.findById(id).orElse(null);
    }

    @Override
    public void removeFactoryDocument(FactoryDocumentEntity entity) throws Exception {
        factoryDocumentRepository.save(entity);
    }

    @Override
    public List<FactoryDocumentEntity> findDocumentsByFactoryId(Integer factoryId) {
        return factoryDocumentRepository.findDocumentsByFactoryId(factoryId);
    }

    @Override
    public List<FactoryLimitEntity> findAllFactoryLimit(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<FactoryLimitEntity> pagedList = factoryLimitRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public FactoryLimitEntity saveFactoryLimit(FactoryLimitDto factoryLimitDto) throws Exception {
        return factoryLimitRepository.save(convertFactoryLimitToEntity(factoryLimitDto));
    }

    private FactoryLimitEntity convertFactoryLimitToEntity(FactoryLimitDto factoryLimitDto) {
        FactoryLimitEntity entity = new FactoryLimitEntity();
        BeanUtils.copyProperties(factoryLimitDto, entity);
        entity.setSortSequence(factoryLimitDto.getSortSequence() == null ? 0 : factoryLimitDto.getSortSequence());
        entity.setIsLocked(factoryLimitDto.getIsLocked() == null ? 0 : factoryLimitDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(factoryLimitDto.getIsDeleted() == null ? 0 : factoryLimitDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyFactoryLimit(FactoryLimitDto factoryLimitDto) throws Exception {
        factoryLimitRepository.save(convertFactoryLimitToEntity(factoryLimitDto));
    }

    @Override
    public FactoryLimitEntity findFactoryLimitById(Integer id) {
        return factoryLimitRepository.findById(id).orElse(null);
    }

    @Override
    public void removeFactoryLimit(FactoryLimitEntity entity) throws Exception {
        factoryLimitRepository.save(entity);
    }

    @Override
    public List<FactoryLimitEntity> findLimitsByFactoryId(Integer factoryId) {
        return factoryLimitRepository.findLimitsByFactoryId(factoryId);
    }


    @Override
    public void removeAll(List<Integer> ids) throws Exception {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        List<FactoryContactEntity> factoryContactEntityList = factoryContactRepository.findAll(filterClauseAppender.getCustomInClassFilter("factoryId", ids));
        List<FactoryDocumentEntity> factoryDocumentEntityList = factoryDocumentRepository.findAll(filterClauseAppender.getCustomInClassFilter("factoryId", ids));
        List<FactoryLimitEntity> factoryLimitEntityList = factoryLimitRepository.findAll(filterClauseAppender.getCustomInClassFilter("factoryId", ids));
        List<FactoryAddressEntity> factoryAddressEntityList = factoryAddressRepository.findAll(filterClauseAppender.getCustomInClassFilter("factoryId", ids));
        List<FactoryEntity> factoryEntityList = factoryRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));

        factoryContactEntityList.stream().forEach(element -> {
            element.setIsDeleted(1);
            element.setModifiedBy(UserContext.getLoggedInUser());
        });
        factoryDocumentEntityList.stream().forEach(element -> {
            element.setIsDeleted(1);
            element.setModifiedBy(UserContext.getLoggedInUser());
        });
        factoryLimitEntityList.stream().forEach(element -> {
            element.setIsDeleted(1);
            element.setModifiedBy(UserContext.getLoggedInUser());
        });
        factoryAddressEntityList.stream().forEach(element -> {
            element.setIsDeleted(1);
            element.setModifiedBy(UserContext.getLoggedInUser());
        });
        factoryEntityList.stream().forEach(element -> {
            element.setIsDeleted(1);
            element.setModifiedBy(UserContext.getLoggedInUser());
        });

        factoryContactRepository.saveAll(factoryContactEntityList);
        factoryDocumentRepository.saveAll(factoryDocumentEntityList);
        factoryLimitRepository.saveAll(factoryLimitEntityList);
        factoryAddressRepository.saveAll(factoryAddressEntityList);
        factoryRepository.saveAll(factoryEntityList);
    }

    @Override
    public List<FactoryEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return factoryRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void lock(List<FactoryEntity> entities) throws Exception {
        entities.stream().forEach(entity -> {
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsLocked(1);
        });
        this.factoryRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<FactoryEntity> entities) throws Exception {
        entities.stream().forEach(entity -> {
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsLocked(0);
        });
        this.factoryRepository.saveAll(entities);
    }

}
