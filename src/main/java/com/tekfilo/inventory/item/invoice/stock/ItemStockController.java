package com.tekfilo.inventory.item.invoice.stock;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.invoice.stock.entity.ItemStockEntity;
import com.tekfilo.inventory.item.invoice.stock.entity.ItemStockTransEntity;
import com.tekfilo.inventory.item.invoice.stock.service.ItemStockService;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inventory/item/stock")
public class ItemStockController {

    @Autowired
    ItemStockService itemStockService;

    @PostMapping("/getlogincompanystock")
    public List<ItemStockEntity> getLoginCompanyStock(@RequestBody List<Integer> shapeIdList) {
        return this.itemStockService.getLoginCompanyStockList(shapeIdList);
    }


    @PostMapping("/getlogincompanystockpaged/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<ItemStockEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<ItemStockEntity>>(itemStockService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection,
                filterClause), HttpStatus.OK);
    }

    @GetMapping("/getstock/{productid}/{binid}")
    public ResponseEntity<ItemStockEntity> getstock(@PathVariable("productid") Integer productId,
                                                @PathVariable("binid") Integer binId) {
        return new ResponseEntity<ItemStockEntity>(itemStockService.getStock(productId, binId, CompanyContext.getCurrentCompany()), HttpStatus.OK);
    }

    @GetMapping("/getproductstock/{productid}")
    public ResponseEntity<List<ItemStockEntity>> getproductstock(@PathVariable("productid") Integer productId) {
        return new ResponseEntity<List<ItemStockEntity>>(itemStockService.getProductStock(productId, CompanyContext.getCurrentCompany()), HttpStatus.OK);
    }

    @GetMapping("/getstocktrans/{productid}")
    public ResponseEntity<List<ItemStockTransEntity>> getStockTransByLotId(@PathVariable("productid") Integer productId) {
        return new ResponseEntity<List<ItemStockTransEntity>>(itemStockService.getProductStockTrans(productId), HttpStatus.OK);
    }
}
