package com.tekfilo.inventory.item.invoice.purchasereturn.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceChargesDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceDetailDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceMainDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceRequestPayload;
import com.tekfilo.inventory.item.invoice.purchasereturn.entity.ItemPurchaseReturnInvoiceChargesEntity;
import com.tekfilo.inventory.item.invoice.purchasereturn.entity.ItemPurchaseReturnInvoiceDetailEntity;
import com.tekfilo.inventory.item.invoice.purchasereturn.entity.ItemPurchaseReturnInvoiceMainEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IItemPurchaseReturnInvoiceService {

    Page<ItemPurchaseReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    ItemPurchaseReturnInvoiceMainEntity save(ItemInvoiceMainDto itemInvoiceMainDto) throws Exception;

    void modify(ItemInvoiceMainDto itemInvoiceMainDto) throws Exception;

    ItemPurchaseReturnInvoiceMainEntity findById(Integer id);

    void remove(ItemPurchaseReturnInvoiceMainEntity entity);


    List<ItemPurchaseReturnInvoiceDetailEntity> findAllDetail(Integer invId);

    ItemPurchaseReturnInvoiceDetailEntity saveDetail(ItemInvoiceDetailDto itemInvoiceDetailDto) throws Exception;

    void modifyDetail(ItemInvoiceDetailDto itemInvoiceDetailDto) throws Exception;

    ItemPurchaseReturnInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(ItemPurchaseReturnInvoiceDetailEntity entity);


    List<ItemPurchaseReturnInvoiceChargesEntity> findAllICharges(Integer invId);

    ItemPurchaseReturnInvoiceChargesEntity saveCharges(ItemInvoiceChargesDto itemInvoiceChargesDto) throws Exception;

    void modifyCharges(ItemInvoiceChargesDto itemInvoiceChargesDto) throws Exception;

    ItemPurchaseReturnInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(ItemPurchaseReturnInvoiceChargesEntity entity);

    List<ItemPurchaseReturnInvoiceMainEntity> findMain();

    ItemPurchaseReturnInvoiceMainEntity createPurchaseInvoice(ItemInvoiceRequestPayload itemInvoiceRequestPayload) throws Exception;

    void saveAllDetail(List<ItemInvoiceDetailDto> itemInvoiceDetailDtoList) throws Exception;

    List<ItemEntity> getProductList(String searchKey);

    void changeStatus(Integer id) throws Exception;

    List<ItemPurchaseReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    void removeAll(ItemPurchaseReturnInvoiceMainEntity entity, List<ItemPurchaseReturnInvoiceDetailEntity> detailEntities, List<ItemPurchaseReturnInvoiceChargesEntity> chargesEntities) throws Exception;

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<ItemPurchaseReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<ItemPurchaseReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<ItemPurchaseReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<ItemPurchaseReturnInvoiceMainEntity> entities) throws Exception;

    void unlock(List<ItemPurchaseReturnInvoiceMainEntity> entities) throws Exception;
}
