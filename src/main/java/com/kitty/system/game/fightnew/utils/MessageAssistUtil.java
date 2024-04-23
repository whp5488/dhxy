package com.kitty.system.game.fightnew.utils;

import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.messages.RespReturnSuccess;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import com.kitty.system.game.role.domain.Role;
import java.util.List;

public class MessageAssistUtil {
  public static void sendIsSuccess(Role role, int attaId, byte type, List<RespFightCommandInfo> fightCommandInfos) {
    RespReturnSuccess respReturnSuccess = new RespReturnSuccess(attaId, type, fightCommandInfos);
    FightMessageUtil.sendFightPackage(role.getId().longValue(), (Message)respReturnSuccess);
  }
}
