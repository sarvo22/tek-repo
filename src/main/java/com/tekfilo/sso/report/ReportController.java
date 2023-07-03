package com.tekfilo.sso.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sso/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/get-report-list/{subscriptionId}")
    public ResponseEntity<Map<String, List<ReportEntity>>> getReportList(@PathVariable("subscriptionId") Integer subscriptionId) {
        return new ResponseEntity<Map<String, List<ReportEntity>>>(reportService.getReportList(subscriptionId), HttpStatus.OK);
    }
}
