package com.github.phillipkruger.klokee;

import com.github.phillipkruger.klokee.handler.KlokeeProperties;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import lombok.extern.java.Log;

/**
 * This service creates a schedule to poll for file
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 */
@Singleton
@Startup
@Log
public class Scheduler {

    @Inject
    private KlokeeProperties klokeeProperties;
    
    @EJB
    private KlokeeService klokeeService;
    
    @Resource
    private TimerService timerService;
    
    private final TimerConfig config = new TimerConfig("Klokee Timer", false);
    private Properties properties;
        
    @PostConstruct
    private void init(){
        properties = klokeeProperties.getProperties();
        schedulePoll();
    }
    
    @Timeout
    public void timeout(Timer timer) {
        klokeeService.checkMessage();
    }
    
    private void schedulePoll(){
        String configuredExpression = getCronExpression();
        if(configuredExpression!=null){
            CronExpression cronExpression = new CronExpression(configuredExpression);
            ScheduleExpression scheduleExpression = new ScheduleExpression();
            scheduleExpression.minute(cronExpression.getMinute());
            scheduleExpression.hour(cronExpression.getHour());
            scheduleExpression.dayOfMonth(cronExpression.getDayOfMonth());
            scheduleExpression.month(cronExpression.getMonth());
            scheduleExpression.dayOfWeek(cronExpression.getDayOfWeek());

            Timer timer = timerService.createCalendarTimer(scheduleExpression, config);
        }
    }
    
    private String getCronExpression(){
        return properties.getProperty("cronExpression",null);
    }
}
