package com.tekfilo.jewellery.jewinvoice.purchaseinvoice.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPurchaseInvoiceService {

    Page<PurchaseInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    PurchaseInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception;

    void modify(InvoiceMainDto invoiceMainDto) throws Exception;

    PurchaseInvoiceMainEntity findById(Integer id);

    void remove(PurchaseInvoiceMainEntity entity);


    List<PurchaseInvoiceDetailEntity> findAllDetail(Integer invId);

    PurchaseInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    PurchaseInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(PurchaseInvoiceDetailEntity entity);


    List<PurchaseInvoiceChargesEntity> findAllICharges(Integer invId);

    PurchaseInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    PurchaseInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(PurchaseInvoiceChargesEntity entity);

    List<PurchaseInvoiceMainEntity> findMain();

    PurchaseInvoiceMainEntity createPurchaseInvoice(InvoiceRequestPayload salesInvoicePayload) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    List<PurchaseInvoiceDetailEntity> findDetailByProductId(Integer productId);

    void changeStatus(Integer id) throws Exception;

    List<PurchaseInvoiceDetailEntity> findDetailByIdWithStock(Integer purchaseInvoiceId);

    List<PurchaseInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency);

    List<PurchaseInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId);

    void removeAll(PurchaseInvoiceMainEntity entity, List<PurchaseInvoiceDetailEntity> detailEntities, List<PurchaseInvoiceChargesEntity> chargesEntities) throws Exception;

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<PurchaseInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<PurchaseInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<PurchaseInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<PurchaseInvoiceMainEntity> entities) throws Exception;

    void unlock(List<PurchaseInvoiceMainEntity> entities) throws Exception;

    List<PurchaseInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);
}
