
package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class MaterialsEvent extends BasePlayerEvent
{
    private Role role;
    private int serial;
    private byte type;
    private int num;
    
    public MaterialsEvent(final EventType evtType, final Role role, final int serial, final int num, final byte type) {
        super(evtType, role);
        this.role = role;
        this.serial = serial;
        this.type = type;
        this.num = num;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getNum() {
        return this.num;
    }
}
