package com.kitty.system.game.task.service.handler;

import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component
public abstract class TimeTaskHandler extends TaskHandler
{
    public TimeTaskHandler() {
    }
    
    public abstract void handleHeartBeat(Role role, long prevHeartTime);
    
    @Override
    public void handleLogin(Role role) {
        super.handleLogin(role);
        this.handleHeartBeat(role, 0L);
    }
}
