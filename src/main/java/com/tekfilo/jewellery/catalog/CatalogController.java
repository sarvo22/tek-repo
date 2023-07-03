package com.tekfilo.jewellery.catalog;

import com.tekfilo.jewellery.catalog.dto.CatalogDto;
import com.tekfilo.jewellery.catalog.dto.CatalogResponse;
import com.tekfilo.jewellery.catalog.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/jew/catalog")
public class CatalogController {


    @Autowired
    CatalogService catalogService;

    @PostMapping("/get")
    public ResponseEntity<List<CatalogResponse>> getCatalog(@RequestBody CatalogDto catalogDto) {
        return new ResponseEntity<List<CatalogResponse>>(catalogService.getCatalog(catalogDto), HttpStatus.OK);
    }

}
