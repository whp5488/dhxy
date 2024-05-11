
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
    @RpcRequestMapping
    public void onReqCallBack(final SCSession session, final CReqCallBack req) {
        final int cmdType = req.getCmd();
        final CallbackHandler handler = CallbackHandler.queryHandler(cmdType);
        if (handler != null) {
            req.deserialize();
            handler.onRequest(session, req);
        }
    }
    
    @RpcRequestMapping
    public void onRespCallBack(final CCSession session, final CRespCallBack respCallBack) {
        final String json = respCallBack.getData();
        try {
            final Message message = (Message)JsonUtils.string2Object(json, Class.forName(respCallBack.getMsgClass()));
            SpringUtils.getBean(CallBackService.class).fillCallBack(respCallBack.getIndex(), message);
        }
        catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
