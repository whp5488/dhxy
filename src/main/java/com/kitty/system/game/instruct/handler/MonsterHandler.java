package com.kitty.system.game.instruct.handler;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.role.domain.Role;

public class MonsterHandler implements NcFather {
  public void handle(Role role, String content) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1)
      return; 
    FightDataPool.load();
    MessagePusher.pushMessage(role, (Message)new RespMsg("重新加载怪物数据完成"));
  }
}
