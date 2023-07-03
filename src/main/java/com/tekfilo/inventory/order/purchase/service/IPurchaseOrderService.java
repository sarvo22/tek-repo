package com.tekfilo.inventory.order.purchase.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.order.purchase.dto.PurchaseOrderDetailDto;
import com.tekfilo.inventory.order.purchase.dto.PurchaseOrderMainDto;
import com.tekfilo.inventory.order.purchase.entity.PurchaseOrderDetailEntity;
import com.tekfilo.inventory.order.purchase.entity.PurchaseOrderMainEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPurchaseOrderService {

    Page<PurchaseOrderMainEntity> findAllPurchaseOrders(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    PurchaseOrderMainEntity findPurchaseOrderById(Integer id);

    List<PurchaseOrderDetailEntity> findPurchaseOrderDetailListByMain(Integer purchaseOrderId);

    Integer savePurchaseOrderMain(PurchaseOrderMainDto purchaseOrderMainDto) throws Exception;

    void modifyPurchaseOrderMain(PurchaseOrderMainDto purchaseOrderMainDto) throws Exception;

    void removePurchaseOrderMain(PurchaseOrderMainDto purchaseOrderMainDto) throws Exception;

    Integer savePurchaseOrderDetail(PurchaseOrderDetailDto purchaseOrderDetailDto) throws Exception;

    void modifyPurchaseOrderDetail(PurchaseOrderDetailDto purchaseOrderDetailDto) throws Exception;

    void removePurchaseOrderDetail(Integer purchaseOrderDetailId, Integer operateBy) throws Exception;

    void removePurchaseOrderDetailByMain(Integer purchaseOrderId, Integer operateBy) throws Exception;

    void removePurchaseOrderMainById(Integer purchaseOrderId, Integer operateBy) throws Exception;

    void removePurchaseOrder(Integer purchaseOrderId, Integer operateBy) throws Exception;

    PurchaseOrderDetailEntity findPurchaseOrderDetailById(Integer purchaseOrderDetailId);

    List<PurchaseOrderMainEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<PurchaseOrderMainEntity> entities) throws Exception;

    void lock(List<PurchaseOrderMainEntity> entities) throws Exception;

    void unlock(List<PurchaseOrderMainEntity> entities) throws Exception;
}
