package com.kitty.cross.core.callback;

import com.kitty.cross.core.server.SCSession;
import com.kitty.mina.message.Message;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

public abstract class CallbackHandler {
  private static Map<Integer, CallbackHandler> handlers = new HashMap<>();
  
  @PostConstruct
  private void init() {
    handlers.put(Integer.valueOf(cmdType()), this);
  }
  
  public abstract void onRequest(SCSession paramSCSession, CReqCallBack paramCReqCallBack);
  
  public void sendBack(SCSession session, CReqCallBack req, Message response) {
    CRespCallBack callBack = CRespCallBack.valueOf(response);
    callBack.setIndex(req.getIndex());
    session.sendMessage(callBack);
  }
  
  public abstract int cmdType();
  
  public static CallbackHandler queryHandler(int type) {
    return handlers.get(Integer.valueOf(type));
  }
}
