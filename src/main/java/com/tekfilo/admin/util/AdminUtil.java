package com.tekfilo.admin.util;

public class AdminUtil {


    public static String getDateFormat(String dateFormat) {
        if (dateFormat == null) {
            return "dd/MM/yyyy";
        }
        if (dateFormat.equalsIgnoreCase("d/m/Y")) {
            return "dd/MM/yyyy";
        }
        return null;
    }
}
