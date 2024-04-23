package com.kitty.cross.login.service;

import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cross.core.callback.CReqCallBack;
import com.kitty.cross.core.callback.CallbackHandler;
import com.kitty.cross.core.server.SCSession;
import com.kitty.cross.login.cmessage.C2GLoginCross;
import com.kitty.mina.message.Message;
import org.springframework.stereotype.Component;

@Component
public class LoginCallBackHandler extends CallbackHandler {
  public void onRequest(SCSession session, CReqCallBack req) {
    String playerJson = (String)req.getParams().get("player");
    String authJson = (String)req.getParams().get("auth");
    int fromServer = NumberUtil.intValue(req.getParams().get("fromServer"));
    ((PlayerRemoteTransferService)SpringUtils.getBean(PlayerRemoteTransferService.class)).receiverPlayerData(session.getClientIp(), fromServer, playerJson, authJson);
    C2GLoginCross resp = new C2GLoginCross();
    resp.setResultCode(1);
    sendBack(session, req, (Message)resp);
  }
  
  public int cmdType() {
    return 2;
  }
}
