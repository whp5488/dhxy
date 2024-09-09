package com.kitty.system.game.fightnew.utils;

import com.kitty.system.game.fightnew.messages.RespReturnSuccess;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import java.util.List;
import com.kitty.system.game.role.domain.Role;

public class MessageAssistUtil
{
    public MessageAssistUtil() {
    }
    
    public static void sendIsSuccess(Role role, int attaId, byte type, List<RespFightCommandInfo> fightCommandInfos) {
        RespReturnSuccess respReturnSuccess = new RespReturnSuccess(attaId, type, fightCommandInfos);
        FightMessageUtil.sendFightPackage((long)role.getId(), respReturnSuccess);
    }
}
