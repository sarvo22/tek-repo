package com.tekfilo.jewellery.order.purchase.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.order.purchase.dto.*;
import com.tekfilo.jewellery.order.purchase.entity.*;
import org.springframework.dao.DataIntegrityViolationException;
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

    List<PurchaseOrderMainEntity> findMainListByIds(List<Integer> ids);

    void lock(List<PurchaseOrderMainEntity> entities) throws Exception;

    void unlock(List<PurchaseOrderMainEntity> entities) throws Exception;


    List<PurchaseOrderDetailLabourEntity> findPurchaseOrderDetailLabourList(Integer purchaseOrderDetailId);

    PurchaseOrderDetailLabourEntity savePurchaseOrderDetailLabour(PurchaseOrderDetailLabourDto purchaseOrderDetailLabourDto) throws Exception;

    void deletePurchaseOrderDetailLabour(PurchaseOrderDetailLabourEntity entity) throws Exception;

    PurchaseOrderDetailLabourEntity findPurchaseOrderDetailLabourById(Integer id);

    List<PurchaseOrderDetailFindingEntity> findPurchaseOrderDetailFindingList(Integer purchaseOrderDetailId);

    PurchaseOrderDetailFindingEntity savePurchaseOrderDetailFinding(PurchaseOrderDetailFindingDto purchaseOrderDetailFindingDto) throws Exception;

    void deletePurchaseOrderDetailFinding(PurchaseOrderDetailFindingEntity entity) throws Exception;

    PurchaseOrderDetailFindingEntity findPurchaseOrderDetailFindingById(Integer id);

    List<PurchaseOrderDetailMouldPartEntity> findPurchaseOrderDetailMouldPartList(Integer purchaseOrderDetailId);

    PurchaseOrderDetailMouldPartEntity savePurchaseOrderDetailMouldPart(PurchaseOrderDetailMouldPartDto purchaseOrderDetailMouldPartDto) throws Exception;

    void deletePurchaseOrderDetailMouldPart(PurchaseOrderDetailMouldPartEntity entity) throws Exception;

    PurchaseOrderDetailMouldPartEntity findPurchaseOrderDetailMouldPartById(Integer id);

    List<PurchaseOrderDetailWaxEntity> findPurchaseOrderDetailWaxList(Integer purchaseOrderDetailId);

    PurchaseOrderDetailWaxEntity savePurchaseOrderDetailWax(PurchaseOrderDetailWaxDto purchaseOrderDetailWaxDto) throws Exception;

    void deletePurchaseOrderDetailWax(PurchaseOrderDetailWaxEntity entity) throws Exception;

    PurchaseOrderDetailWaxEntity findPurchaseOrderDetailWaxById(Integer id);

    PurchaseOrderComponentEntity savePurchaseOrderComponent(PurchaseOrderComponentDto purchaseOrderComponentDto);

    List<PurchaseOrderComponentEntity> findPurchaseOrderComponentByDetailId(Integer id);

    void deleteAll(List<Integer> ids) throws DataIntegrityViolationException;
}
