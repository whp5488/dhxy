package com.kitty.system.game.instruct.inter;

import com.kitty.system.game.role.domain.Role;

public interface NcFather
{
    public abstract void handle(Role role, String content);
}
