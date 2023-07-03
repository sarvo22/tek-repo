package com.tekfilo.jewellery.order.sales.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderMainEntity;
import com.tekfilo.jewellery.order.sales.dto.SalesOrderDetailDto;
import com.tekfilo.jewellery.order.sales.dto.SalesOrderMainDto;
import com.tekfilo.jewellery.order.sales.entity.SalesOrderDetailEntity;
import com.tekfilo.jewellery.order.sales.entity.SalesOrderMainEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISalesOrderService {

    Page<SalesOrderMainEntity> findAllSalesOrders(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    SalesOrderMainEntity findSalesOrderById(Integer id);

    List<SalesOrderDetailEntity> findSalesOrderDetailListByMain(Integer salesOrderId);

    Integer saveSalesOrderMain(SalesOrderMainDto salesOrderMainDto) throws Exception;

    void modifySalesOrderMain(SalesOrderMainDto salesOrderMainDto) throws Exception;

    void removeSalesOrderMain(SalesOrderMainDto salesOrderMainDto) throws Exception;

    void saveSalesOrderDetail(SalesOrderDetailDto salesOrderDetailDto) throws Exception;

    void modifySalesOrderDetail(SalesOrderDetailDto salesOrderDetailDto) throws Exception;

    void removeSalesOrderDetail(Integer salesOrderDetailId, Integer operateBy) throws Exception;

    void removeSalesOrderDetailByMain(Integer salesOrderId, Integer operateBy) throws Exception;

    void removeSalesOrderMainById(Integer salesOrderId, Integer operateBy) throws Exception;

    void removeSalesOrder(Integer salesOrderId, Integer operateBy) throws Exception;

    List<SalesOrderMainEntity> findMainListByIds(List<Integer> ids);

    void lock(List<SalesOrderMainEntity> entities) throws Exception;

    void unlock(List<SalesOrderMainEntity> entities) throws Exception;
}
