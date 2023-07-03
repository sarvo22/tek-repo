package com.tekfilo.inventory.item.invoice.salesinvoice.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceChargesDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceDetailDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceMainDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceRequestPayload;
import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceChargesEntity;
import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceDetailEntity;
import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceMainEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IItemSalesInvoiceService {

    Page<ItemSalesInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    ItemSalesInvoiceMainEntity save(ItemInvoiceMainDto invoiceMainDto) throws Exception;

    void modify(ItemInvoiceMainDto invoiceMainDto) throws Exception;

    ItemSalesInvoiceMainEntity findById(Integer id);

    void remove(ItemSalesInvoiceMainEntity entity);


    List<ItemSalesInvoiceDetailEntity> findAllDetail(Integer invId);

    ItemSalesInvoiceDetailEntity saveDetail(ItemInvoiceDetailDto invoiceDetailDto) throws Exception;

    void modifyDetail(ItemInvoiceDetailDto invoiceDetailDto) throws Exception;

    ItemSalesInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(ItemSalesInvoiceDetailEntity entity);


    List<ItemSalesInvoiceChargesEntity> findAllICharges(Integer invId);

    ItemSalesInvoiceChargesEntity saveCharges(ItemInvoiceChargesDto invoiceChargesDto) throws Exception;

    void modifyCharges(ItemInvoiceChargesDto invoiceChargesDto) throws Exception;

    ItemSalesInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(ItemSalesInvoiceChargesEntity entity);

    List<ItemSalesInvoiceMainEntity> findMain();

    ItemSalesInvoiceMainEntity createSalesInvoice(ItemInvoiceRequestPayload salesInvoicePayload) throws Exception;

    List<ItemEntity> getProductList(String searchKey);

    List<ItemSalesInvoiceDetailEntity> findDetailByProductId(Integer productId);

    void changeStatus(Integer id) throws Exception;

    List<ItemSalesInvoiceDetailEntity> findDetailByIdWithStock(Integer salesInvoiceId);

    List<ItemSalesInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency);

    List<ItemSalesInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId);

    void removeAll(ItemSalesInvoiceMainEntity entity, List<ItemSalesInvoiceDetailEntity> detailEntities, List<ItemSalesInvoiceChargesEntity> chargesEntities) throws Exception;

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<ItemSalesInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<ItemSalesInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<ItemSalesInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<ItemSalesInvoiceMainEntity> entities) throws Exception;

    void unlock(List<ItemSalesInvoiceMainEntity> entities) throws Exception;

    List<ItemSalesInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);
}
