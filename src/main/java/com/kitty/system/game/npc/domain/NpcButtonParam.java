package com.kitty.system.game.npc.domain;

import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.role.domain.Role;

public class NpcButtonParam
{
    private Role role;
    private Npc npc;
    private String msg;
    private boolean auto;
    
    public NpcButtonParam(Role role, Npc npc, String msg, boolean auto) {
        this.role = role;
        this.npc = npc;
        this.msg = msg;
        this.auto = auto;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public Npc getNpc() {
        return this.npc;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public boolean isAuto() {
        return this.auto;
    }
}
