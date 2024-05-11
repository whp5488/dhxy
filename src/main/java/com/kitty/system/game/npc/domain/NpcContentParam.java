
package com.kitty.system.game.npc.domain;

import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.role.domain.Role;

public class NpcContentParam
{
    private Role role;
    private Npc npc;
    
    public NpcContentParam(final Role role, final Npc npc) {
        this.role = role;
        this.npc = npc;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public Npc getNpc() {
        return this.npc;
    }
}
