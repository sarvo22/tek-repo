package com.tekfilo.admin.factory;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.factory.dto.*;
import com.tekfilo.admin.factory.entity.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IFactoryService {

    List<FactoryEntity> getFactoryList(String searchKey);

    Page<FactoryEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    FactoryEntity save(FactoryDto factoryDto) throws Exception;

    void modify(FactoryDto factoryDto) throws Exception;

    FactoryEntity findById(Integer id);

    void remove(FactoryEntity entity) throws Exception;


    List<FactoryAddressEntity> findAllFactoryAddress(int pageNo, int pageSize, String sortName, String sortDirection);

    FactoryAddressEntity saveFactoryAddress(FactoryAddressDto factoryAddressDto) throws Exception;

    void modifyFactoryAddress(FactoryAddressDto factoryAddressDto) throws Exception;

    FactoryAddressEntity findFactoryAddressById(Integer id);

    void removeFactoryAddress(FactoryAddressEntity entity) throws Exception;

    List<FactoryAddressEntity> findAddressByFactoryId(Integer id);


    List<FactoryContactEntity> findAllFactoryContact(int pageNo, int pageSize, String sortName, String sortDirection);

    FactoryContactEntity saveFactoryContact(FactoryContactDto factoryContactDto) throws Exception;

    void modifyFactoryContact(FactoryContactDto factoryContactDto) throws Exception;

    FactoryContactEntity findFactoryContactById(Integer factoryId);

    void removeFactoryContact(FactoryContactEntity entity) throws Exception;

    List<FactoryContactEntity> findContactsByFactoryId(Integer factoryId);


    List<FactoryDocumentEntity> findAllFactoryDocument(int pageNo, int pageSize, String sortName, String sortDirection);

    FactoryDocumentEntity saveFactoryDocument(FactoryDocumentDto factoryDocumentDto) throws Exception;

    void modifyFactoryDocument(FactoryDocumentDto factoryDocumentDto) throws Exception;

    FactoryDocumentEntity findFactoryDocumentById(Integer id);

    void removeFactoryDocument(FactoryDocumentEntity entity) throws Exception;

    List<FactoryDocumentEntity> findDocumentsByFactoryId(Integer factoryId);

    List<FactoryLimitEntity> findAllFactoryLimit(int pageNo, int pageSize, String sortName, String sortDirection);

    FactoryLimitEntity saveFactoryLimit(FactoryLimitDto factoryLimitDto) throws Exception;

    void modifyFactoryLimit(FactoryLimitDto factoryLimitDto) throws Exception;

    FactoryLimitEntity findFactoryLimitById(Integer id);

    void removeFactoryLimit(FactoryLimitEntity entity) throws Exception;

    List<FactoryLimitEntity> findLimitsByFactoryId(Integer factoryId);

    void removeAll(List<Integer> ids) throws Exception;

    List<FactoryEntity> findAllEntitiesByIds(List<Integer> ids);

    void lock(List<FactoryEntity> entities) throws Exception;

    void unlock(List<FactoryEntity> entities) throws Exception;
}
