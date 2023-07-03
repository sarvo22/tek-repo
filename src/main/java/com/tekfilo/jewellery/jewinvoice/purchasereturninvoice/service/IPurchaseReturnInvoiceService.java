package com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceMainEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPurchaseReturnInvoiceService {

    Page<PurchaseReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    PurchaseReturnInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception;

    void modify(InvoiceMainDto invoiceMainDto) throws Exception;

    PurchaseReturnInvoiceMainEntity findById(Integer id);

    void remove(PurchaseReturnInvoiceMainEntity entity);


    List<PurchaseReturnInvoiceDetailEntity> findAllDetail(Integer invId);

    PurchaseReturnInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    PurchaseReturnInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(PurchaseReturnInvoiceDetailEntity entity);


    List<PurchaseReturnInvoiceChargesEntity> findAllICharges(Integer invId);

    PurchaseReturnInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    PurchaseReturnInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(PurchaseReturnInvoiceChargesEntity entity);

    List<PurchaseReturnInvoiceMainEntity> findMain();

    PurchaseReturnInvoiceMainEntity createPurchaseInvoice(InvoiceRequestPayload salesInvoicePayload) throws Exception;

    void saveAllDetail(List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    void changeStatus(Integer id) throws Exception;

    List<PurchaseReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    void removeAll(PurchaseReturnInvoiceMainEntity entity, List<PurchaseReturnInvoiceDetailEntity> detailEntities, List<PurchaseReturnInvoiceChargesEntity> chargesEntities) throws Exception;

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<PurchaseReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<PurchaseReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<PurchaseReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<PurchaseReturnInvoiceMainEntity> entities) throws Exception;

    void unlock(List<PurchaseReturnInvoiceMainEntity> entities) throws Exception;
}
