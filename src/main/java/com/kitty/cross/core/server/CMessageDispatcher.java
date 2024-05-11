
package com.kitty.cross.core.server;

import com.kitty.cross.core.client.CCSession;
import com.kitty.mina.message.Message;

public interface CMessageDispatcher
{
    void serverDispatch(final SCSession p0, final Message p1);
    
    void clientDispatch(final CCSession p0, final Message p1);
}
