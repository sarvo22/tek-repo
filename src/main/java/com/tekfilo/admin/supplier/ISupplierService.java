package com.tekfilo.admin.supplier;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.supplier.dto.*;
import com.tekfilo.admin.supplier.entity.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISupplierService {

    List<SupplierEntity> getSupplierList(String searchKey);

    Page<SupplierEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    SupplierEntity save(SupplierDto supplierDto) throws Exception;

    void modify(SupplierDto supplierDto) throws Exception;

    SupplierEntity findById(Integer id);

    void remove(SupplierEntity entity) throws Exception;


    List<SupplierAddressEntity> findAllSupplierAddress(int pageNo, int pageSize, String sortName, String sortDirection);

    SupplierAddressEntity saveSupplierAddress(SupplierAddressDto supplierAddressDto) throws Exception;

    void modifySupplierAddress(SupplierAddressDto supplierAddressDto) throws Exception;

    SupplierAddressEntity findSupplierAddressById(Integer id);

    void removeSupplierAddress(SupplierAddressEntity entity) throws Exception;

    List<SupplierAddressEntity> findAddressBySupplierId(Integer id);


    List<SupplierContactEntity> findAllSupplierContact(int pageNo, int pageSize, String sortName, String sortDirection);

    SupplierContactEntity saveSupplierContact(SupplierContactDto supplierContactDto) throws Exception;

    void modifySupplierContact(SupplierContactDto supplierContactDto) throws Exception;

    SupplierContactEntity findSupplierContactById(Integer supplierId);

    void removeSupplierContact(SupplierContactEntity entity) throws Exception;

    List<SupplierContactEntity> findContactsBySupplierId(Integer supplierId);


    List<SupplierDocumentEntity> findAllSupplierDocument(int pageNo, int pageSize, String sortName, String sortDirection);

    SupplierDocumentEntity saveSupplierDocument(SupplierDocumentDto supplierDocumentDto) throws Exception;

    void modifySupplierDocument(SupplierDocumentDto supplierDocumentDto) throws Exception;

    SupplierDocumentEntity findSupplierDocumentById(Integer id);

    void removeSupplierDocument(SupplierDocumentEntity entity) throws Exception;

    List<SupplierDocumentEntity> findDocumentsBySupplierId(Integer supplierId);

    List<SupplierLimitEntity> findAllSupplierLimit(int pageNo, int pageSize, String sortName, String sortDirection);

    SupplierLimitEntity saveSupplierLimit(SupplierLimitDto supplierLimitDto) throws Exception;

    void modifySupplierLimit(SupplierLimitDto supplierLimitDto) throws Exception;

    SupplierLimitEntity findSupplierLimitById(Integer id);

    void removeSupplierLimit(SupplierLimitEntity entity) throws Exception;

    List<SupplierLimitEntity> findLimitsBySupplierId(Integer supplierId);

    void removeAll(List<Integer> ids) throws Exception;

    List<SupplierEntity> findAllEntitiesByIds(List<Integer> ids);

    void lock(List<SupplierEntity> entities) throws Exception;

    void unlock(List<SupplierEntity> entities) throws Exception;
}
