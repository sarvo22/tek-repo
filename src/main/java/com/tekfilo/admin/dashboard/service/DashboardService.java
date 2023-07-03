package com.tekfilo.admin.dashboard.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@Service("dashboardService")
@PropertySource("classpath:dashboard-sql.properties")
@Transactional
public class DashboardService {

    @Autowired
    Environment environment;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getSalesSummary(String currency) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_ONE_SALES_TITLE"), new Object[]{currency});
        return resultList;
    }

    public List<Map<String, Object>> getPurchaseSummary(String currency) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_ONE_PURCHASE_TITLE"), new Object[]{currency});
        return resultList;
    }

    public List<Map<String, Object>> getPayableSummary(String currency) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_ONE_PAYABLE_TITLE"), new Object[]{currency});
        return resultList;
    }

    public List<Map<String, Object>> getReceivabeSummary(String currency) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_ONE_RECEIVABLE_TITLE"), new Object[]{currency});
        return resultList;
    }

    public List<Map<String, Object>> getInventorySummary(String currency) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_ONE_INVENTORY_TITLE"), new Object[]{currency});
        return resultList;
    }

    public List<Map<String, Object>> getWeeklySales(String currency) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_ONE_LAST_WEEK_SALES"), new Object[]{currency});
        return resultList;
    }

    public List<Map<String, Object>> getMonthlySales(String currency) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_ONE_MONTHLY_SALES"), new Object[]{currency});
        return resultList;
    }

    public List<Map<String, Object>> getCommodityGroupInventory() {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_ONE_INVENTORY_COMMODITY_GROUP"));
        return resultList;
    }

    public List<Map<String, Object>> getShapeGroupInventory() {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_ONE_INVENTORY_SHAPE_GROUP"));
        return resultList;
    }

    public List<Map<String, Object>> getWeeklyPaymentReceived() {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_ONE_LAST_WEEK_PAYMENT_RECEIVED"));
        return resultList;
    }

    public List<Map<String, Object>> getBillsReceivablesList() {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_BILLS_RECEIBALE_TABLE"));
        return resultList;
    }

    public List<Map<String, Object>> getBillsPayablesList() {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(environment.getProperty("DASHBOARD_BILLS_PAYABLE_TABLE"));
        return resultList;
    }
}
