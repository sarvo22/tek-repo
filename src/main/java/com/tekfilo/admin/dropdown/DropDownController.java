package com.tekfilo.admin.dropdown;

import com.tekfilo.admin.multitenancy.CompanyContext;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/dropdown")
public class DropDownController {


    @Autowired
    IDropDownService iDropDownService;

    @GetMapping("/getnumberdropdown/{key}")
    public ResponseEntity<List<DropDownNumberDto>> getNumberDropDownList(@PathVariable("key") String key) {
        List<DropDownNumberDto> dropDownNumberDtoList = new ArrayList<>();
        switch (DropDownKeys.valueOf(key.toUpperCase())) {
            case DD_DEPARTMENT:
                dropDownNumberDtoList = this.iDropDownService.dropDownNumberList(DropDownKeys.DD_DEPARTMENT.toString());
                break;
            default:
                break;
        }
        return new ResponseEntity<List<DropDownNumberDto>>(dropDownNumberDtoList, HttpStatus.OK);
    }


    @GetMapping("/getpurchaseorderlist/{supplierId}")
    public ResponseEntity<List<DropDownDto>> getPurchaseOrderList(@PathVariable("supplierId") Integer supplierId) {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("supplierId", supplierId);
        dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_PURCHASE_ORDER_NO_BY_SUPPLIER.toString(), parameters);
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/getsalesorderlist/{customerId}")
    public ResponseEntity<List<DropDownDto>> getCustomerOrderList(@PathVariable("customerId") Integer customerId) {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("customerId", customerId);
        dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SALES_ORDER_NO_BY_CUSTOMER.toString(), parameters);
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/getjewsalesorderlist/{customerId}")
    public ResponseEntity<List<DropDownDto>> getJewCustomerOrderList(@PathVariable("customerId") Integer customerId) {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("customerId", customerId);
        dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_JEW_SALES_ORDER_NO_BY_CUSTOMER.toString(), parameters);
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }


    @GetMapping("/getcompanysubscription")
    public ResponseEntity<List<DropDownDto>> getCompanySubscription() {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("companyId", CompanyContext.getCurrentCompany());
        dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COMPANY_SUBSCRIPTION.toString(), parameters);
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }


    @GetMapping("/getaccount/{partytype}/{partyid}")
    public ResponseEntity<List<DropDownDto>> getAccountByParty(@PathVariable("partytype") String partyType, @PathVariable("partyid") Integer partyId) {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("partyType", partyType);
        parameters.put("partyId", partyId);
        dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_ACCOUNT_BY_PARTY.toString(), parameters);
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }

    @GetMapping("/get/{key}")
    @ApiOperation("Dropdown methods, DD_COUNTRY, DD_COMMODITY,DD_SALESMAN,DD_HEAD_SALESMAN,DD_PAYMENT_TERMS,DD_BUSINESS_TYPE, " +
            " DD_CUT,DD_COLOR,DD_SHAPE,DD_CLARITY,DD_BUYER are the  keys tobe passed from front end ")
    public ResponseEntity<List<DropDownDto>> getDropDownList(@PathVariable("key") String key) {
        List<DropDownDto> dropDownDtoList = new ArrayList<>();
        switch (DropDownKeys.valueOf(key.toUpperCase())) {
            case DD_COUNTRY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COUNTRY.toString());
                break;
            case DD_COUNTRY_BY_NAME:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COUNTRY_BY_NAME.toString());
                break;
            case DD_STATE_BY_NAME:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_STATE_BY_NAME.toString());
                break;
            case DD_COMMODITY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COMMODITY.toString());
                break;
            case DD_STONE_COMMODITY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_STONE_COMMODITY.toString());
                break;
            case DD_GEMSTONE_COMMODITY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_GEMSTONE_COMMODITY.toString());
                break;
            case DD_METAL_COMMODITY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_METAL_COMMODITY.toString());
                break;
            case DD_SALESMAN:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SALESMAN.toString());
                break;
            case DD_HEAD_SALESMAN:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_HEAD_SALESMAN.toString());
                break;
            case DD_PAYMENT_TERMS:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_PAYMENT_TERMS.toString());
                break;
            case DD_BUSINESS_TYPE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_BUSINESS_TYPE.toString());
                break;
            case DD_CURRENCY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_CURRENCY.toString());
                break;
            case DD_CUT:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_CUT.toString());
                break;
            case DD_COLOR:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COLOR.toString());
                break;
            case DD_SHAPE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SHAPE.toString());
                break;
            case DD_CLARITY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_CLARITY.toString());
                break;
            case DD_BIN:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_BIN.toString());
                break;
            case DD_RM_STOCK_BIN:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_RM_STOCK_BIN.toString());
                break;
            case DD_RM_MEMO_PUR_STOCK_BIN:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_RM_MEMO_PUR_STOCK_BIN.toString());
                break;
            case DD_BUYER:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_BUYER.toString());
                break;
            case DD_SUPPLIER:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SUPPLIER.toString());
                break;
            case DD_COMMODITY_GROUP:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COMMODITY_GROUP.toString());
                break;
            case DD_STONE_COMMODITY_GROUP:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_STONE_COMMODITY_GROUP.toString());
                break;
            case DD_GEMSTONE_COMMODITY_GROUP:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_GEMSTONE_COMMODITY_GROUP.toString());
                break;
            case DD_METAL_COMMODITY_GROUP:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_METAL_COMMODITY_GROUP.toString());
                break;
            case DD_DEPARTMENT:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_DEPARTMENT.toString());
                break;
            case DD_DESIGNATION:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_DESIGNATION.toString());
                break;
            case DD_GROUP_CATEGORY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_GROUP_CATEGORY.toString());
                break;
            case DD_CUSTOMER:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_CUSTOMER.toString());
                break;
            case DD_STONE_TYPE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_STONE_TYPE.toString());
                break;
            case DD_POLISH:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_POLISH.toString());
                break;
            case DD_SYMMETRY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SYMMETRY.toString());
                break;
            case DD_FLUORESCENCE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_FLUORESCENCE.toString());
                break;
            case DD_LAB:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_LAB.toString());
                break;
            case DD_UNIT_STONE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_UNIT_STONE.toString());
                break;
            case DD_UNIT_METAL:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_UNIT_METAL.toString());
                break;
            case DD_KARATAGE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_KARATAGE.toString());
                break;
            case DD_COMMODITY_GROUP_TYPE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COMMODITY_GROUP_TYPE.toString());
                break;
            case DD_TREATMENT:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_TREATMENT.toString());
                break;
            case DD_VOUCHER_TYPE_GROUP:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_VOUCHER_TYPE_GROUP.toString());
                break;
            case DD_COMPANY_CURRENCY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COMPANY_CURRENCY.toString());
                break;
            case DD_CASH_ACCOUNT:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_CASH_ACCOUNT.toString());
                break;
            case DD_BANK_ACCOUNT:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_BANK_ACCOUNT.toString());
                break;
            case DD_SCHEDULE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SCHEDULE.toString());
                break;
            case DD_IFRS_SCHEDULE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_IFRS_SCHEDULE.toString());
                break;
            case DD_GROUP:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_GROUP.toString());
                break;
            case DD_IFRS_GROUP:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_IFRS_GROUP.toString());
                break;
            case DD_SUBGROUP:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SUBGROUP.toString());
                break;
            case DD_IFRS_SUBGROUP:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_IFRS_SUBGROUP.toString());
                break;
            case DD_COST_CATEGORY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COST_CATEGORY.toString());
                break;
            case DD_ACCOUNT_CATEGORY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_ACCOUNT_CATEGORY.toString());
                break;
            case DD_MARKET:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_MARKET.toString());
                break;
            case DD_JEW_TYPE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_JEW_TYPE.toString());
                break;
            case DD_COLLECTION:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COLLECTION.toString());
                break;
            case DD_UNIT_JEW:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_UNIT_JEW.toString());
                break;
            case DD_METAL_CODE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_METAL_CODE.toString());
                break;
            case DD_SETTING_TYPE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SETTING_TYPE.toString());
                break;
            case DD_SKU:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SKU.toString());
                break;
            case DD_FACTORY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_FACTORY.toString());
                break;
            case DD_IDENTIFICATION_TYPES:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_IDENTIFICATION_TYPES.toString());
                break;
            case DD_PAYMENT_MODES:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_PAYMENT_MODES.toString());
                break;
            case DD_RECEIVED_THROUGH:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_RECEIVED_THROUGH.toString());
                break;
            case DD_ACCOUNT:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_ACCOUNT.toString());
                break;
            case DD_CONTRACT_TYPE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_CONTRACT_TYPE.toString());
                break;
            case DD_CONTACT_TYPE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_CONTACT_TYPE.toString());
                break;
            case DD_ORIGIN:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_ORIGIN.toString());
                break;
            case DD_BIN_TYPE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_BIN_TYPE.toString());
                break;
            case DD_COST_CENTER:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_COST_CENTER.toString());
                break;
            case DD_DESIGNER:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_DESIGNER.toString());
                break;
            case DD_METAL:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_METAL.toString());
                break;
            case DD_DESIGN_NO:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_DESIGN_NO.toString());
                break;
            case DD_CATEGORY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_CATEGORY.toString());
                break;
            case DD_PROCESS:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_PROCESS.toString());
                break;
            case DD_SKU_NO:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SKU_NO.toString());
                break;
            case DD_GLOBAL_CURRENCY:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_GLOBAL_CURRENCY.toString());
                break;
            case DD_ORDER_TYPE:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_ORDER_TYPE.toString());
                break;
            case DD_ITEM_DIMENSION_UOM:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_ITEM_DIMENSION_UOM.toString());
                break;
            case DD_ITEM_WEIGHT_UOM:
                dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_ITEM_WEIGHT_UOM.toString());
                break;
            case DD_MANUFACTURE:
                 dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_MANUFACTURE.toString());
                break;
            case DD_SALES_ACCOUNT:
                 dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_SALES_ACCOUNT.toString());
                break;
            case DD_PURCHASE_ACCOUNT:
                 dropDownDtoList = this.iDropDownService.dropDownList(DropDownKeys.DD_PURCHASE_ACCOUNT.toString());
                break;
            default:
                break;
        }
        return new ResponseEntity<List<DropDownDto>>(dropDownDtoList, HttpStatus.OK);
    }
}
