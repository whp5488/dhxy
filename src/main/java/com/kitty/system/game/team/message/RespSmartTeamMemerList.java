
package com.kitty.system.game.team.message;

import java.util.ArrayList;
import com.kitty.system.game.team.domain.vo.SmartTeamMember;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4117)
public class RespSmartTeamMemerList extends Message
{
    private String serial;
    private String name;
    private List<SmartTeamMember> teamMemberList;
    
    public RespSmartTeamMemerList() {
        this.teamMemberList = new ArrayList<SmartTeamMember>();
    }
    
    public String getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<SmartTeamMember> getTeamMemberList() {
        return this.teamMemberList;
    }
    
    public void setSerial(final String serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setTeamMemberList(final List<SmartTeamMember> teamMemberList) {
        this.teamMemberList = teamMemberList;
    }
}
