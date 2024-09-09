package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class RoleChangeEvent extends BasePlayerEvent
{
    public static int REI;
    public static int RACE;
    public static int NAME;
    private Role role;
    private String prevName;
    private String currName;
    private int type;
    
    public RoleChangeEvent(EventType evtType, Role role, String prevName, String currName, int type) {
        super(evtType, role);
        this.role = role;
        this.prevName = prevName;
        this.currName = currName;
        this.type = type;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public String getPrevName() {
        return this.prevName;
    }
    
    public String getCurrName() {
        return this.currName;
    }
    
    public int getType() {
        return this.type;
    }
    
    static {
        RoleChangeEvent.REI = 0;
        RoleChangeEvent.RACE = 1;
        RoleChangeEvent.NAME = 2;
    }
}
