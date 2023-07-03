package com.tekfilo.sso.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ReportService {

    @Autowired
    ReportRepository reportRepository;

    public Map<String, List<ReportEntity>> getReportList(Integer subscriptionId) {
        List<ReportEntity> reportEntityList = reportRepository.findAllBySubscriptionId(subscriptionId);
        Map<String, List<ReportEntity>> groupList = reportEntityList.stream().collect(Collectors.groupingBy(ReportEntity::getReportGroup));
        //TODO restriction to be added user rights exists for this report or not
        return groupList;
    }
}
