package com.tekfilo.inventory.item.invoice.salesreturn.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceChargesDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceDetailDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceMainDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceRequestPayload;
import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceChargesEntity;
import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceDetailEntity;
import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceMainEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IItemSalesReturnInvoiceService {

    Page<ItemSalesReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    ItemSalesReturnInvoiceMainEntity save(ItemInvoiceMainDto invoiceMainDto) throws Exception;

    void modify(ItemInvoiceMainDto invoiceMainDto) throws Exception;

    ItemSalesReturnInvoiceMainEntity findById(Integer id);

    void remove(ItemSalesReturnInvoiceMainEntity entity);


    List<ItemSalesReturnInvoiceDetailEntity> findAllDetail(Integer invId);

    ItemSalesReturnInvoiceDetailEntity saveDetail(ItemInvoiceDetailDto invoiceDetailDto) throws Exception;

    void modifyDetail(ItemInvoiceDetailDto invoiceDetailDto) throws Exception;

    ItemSalesReturnInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(ItemSalesReturnInvoiceDetailEntity entity);


    List<ItemSalesReturnInvoiceChargesEntity> findAllICharges(Integer invId);

    ItemSalesReturnInvoiceChargesEntity saveCharges(ItemInvoiceChargesDto invoiceChargesDto) throws Exception;

    void modifyCharges(ItemInvoiceChargesDto invoiceChargesDto) throws Exception;

    ItemSalesReturnInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(ItemSalesReturnInvoiceChargesEntity entity);

    List<ItemSalesReturnInvoiceMainEntity> findMain();

    ItemSalesReturnInvoiceMainEntity createSalesInvoice(ItemInvoiceRequestPayload salesInvoicePayload) throws Exception;

    List<ItemEntity> getProductList(String searchKey);

    void saveAllDetail(List<ItemInvoiceDetailDto> invoiceDetailDtoList) throws Exception;

    void changeStatus(Integer id) throws Exception;

    List<ItemSalesReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    void removeAll(ItemSalesReturnInvoiceMainEntity entity, List<ItemSalesReturnInvoiceDetailEntity> detailEntities, List<ItemSalesReturnInvoiceChargesEntity> chargesEntities) throws Exception;

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<ItemSalesReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<ItemSalesReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<ItemSalesReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<ItemSalesReturnInvoiceMainEntity> entities) throws Exception;

    void unlock(List<ItemSalesReturnInvoiceMainEntity> entities) throws Exception;
}
