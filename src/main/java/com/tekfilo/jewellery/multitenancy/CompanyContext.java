package com.tekfilo.jewellery.multitenancy;

public class CompanyContext {

    private static final ThreadLocal<Integer> currentCompany = new ThreadLocal<Integer>();

    public static Integer getCurrentCompany() {
        return currentCompany.get();
    }

    public static void setCurrentCompany(Integer companyId) {
        currentCompany.set(companyId);
    }

    public static void clear() {
        currentCompany.remove();
    }
}
