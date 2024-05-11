
package com.kitty.system.game.task.service.handler;

import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component
public abstract class TimeTaskHandler extends TaskHandler
{
    public abstract void handleHeartBeat(final Role p0, final long p1);
    
    @Override
    public void handleLogin(final Role role) {
        super.handleLogin(role);
        this.handleHeartBeat(role, 0L);
    }
}
