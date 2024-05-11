
package com.kitty.listener.event.ranking;

import com.kitty.listener.EventType;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.List;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.listener.BaseGameEvent;

public class PetChangeEvent extends BaseGameEvent
{
    private Pet pet;
    private List<PetEquip> equips;
    
    public PetChangeEvent(final EventType evtType, final Pet pet, final List<PetEquip> equips) {
        super(evtType);
        this.pet = pet;
        this.equips = equips;
    }
    
    public Pet getPet() {
        return this.pet;
    }
    
    public List<PetEquip> getEquips() {
        return this.equips;
    }
}
