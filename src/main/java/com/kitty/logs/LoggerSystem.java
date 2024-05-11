
package com.kitty.logs;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public enum LoggerSystem
{
    EXCEPTION, 
    HTTP_COMMAND, 
    CRON_JOB, 
    MONITOR;
    
    public Logger getLogger() {
        return LoggerFactory.getLogger(this.name());
    }
}
