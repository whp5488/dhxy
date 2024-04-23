package com.kitty.system.game.task.service.handler;

import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component
public abstract class TimeTaskHandler extends TaskHandler {
  public abstract void handleHeartBeat(Role paramRole, long paramLong);
  
  public void handleLogin(Role role) {
    super.handleLogin(role);
    handleHeartBeat(role, 0L);
  }
}
