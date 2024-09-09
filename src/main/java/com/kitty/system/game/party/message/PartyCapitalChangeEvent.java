package com.kitty.system.game.party.message;

import com.kitty.listener.EventType;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class PartyCapitalChangeEvent extends BasePlayerEvent
{
    private Role role;
    private Party party;
    
    public PartyCapitalChangeEvent(EventType evtType, Role role, Party party) {
        super(evtType, role);
        this.role = role;
        this.party = party;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public Party getParty() {
        return this.party;
    }
}
