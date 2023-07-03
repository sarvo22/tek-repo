package com.tekfilo.admin.report;

import com.tekfilo.admin.multitenancy.UserContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Description: Utility methods for Tekfilo Report
 *
 * @author siva
 * @version 1.0
 */
public class ReportUtil {

    protected static Log log = LogFactory.getLog(ReportUtil.class);


    public static void setResponse(HttpServletResponse response, String reporttype, String fileName) {
        switch (ReportExportTypes.valueOf(reporttype.toUpperCase())) {
            case PDF:
                setPDFResponse(response, fileName);
                break;
            case XLS:
                setXlsResponse(response, fileName);
                break;
            case XLSX:
                setXlsxResponse(response, fileName);
                break;
            case CSV:
                setCSVResponse(response, fileName);
                break;
            case TXT:
            case TEXT:
                break;
            default:
                break;
        }
    }

    public static void setCSVResponse(HttpServletResponse response, String fileName) {
        response.setContentType("text/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName.concat(".csv") + "\"");
        response.setHeader("Cache-Control", "private");
        response.setDateHeader("Expires", -1);
    }

    public static void setXlsxResponse(HttpServletResponse response, String fileName) {
        response.setContentType("text/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\"");
        response.setHeader("Cache-Control", "private");
        response.setDateHeader("Expires", -1);
    }

    public static void setXlsResponse(HttpServletResponse response, String fileName) {
        response.setContentType("text/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xls\"");
        response.setHeader("Cache-Control", "private");
        response.setDateHeader("Expires", -1);

    }

    public static void setPDFResponse(HttpServletResponse response, String fileName) {
        response.setContentType("text/x-msdownload");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".pdf\"");
        response.setHeader("Cache-Control", "private");
        response.setDateHeader("Expires", -1);
    }


    public static void setDefaultReportParameters(Map<String, Object> parameters) {
        parameters.put("ReportPrintDateTime", "'" + LocalDateTime.now() + "'");
        parameters.put("ReportPrintedBy", "'" + UserContext.getLoggedInUser() + "'");
    }
}
