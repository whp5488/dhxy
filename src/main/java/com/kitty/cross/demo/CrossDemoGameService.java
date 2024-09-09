package com.kitty.cross.demo;

import com.kitty.mina.message.Message;
import com.kitty.cross.core.client.CCSession;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cross.core.CrossTransportManager;
import com.kitty.cross.core.client.C2SSessionPoolFactory;
import com.kitty.cross.core.callback.CReqCallBack;
import org.springframework.stereotype.Service;

@Service
public class CrossDemoGameService
{
    public CrossDemoGameService() {
    }
    
    public void sayHello() {
        CReqCallBack req = new CReqCallBack();
        req.addParam("name", "Lily");
        req.setCmd(1);
        CCSession session = C2SSessionPoolFactory.getInstance().borrowCrossSession();
        try {
            Message callBack = ((CrossTransportManager)SpringUtils.getBean(CrossTransportManager.class)).callBack(session, req);
            System.out.println(callBack);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
