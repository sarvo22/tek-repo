package com.tekfilo.inventory.upload;

import com.tekfilo.inventory.clarity.ClarityService;
import com.tekfilo.inventory.color.ColorEntity;
import com.tekfilo.inventory.color.ColorService;
import com.tekfilo.inventory.commodity.CommodityEntity;
import com.tekfilo.inventory.commodity.CommodityService;
import com.tekfilo.inventory.cut.CutEntity;
import com.tekfilo.inventory.cut.CutService;
import com.tekfilo.inventory.product.ProductService;
import com.tekfilo.inventory.shape.ShapeEntity;
import com.tekfilo.inventory.shape.ShapeService;
import com.tekfilo.inventory.util.InventoryResponse;
import com.tekfilo.inventory.util.TekfiloUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/inventory/upload")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @Autowired
    ProductService productService;

    @Autowired
    CommodityService commodityService;

    @Autowired
    ShapeService shapeService;

    @Autowired
    CutService cutService;

    @Autowired
    ColorService colorService;

    @Autowired
    ClarityService clarityService;

    @PostMapping("/update")
    public ResponseEntity<InventoryResponse> updateUploadedDocumentsUrl(@RequestBody DocumentDto documentDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            uploadService.updateUploadedDocumentUrls(documentDto);
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Document Uploaded");
        } catch (Exception e) {
            response.setId(null);
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            log.error("Server error on Document Upload");
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/diamondproduct")
    public ResponseEntity<InventoryResponse> uploadData(@RequestBody ProductDataUploadDto productDataUploadDto) {
        InventoryResponse response = new InventoryResponse();
        boolean isValidData = true;
        try {
            if(StringUtils.isEmpty(productDataUploadDto.getProductNo())){
                response.setStatus(101);
                response.setMessage("Product No not exists in given attachment");
                isValidData = false;
            }else{
                int count = this.productService.validateProductNo(productDataUploadDto.getProductNo());
                if(count > 0){
                    response.setStatus(101);
                    response.setMessage("Lot Number already exists in system");
                    isValidData = false;
                }
            }

            if(StringUtils.isEmpty(productDataUploadDto.getUnit1())){
                productDataUploadDto.setUnit1("CT");
            }
            if(StringUtils.isEmpty(productDataUploadDto.getUnit2())){
                productDataUploadDto.setUnit2("PCS");
            }

            if(StringUtils.isEmpty(productDataUploadDto.getCommodityId())){
                List<CommodityEntity> commodityList = this.commodityService.findListByName(productDataUploadDto.getCommodityId());
                if(commodityList.size() == 0){
                    isValidData = false;
                    response.setStatus(101);
                    response.setMessage("Commodity not found");
                }else if(commodityList.size() > 1){
                    isValidData = false;
                    response.setStatus(101);
                    response.setMessage("More than one Commodity found");
                }
            }

            if(!StringUtils.isEmpty(productDataUploadDto.getCommodityId())){
                List<ShapeEntity> shapeEntityList = this.shapeService.findListByName(productDataUploadDto.getShapeId());
                if(shapeEntityList.size() == 0){
                    isValidData = false;
                    response.setStatus(101);
                    response.setMessage("Shape not found");
                }else if(shapeEntityList.size() > 1){
                    isValidData = false;
                    response.setStatus(101);
                    response.setMessage("More than one Shape found");
                }
            }

            if(!StringUtils.isEmpty(productDataUploadDto.getCutId())){
                List<CutEntity> cutEntityList = this.cutService.findListByName(productDataUploadDto.getShapeId());
                if(cutEntityList.size() == 0){
                    isValidData = false;
                    response.setStatus(101);
                    response.setMessage("Shape not found");
                }else if(cutEntityList.size() > 1){
                    isValidData = false;
                    response.setStatus(101);
                    response.setMessage("More than one Shape found");
                }
            }

            if(!StringUtils.isEmpty(productDataUploadDto.getColorId())){
                List<ColorEntity> colorEntityList = this.colorService.findListByName(productDataUploadDto.getShapeId());
                if(colorEntityList.size() == 0){
                    isValidData = false;
                    response.setStatus(101);
                    response.setMessage("Shape not found");
                }else if(colorEntityList.size() > 1){
                    isValidData = false;
                    response.setStatus(101);
                    response.setMessage("More than one Shape found");
                }
            }

            productDataUploadDto.getClarityId();
            productDataUploadDto.getCostPrice();
            productDataUploadDto.getSalesPrice();
            productDataUploadDto.getCurrency();
            productDataUploadDto.getExchangeRate();




            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Data Uploaded Successfully");
        } catch (Exception e) {
            TekfiloUtils.setErrorResponse(response,e);
            log.error("File Upload error " + e.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }
}
