package com.tekfilo.jewellery.bintransfer.service;

import com.tekfilo.jewellery.autonumber.AutoNumberGeneratorService;
import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.bintransfer.dto.BinTransferCommonDto;
import com.tekfilo.jewellery.bintransfer.dto.BinTransferDetailDto;
import com.tekfilo.jewellery.bintransfer.dto.BinTransferDto;
import com.tekfilo.jewellery.bintransfer.entity.BinTransferDetailEntity;
import com.tekfilo.jewellery.bintransfer.entity.BinTransferEntity;
import com.tekfilo.jewellery.bintransfer.repository.BinTransferDetailRepository;
import com.tekfilo.jewellery.bintransfer.repository.BinTransferRepository;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import com.tekfilo.jewellery.product.repository.ProductRepository;
import com.tekfilo.jewellery.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class BinTransferService {

    @Autowired
    BinTransferRepository binTransferRepository;

    @Autowired
    BinTransferDetailRepository binTransferDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;


    public Page<BinTransferEntity> findAll(int pageNo, int pageSize, String sortName,
                                           String sortDirection,
                                           List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.binTransferRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public void save(BinTransferCommonDto binTransferCommonDto) throws Exception {
        BinTransferEntity entity = binTransferRepository.save(convertMainEntity(binTransferCommonDto.getMainInfo()));
        binTransferDetailRepository.saveAll(convertDetailEntityList(entity.getId(), binTransferCommonDto));
    }

    private List<BinTransferDetailEntity> convertDetailEntityList(Integer mainId, BinTransferCommonDto binTransferCommonDto) {
        List<BinTransferDetailEntity> entities = new ArrayList<>();
        binTransferCommonDto.getDetailInfo().stream().forEachOrdered(e -> {
            BinTransferDetailEntity entity = new BinTransferDetailEntity();
            BeanUtils.copyProperties(e, entity);
            entity.setId(null);
            entity.setTransferId(mainId);
            entity.setSourceBinId(binTransferCommonDto.getMainInfo().getSourceBinId());
            entity.setDestinationBinId(binTransferCommonDto.getMainInfo().getDestinationBinId());
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(0);
            entities.add(entity);
        });
        return entities;
    }

    private BinTransferEntity convertMainEntity(BinTransferDto mainInfo) {
        BinTransferEntity entity = new BinTransferEntity();
        entity.setId(mainInfo.getId());
        BeanUtils.copyProperties(mainInfo, entity);
        entity.setTotalQty(mainInfo.getTotalQty() == null ? new BigDecimal(0.00) : mainInfo.getTotalQty());
        entity.setTransferBy(UserContext.getLoggedInUser());
        entity.setIsLocked(0);
        entity.setTransferDt(Date.valueOf(mainInfo.getTransferDt()));
        entity.setSequence(0);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    private List<BinTransferDetailDto> convert2DetailDto(List<ProductEntity> productEntityList) {
        List<BinTransferDetailDto> binTransferDetailDtoList = new ArrayList<>();
        productEntityList.stream().forEachOrdered(e -> {
            BinTransferDetailDto binTransferDetailDto = new BinTransferDetailDto();
            binTransferDetailDto.setProductId(e.getId());
            binTransferDetailDto.setDescription(e.getDescription());
            binTransferDetailDto.setUom(e.getUnit());
            binTransferDetailDto.setProductNo(e.getProductNo());
            binTransferDetailDto.setDescription(e.getDescription());
            binTransferDetailDto.setPicturePath(e.getPicturePath());
            binTransferDetailDtoList.add(binTransferDetailDto);
        });
        return binTransferDetailDtoList;

    }

    public BinTransferEntity findById(Integer id) {
        return binTransferRepository.findById(id).get();
    }

    public void deleteAllSelected(List<Integer> ids) throws Exception {
        for (Integer id : ids) {
            this.deleteAll(id);
        }
    }

    public void deleteAll(Integer id) throws Exception {
        List<BinTransferDetailEntity> detailEntities = this.binTransferDetailRepository.findAllDetailByMainId(id);
        setDeleteInfo(detailEntities);
        BinTransferEntity entity = this.binTransferRepository.findById(id).get();
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(1);
        binTransferDetailRepository.saveAll(detailEntities);
        binTransferRepository.save(entity);
    }

    private void setDeleteInfo(List<BinTransferDetailEntity> entities) {
        entities.stream().forEachOrdered(e -> {
            e.setModifiedBy(UserContext.getLoggedInUser());
            e.setIsDeleted(1);
        });
    }

    public List<BinTransferDetailEntity> findAllDetailByMainId(Integer mainId) {
        return (this.binTransferDetailRepository.findAllDetailByMainId(mainId));
    }

    private List<BinTransferDetailDto> convertEntity2DetailDto(List<BinTransferDetailEntity> allDetailByMainId) {
        List<BinTransferDetailDto> binTransferDetailDtoList = new ArrayList<>();
        allDetailByMainId.stream().forEachOrdered(e -> {
            BinTransferDetailDto binTransferDetailDto = new BinTransferDetailDto();
            binTransferDetailDto.setId(e.getId());
            binTransferDetailDto.setRowId(e.getId());
            binTransferDetailDto.setTransferId(e.getTransferId());
            binTransferDetailDto.setSourceBinId(e.getSourceBinId());
            binTransferDetailDto.setDestinationBinId(e.getDestinationBinId());
            binTransferDetailDto.setProductId(e.getProductId());
            binTransferDetailDto.setSourceStockQty(0.00);
            binTransferDetailDto.setProductNo(e.getProduct() == null ? null : e.getProduct().getProductNo());
            binTransferDetailDto.setPurchasePrice(0.00);
            binTransferDetailDto.setUom(e.getProduct() == null ? null : e.getProduct().getUnit());
            binTransferDetailDto.setTransferQty(e.getTransferQty());
            binTransferDetailDto.setImageUrl(e.getImageUrl());
            binTransferDetailDto.setCostPrice(e.getProduct() == null ? 0.00 : e.getProduct().getCostPrice());
            binTransferDetailDto.setSellingPrice(e.getProduct() == null ? 0.00 : e.getProduct().getSalesPrice());
            binTransferDetailDto.setDescription(e.getProduct() == null ? null : e.getProduct().getDescription());
            binTransferDetailDto.setDestinationStockQty(0.00);
            binTransferDetailDto.setPicturePath(e.getProduct() == null ? null : e.getProduct().getPicturePath());
            binTransferDetailDtoList.add(binTransferDetailDto);
        });
        return binTransferDetailDtoList;
    }


    public BinTransferEntity saveMain(BinTransferDto binTransferDto) throws Exception {
        BinTransferEntity createEntity = this.binTransferRepository.save(convertMainEntity(binTransferDto));
        if (!Optional.ofNullable(binTransferDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(binTransferDto.getTransferType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setTransferNo(nextNumber);
        }
        return createEntity;
    }

    public BinTransferDetailEntity saveDetail(BinTransferDetailDto binTransferDetailDto) throws Exception {
        return binTransferDetailRepository.save(this.convert2Detail(binTransferDetailDto));
    }

    private BinTransferDetailEntity convert2Detail(BinTransferDetailDto binTransferDetailDto) {
        BinTransferDetailEntity entity = new BinTransferDetailEntity();
        BeanUtils.copyProperties(binTransferDetailDto, entity);
        entity.setCostPrice(binTransferDetailDto.getCostPrice() == null ? 0 : binTransferDetailDto.getCostPrice());
        entity.setSequence(binTransferDetailDto.getRowId() == null ? 0 : binTransferDetailDto.getRowId());
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public BinTransferDetailEntity findDetailById(Integer id) {
        return this.binTransferDetailRepository.findById(id).get();
    }

    public void deleteDetail(BinTransferDetailEntity entity) throws Exception {
        this.binTransferDetailRepository.save(entity);
    }

    public List<BinTransferEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.binTransferRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void removeAll(List<BinTransferEntity> entities) throws Exception {

        this.binTransferRepository.saveAll(entities);
    }

    public void lock(List<BinTransferEntity> entities) throws Exception {
        this.binTransferRepository.saveAll(entities);
    }

    public void unlock(List<BinTransferEntity> entities) throws Exception {
        this.binTransferRepository.saveAll(entities);
    }
}
