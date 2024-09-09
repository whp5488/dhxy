package com.kitty.mina;

import org.slf4j.LoggerFactory;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ModuleCounterSchedule
{
    private static final Logger log;
    
    public ModuleCounterSchedule() {
    }
    
    public static void disconnected(IoSession session) {
    }
    
    static {
        log = LoggerFactory.getLogger(ModuleCounterSchedule.class);
    }
}
