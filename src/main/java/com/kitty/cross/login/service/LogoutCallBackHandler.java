package com.kitty.cross.login.service;

import com.kitty.common.utils.JsonUtils;
import com.kitty.cross.core.callback.CReqCallBack;
import com.kitty.cross.core.callback.CallbackHandler;
import com.kitty.cross.core.server.SCSession;
import com.kitty.cross.login.cmessage.G2CLeaveCross;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.system.game.gate.LoginAuth;
import org.springframework.stereotype.Component;

@Component
public class LogoutCallBackHandler extends CallbackHandler {
  public void onRequest(SCSession session, CReqCallBack req) {
    String authJson = (String)req.getParams().get("auth");
    String account = (String)req.getParams().get("account");
    LoginAuth loginAuth = (LoginAuth)JsonUtils.string2Object(authJson, LoginAuth.class);
    DataCache.LOGIN_AUTH_MAP.put(account, loginAuth);
    G2CLeaveCross resp = new G2CLeaveCross();
    sendBack(session, req, (Message)resp);
  }
  
  public int cmdType() {
    return 3;
  }
}
