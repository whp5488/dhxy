package com.kitty.cross.core.server;

import com.kitty.cross.core.client.CCSession;
import com.kitty.mina.message.Message;

public interface CMessageDispatcher
{
    public abstract void serverDispatch(SCSession session, Message message);
    
    public abstract void clientDispatch(CCSession session, Message message);
}
