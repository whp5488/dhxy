package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4161)
public class ReqGetTeamById extends Message
{
    private int roleId;
    
    public ReqGetTeamById() {
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
}
