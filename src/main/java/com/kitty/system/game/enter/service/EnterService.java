package com.kitty.system.game.enter.service;

import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.enter.message.RespHmlNotify;
import com.kitty.common.utils.TimeUtil;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.game.role.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.mina.ServerConfig;
import org.springframework.stereotype.Service;

@Service
public class EnterService
{
    @Autowired
    private ServerConfig serverConfig;
    
    public EnterService() {
    }
    
    public void loadHmlNotify(Role role) {
        long serverDate = role.getCreateTime() + (long)WelfareDataPool.newDistrictMap.size() * 86400000L;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(serverDate);
        int begin = TimeUtil.dayInMillis(simpleDateFormat.format(date));
        int current = TimeUtil.dayInMillis(TimeUtil.when());
        if (current < begin) {
            MessagePusher.pushMessage(role, new RespHmlNotify((short)1, (byte)1));
        }
    }
}
