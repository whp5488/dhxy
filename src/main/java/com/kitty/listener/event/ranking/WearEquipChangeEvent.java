
package com.kitty.listener.event.ranking;

import com.kitty.listener.EventType;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.listener.BaseGameEvent;

public class WearEquipChangeEvent extends BaseGameEvent
{
    private WearEquip wearEquip;
    
    public WearEquipChangeEvent(final EventType eventType, final WearEquip wearEquip) {
        super(eventType);
        this.wearEquip = wearEquip;
    }
    
    public WearEquip getWearEquip() {
        return this.wearEquip;
    }
    
    public void setWearEquip(final WearEquip wearEquip) {
        this.wearEquip = wearEquip;
    }
}
