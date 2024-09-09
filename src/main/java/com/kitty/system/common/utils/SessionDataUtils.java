package com.kitty.system.common.utils;

import org.apache.mina.core.session.IoSession;
import com.kitty.mina.cache.SessionUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;

public class SessionDataUtils
{
    public SessionDataUtils() {
    }
    
    public static NutMap getData(Role role) {
        if (role == null) {
            return new NutMap();
        }
        IoSession session = SessionUtils.getSessionBy((long)role.getId());
        if (session != null) {
            Object object = session.getAttribute("data");
            if (object == null) {
                NutMap nutMap = new NutMap();
                session.setAttribute("data", nutMap);
            }
            object = session.getAttribute("data");
            return (NutMap)object;
        }
        else {
            return new NutMap();
        }
    }
}
