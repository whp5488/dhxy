
package com.kitty.system.common.utils;

import org.apache.mina.core.session.IoSession;
import com.kitty.mina.cache.SessionUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;

public class SessionDataUtils
{
    public static NutMap getData(final Role role) {
        if (role == null) {
            return new NutMap();
        }
        final IoSession session = SessionUtils.getSessionBy(role.getId());
        if (session != null) {
            Object object = session.getAttribute((Object)"data");
            if (object == null) {
                final NutMap nutMap = new NutMap();
                session.setAttribute((Object)"data", (Object)nutMap);
            }
            object = session.getAttribute((Object)"data");
            return (NutMap)object;
        }
        return new NutMap();
    }
}
