package com.tekfilo.sps.ibot;

import com.tekfilo.sps.ibot.entities.ApiConfigEntity;
import com.tekfilo.sps.ibot.entities.IBotTask;
import com.tekfilo.sps.ibot.job.IBotJob;
import com.tekfilo.sps.ibot.service.IBotService;
import com.tekfilo.sps.util.SPSUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class IBotListener extends QuartzInitializerListener {

    @Autowired
    IBotService iBotService;

    List<IBotTask> iBotTaskList = new ArrayList<>();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        log.info("IBot Init started");
        log.info("Init all API Service Bots");
        ServletContext ctx = servletContextEvent.getServletContext();
        try {
            //this.initApiService();
            //this.initSchedule(ctx);
        } catch (Exception exception) {
            log.error("exeption raised DB init {} ");
            log.error(exception.getMessage());
        }
    }

    private void initSchedule(ServletContext ctx) {
        log.info("Starting schedules");
        if (this.iBotTaskList.size() > 0) {
            try {
                SchedulerFactory schedulerFactory = (SchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);
                Scheduler scheduler = schedulerFactory.getScheduler();
                JobDetail jobDetail = null;
                for (IBotTask iBotTask : this.iBotTaskList) {
                    if (iBotTask.getEnabled() == 1) {
                        if (CronExpression.isValidExpression(iBotTask.getCronExpression())) {
                            jobDetail = JobBuilder.newJob(IBotJob.class)
                                    .withDescription(iBotTask.getBotDescription())
                                    .withIdentity(iBotTask.getBotUniqueId()).build();
                            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("triger" + iBotTask.getBotUniqueId())
                                    .withSchedule(CronScheduleBuilder.cronSchedule(iBotTask.getCronExpression())).build();
                            jobDetail.getJobDataMap().put("iBotTask", iBotTask);
                            jobDetail.getJobDataMap().put("iBotService", iBotService);
                            scheduler.scheduleJob(jobDetail, trigger);
                        } else {
                            log.warn("Cron expression is wrong; unable to schedule jobs {} " + iBotTask.getCronExpression());
                        }
                    } else {
                        log.warn("Disabled Cron expression found {} " + iBotTask.getBotDescription());
                    }
                }
            } catch (Exception e) {
                log.error("Exception raised while scheduling jobs {} " + e.getMessage());
            }
        }
        log.info("End of Schedule");
    }

    private void initApiService() throws Exception {
        log.info("Started initialization Apiservice");
        List<ApiConfigEntity> apiConfigEntityList = iBotService.getApiServiceBots();
        apiConfigEntityList.stream().forEachOrdered(e -> {
            if (Optional.ofNullable(e.getCronexpression()).isPresent()) {
                if (!e.getCronexpression().equalsIgnoreCase("NONE") && !e.getCronexpression().equalsIgnoreCase("NA")) {
                    IBotTask task = new IBotTask();
                    task.setBotUniqueId(SPSUtil.getRandomString());
                    task.setBotId(e.getId());
                    task.setBotCode(e.getApi().getApiName());
                    task.setBotName(e.getApi().getApiName());
                    task.setBotDescription(e.getApi().getDescription());
                    task.setCronExpression(e.getCronexpression());
                    task.setEnabled(e.getEnabled());
                    task.setLocked(e.getLocked());
                    task.setCompanyId(e.getCompanyId());
                    List<ApiConfigEntity> apiConfigList = new ArrayList<>();
                    apiConfigList.add(e);
                    task.setApiConfigEntityList(apiConfigList);
                    task.setApiService(true);
                    iBotTaskList.add(task);
                }
            }
        });
        log.info("End of initialization Api Service");
    }
}
