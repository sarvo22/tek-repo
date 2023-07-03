package com.tekfilo.sps.ibot.job;

import com.tekfilo.sps.ibot.entities.ApiConfigEntity;
import com.tekfilo.sps.ibot.entities.IBotTask;
import com.tekfilo.sps.ibot.entities.Rapaport;
import com.tekfilo.sps.ibot.service.IBotService;
import com.tekfilo.sps.util.RapaportSoapUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class IBotJob implements Job {

    IBotService iBotService = null;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Job execution started ");
        IBotTask iBotTask = new IBotTask();
        iBotTask = (IBotTask) jobExecutionContext.getJobDetail().getJobDataMap().get("iBotTask");
        if (!Optional.ofNullable(iBotService).isPresent()) {
            log.info("Service is not present or active, setting service from Job");
            this.iBotService = (IBotService) jobExecutionContext.getJobDetail().getJobDataMap().get("iBotService");
        }
        log.info("Found the Tasks of Company {} " + iBotTask.getCompanyId() + "=" + iBotTask.getBotUniqueId() + "=" + iBotTask.getBotDescription());
        executeJobAction(iBotTask);
    }

    private void executeJobAction(IBotTask iBotTask) {
        log.info("Execution Job Actions for {} " + iBotTask.getBotUniqueId() + "; company Id : {} " + iBotTask.getCompanyId() + "; Name : {} " + iBotTask.getBotName());
        if (iBotTask.isApiService()) {
            executeApiServiceAction(iBotTask);
        }
        log.info("Completed execution of Job");
    }

    private void executeApiServiceAction(IBotTask iBotTask) {
        if (iBotTask.getApiConfigEntityList() != null) {
            if (iBotTask.getApiConfigEntityList().size() > 0) {
                iBotTask.getApiConfigEntityList().stream().forEachOrdered(api -> {
                    executeApiServiceAction(iBotTask, api);
                });
            }
        }
    }

    private void executeApiServiceAction(IBotTask iBotTask, ApiConfigEntity apiConfig) {
        log.info("started API execution for api => " + apiConfig.getApi().getApiName() + " of company = " + apiConfig.getCompanyId());

        switch (apiConfig.getApi().getActionCode()) {
            case "METAL_RATE":
                executeMetalRateApi(iBotTask, apiConfig);
                break;
            case "RAPAPORT_RATE":
                executeRapaportPriceApi(iBotTask, apiConfig);
                break;
            default:
                break;
        }
        log.info("completed API execution for api => " + apiConfig.getApi().getApiName() + " of company = " + apiConfig.getCompanyId());
    }

    private void executeMetalRateApi(IBotTask iBotTask, ApiConfigEntity apiConfig) {
        if (Optional.ofNullable(apiConfig.getApi().getApiEndpoint()).isPresent()) {
            iBotService.executeMetalRateApi(apiConfig);
        } else {
            log.info("API service " + apiConfig.getApi().getApiName() + " does not have end point url, unable to execute job");
        }
    }

    private void executeRapaportPriceApi(IBotTask iBotTask, ApiConfigEntity apiConfig) {
        if (Optional.ofNullable(apiConfig.getApi().getApiEndpoint()).isPresent()) {
            List<String> shapesList = new ArrayList<>();
            shapesList.add("Round");
            shapesList.add("Pear");

            RapaportSoapUtil rapaportSoapUtil = new RapaportSoapUtil();
            shapesList.stream().forEachOrdered(e -> {
                try {
                    List<Rapaport> rapaportList = rapaportSoapUtil.getRapaportPriceSheet(e, apiConfig.getApi().getApiEndpoint(), apiConfig.getApiKey(), apiConfig.getApiSid());
                    log.info("Shape ======= {} " + e + " ; Size {} " + rapaportList.size());
                    iBotService.executeRapaportPriceAction(rapaportList, iBotTask, apiConfig);
                } catch (SOAPException ex) {
                    log.error(ex.getMessage());
                } catch (TransformerException ex) {
                    log.error(ex.getMessage());
                }
            });


        } else {
            log.info("API service " + apiConfig.getApi().getApiName() + " does not have end point url, unable to execute job");
        }
    }


}
