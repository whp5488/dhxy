
package com.kitty.cross.login.service;

import com.kitty.mina.message.Message;
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
    @Override
    public void onRequest(final SCSession session, final CReqCallBack req) {
        final String authJson = req.getParams().get("auth");
        final String account = req.getParams().get("account");
        final LoginAuth loginAuth = JsonUtils.string2Object(authJson, LoginAuth.class);
        DataCache.LOGIN_AUTH_MAP.put(account, loginAuth);
        final G2CLeaveCross resp = new G2CLeaveCross();
        this.sendBack(session, req, resp);
    }
    
    @Override
    public int cmdType() {
        return 3;
    }
}
