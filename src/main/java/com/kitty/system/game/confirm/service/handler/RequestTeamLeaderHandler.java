
package com.kitty.system.game.confirm.service.handler;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.confirm.model.RequestTeamLeaderConfirm;
import com.kitty.system.game.confirm.message.ReqConfirmResult;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;
import com.kitty.system.game.confirm.handler.ConfirmHandler;

@Component
public class RequestTeamLeaderHandler extends ConfirmHandler
{
    @Override
    public void handleConfirmResult(final Role role, final ReqConfirmResult reqConfirmResult) {
        final RequestTeamLeaderConfirm requestTeamLeaderConfirm = (RequestTeamLeaderConfirm)role.getConfirm();
        final int requestId = requestTeamLeaderConfirm.getRequestId();
        requestTeamLeaderConfirm.setAgree(true);
        if ("1".equals(reqConfirmResult.getSelect())) {
            SpringUtils.getTeamService().acceptRequestTeamLeader(role, requestId);
        }
        else {
            final Role request = SpringUtils.getRoleService().getOnlinePlayer(requestId);
            MessagePusher.pushMessage(request, new RespMsg("队长拒绝了你的带队申请。"));
        }
    }
}
