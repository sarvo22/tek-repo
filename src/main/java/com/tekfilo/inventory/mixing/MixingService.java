package com.tekfilo.inventory.mixing;

import com.tekfilo.inventory.autonumber.AutoNumberGeneratorService;
import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.commodity.CommodityEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceMainEntity;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.product.ProductDto;
import com.tekfilo.inventory.product.ProductEntity;
import com.tekfilo.inventory.product.ProductRepository;
import com.tekfilo.inventory.stock.StockEntity;
import com.tekfilo.inventory.stock.StockService;
import com.tekfilo.inventory.util.FilterClauseAppender;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MixingService {

    @Autowired
    MixingRepository mixingRepository;

    @Autowired
    MixingDetailRepository mixingDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @Autowired
    StockService stockService;


    public Page<MixingEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.mixingRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public void save(MixingCommonDto mixingCommonDto) throws Exception {
        MixingEntity entity = mixingRepository.save(convertMainEntity(mixingCommonDto.getMainInfo()));
        mixingDetailRepository.saveAll(convertDetailEntityList(entity.getId(), mixingCommonDto));
    }

    private List<MixingDetailEntity> convertDetailEntityList(Integer mainId, MixingCommonDto mixingCommonDto) {
        List<MixingDetailEntity> entities = new ArrayList<>();
        mixingCommonDto.getDetailInfo().stream().forEachOrdered(e -> {
            MixingDetailEntity entity = new MixingDetailEntity();
            BeanUtils.copyProperties(e, entity);
            entity.setId(null);
            entity.setMixingId(mainId);
            entity.setCreatedBy(UserContext.getLoggedInUser());
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(0);
            entities.add(entity);
        });
        return entities;
    }

    private MixingEntity convertMainEntity(MixingDto mainInfo) {
        MixingEntity entity = new MixingEntity();
        entity.setId(mainInfo.getId());
        BeanUtils.copyProperties(mainInfo, entity);
        entity.setMixingBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setMixingDt(Date.valueOf(mainInfo.getMixingDt()));
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    @Synchronized
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return productEntityList;
    }

    private List<MixingDetailDto> convert2DetailDto(List<ProductEntity> productEntityList) {
        List<MixingDetailDto> mixingDetailDtoList = new ArrayList<>();
        productEntityList.stream().forEachOrdered(e -> {
            MixingDetailDto mixingDetailDto = new MixingDetailDto();
//            mixingDetailDto.setProductId(e.getId());
//            mixingDetailDto.setDescription(e.getDescription());
//            mixingDetailDto.setUom1(e.getUnit1());
//            mixingDetailDto.setUom2(e.getUnit2());
//            mixingDetailDto.setProductNo(e.getProductNo());
//            mixingDetailDto.setDescription(e.getDescription());
//            mixingDetailDto.setPicture1Path(e.getPicture1Path());
            mixingDetailDtoList.add(mixingDetailDto);
        });
        return mixingDetailDtoList;

    }

    public MixingEntity findById(Integer id) {
        return mixingRepository.findById(id).get();
    }

    public void deleteAll(Integer id) {
        List<MixingDetailEntity> detailEntities = this.mixingDetailRepository.findAllDetailByMainId(id);
        setDeleteInfo(detailEntities);
        MixingEntity entity = this.mixingRepository.findById(id).get();
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(1);
        mixingDetailRepository.saveAll(detailEntities);
        mixingRepository.save(entity);
    }

    private void setDeleteInfo(List<MixingDetailEntity> entities) {
        entities.stream().forEachOrdered(e -> {
            e.setModifiedBy(UserContext.getLoggedInUser());
            e.setIsDeleted(1);
        });
    }

    public List<MixingDetailEntity> findAllDetailByMainId(Integer mainId) {
        return (this.mixingDetailRepository.findAllDetailByMainId(mainId));
    }

    public List<MixingDetailEntity> findAllDetailWithStockByMainId(Integer mainId) {
        List<MixingDetailEntity> detailEntities = this.mixingDetailRepository.findAllDetailByMainId(mainId);
        detailEntities.stream().forEach(mixingDetailEntity -> {
            StockEntity sourceStock = this.stockService.getStock(mixingDetailEntity.getSourceProductId(), mixingDetailEntity.getSourceBinId(), CompanyContext.getCurrentCompany());
            StockEntity destinationStock = this.stockService.getStock(mixingDetailEntity.getDestinationProductId(), mixingDetailEntity.getDestinationBinId(), CompanyContext.getCurrentCompany());
            mixingDetailEntity.setSourceStockQty1(sourceStock.getBalanceQty1() == null ? 0.00 : sourceStock.getBalanceQty1().doubleValue());
            mixingDetailEntity.setSourceStockQty2(sourceStock.getBalanceQty2() == null ? 0.00 : sourceStock.getBalanceQty2().doubleValue());
            mixingDetailEntity.setDestinationStockQty1(destinationStock.getBalanceQty1() == null ? 0.00 : destinationStock.getBalanceQty1().doubleValue());
            mixingDetailEntity.setDestinationStockQty2(destinationStock.getBalanceQty2() == null ? 0.00 : destinationStock.getBalanceQty2().doubleValue());
        });
        return detailEntities;
    }


    private List<MixingDetailDto> convertEntity2DetailDto(List<MixingDetailEntity> allDetailByMainId) {
        List<MixingDetailDto> mixingDetailDtoList = new ArrayList<>();
        allDetailByMainId.stream().forEachOrdered(e -> {


            MixingDetailDto mixingDetailDto = new MixingDetailDto();
            mixingDetailDto.setId(e.getId());
            mixingDetailDto.setRowId(e.getId());
            mixingDetailDto.setMixingId(e.getMixingId());
            mixingDetailDto.setSourceBinId(e.getSourceBinId());
            mixingDetailDto.setDestinationBinId(e.getDestinationBinId());
            mixingDetailDto.setSourceProductId(e.getSourceProductId());
            mixingDetailDto.setSourceStockQty1(0.00);
            mixingDetailDto.setSourceStockQty2(0.00);

            mixingDetailDto.setDestinationProductId(e.getDestinationProductId());
            mixingDetailDto.setDestinationStockQty1(0.00);
            mixingDetailDto.setDestinationStockQty2(0.00);

            mixingDetailDto.setSourceCostPrice(e.getSourceCostPrice());
            mixingDetailDto.setDestinationCostPrice(e.getDestinationCostPrice());

            mixingDetailDto.setMixingQty1(e.getMixingQty1());
            mixingDetailDto.setMixingQty2(e.getMixingQty2());

            ProductDto sourceProduct = new ProductDto();
            BeanUtils.copyProperties(e.getSourceProduct(), sourceProduct);
            mixingDetailDto.setSourceProduct(sourceProduct);

            ProductDto destinationProduct = new ProductDto();
            BeanUtils.copyProperties(e.getDestinationProduct(), destinationProduct);
            mixingDetailDto.setDestinationProduct(destinationProduct);

            mixingDetailDtoList.add(mixingDetailDto);
        });
        return mixingDetailDtoList;
    }

    public MixingEntity saveMain(MixingDto mixingDto) throws Exception {
        MixingEntity createEntity = convertMainEntity(mixingDto);
        if (!Optional.ofNullable(mixingDto.getId()).isPresent()) {
            String nextNumber = autoNumberGeneratorService.getNextNumber(mixingDto.getMixingType());
            log.info("Next Number generated {} " + nextNumber);
            createEntity.setMixingNo(nextNumber);
        }
        return this.mixingRepository.save(createEntity);
    }

    public MixingDetailEntity saveDetail(MixingDetailDto mixingDetailDto) throws Exception {
        return mixingDetailRepository.save(this.convert2Detail(mixingDetailDto));
    }

    private MixingDetailEntity convert2Detail(MixingDetailDto mixingDetailDto) {
        MixingDetailEntity entity = new MixingDetailEntity();
        entity.setId(mixingDetailDto.getId());
        entity.setMixingId(mixingDetailDto.getMixingId());
        entity.setSourceBinId(mixingDetailDto.getSourceBinId());
        entity.setDestinationBinId(mixingDetailDto.getDestinationBinId());
        entity.setSourceProductId(mixingDetailDto.getSourceProductId());
        entity.setDestinationProductId(mixingDetailDto.getDestinationProductId());
        entity.setMixingQty1(mixingDetailDto.getMixingQty1());
        entity.setMixingQty2(mixingDetailDto.getMixingQty2());
        entity.setSourceImageUrl(mixingDetailDto.getSourceProduct() == null ? null : mixingDetailDto.getSourceProduct().getPicture1Path());
        entity.setSourceCostPrice(mixingDetailDto.getSourceProduct() == null ? 0 : mixingDetailDto.getSourceProduct().getCostPrice());
        entity.setDestinationImageUrl(mixingDetailDto.getDestinationProduct() == null ? null : mixingDetailDto.getDestinationProduct().getPicture1Path());
        entity.setDestinationCostPrice(mixingDetailDto.getDestinationProduct() == null ? 0 : mixingDetailDto.getDestinationProduct().getCostPrice());
        entity.setSequence(mixingDetailDto.getRowId() == null ? 0 : mixingDetailDto.getRowId());
        entity.setIsLocked(0);
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    public MixingDetailEntity findDetailById(Integer id) {
        return this.mixingDetailRepository.findById(id).get();
    }

    public void deleteDetail(MixingDetailEntity entity) throws Exception {
        this.mixingDetailRepository.save(entity);
    }

    public List<MixingEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.mixingRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void lock(List<MixingEntity> entities) throws Exception {
        this.mixingRepository.saveAll(entities);
    }

    public void unlock(List<MixingEntity> entities) throws Exception {
        this.mixingRepository.saveAll(entities);
    }

    public List<MixingDetailEntity> findAllDetailByMainIds(List<Integer> ids) {
        return this.mixingDetailRepository.findAllDetailByMainIds(ids);
    }

    @Transactional
    public void removeAll(List<MixingEntity> entityList, List<MixingDetailEntity> detailEntities) throws Exception {
        this.mixingDetailRepository.deleteDetailById(detailEntities.stream().map(MixingDetailEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));

        this.mixingRepository.deleteMainByIdList(entityList.stream().map(MixingEntity::getId).collect(Collectors.toList()),
                UserContext.getLoggedInUser(), java.sql.Timestamp.from(Instant.now()));
    }
}
