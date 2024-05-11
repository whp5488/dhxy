
package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class RemovePetEvent extends BasePlayerEvent
{
    private Role role;
    private Pet pet;
    
    public RemovePetEvent(final EventType evtType, final Role role, final Pet pet) {
        super(evtType, role);
        this.role = role;
        this.pet = pet;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public Pet getPet() {
        return this.pet;
    }
}
