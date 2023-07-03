package com.tekfilo.jewellery.goodsoutward.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardChargesDto;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardDetailDto;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardMainDto;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardRequestPayload;
import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardChargesEntity;
import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardDetailEntity;
import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardMainEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IGoodsOutwardService {

    Page<GoodsOutwardMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    GoodsOutwardMainEntity save(GoodsOutwardMainDto goodsOutwardMainDto) throws Exception;

    void modify(GoodsOutwardMainDto goodsOutwardMainDto) throws Exception;

    GoodsOutwardMainEntity findById(Integer id);

    void remove(GoodsOutwardMainEntity entity);


    List<GoodsOutwardDetailEntity> findAllDetail(Integer invId);

    GoodsOutwardDetailEntity saveDetail(GoodsOutwardDetailDto goodsOutwardDetailDto) throws Exception;

    void modifyDetail(GoodsOutwardDetailDto goodsOutwardDetailDto) throws Exception;

    GoodsOutwardDetailEntity findDetailById(Integer id);

    void removeDetail(GoodsOutwardDetailEntity entity);


    List<GoodsOutwardChargesEntity> findAllICharges(Integer invId);

    GoodsOutwardChargesEntity saveCharges(GoodsOutwardChargesDto goodsOutwardChargesDto) throws Exception;

    void modifyCharges(GoodsOutwardChargesDto goodsOutwardChargesDto) throws Exception;

    GoodsOutwardChargesEntity findChargesById(Integer id);

    void removeCharges(GoodsOutwardChargesEntity entity);

    List<GoodsOutwardMainEntity> findMain();

    GoodsOutwardMainEntity createInvoice(GoodsOutwardRequestPayload salesInvoicePayload) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    List<GoodsOutwardMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<GoodsOutwardDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<GoodsOutwardChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    void lock(List<GoodsOutwardMainEntity> entities) throws Exception;

    void unlock(List<GoodsOutwardMainEntity> entities) throws Exception;
}
