package com.tekfilo.jewellery.catalog.service;

import com.tekfilo.jewellery.catalog.dto.CatalogDto;
import com.tekfilo.jewellery.catalog.dto.CatalogResponse;
import com.tekfilo.jewellery.design.entity.DesignEntity;
import com.tekfilo.jewellery.design.repository.DesignRepository;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import com.tekfilo.jewellery.product.repository.ProductRepository;
import com.tekfilo.jewellery.sku.entity.SkuEntity;
import com.tekfilo.jewellery.sku.repository.SkuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CatalogService {

    @Autowired
    SkuRepository skuRepository;

    @Autowired
    DesignRepository designRepository;

    @Autowired
    ProductRepository productRepository;

    public List<CatalogResponse> getCatalog(CatalogDto catalogDto) {

        List<CatalogResponse> catalogResponseList = new ArrayList<>();
        switch (catalogDto.getCatalogFor()) {
            case "DESIGN":
                List<DesignEntity> designList = designRepository.findByDesignNos(catalogDto.getInputNos(), CompanyContext.getCurrentCompany());
                designList.forEach(designEntity -> {
                    CatalogResponse catalogResponse = new CatalogResponse();
                    catalogResponse.setProductName(designEntity.getDesignNo());
                    catalogResponse.setProductType(designEntity.getProductType() == null ? null : designEntity.getProductType().getProductTypeName());
                    catalogResponse.setDiamondWt(0.00);
                    catalogResponse.setGemstoneWt(0.00);
                    catalogResponse.setMetalWt(designEntity.getMetalWeight());
                    catalogResponse.setMetalKaratage(null);
                    catalogResponse.setCostPrice(0.00);
                    catalogResponse.setSalesPrice(0.00);
                    catalogResponse.setMarkupPct(0.00);
                    catalogResponse.setMarkupPrice(0.00);
                    catalogResponse.setImageUrl(designEntity.getImageUrl());
                    catalogResponseList.add(catalogResponse);
                });
                break;
            case "SKU":
                List<SkuEntity> skuList = skuRepository.findBySkunos(catalogDto.getInputNos(), CompanyContext.getCurrentCompany());
                skuList.forEach(skuEntity -> {
                    CatalogResponse catalogResponse = new CatalogResponse();
                    catalogResponse.setProductName(skuEntity.getSkuNo());
                    catalogResponse.setProductType(skuEntity.getProductType() == null ? null : skuEntity.getProductType().getProductTypeName());
                    catalogResponse.setDiamondWt(0.00);
                    catalogResponse.setGemstoneWt(0.00);
                    catalogResponse.setMetalWt(skuEntity.getMetalWeight());
                    catalogResponse.setMetalKaratage(null);
                    catalogResponse.setCostPrice(0.00);
                    catalogResponse.setSalesPrice(0.00);
                    catalogResponse.setMarkupPct(0.00);
                    catalogResponse.setMarkupPrice(0.00);
                    catalogResponse.setImageUrl(skuEntity.getImageUrl());
                    catalogResponseList.add(catalogResponse);
                });
                break;
            case "JEW":
            case "JEWELLERY":
                List<ProductEntity> productEntityList = this.productRepository.findByJewNos(catalogDto.getInputNos(), CompanyContext.getCurrentCompany());
                productEntityList.forEach(productEntity -> {
                    CatalogResponse catalogResponse = new CatalogResponse();
                    catalogResponse.setProductName(productEntity.getProductType().concat("/").concat(productEntity.getProductNo()));
                    catalogResponse.setProductType(productEntity.getJewType() == null ? null : productEntity.getJewType().getProductTypeName());
                    catalogResponse.setDiamondWt(0.00);
                    catalogResponse.setGemstoneWt(0.00);
                    catalogResponse.setMetalWt(productEntity.getMetalWeight());
                    catalogResponse.setMetalKaratage(null);
                    catalogResponse.setCostPrice(0.00);
                    catalogResponse.setSalesPrice(0.00);
                    catalogResponse.setMarkupPct(0.00);
                    catalogResponse.setMarkupPrice(0.00);
                    catalogResponse.setImageUrl(productEntity.getPicturePath());
                    catalogResponseList.add(catalogResponse);
                });
                break;
            default:
                break;
        }
        return catalogResponseList;
    }
}
