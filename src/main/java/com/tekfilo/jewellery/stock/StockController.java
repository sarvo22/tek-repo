package com.tekfilo.jewellery.stock;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/jew/stock")
public class StockController {

    @Autowired
    StockService stockService;


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
