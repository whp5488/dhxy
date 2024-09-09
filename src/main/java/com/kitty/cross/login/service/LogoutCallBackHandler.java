package com.kitty.cross.login.service;

import com.kitty.cross.login.cmessage.G2CLeaveCross;
import com.kitty.mina.cache.DataCache;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.gate.LoginAuth;
import com.kitty.cross.core.callback.CReqCallBack;
import com.kitty.cross.core.server.SCSession;
import org.springframework.stereotype.Component;
import com.kitty.cross.core.callback.CallbackHandler;

@Component
public class LogoutCallBackHandler extends CallbackHandler
{
    public LogoutCallBackHandler() {
    }
    
    @Override
    public void onRequest(SCSession session, CReqCallBack req) {
        String authJson = (String)req.getParams().get("auth");
        String account = (String)req.getParams().get("account");
        LoginAuth loginAuth = (LoginAuth)JsonUtils.string2Object(authJson, LoginAuth.class);
        DataCache.LOGIN_AUTH_MAP.put(account, loginAuth);
        G2CLeaveCross resp = new G2CLeaveCross();
        this.sendBack(session, req, resp);
    }
    
    @Override
    public int cmdType() {
        return 3;
    }
}
