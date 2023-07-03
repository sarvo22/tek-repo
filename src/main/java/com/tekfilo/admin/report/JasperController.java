package com.tekfilo.admin.report;

import com.tekfilo.admin.multitenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/admin/report")
public class JasperController {

    private static final String REPORT_JASPER_CLASSPATH = "report/jasper/";

    @Autowired
    private DataSource dataSource;


    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @PostMapping("/doprint/{export}")
    public ResponseEntity<Resource> doPrint(@PathVariable("export") String export, @RequestBody Map<String, Object> input,
                                            HttpServletResponse response) {
        Map<String, Object> parameters = input;
        ReportUtil.setDefaultReportParameters(parameters);
        final String reportName = (String) parameters.get("report");
        final String reportFormat = parameters.containsKey("export") ? (String) parameters.get("export") : "pdf";
        final String onePagePerSheet = parameters.containsKey("onePagePerSheet") ? (String) parameters.get("onePagePerSheet") : "N";
        final String sheetNames = parameters.containsKey("sheetNames") ? (String) parameters.get("sheetNames") : reportName;
        parameters.put("schema", TenantContext.getCurrentTenant());

        log.info("Parameters set for this report {} " + (parameters.size() > 0 ? parameters.toString() : "empty parameter"));

        Connection connection = null;
        try {
            // Testing purpose added
            log.info("Entity manager schema {} " + entityManagerFactory.getDataSource().getConnection().getSchema());
            entityManagerFactory.getDataSource().getConnection().setSchema(TenantContext.getCurrentTenant());
            connection = entityManagerFactory.getDataSource().getConnection();
            connection.setSchema(TenantContext.getCurrentTenant());
            log.info("Schema name received from Connection {} " + connection.getSchema());
            log.info("Received Connection from Datasource for Reports");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        try {
            ClassPathResource resource = new ClassPathResource(REPORT_JASPER_CLASSPATH + getProperFileName(reportName));
            log.info("ClasspathResource found with given report name");
            JasperPrint jasperPrint = null;
            jasperPrint = JasperFillManager.fillReport(resource.getInputStream(), parameters, connection);
            String tempDirectory = System.getProperty("java.io.tmpdir");
            final String fileNameWithPath = tempDirectory + reportName + "." + reportFormat;
            HttpHeaders header = new HttpHeaders();
            switch (ReportExportTypes.valueOf(reportFormat.toUpperCase())) {
                case PDF:
                    JasperExportManager.exportReportToPdfFile(jasperPrint, fileNameWithPath);
                    break;
                case HTML:
                    JasperExportManager.exportReportToHtmlFile(jasperPrint, fileNameWithPath);
                    break;
                case XLS:
                case XLSX:
                    JRXlsxExporter jrXlsxExporter = new JRXlsxExporter();
                    jrXlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    jrXlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileNameWithPath));
                    SimpleXlsxReportConfiguration simpleXlsxExporterConfiguration = new SimpleXlsxReportConfiguration();
                    simpleXlsxExporterConfiguration.setDetectCellType(true);
                    simpleXlsxExporterConfiguration.setRemoveEmptySpaceBetweenColumns(Boolean.TRUE);
                    simpleXlsxExporterConfiguration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
                    simpleXlsxExporterConfiguration.setShowGridLines(Boolean.TRUE);
                    simpleXlsxExporterConfiguration.setWhitePageBackground(Boolean.FALSE);
                    if (onePagePerSheet.equalsIgnoreCase("Y")) {
                        simpleXlsxExporterConfiguration.setOnePagePerSheet(Boolean.TRUE);
                        simpleXlsxExporterConfiguration.setSheetNames(new String[]{sheetNames});
                    }
                    jrXlsxExporter.setConfiguration(simpleXlsxExporterConfiguration);
                    jrXlsxExporter.exportReport();
                    break;
                case CSV:
                    JRCsvExporter jrCsvExporter = new JRCsvExporter();
                    jrCsvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    jrCsvExporter.setExporterOutput(new SimpleWriterExporterOutput(fileNameWithPath));
                    SimpleCsvExporterConfiguration csvConfiguration = new SimpleCsvExporterConfiguration();
                    csvConfiguration.setWriteBOM(Boolean.TRUE);
                    csvConfiguration.setRecordDelimiter("\r\n");
                    jrCsvExporter.setConfiguration(csvConfiguration);
                    jrCsvExporter.exportReport();
                    break;
            }
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + reportName + "." + reportFormat.toLowerCase());
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");
            File file = new File(fileNameWithPath);
            Path paths = Paths.get(file.getAbsolutePath());
            ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(paths));
            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType(applicationTypes(export)))
                    .body(byteArrayResource);
        } catch (JRException | IOException jrException) {
            jrException.printStackTrace();
            log.error("Exception on executing report {} " + jrException.getMessage());
        }
        return null;
    }

    private String applicationTypes(String export) {
        String applicationType = "application/octet-stream";

        switch (ReportExportTypes.valueOf(export.toUpperCase())) {
            case PDF:
                applicationType = "application/pdf";
                break;
            case XLS:
            case XLSX:
                applicationType = "application/vnd.ms-excel";
                break;
            case HTML:
                applicationType = "text/html";
                break;
            case CSV:
                applicationType = "text/csv";
                break;
            default:
                break;
        }

        return applicationType;
    }

    private String getProperFileName(String reportName) {
        if (reportName.toLowerCase().contains(".jasper")) {
            return reportName;
        }
        return reportName.concat(".jasper");
    }
}
