package com.tekfilo.admin.customer.service;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.customer.dto.*;
import com.tekfilo.admin.customer.entity.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICustomerService {

    List<CustomerEntity> getCustomerList(String searchKey);

    Page<CustomerEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    CustomerEntity save(CustomerDto commodityDto) throws Exception;

    void modify(CustomerDto commodityDto) throws Exception;

    CustomerEntity findById(Integer id);

    void remove(CustomerEntity entity) throws Exception;


    List<CustomerAddressEntity> findAllCustomerAddress(int pageNo, int pageSize, String sortName, String sortDirection);

    CustomerAddressEntity saveCustomerAddress(CustomerAddressDto customerAddressDto) throws Exception;

    void modifyCustomerAddress(CustomerAddressDto customerAddressDto) throws Exception;

    CustomerAddressEntity findCustomerAddressById(Integer id);

    void removeCustomerAddress(CustomerAddressEntity entity) throws Exception;

    List<CustomerAddressEntity> findAddressByCustomerId(Integer id);


    List<CustomerContactEntity> findAllCustomerContact(int pageNo, int pageSize, String sortName, String sortDirection);

    CustomerContactEntity saveCustomerContact(CustomerContactDto customerContactDto) throws Exception;

    void modifyCustomerContact(CustomerContactDto customerContactDto) throws Exception;

    CustomerContactEntity findCustomerContactById(Integer customerId);

    void removeCustomerContact(CustomerContactEntity entity) throws Exception;

    List<CustomerContactEntity> findContactsByCustomerId(Integer customerId);


    List<CustomerDocumentEntity> findAllCustomerDocument(int pageNo, int pageSize, String sortName, String sortDirection);

    CustomerDocumentEntity saveCustomerDocument(CustomerDocumentDto customerDocumentDto) throws Exception;

    void modifyCustomerDocument(CustomerDocumentDto customerDocumentDto) throws Exception;

    CustomerDocumentEntity findCustomerDocumentById(Integer id);

    void removeCustomerDocument(CustomerDocumentEntity entity) throws Exception;

    List<CustomerDocumentEntity> findDocumentsByCustomerId(Integer customerId);

    List<CustomerLimitEntity> findAllCustomerLimit(int pageNo, int pageSize, String sortName, String sortDirection);

    CustomerLimitEntity saveCustomerLimit(CustomerLimitDto customerLimitDto) throws Exception;

    void modifyCustomerLimit(CustomerLimitDto customerLimitDto) throws Exception;

    CustomerLimitEntity findCustomerLimitById(Integer id);

    void removeCustomerLimit(CustomerLimitEntity entity) throws Exception;

    List<CustomerLimitEntity> findLimitsByCustomerId(Integer customerId);

    List<CustomerEntity> findAllEntitiesByIds(List<Integer> ids);

    void lock(List<CustomerEntity> entities) throws Exception;

    void unlock(List<CustomerEntity> entities) throws Exception;

    List<CustomerDocumentEntity> findAllDocumentByCustomerIds(List<Integer> ids);

    List<CustomerContactEntity> findAllContactByCustomerIds(List<Integer> ids);

    List<CustomerLimitEntity> findAllLimitByCustomerIds(List<Integer> ids);

    List<CustomerAddressEntity> findAllAddressCustomerIds(List<Integer> ids);

    List<CustomerEntity> findCustomerByIds(List<Integer> ids);

    void removeAll(List<CustomerDocumentEntity> documentEntities, List<CustomerContactEntity> contactEntities, List<CustomerLimitEntity> limitEntities, List<CustomerAddressEntity> addressEntities, List<CustomerEntity> customerEntities) throws Exception;
}
