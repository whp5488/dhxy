
package com.kitty.system.game.fightnew.utils;

import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.messages.RespReturnSuccess;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import java.util.List;
import com.kitty.system.game.role.domain.Role;

public class MessageAssistUtil
{
    public static void sendIsSuccess(final Role role, final int attaId, final byte type, final List<RespFightCommandInfo> fightCommandInfos) {
        final RespReturnSuccess respReturnSuccess = new RespReturnSuccess(attaId, type, fightCommandInfos);
        FightMessageUtil.sendFightPackage(role.getId(), respReturnSuccess);
    }
}
