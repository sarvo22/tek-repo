package com.tekfilo.jewellery.goodsinward.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardChargesDto;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardDetailDto;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardMainDto;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardRequestPayload;
import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardChargesEntity;
import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardDetailEntity;
import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardMainEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IGoodsInwardService {

    Page<GoodsInwardMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    GoodsInwardMainEntity save(GoodsInwardMainDto goodsInwardMainDto) throws Exception;

    void modify(GoodsInwardMainDto goodsInwardMainDto) throws Exception;

    GoodsInwardMainEntity findById(Integer id);

    void remove(GoodsInwardMainEntity entity);


    List<GoodsInwardDetailEntity> findAllDetail(Integer invId);

    GoodsInwardDetailEntity saveDetail(GoodsInwardDetailDto goodsInwardDetailDto) throws Exception;

    void modifyDetail(GoodsInwardDetailDto goodsInwardDetailDto) throws Exception;

    GoodsInwardDetailEntity findDetailById(Integer id);

    void removeDetail(GoodsInwardDetailEntity entity);


    List<GoodsInwardChargesEntity> findAllICharges(Integer invId);

    GoodsInwardChargesEntity saveCharges(GoodsInwardChargesDto goodsInwardChargesDto) throws Exception;

    void modifyCharges(GoodsInwardChargesDto goodsInwardChargesDto) throws Exception;

    GoodsInwardChargesEntity findChargesById(Integer id);

    void removeCharges(GoodsInwardChargesEntity entity);

    List<GoodsInwardMainEntity> findMain();

    GoodsInwardMainEntity createInvoice(GoodsInwardRequestPayload salesInvoicePayload) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    List<GoodsInwardMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<GoodsInwardDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<GoodsInwardChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    void lock(List<GoodsInwardMainEntity> entities) throws Exception;
    void unlock(List<GoodsInwardMainEntity> entities) throws Exception;
}
