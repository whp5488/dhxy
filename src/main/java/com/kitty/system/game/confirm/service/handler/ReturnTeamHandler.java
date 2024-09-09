package com.kitty.system.game.confirm.service.handler;

import com.kitty.system.game.chat.message.RespDanMuMessage;
import com.kitty.system.game.team.domain.Team;
import com.kitty.mina.message.MessagePusher;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.confirm.message.ReqConfirmResult;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;
import com.kitty.system.game.confirm.handler.ConfirmHandler;

@Component
public class ReturnTeamHandler extends ConfirmHandler
{
    public ReturnTeamHandler() {
    }
    
    @Override
    public void handleConfirmResult(Role role, ReqConfirmResult reqConfirmResult) {
        Team team = SpringUtils.getTeamService().getTeam((int)role.getRoleId());
        if (team == null) {
            return;
        }
        if ("1".equals(reqConfirmResult.getSelect())) {
            SpringUtils.getTeamService().returnTeam(role, false);
        }
        else {
            RespDanMuMessage respDanMuMessage = SpringUtils.getChatService().sendDanMu(role, team, "我现在不方便归队，请稍等片刻#<E:37>#");
            MessagePusher.pushMessage((long)role.getId(), respDanMuMessage);
        }
        return;
    }
}
