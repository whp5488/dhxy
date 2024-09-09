package com.kitty.cross.core.callback;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.JsonUtils;
import com.kitty.mina.message.Message;
import com.kitty.cross.core.client.CCSession;
import com.kitty.cross.core.server.RpcRequestMapping;
import com.kitty.cross.core.server.SCSession;
import org.springframework.stereotype.Controller;

@Controller
public class CallbackController
{
    public CallbackController() {
    }
    
    @RpcRequestMapping
    public void onReqCallBack(SCSession session, CReqCallBack req) {
        int cmdType = req.getCmd();
        CallbackHandler handler = CallbackHandler.queryHandler(cmdType);
        if (handler != null) {
            req.deserialize();
            handler.onRequest(session, req);
        }
    }
    
    @RpcRequestMapping
    public void onRespCallBack(CCSession session, CRespCallBack respCallBack) {
        String json = respCallBack.getData();
        try {
            Message message = (Message)JsonUtils.string2Object(json, Class.forName(respCallBack.getMsgClass()));
            SpringUtils.getBean(CallBackService.class).fillCallBack(respCallBack.getIndex(), message);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
