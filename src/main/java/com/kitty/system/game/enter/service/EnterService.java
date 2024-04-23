package com.kitty.system.game.enter.service;

import com.kitty.common.utils.TimeUtil;
import com.kitty.mina.ServerConfig;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.enter.message.RespHmlNotify;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnterService {
  @Autowired
  private ServerConfig serverConfig;
  
  public void loadHmlNotify(Role role) {
    long serverDate = role.getCreateTime() + WelfareDataPool.newDistrictMap.size() * 86400000L;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date(serverDate);
    int begin = TimeUtil.dayInMillis(simpleDateFormat.format(date));
    int current = TimeUtil.dayInMillis(TimeUtil.when());
    if (current < begin)
      MessagePusher.pushMessage(role, (Message)new RespHmlNotify((short)1, (byte)1)); 
  }
}
