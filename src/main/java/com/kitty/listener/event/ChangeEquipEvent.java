
package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class ChangeEquipEvent extends BasePlayerEvent
{
    private Role role;
    private BagEquip bagEquip;
    private WearEquip wearEquip;
    
    public ChangeEquipEvent(final EventType evtType, final Role role, final BagEquip roleEquip, final WearEquip wearEquip) {
        super(evtType, role);
        this.role = role;
        this.bagEquip = roleEquip;
        this.wearEquip = wearEquip;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public BagEquip getBagEquip() {
        return this.bagEquip;
    }
    
    public WearEquip getWearEquip() {
        return this.wearEquip;
    }
}
