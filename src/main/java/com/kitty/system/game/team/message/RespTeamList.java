
package com.kitty.system.game.team.message;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import com.kitty.system.game.team.domain.vo.SmartTeam;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4118)
public class RespTeamList extends Message
{
    private List<SmartTeam> smartTeams;
    
    public List<SmartTeam> getSmartTeams() {
        return this.smartTeams;
    }
    
    public void setSmartTeams(final List<SmartTeam> smartTeams) {
        this.smartTeams = smartTeams;
    }
    
    @ConstructorProperties({ "smartTeams" })
    public RespTeamList(final List<SmartTeam> smartTeams) {
        this.smartTeams = new ArrayList<SmartTeam>();
        this.smartTeams = smartTeams;
    }
}
