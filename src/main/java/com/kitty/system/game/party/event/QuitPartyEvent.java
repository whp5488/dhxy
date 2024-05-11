
package com.kitty.system.game.party.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class QuitPartyEvent extends BasePlayerEvent
{
    private Role role;
    private Party party;
    
    public QuitPartyEvent(final EventType evtType, final Role role, final Party party) {
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
