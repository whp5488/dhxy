package com.kitty.cross.core.server;

import com.kitty.cross.core.client.CCSession;
import com.kitty.mina.message.Message;

public interface CMessageDispatcher {
  void serverDispatch(SCSession paramSCSession, Message paramMessage);
  
  void clientDispatch(CCSession paramCCSession, Message paramMessage);
}
