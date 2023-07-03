package com.tekfilo.jewellery.multitenancy;

public class TenantContext {

    public static final String DEFAULT_TENANT_ID = "public";

    private static ThreadLocal<String> currentTenant = new ThreadLocal<String>();

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static void clear() {
        currentTenant.remove();
    }
}
