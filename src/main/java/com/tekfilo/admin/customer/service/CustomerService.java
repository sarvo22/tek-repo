package com.tekfilo.admin.customer.service;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.customer.dto.*;
import com.tekfilo.admin.customer.entity.*;
import com.tekfilo.admin.customer.repository.*;
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
public class CustomerService implements ICustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @Autowired
    CustomerContactRepository customerContactRepository;

    @Autowired
    CustomerDocumentRepository customerDocumentRepository;

    @Autowired
    CustomerLimitRepository customerLimitRepository;

    @Override
    public List<CustomerEntity> getCustomerList(String searchKey) {
        log.info("Getting Customer List ");
        return customerRepository.getCustomerList(searchKey);
    }

    @Override
    public Page<CustomerEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.customerRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public CustomerEntity save(CustomerDto customerDto) throws Exception {
        return customerRepository.save(convertToEntity(customerDto));
    }

    private CustomerEntity convertToEntity(CustomerDto customerDto) {
        CustomerEntity entity = new CustomerEntity();
        BeanUtils.copyProperties(customerDto, entity);
        entity.setSortSequence(customerDto.getSortSequence() == null ? 0 : customerDto.getSortSequence());
        entity.setIsLocked(customerDto.getIsLocked() == null ? 0 : customerDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(customerDto.getIsDeleted() == null ? 0 : customerDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(CustomerDto customerDto) throws Exception {
        customerRepository.save(convertToEntity(customerDto));
    }


    @Override
    public CustomerEntity findById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(CustomerEntity entity) throws Exception {
        customerRepository.save(entity);
    }

    @Override
    public List<CustomerAddressEntity> findAllCustomerAddress(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<CustomerAddressEntity> pagedList = customerAddressRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public CustomerAddressEntity saveCustomerAddress(CustomerAddressDto customerAddressDto) throws Exception {
        return customerAddressRepository.save(convertCustomerAddressToEntity(customerAddressDto));
    }

    private CustomerAddressEntity convertCustomerAddressToEntity(CustomerAddressDto customerAddressDto) {
        CustomerAddressEntity entity = new CustomerAddressEntity();
        BeanUtils.copyProperties(customerAddressDto, entity);
        entity.setSortSequence(customerAddressDto.getSortSequence() == null ? 0 : customerAddressDto.getSortSequence());
        entity.setIsLocked(customerAddressDto.getIsLocked() == null ? 0 : customerAddressDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(customerAddressDto.getIsDeleted() == null ? 0 : customerAddressDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyCustomerAddress(CustomerAddressDto customerAddressDto) throws Exception {
        customerAddressRepository.save(convertCustomerAddressToEntity(customerAddressDto));
    }

    @Override
    public CustomerAddressEntity findCustomerAddressById(Integer id) {
        return customerAddressRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCustomerAddress(CustomerAddressEntity entity) throws Exception {
        customerAddressRepository.save(entity);
    }

    @Override
    public List<CustomerAddressEntity> findAddressByCustomerId(Integer customerId) {
        return customerAddressRepository.findAddressByCustomerId(customerId);
    }

    @Override
    public List<CustomerContactEntity> findAllCustomerContact(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<CustomerContactEntity> pagedList = customerContactRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public CustomerContactEntity saveCustomerContact(CustomerContactDto customerContactDto) throws Exception {
        return customerContactRepository.save(convertCustomerContactToEntity(customerContactDto));
    }

    private CustomerContactEntity convertCustomerContactToEntity(CustomerContactDto customerContactDto) {
        CustomerContactEntity entity = new CustomerContactEntity();
        BeanUtils.copyProperties(customerContactDto, entity);
        entity.setSortSequence(customerContactDto.getSortSequence() == null ? 0 : customerContactDto.getSortSequence());
        entity.setIsLocked(customerContactDto.getIsLocked() == null ? 0 : customerContactDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(customerContactDto.getIsDeleted() == null ? 0 : customerContactDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyCustomerContact(CustomerContactDto customerContactDto) throws Exception {
        customerContactRepository.save(convertCustomerContactToEntity(customerContactDto));
    }

    @Override
    public CustomerContactEntity findCustomerContactById(Integer id) {
        return customerContactRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCustomerContact(CustomerContactEntity entity) throws Exception {
        customerContactRepository.save(entity);
    }

    @Override
    public List<CustomerContactEntity> findContactsByCustomerId(Integer customerId) {
        return customerContactRepository.findContactsByCustomerId(customerId);
    }

    @Override
    public List<CustomerDocumentEntity> findAllCustomerDocument(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<CustomerDocumentEntity> pagedList = customerDocumentRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public CustomerDocumentEntity saveCustomerDocument(CustomerDocumentDto customerDocumentDto) throws Exception {
        return customerDocumentRepository.save(convertCustomerDocumentToEntity(customerDocumentDto));
    }

    private CustomerDocumentEntity convertCustomerDocumentToEntity(CustomerDocumentDto customerDocumentDto) {
        CustomerDocumentEntity entity = new CustomerDocumentEntity();
        BeanUtils.copyProperties(customerDocumentDto, entity);
        entity.setSortSequence(customerDocumentDto.getSortSequence() == null ? 0 : customerDocumentDto.getSortSequence());
        entity.setIsLocked(customerDocumentDto.getIsLocked() == null ? 0 : customerDocumentDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(customerDocumentDto.getIsDeleted() == null ? 0 : customerDocumentDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyCustomerDocument(CustomerDocumentDto customerDocumentDto) throws Exception {
        customerDocumentRepository.save(convertCustomerDocumentToEntity(customerDocumentDto));
    }

    @Override
    public CustomerDocumentEntity findCustomerDocumentById(Integer id) {
        return customerDocumentRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCustomerDocument(CustomerDocumentEntity entity) throws Exception {
        customerDocumentRepository.save(entity);
    }

    @Override
    public List<CustomerDocumentEntity> findDocumentsByCustomerId(Integer customerId) {
        return customerDocumentRepository.findDocumentsByCustomerId(customerId);
    }

    @Override
    public List<CustomerLimitEntity> findAllCustomerLimit(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<CustomerLimitEntity> pagedList = customerLimitRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public CustomerLimitEntity saveCustomerLimit(CustomerLimitDto customerLimitDto) throws Exception {
        return customerLimitRepository.save(convertCustomerLimitToEntity(customerLimitDto));
    }

    private CustomerLimitEntity convertCustomerLimitToEntity(CustomerLimitDto customerLimitDto) {
        CustomerLimitEntity entity = new CustomerLimitEntity();
        BeanUtils.copyProperties(customerLimitDto, entity);
        entity.setSortSequence(customerLimitDto.getSortSequence() == null ? 0 : customerLimitDto.getSortSequence());
        entity.setIsLocked(customerLimitDto.getIsLocked() == null ? 0 : customerLimitDto.getIsLocked());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(customerLimitDto.getIsDeleted() == null ? 0 : customerLimitDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyCustomerLimit(CustomerLimitDto customerLimitDto) throws Exception {
        customerLimitRepository.save(convertCustomerLimitToEntity(customerLimitDto));
    }

    @Override
    public CustomerLimitEntity findCustomerLimitById(Integer id) {
        return customerLimitRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCustomerLimit(CustomerLimitEntity entity) throws Exception {
        customerLimitRepository.save(entity);
    }

    @Override
    public List<CustomerLimitEntity> findLimitsByCustomerId(Integer customerId) {
        return customerLimitRepository.findLimitsByCustomerId(customerId);
    }

    public List<CustomerEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.customerRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void lock(List<CustomerEntity> entities) throws Exception {
        this.customerRepository.saveAll(entities);
    }

    @Override
    public void unlock(List<CustomerEntity> entities) throws Exception {
        this.customerRepository.saveAll(entities);
    }

    @Override
    public List<CustomerDocumentEntity> findAllDocumentByCustomerIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.customerDocumentRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    @Override
    public List<CustomerContactEntity> findAllContactByCustomerIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.customerContactRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    @Override
    public List<CustomerLimitEntity> findAllLimitByCustomerIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.customerLimitRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    @Override
    public List<CustomerAddressEntity> findAllAddressCustomerIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.customerAddressRepository.findAll(filterClauseAppender.getCustomInClassFilter("id", ids));
    }

    @Override
    public List<CustomerEntity> findCustomerByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.customerRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAll(List<CustomerDocumentEntity> documentEntities, List<CustomerContactEntity> contactEntities, List<CustomerLimitEntity> limitEntities, List<CustomerAddressEntity> addressEntities, List<CustomerEntity> customerEntities) throws Exception {
        this.customerDocumentRepository.saveAll(documentEntities);
        this.customerContactRepository.saveAll(contactEntities);
        this.customerLimitRepository.saveAll(limitEntities);
        this.customerAddressRepository.saveAll(addressEntities);
        this.customerRepository.saveAll(customerEntities);
    }
}
