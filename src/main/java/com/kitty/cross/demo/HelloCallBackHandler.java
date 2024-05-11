
package com.kitty.cross.demo;

import com.kitty.mina.message.Message;
import com.kitty.cross.core.callback.CReqCallBack;
import com.kitty.cross.core.server.SCSession;
import org.springframework.stereotype.Component;
import com.kitty.cross.core.callback.CallbackHandler;

@Component
public class HelloCallBackHandler extends CallbackHandler
{
    @Override
    public void onRequest(final SCSession session, final CReqCallBack req) {
        final CRespCrossHeartBeat resp = new CRespCrossHeartBeat();
        this.sendBack(session, req, resp);
    }
    
    @Override
    public int cmdType() {
        return 1;
    }
}
