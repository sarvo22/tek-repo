package com.tekfilo.admin.vouchertype;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.util.AdminResponse;
import com.tekfilo.admin.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/vouchertype")
public class VoucherTypeController {

    @Autowired
    VoucherService voucherService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<VoucherTypeEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<VoucherTypeEntity>>(voucherService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{vt}")
    public ResponseEntity<VoucherTypeEntity> findById(@PathVariable("vt") String vt) {
        return new ResponseEntity<VoucherTypeEntity>(voucherService.findById(vt), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody VoucherTypeDto voucherTypeDto) {
        AdminResponse response = new AdminResponse();
        try {
            int count = this.voucherService.voucherTypeExist(voucherTypeDto);
            if (count > 0) {
                response.setId(null);
                response.setStatus(MessageConstants.STATUS_SUCCESS);
                response.setLangStatus("save_100");
                response.setMessage(MessageConstants.VOUCHER_TYPE_EXIST);
                return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
            }
            VoucherTypeEntity entity = voucherService.save(voucherTypeDto);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/getnextnumber/{vouchertype}")
    public ResponseEntity<String> getNextVoucherNumber(@PathVariable("vouchertype") String voucherType) {
        return new ResponseEntity<String>(this.voucherService.getNextNumber(voucherType), HttpStatus.OK);
    }

}
