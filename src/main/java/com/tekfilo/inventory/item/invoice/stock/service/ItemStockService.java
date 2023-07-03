package com.tekfilo.inventory.item.invoice.stock.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.invoice.stock.entity.ItemStockEntity;
import com.tekfilo.inventory.item.invoice.stock.entity.ItemStockTransEntity;
import com.tekfilo.inventory.item.invoice.stock.repository.ItemStockRepository;
import com.tekfilo.inventory.item.invoice.stock.repository.ItemStockTransRepository;
import com.tekfilo.inventory.multitenancy.CompanyContext;
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

@Slf4j
@Service
@Transactional
public class ItemStockService {

    @Autowired
    ItemStockRepository itemStockRepository;

    @Autowired
    ItemStockTransRepository itemStockTransRepository;

    public List<ItemStockEntity> getLoginCompanyStockList(List<Integer> shapeIdList) {
        return this.itemStockRepository.findAll(getStockFilters(shapeIdList));
    }

    public Page<ItemStockEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {

        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.itemStockRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    private Specification getStockFilters(List<Integer> shapeIdList) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("companyId"), CompanyContext.getCurrentCompany()));
            predicates.add(builder.equal(root.get("isDeleted"), 0));

            if (shapeIdList != null) {
                if (shapeIdList.size() > 0) {
                    Join<ItemStockEntity, ItemEntity> shapeJoin = root.join("product", JoinType.INNER);
                }
            }
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public ItemStockEntity getStock(Integer productId, Integer binId, Integer currentCompany) {
        List<ItemStockEntity> stockEntityList = itemStockRepository.findAll(getStockFilters(productId, binId, currentCompany));
        if (stockEntityList.size() > 0) {
            return stockEntityList.get(0);
        }
        ItemStockEntity stock = new ItemStockEntity();
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

    public List<ItemStockEntity> getProductStock(Integer productId, Integer currentCompany) {
        List<ItemStockEntity> stockEntityList = itemStockRepository.findAll(getStockFilters(productId, currentCompany));
        return stockEntityList;
    }

    public List<ItemStockTransEntity> getProductStockTrans(Integer productId) {
        return this.itemStockTransRepository.getProductTransListByProduct(CompanyContext.getCurrentCompany(), productId);
    }
}
