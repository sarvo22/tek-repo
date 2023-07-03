package com.tekfilo.admin.supplier;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.multitenancy.CompanyContext;
import com.tekfilo.admin.multitenancy.UserContext;
import com.tekfilo.admin.supplier.dto.*;
import com.tekfilo.admin.supplier.entity.*;
import com.tekfilo.admin.supplier.repository.*;
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
public class SupplierService implements ISupplierService {

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SupplierAddressRepository supplierAddressRepository;

    @Autowired
    SupplierContactRepository supplierContactRepository;

    @Autowired
    SupplierDocumentRepository supplierDocumentRepository;

    @Autowired
    SupplierLimitRepository supplierLimitRepository;

    @Override
    public List<SupplierEntity> getSupplierList(String searchKey) {
        log.info("Getting Supplier List ");
        return supplierRepository.getSupplierList(searchKey);
    }

    @Override
    public Page<SupplierEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.supplierRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public SupplierEntity save(SupplierDto supplierDto) throws Exception {
        return supplierRepository.save(convertToEntity(supplierDto));
    }

    private SupplierEntity convertToEntity(SupplierDto supplierDto) {
        SupplierEntity entity = new SupplierEntity();
        BeanUtils.copyProperties(supplierDto, entity);
        entity.setSortSequence(supplierDto.getSortSequence() == null ? 0 : supplierDto.getSortSequence());
        entity.setIsLocked(supplierDto.getIsLocked() == null ? 0 : supplierDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(supplierDto.getIsDeleted() == null ? 0 : supplierDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(SupplierDto supplierDto) throws Exception {
        supplierRepository.save(convertToEntity(supplierDto));
    }


    @Override
    public SupplierEntity findById(Integer id) {
        return supplierRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(SupplierEntity entity) throws Exception {
        supplierRepository.save(entity);
    }

    @Override
    public List<SupplierAddressEntity> findAllSupplierAddress(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<SupplierAddressEntity> pagedList = supplierAddressRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public SupplierAddressEntity saveSupplierAddress(SupplierAddressDto supplierAddressDto) throws Exception {
        return supplierAddressRepository.save(convertSupplierAddressToEntity(supplierAddressDto));
    }

    private SupplierAddressEntity convertSupplierAddressToEntity(SupplierAddressDto supplierAddressDto) {
        SupplierAddressEntity entity = new SupplierAddressEntity();
        BeanUtils.copyProperties(supplierAddressDto, entity);
        entity.setSortSequence(supplierAddressDto.getSortSequence() == null ? 0 : supplierAddressDto.getSortSequence());
        entity.setIsLocked(supplierAddressDto.getIsLocked() == null ? 0 : supplierAddressDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(supplierAddressDto.getIsDeleted() == null ? 0 : supplierAddressDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifySupplierAddress(SupplierAddressDto supplierAddressDto) throws Exception {
        supplierAddressRepository.save(convertSupplierAddressToEntity(supplierAddressDto));
    }

    @Override
    public SupplierAddressEntity findSupplierAddressById(Integer id) {
        return supplierAddressRepository.findById(id).orElse(null);
    }

    @Override
    public void removeSupplierAddress(SupplierAddressEntity entity) throws Exception {
        supplierAddressRepository.save(entity);
    }

    @Override
    public List<SupplierAddressEntity> findAddressBySupplierId(Integer supplierId) {
        return supplierAddressRepository.findAddressBySupplierId(supplierId);
    }

    @Override
    public List<SupplierContactEntity> findAllSupplierContact(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<SupplierContactEntity> pagedList = supplierContactRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public SupplierContactEntity saveSupplierContact(SupplierContactDto supplierContactDto) throws Exception {
        return supplierContactRepository.save(convertSupplierContactToEntity(supplierContactDto));
    }

    private SupplierContactEntity convertSupplierContactToEntity(SupplierContactDto supplierContactDto) {
        SupplierContactEntity entity = new SupplierContactEntity();
        BeanUtils.copyProperties(supplierContactDto, entity);
        entity.setSortSequence(supplierContactDto.getSortSequence() == null ? 0 : supplierContactDto.getSortSequence());
        entity.setIsLocked(supplierContactDto.getIsLocked() == null ? 0 : supplierContactDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(supplierContactDto.getIsDeleted() == null ? 0 : supplierContactDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifySupplierContact(SupplierContactDto supplierContactDto) throws Exception {
        supplierContactRepository.save(convertSupplierContactToEntity(supplierContactDto));
    }

    @Override
    public SupplierContactEntity findSupplierContactById(Integer id) {
        return supplierContactRepository.findById(id).orElse(null);
    }

    @Override
    public void removeSupplierContact(SupplierContactEntity entity) throws Exception {
        supplierContactRepository.save(entity);
    }

    @Override
    public List<SupplierContactEntity> findContactsBySupplierId(Integer supplierId) {
        return supplierContactRepository.findContactsBySupplierId(supplierId);
    }

    @Override
    public List<SupplierDocumentEntity> findAllSupplierDocument(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<SupplierDocumentEntity> pagedList = supplierDocumentRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public SupplierDocumentEntity saveSupplierDocument(SupplierDocumentDto supplierDocumentDto) throws Exception {
        return supplierDocumentRepository.save(convertSupplierDocumentToEntity(supplierDocumentDto));
    }

    private SupplierDocumentEntity convertSupplierDocumentToEntity(SupplierDocumentDto supplierDocumentDto) {
        SupplierDocumentEntity entity = new SupplierDocumentEntity();
        BeanUtils.copyProperties(supplierDocumentDto, entity);
        entity.setSortSequence(supplierDocumentDto.getSortSequence() == null ? 0 : supplierDocumentDto.getSortSequence());
        entity.setIsLocked(supplierDocumentDto.getIsLocked() == null ? 0 : supplierDocumentDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(supplierDocumentDto.getIsDeleted() == null ? 0 : supplierDocumentDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifySupplierDocument(SupplierDocumentDto supplierDocumentDto) throws Exception {
        supplierDocumentRepository.save(convertSupplierDocumentToEntity(supplierDocumentDto));
    }

    @Override
    public SupplierDocumentEntity findSupplierDocumentById(Integer id) {
        return supplierDocumentRepository.findById(id).orElse(null);
    }

    @Override
    public void removeSupplierDocument(SupplierDocumentEntity entity) throws Exception {
        supplierDocumentRepository.save(entity);
    }

    @Override
    public List<SupplierDocumentEntity> findDocumentsBySupplierId(Integer supplierId) {
        return supplierDocumentRepository.findDocumentsBySupplierId(supplierId);
    }

    @Override
    public List<SupplierLimitEntity> findAllSupplierLimit(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<SupplierLimitEntity> pagedList = supplierLimitRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public SupplierLimitEntity saveSupplierLimit(SupplierLimitDto supplierLimitDto) throws Exception {
        return supplierLimitRepository.save(convertSupplierLimitToEntity(supplierLimitDto));
    }

    private SupplierLimitEntity convertSupplierLimitToEntity(SupplierLimitDto supplierLimitDto) {
        SupplierLimitEntity entity = new SupplierLimitEntity();
        BeanUtils.copyProperties(supplierLimitDto, entity);
        entity.setSortSequence(supplierLimitDto.getSortSequence() == null ? 0 : supplierLimitDto.getSortSequence());
        entity.setIsLocked(supplierLimitDto.getIsLocked() == null ? 0 : supplierLimitDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(supplierLimitDto.getIsDeleted() == null ? 0 : supplierLimitDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifySupplierLimit(SupplierLimitDto supplierLimitDto) throws Exception {
        supplierLimitRepository.save(convertSupplierLimitToEntity(supplierLimitDto));
    }

    @Override
    public SupplierLimitEntity findSupplierLimitById(Integer id) {
        return supplierLimitRepository.findById(id).orElse(null);
    }

    @Override
    public void removeSupplierLimit(SupplierLimitEntity entity) throws Exception {
        supplierLimitRepository.save(entity);
    }

    @Override
    public List<SupplierLimitEntity> findLimitsBySupplierId(Integer supplierId) {
        return supplierLimitRepository.findLimitsBySupplierId(supplierId);
    }


    @Override
    public void removeAll(List<Integer> ids) throws Exception {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        List<SupplierContactEntity> supplierContactEntityList = supplierContactRepository.findAll(filterClauseAppender.getCustomInClassFilter("supplierId", ids));
        List<SupplierDocumentEntity> supplierDocumentEntityList = supplierDocumentRepository.findAll(filterClauseAppender.getCustomInClassFilter("supplierId", ids));
        List<SupplierLimitEntity> supplierLimitEntityList = supplierLimitRepository.findAll(filterClauseAppender.getCustomInClassFilter("supplierId", ids));
        List<SupplierAddressEntity> supplierAddressEntityList = supplierAddressRepository.findAll(filterClauseAppender.getCustomInClassFilter("supplierId", ids));
        List<SupplierEntity> supplierEntityList = supplierRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));

        supplierContactEntityList.stream().forEach(element -> {
            element.setIsDeleted(1);
            element.setModifiedBy(UserContext.getLoggedInUser());
        });
        supplierDocumentEntityList.stream().forEach(element -> {
            element.setIsDeleted(1);
            element.setModifiedBy(UserContext.getLoggedInUser());
        });
        supplierLimitEntityList.stream().forEach(element -> {
            element.setIsDeleted(1);
            element.setModifiedBy(UserContext.getLoggedInUser());
        });
        supplierAddressEntityList.stream().forEach(element -> {
            element.setIsDeleted(1);
            element.setModifiedBy(UserContext.getLoggedInUser());
        });
        supplierEntityList.stream().forEach(element -> {
            element.setIsDeleted(1);
            element.setModifiedBy(UserContext.getLoggedInUser());
        });

        supplierContactRepository.saveAll(supplierContactEntityList);
        supplierDocumentRepository.saveAll(supplierDocumentEntityList);
        supplierLimitRepository.saveAll(supplierLimitEntityList);
        supplierAddressRepository.saveAll(supplierAddressEntityList);
        supplierRepository.saveAll(supplierEntityList);
    }

    @Override
    public List<SupplierEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return supplierRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void lock(List<SupplierEntity> entities) throws Exception {
        entities.stream().forEach(entity -> {
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsLocked(1);
        });
        this.supplierRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<SupplierEntity> entities) throws Exception {
        entities.stream().forEach(entity -> {
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsLocked(0);
        });
        this.supplierRepository.saveAll(entities);
    }

}
