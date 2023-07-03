package com.tekfilo.inventory.stock;

import com.tekfilo.inventory.base.FilterClause;
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
@RequestMapping("/inventory/stock")
public class StockController {

    @Autowired
    StockService stockService;

    @PostMapping("/getlogincompanystock")
    public List<StockEntity> getLoginCompanyStock(@RequestBody List<Integer> shapeIdList) {
        return this.stockService.getLoginCompanyStockList(shapeIdList);
    }


    @PostMapping("/getlogincompanystockpaged/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<StockEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<StockEntity>>(stockService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection,
                filterClause), HttpStatus.OK);
    }

    @PostMapping("/getlogincompanystockpagedfilter/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<StockEntity>> findAllStocks(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody StockFilterDto filterClause) {
        return new ResponseEntity<Page<StockEntity>>(stockService.findAllPagedStockWithList(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection,
                filterClause), HttpStatus.OK);
    }

    @GetMapping("/getstock/{productid}/{binid}")
    public ResponseEntity<StockEntity> getstock(@PathVariable("productid") Integer productId,
                                                @PathVariable("binid") Integer binId) {
        return new ResponseEntity<StockEntity>(stockService.getStock(productId, binId, CompanyContext.getCurrentCompany()), HttpStatus.OK);
    }

    @GetMapping("/getproductstock/{productid}")
    public ResponseEntity<List<StockEntity>> getproductstock(@PathVariable("productid") Integer productId) {
        return new ResponseEntity<List<StockEntity>>(stockService.getProductStock(productId, CompanyContext.getCurrentCompany()), HttpStatus.OK);
    }

    @GetMapping("/getstocktrans/{productid}")
    public ResponseEntity<List<StockTransEntity>> getStockTransByLotId(@PathVariable("productid") Integer productId) {
        return new ResponseEntity<List<StockTransEntity>>(stockService.getProductStockTrans(productId), HttpStatus.OK);
    }

}
