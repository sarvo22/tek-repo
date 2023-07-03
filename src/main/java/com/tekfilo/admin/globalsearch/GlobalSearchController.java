package com.tekfilo.admin.globalsearch;

import com.tekfilo.admin.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/gsearch")
public class GlobalSearchController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalSearchController.class);

    @Autowired
    CustomerService customerService;


    /**
     * Global Search concept for entire menus
     *
     * @param searchKeyword
     * @param type
     * @return
     */
    @GetMapping("/search/{type}/{searchKeyword}")
    public ResponseEntity<List<?>> search(@PathVariable("type") String type,
                                          @PathVariable("searchKeyword") String searchKeyword) {
        List<?> resultList = new ArrayList<>();
        switch (GlobalSearchTypes.valueOf(type.toUpperCase())) {
            case CUSTOMER:
                resultList = customerService.getCustomerList(Optional.ofNullable(searchKeyword) == null ? "" : searchKeyword.toLowerCase());
                break;
            default:
                break;
        }
        return new ResponseEntity<List<?>>(resultList, HttpStatus.OK);
    }

}
