
package com.kitty.cross.login.service;

import com.kitty.mina.message.Message;
import com.kitty.cross.login.cmessage.C2GLoginCross;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.NumberUtil;
import com.kitty.cross.core.callback.CReqCallBack;
import com.kitty.cross.core.server.SCSession;
import org.springframework.stereotype.Component;
import com.kitty.cross.core.callback.CallbackHandler;

@Component
public class LoginCallBackHandler extends CallbackHandler
{
    @Override
    public void onRequest(final SCSession session, final CReqCallBack req) {
        final String playerJson = req.getParams().get("player");
        final String authJson = req.getParams().get("auth");
        final int fromServer = NumberUtil.intValue(req.getParams().get("fromServer"));
        SpringUtils.getBean(PlayerRemoteTransferService.class).receiverPlayerData(session.getClientIp(), fromServer, playerJson, authJson);
        final C2GLoginCross resp = new C2GLoginCross();
        resp.setResultCode(1);
        this.sendBack(session, req, resp);
    }
    
    @Override
    public int cmdType() {
        return 2;
    }
}
