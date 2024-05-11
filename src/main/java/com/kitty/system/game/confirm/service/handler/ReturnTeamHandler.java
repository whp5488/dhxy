
package com.kitty.system.game.confirm.service.handler;

import com.kitty.system.game.chat.message.RespDanMuMessage;
import com.kitty.system.game.team.domain.Team;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.confirm.message.ReqConfirmResult;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;
import com.kitty.system.game.confirm.handler.ConfirmHandler;

@Component
public class ReturnTeamHandler extends ConfirmHandler
{
    @Override
    public void handleConfirmResult(final Role role, final ReqConfirmResult reqConfirmResult) {
        final Team team = SpringUtils.getTeamService().getTeam(role.getRoleId());
        if (team == null) {
            return;
        }
        if ("1".equals(reqConfirmResult.getSelect())) {
            SpringUtils.getTeamService().returnTeam(role, false);
        }
        else {
            final RespDanMuMessage respDanMuMessage = SpringUtils.getChatService().sendDanMu(role, team, "我现在不方便归队，请稍等片刻#<E:37>#");
            MessagePusher.pushMessage(role.getId(), respDanMuMessage);
        }
    }
}
