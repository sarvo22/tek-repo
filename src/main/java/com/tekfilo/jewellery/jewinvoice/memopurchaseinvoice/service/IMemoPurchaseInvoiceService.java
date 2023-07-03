package com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceMainEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import org.springframework.data.domain.Page;

import java.sql.SQLException;
import java.util.List;

public interface IMemoPurchaseInvoiceService {

    Page<MemoPurchaseInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    MemoPurchaseInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception;

    void modify(InvoiceMainDto invoiceMainDto) throws Exception;

    MemoPurchaseInvoiceMainEntity findById(Integer id);

    void remove(MemoPurchaseInvoiceMainEntity entity);


    List<MemoPurchaseInvoiceDetailEntity> findAllDetail(Integer invId);

    MemoPurchaseInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    MemoPurchaseInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(MemoPurchaseInvoiceDetailEntity entity) throws SQLException;


    List<MemoPurchaseInvoiceChargesEntity> findAllICharges(Integer invId);

    MemoPurchaseInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    MemoPurchaseInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(MemoPurchaseInvoiceChargesEntity entity) throws SQLException;

    List<MemoPurchaseInvoiceMainEntity> findMain();

    MemoPurchaseInvoiceMainEntity createPurchaseInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    List<MemoPurchaseInvoiceDetailEntity> findDetailByIdWithStock(Integer salesInvoiceId);

    List<MemoPurchaseInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency);

    List<MemoPurchaseInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId);

    void changeStatus(Integer id) throws Exception;

    void removeAll(MemoPurchaseInvoiceMainEntity entity, List<MemoPurchaseInvoiceDetailEntity> detailEntities, List<MemoPurchaseInvoiceChargesEntity> chargesEntities) throws Exception;

    List<MemoPurchaseInvoiceMainEntity> findAllMainByMainIds(List<Integer> ids);

    List<MemoPurchaseInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> ids);

    List<MemoPurchaseInvoiceChargesEntity> findAllIChargesByMainIds(List<Integer> ids);

    void removeAll(List<MemoPurchaseInvoiceMainEntity> entityList, List<MemoPurchaseInvoiceDetailEntity> detailEntities, List<MemoPurchaseInvoiceChargesEntity> chargesEntities) throws Exception;

    void lock(List<MemoPurchaseInvoiceMainEntity> entities) throws SQLException;

    void unlock(List<MemoPurchaseInvoiceMainEntity> entities) throws SQLException;
}
