
package com.kitty.system.game.team.domain.vo;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class SmartTeam implements Serializable
{
    List<SmartTeamMember> smartTeamMembers;
    
    public List<SmartTeamMember> getSmartTeamMembers() {
        return this.smartTeamMembers;
    }
    
    public void setSmartTeamMembers(final List<SmartTeamMember> smartTeamMembers) {
        this.smartTeamMembers = smartTeamMembers;
    }
    
    @ConstructorProperties({ "smartTeamMembers" })
    public SmartTeam(final List<SmartTeamMember> smartTeamMembers) {
        this.smartTeamMembers = new ArrayList<SmartTeamMember>();
        this.smartTeamMembers = smartTeamMembers;
    }
}
