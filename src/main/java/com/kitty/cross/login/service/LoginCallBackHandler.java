package com.kitty.cross.login.service;

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
    public LoginCallBackHandler() {
    }
    
    @Override
    public void onRequest(SCSession session, CReqCallBack req) {
        String playerJson = (String)req.getParams().get("player");
        String authJson = (String)req.getParams().get("auth");
        int fromServer = NumberUtil.intValue(req.getParams().get("fromServer"));
        ((PlayerRemoteTransferService)SpringUtils.getBean(PlayerRemoteTransferService.class)).receiverPlayerData(session.getClientIp(), fromServer, playerJson, authJson);
        C2GLoginCross resp = new C2GLoginCross();
        resp.setResultCode(1);
        this.sendBack(session, req, resp);
    }
    
    @Override
    public int cmdType() {
        return 2;
    }
}
