
package com.kitty.cross.demo;

import com.kitty.cross.core.client.CCSession;
import com.kitty.mina.message.Message;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cross.core.CrossTransportManager;
import com.kitty.cross.core.client.C2SSessionPoolFactory;
import com.kitty.cross.core.callback.CReqCallBack;
import org.springframework.stereotype.Service;

@Service
public class CrossDemoGameService
{
    public void sayHello() {
        final CReqCallBack req = new CReqCallBack();
        req.addParam("name", "Lily");
        req.setCmd(1);
        final CCSession session = C2SSessionPoolFactory.getInstance().borrowCrossSession();
        try {
            final Message callBack = SpringUtils.getBean(CrossTransportManager.class).callBack(session, req);
            System.out.println(callBack);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
