package com.tekfilo.inventory.invoice.memopurchasereturninvoice.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.common.InvoiceChargesDto;
import com.tekfilo.inventory.invoice.common.InvoiceDetailDto;
import com.tekfilo.inventory.invoice.common.InvoiceMainDto;
import com.tekfilo.inventory.invoice.common.InvoiceRequestPayload;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceMainEntity;
import com.tekfilo.inventory.product.ProductEntity;
import org.springframework.data.domain.Page;

import java.sql.SQLException;
import java.util.List;

public interface IMemoPurchaseReturnInvoiceService {

    Page<MemoPurchaseReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    MemoPurchaseReturnInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception;

    void modify(InvoiceMainDto invoiceMainDto) throws Exception;

    MemoPurchaseReturnInvoiceMainEntity findById(Integer id);

    void remove(MemoPurchaseReturnInvoiceMainEntity entity);


    List<MemoPurchaseReturnInvoiceDetailEntity> findAllDetail(Integer invId);

    MemoPurchaseReturnInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    MemoPurchaseReturnInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(MemoPurchaseReturnInvoiceDetailEntity entity) throws SQLException;


    List<MemoPurchaseReturnInvoiceChargesEntity> findAllICharges(Integer invId);

    MemoPurchaseReturnInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    MemoPurchaseReturnInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(MemoPurchaseReturnInvoiceChargesEntity entity) throws SQLException;

    List<MemoPurchaseReturnInvoiceMainEntity> findMain();

    MemoPurchaseReturnInvoiceMainEntity createMemoPurchaseReturnInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    void saveAllDetail(List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception;

    void changeStatus(Integer id) throws Exception;

    void removeAll(MemoPurchaseReturnInvoiceMainEntity entity, List<MemoPurchaseReturnInvoiceDetailEntity> detailEntities, List<MemoPurchaseReturnInvoiceChargesEntity> chargesEntities) throws Exception;

    List<MemoPurchaseReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<MemoPurchaseReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<MemoPurchaseReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<MemoPurchaseReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<MemoPurchaseReturnInvoiceMainEntity> entities) throws Exception;

    void unlock(List<MemoPurchaseReturnInvoiceMainEntity> entities) throws Exception;
}
