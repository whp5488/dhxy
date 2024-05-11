
package com.kitty.system.game.confirm.service;

import com.kitty.system.game.confirm.handler.ConfirmHandler;
import com.kitty.system.game.confirm.model.RoleConfirm;
import com.kitty.system.game.confirm.handler.ConfirmHandlerHelper;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.confirm.message.ReqConfirmResult;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Service;

@Service
public class ConfirmService
{
    public void handleConfirmResult(final Role role, final ReqConfirmResult reqConfirmResult) {
        final RoleConfirm roleConfirm = role.getConfirm();
        if (roleConfirm == null) {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
            return;
        }
        final ConfirmHandler handler = ConfirmHandlerHelper.INSTANCE.getTargetHandler(roleConfirm.getConfirmType());
        if (handler != null) {
            handler.handleConfirmResult(role, reqConfirmResult);
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
    }
}
