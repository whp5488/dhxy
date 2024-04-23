package com.kitty.system.common.utils;

import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.util.NutMap;

public class SessionDataUtils {
  public static NutMap getData(Role role) {
    if (role == null)
      return new NutMap(); 
    IoSession session = SessionUtils.getSessionBy(role.getId().longValue());
    if (session != null) {
      Object object = session.getAttribute("data");
      if (object == null) {
        NutMap nutMap = new NutMap();
        session.setAttribute("data", nutMap);
      } 
      object = session.getAttribute("data");
      return (NutMap)object;
    } 
    return new NutMap();
  }
}
