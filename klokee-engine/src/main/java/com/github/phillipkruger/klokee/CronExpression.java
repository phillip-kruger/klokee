package com.github.phillipkruger.klokee;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * a Cron expression
 * @author Phillip Kruger (klokee@phillip-kruger.com)
 *      Cron expression that indicate when we should poll for a message
 *      .---------------- minute (0 - 59)
 *      |  .------------- hour (0 - 23)
 *      |  |  .---------- day of month (1 - 31)
 *      |  |  |  .------- month (1 - 12) OR jan,feb,mar,apr ...
 *      |  |  |  |  .---- day of week (0 - 7) (Sunday=0 or 7)  OR sun,mon,tue,wed,thu,fri,sat
 *      |  |  |  |  |
 *      *  *  *  *  *
*/
@Log
public class CronExpression {

    @Getter
    private String minute = STAR;
    @Getter
    private String hour = STAR;
    @Getter
    private String dayOfMonth = STAR;
    @Getter
    private String month = STAR;
    @Getter
    private String dayOfWeek = STAR;
    
    public CronExpression(@NotNull String cron){
        // TODO: Validate the cron with some regex or bean validation
        String[] parts = cron.split(" ");
        
        if(parts.length>0){
            minute = parts[0];
        }
        if(parts.length>1){
            hour = parts[1];
        }
        if(parts.length>2){
            dayOfMonth = parts[2];
        }
        if(parts.length>3){
            month = parts[3];
        }
        if(parts.length>4){
            dayOfWeek = parts[4];
        }
    }
    
    private static final String STAR = "*";
}
