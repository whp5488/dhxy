
package com.kitty.system.game.role.service;

import java.util.Iterator;
import java.util.List;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.system.game.broadcast.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kitty.system.game.echo.handler.EchoHandler;

@Service
public class StateService extends EchoHandler
{
    @Autowired
    RoleService roleService;
    @Autowired
    BroadcastService broadcastService;
    
    @Override
    public void handleEcho(final HeartBeatEvent heartBeatEvent) {
        final Role role = heartBeatEvent.getRole();
        if (role == null) {
            return;
        }
        final NutMap stateNutMap = role.getExtendBox().getState();
        if (stateNutMap.size() > 0) {
            final List<String> list = new ArrayList<String>();
            final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
            for (final String key : stateNutMap.keySet()) {
                if (stateNutMap.getInt(key, 0) >= currTime) {
                    list.add(key);
                }
            }
            if (list.size() > 0) {
                for (final String key : list) {
                    stateNutMap.remove((Object)key);
                }
                this.roleService.stateRefresh(role);
                role.save();
                this.broadcastService.sendUpdateAppear(role);
            }
        }
    }
}
