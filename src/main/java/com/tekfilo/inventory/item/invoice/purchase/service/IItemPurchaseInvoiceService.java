package com.tekfilo.inventory.item.invoice.purchase.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceChargesDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceDetailDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceMainDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceRequestPayload;
import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceChargesEntity;
import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceDetailEntity;
import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceMainEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IItemPurchaseInvoiceService {

    Page<ItemPurchaseInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    ItemPurchaseInvoiceMainEntity save(ItemInvoiceMainDto itemInvoiceMainDto) throws Exception;

    void modify(ItemInvoiceMainDto itemInvoiceMainDto) throws Exception;

    ItemPurchaseInvoiceMainEntity findById(Integer id);

    void remove(ItemPurchaseInvoiceMainEntity entity);


    List<ItemPurchaseInvoiceDetailEntity> findAllDetail(Integer invId);

    ItemPurchaseInvoiceDetailEntity saveDetail(ItemInvoiceDetailDto itemInvoiceDetailDto) throws Exception;

    void modifyDetail(ItemInvoiceDetailDto itemInvoiceDetailDto) throws Exception;

    ItemPurchaseInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(ItemPurchaseInvoiceDetailEntity entity);


    List<ItemPurchaseInvoiceChargesEntity> findAllICharges(Integer invId);

    ItemPurchaseInvoiceChargesEntity saveCharges(ItemInvoiceChargesDto itemInvoiceChargesDto) throws Exception;

    void modifyCharges(ItemInvoiceChargesDto itemInvoiceChargesDto) throws Exception;

    ItemPurchaseInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(ItemPurchaseInvoiceChargesEntity entity);

    List<ItemPurchaseInvoiceMainEntity> findMain();

    ItemPurchaseInvoiceMainEntity createPurchaseInvoice(ItemInvoiceRequestPayload itemInvoiceRequestPayload) throws Exception;

    List<ItemEntity> getProductList(String searchKey);

    List<ItemPurchaseInvoiceDetailEntity> findDetailByProductId(Integer productId);

    void changeStatus(Integer id) throws Exception;

    List<ItemPurchaseInvoiceDetailEntity> findDetailByIdWithStock(Integer purchaseInvoiceId);

    List<ItemPurchaseInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency);

    List<ItemPurchaseInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId);

    void removeAll(ItemPurchaseInvoiceMainEntity entity, List<ItemPurchaseInvoiceDetailEntity> detailEntities, List<ItemPurchaseInvoiceChargesEntity> chargesEntities) throws Exception;

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<ItemPurchaseInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<ItemPurchaseInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<ItemPurchaseInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<ItemPurchaseInvoiceMainEntity> entities) throws Exception;

    void unlock(List<ItemPurchaseInvoiceMainEntity> entities) throws Exception;

    List<ItemPurchaseInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);
}
