package com.kitty.system.game.confirm.service.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.chat.message.RespDanMuMessage;
import com.kitty.system.game.confirm.handler.ConfirmHandler;
import com.kitty.system.game.confirm.message.ReqConfirmResult;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;
import org.springframework.stereotype.Component;

@Component
public class ReturnTeamHandler extends ConfirmHandler {
  public void handleConfirmResult(Role role, ReqConfirmResult reqConfirmResult) {
    Team team = SpringUtils.getTeamService().getTeam(role.getRoleId().intValue());
    if (team == null)
      return; 
    if ("1".equals(reqConfirmResult.getSelect())) {
      SpringUtils.getTeamService().returnTeam(role, false);
    } else {
      RespDanMuMessage respDanMuMessage = SpringUtils.getChatService().sendDanMu(role, team, "我现在不方便归队，请稍等片刻#<E:37>#");
      MessagePusher.pushMessage(role.getId().longValue(), (Message)respDanMuMessage);
    } 
  }
}
