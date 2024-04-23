package com.kitty.cross.demo;

import com.kitty.cross.core.callback.CReqCallBack;
import com.kitty.cross.core.callback.CallbackHandler;
import com.kitty.cross.core.server.SCSession;
import org.springframework.stereotype.Component;

@Component
public class HelloCallBackHandler extends CallbackHandler {
  public void onRequest(SCSession session, CReqCallBack req) {
    CRespCrossHeartBeat resp = new CRespCrossHeartBeat();
    sendBack(session, req, resp);
  }
  
  public int cmdType() {
    return 1;
  }
}
