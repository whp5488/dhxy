package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4113)
public class ReqFightNewPK extends Message
{
    private int roleId;
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public ReqFightNewPK(final int roleId) {
        this.roleId = roleId;
    }
    
    public ReqFightNewPK() {
    }
}
