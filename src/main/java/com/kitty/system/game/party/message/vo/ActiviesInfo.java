
package com.kitty.system.game.party.message.vo;

public class ActiviesInfo
{
    private String name;
    private short participation;
    private String activeId;
    
    public String getName() {
        return this.name;
    }
    
    public short getParticipation() {
        return this.participation;
    }
    
    public String getActiveId() {
        return this.activeId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setParticipation(final short participation) {
        this.participation = participation;
    }
    
    public void setActiveId(final String activeId) {
        this.activeId = activeId;
    }
}
