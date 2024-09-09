package com.kitty.cross.demo;

import com.kitty.cross.core.callback.CReqCallBack;
import com.kitty.cross.core.server.SCSession;
import org.springframework.stereotype.Component;
import com.kitty.cross.core.callback.CallbackHandler;

@Component
public class HelloCallBackHandler extends CallbackHandler
{
    public HelloCallBackHandler() {
    }
    
    @Override
    public void onRequest(SCSession session, CReqCallBack req) {
        CRespCrossHeartBeat resp = new CRespCrossHeartBeat();
        this.sendBack(session, req, resp);
    }
    
    @Override
    public int cmdType() {
        return 1;
    }
}
