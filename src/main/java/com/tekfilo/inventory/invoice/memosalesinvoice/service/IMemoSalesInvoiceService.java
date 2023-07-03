package com.tekfilo.inventory.invoice.memosalesinvoice.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.common.InvoiceChargesDto;
import com.tekfilo.inventory.invoice.common.InvoiceDetailDto;
import com.tekfilo.inventory.invoice.common.InvoiceMainDto;
import com.tekfilo.inventory.invoice.common.InvoiceRequestPayload;
import com.tekfilo.inventory.invoice.memosalesinvoice.entity.MemoSalesInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.memosalesinvoice.entity.MemoSalesInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.memosalesinvoice.entity.MemoSalesInvoiceMainEntity;
import com.tekfilo.inventory.product.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IMemoSalesInvoiceService {

    Page<MemoSalesInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    MemoSalesInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception;

    void modify(InvoiceMainDto invoiceMainDto) throws Exception;

    MemoSalesInvoiceMainEntity findById(Integer id);

    void remove(MemoSalesInvoiceMainEntity entity);


    List<MemoSalesInvoiceDetailEntity> findAllDetail(Integer invId);

    MemoSalesInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    MemoSalesInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(MemoSalesInvoiceDetailEntity entity);


    List<MemoSalesInvoiceChargesEntity> findAllICharges(Integer invId);

    MemoSalesInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    MemoSalesInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(MemoSalesInvoiceChargesEntity entity);

    List<MemoSalesInvoiceMainEntity> findMain();

    MemoSalesInvoiceMainEntity createSalesInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    @Deprecated
    String confirmSales(Integer memoInvoiceId) throws Exception;

    @Deprecated
    void returnMemoSales(Integer memoInvoiceId) throws Exception;

    @Deprecated
    String confirmPartialSales(Integer memoInvoiceMainId, List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception;

    @Deprecated
    void partialReturnMemoSales(Integer memoInvoiceId, List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception;

    List<MemoSalesInvoiceDetailEntity> findDetailByIdWithStock(Integer salesInvoiceId);

    List<MemoSalesInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency);

    List<MemoSalesInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId);

    void changeStatus(Integer id) throws Exception;

    void removeAll(MemoSalesInvoiceMainEntity entity, List<MemoSalesInvoiceDetailEntity> detailEntities, List<MemoSalesInvoiceChargesEntity> chargesEntities) throws Exception;

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<MemoSalesInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<MemoSalesInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<MemoSalesInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<MemoSalesInvoiceMainEntity> entities) throws Exception;

    void unlock(List<MemoSalesInvoiceMainEntity> entities) throws Exception;
}
