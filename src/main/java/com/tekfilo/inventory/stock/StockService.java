package com.tekfilo.inventory.stock;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.product.ProductEntity;
import com.tekfilo.inventory.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class StockService {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    StockTransRepository stockTransRepository;

    public List<StockEntity> getLoginCompanyStockList(List<Integer> shapeIdList) {
        return this.stockRepository.findAll(getStockFilters(shapeIdList));
    }

    public Page<StockEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.stockRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public Page<StockEntity> findAllPagedStockWithList(int pageNo, int pageSize,
                                                       String sortName, String sortDirection,
                                                       StockFilterDto stockFilterDto) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.stockRepository.findAll(getStockFilters(stockFilterDto), pageable);
    }

    private Specification getStockFilters(List<Integer> shapeIdList) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("companyId"), CompanyContext.getCurrentCompany()));
            predicates.add(builder.equal(root.get("isDeleted"), 0));

            if (shapeIdList != null) {
                if (shapeIdList.size() > 0) {
                    Join<StockEntity, ProductEntity> shapeJoin = root.join("product", JoinType.INNER);
                    predicates.add(builder.in(shapeJoin.get("shapeId")).value(shapeIdList));
                }
            }
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    private Specification getStockFilters(StockFilterDto stockFilterDto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("companyId"), CompanyContext.getCurrentCompany()));
            predicates.add(builder.equal(root.get("isDeleted"), 0));

            if(stockFilterDto != null){
                if (stockFilterDto.getShapeIds() != null) {
                    if (stockFilterDto.getShapeIds().size() > 0) {
                        Join<StockEntity, ProductEntity> shapeJoin = root.join("product", JoinType.INNER);
                        predicates.add(builder.in(shapeJoin.get("shapeId")).value(stockFilterDto.getShapeIds()));
                    }
                }
                if (stockFilterDto.getColorIds() != null) {
                    if (stockFilterDto.getColorIds().size() > 0) {
                        Join<StockEntity, ProductEntity> shapeJoin = root.join("product", JoinType.INNER);
                        predicates.add(builder.in(shapeJoin.get("colorId")).value(stockFilterDto.getColorIds()));
                    }
                }

                if (stockFilterDto.getCutIds() != null) {
                    if (stockFilterDto.getCutIds().size() > 0) {
                        Join<StockEntity, ProductEntity> shapeJoin = root.join("product", JoinType.INNER);
                        predicates.add(builder.in(shapeJoin.get("cutId")).value(stockFilterDto.getCutIds()));
                    }
                }

                if (stockFilterDto.getClarityIds() != null) {
                    if (stockFilterDto.getClarityIds().size() > 0) {
                        Join<StockEntity, ProductEntity> shapeJoin = root.join("product", JoinType.INNER);
                        predicates.add(builder.in(shapeJoin.get("clarityId")).value(stockFilterDto.getClarityIds()));
                    }
                }

            }
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public StockEntity getStock(Integer productId, Integer binId, Integer currentCompany) {
        List<StockEntity> stockEntityList = stockRepository.findAll(getStockFilters(productId, binId, currentCompany));
        if (stockEntityList.size() > 0) {
            return stockEntityList.get(0);
        }
        StockEntity stock = new StockEntity();
        BigDecimal defaultZeroQty = new BigDecimal(0.00);
        stock.setInQty1(defaultZeroQty);
        stock.setInQty2(defaultZeroQty);
        stock.setAdjInQty1(defaultZeroQty);
        stock.setAdjInQty2(defaultZeroQty);
        stock.setOutQty1(defaultZeroQty);
        stock.setOutQty2(defaultZeroQty);
        stock.setAdjOutQty1(defaultZeroQty);
        stock.setAdjOutQty2(defaultZeroQty);
        stock.setBalanceQty1(defaultZeroQty);
        stock.setBalanceQty2(defaultZeroQty);
        return stock;
    }

    private Specification getStockFilters(Integer productId, Integer binId, Integer companyId) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("productId"), productId));
            predicates.add(builder.equal(root.get("binId"), binId));
            predicates.add(builder.equal(root.get("companyId"), companyId));
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    private Specification getStockFilters(Integer productId, Integer companyId) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("productId"), productId));
            predicates.add(builder.equal(root.get("companyId"), companyId));
            predicates.add(builder.equal(root.get("isDeleted"), 0));
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public List<StockEntity> getProductStock(Integer productId, Integer currentCompany) {
        List<StockEntity> stockEntityList = stockRepository.findAll(getStockFilters(productId, currentCompany));
        return stockEntityList;
    }

    public List<StockTransEntity> getProductStockTrans(Integer productId) {
        return this.stockTransRepository.getProductTransListByProduct(CompanyContext.getCurrentCompany(), productId);
    }
}
