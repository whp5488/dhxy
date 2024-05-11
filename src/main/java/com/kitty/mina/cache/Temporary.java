
package com.kitty.mina.cache;

import com.kitty.system.game.role.domain.Role;
import java.util.concurrent.ConcurrentHashMap;

public interface Temporary
{
    public static final ConcurrentHashMap<Long, Role> ROLES = new ConcurrentHashMap<Long, Role>();
}
