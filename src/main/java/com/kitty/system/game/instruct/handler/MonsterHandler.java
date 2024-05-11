
package com.kitty.system.game.instruct.handler;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class MonsterHandler implements NcFather
{
    @Override
    public void handle(final Role role, final String content) {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            return;
        }
        FightDataPool.load();
        MessagePusher.pushMessage(role, new RespMsg("重新加载怪物数据完成"));
    }
}
