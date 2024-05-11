
package com.kitty.cross.core.callback;

import java.util.HashMap;
import com.kitty.mina.message.Message;
import com.kitty.cross.core.server.SCSession;
import javax.annotation.PostConstruct;
import java.util.Map;

public abstract class CallbackHandler
{
    private static Map<Integer, CallbackHandler> handlers;
    
    @PostConstruct
    private void init() {
        CallbackHandler.handlers.put(this.cmdType(), this);
    }
    
    public abstract void onRequest(final SCSession p0, final CReqCallBack p1);
    
    public void sendBack(final SCSession session, final CReqCallBack req, final Message response) {
        final CRespCallBack callBack = CRespCallBack.valueOf(response);
        callBack.setIndex(req.getIndex());
        session.sendMessage(callBack);
    }
    
    public abstract int cmdType();
    
    public static CallbackHandler queryHandler(final int type) {
        return CallbackHandler.handlers.get(type);
    }
    
    static {
        CallbackHandler.handlers = new HashMap<Integer, CallbackHandler>();
    }
}
