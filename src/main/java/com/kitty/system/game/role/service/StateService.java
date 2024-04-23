package com.kitty.system.game.role.service;

import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.echo.handler.EchoHandler;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateService extends EchoHandler {
  @Autowired
  RoleService roleService;
  
  @Autowired
  BroadcastService broadcastService;
  
  public void handleEcho(HeartBeatEvent heartBeatEvent) {
    Role role = heartBeatEvent.getRole();
    if (role == null)
      return; 
    NutMap stateNutMap = role.getExtendBox().getState();
    if (stateNutMap.size() > 0) {
      List<String> list = new ArrayList<>();
      int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
      for (String key : stateNutMap.keySet()) {
        if (stateNutMap.getInt(key, 0) >= currTime)
          list.add(key); 
      } 
      if (list.size() > 0) {
        for (String key : list)
          stateNutMap.remove(key); 
        this.roleService.stateRefresh(role);
        role.save();
        this.broadcastService.sendUpdateAppear(role);
      } 
    } 
  }
}
