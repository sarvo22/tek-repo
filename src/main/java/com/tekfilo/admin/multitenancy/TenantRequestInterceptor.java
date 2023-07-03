package com.tekfilo.admin.multitenancy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
public class TenantRequestInterceptor implements AsyncHandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        setResponseHeader(request, response);
        final boolean userContext = Optional.ofNullable(request)
                .map(req -> req.getHeader("X-UserID"))
                .map(user -> setUserContext(user))
                .orElse(false);

        final boolean companyContext = Optional.ofNullable(request)
                .map(req -> req.getHeader("X-CompanyID"))
                .map(company -> setCompanyContext(company))
                .orElse(false);
        final boolean tenantContext = Optional.ofNullable(request)
                .map(req -> req.getHeader("X-TenantID"))
                .map(tenant -> setTenantContext(tenant))
                .orElse(false);

        return companyContext && tenantContext && userContext;
    }

    private boolean setCompanyContext(String companyId) {
        log.info("Setting current company context = " + companyId);
        CompanyContext.setCurrentCompany(Integer.parseInt(companyId));
        return true;
    }

    private boolean setUserContext(String userId) {
        log.info("Setting login User context = " + userId);
        UserContext.setLoggedInUser(Integer.parseInt(userId));
        return true;
    }

    private void setResponseHeader(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Accept-Encoding", "*");
        response.setHeader("Accept-Language:", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, authorization,Authorization,Accept, X-Requested-With,X-TenantID, X-CompanyID, X-UserID,remember-me");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        UserContext.clear();
        CompanyContext.clear();
        TenantContext.clear();
    }

    private boolean setTenantContext(String tenant) {
        TenantContext.setCurrentTenant(tenant);
        return true;
    }

}
