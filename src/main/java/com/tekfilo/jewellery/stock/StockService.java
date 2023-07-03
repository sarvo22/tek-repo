package com.tekfilo.jewellery.stock;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.util.FilterClauseAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class StockService {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    StockTransRepository stockTransRepository;


    public Page<StockEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.stockRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    public StockEntity getStock(Integer productId, Integer binId, Integer currentCompany) {
        List<StockEntity> stockEntityList = stockRepository.findAll(getStockFilters(productId, binId, currentCompany));
        if (stockEntityList.size() > 0) {
            return stockEntityList.get(0);
        }
        StockEntity stock = new StockEntity();
        BigDecimal defaultZeroQty = new BigDecimal(0.00);
        stock.setInQty(defaultZeroQty);
        stock.setAdjInQty(defaultZeroQty);
        stock.setOutQty(defaultZeroQty);
        stock.setAdjOutQty(defaultZeroQty);
        stock.setBalanceQty(defaultZeroQty);
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
