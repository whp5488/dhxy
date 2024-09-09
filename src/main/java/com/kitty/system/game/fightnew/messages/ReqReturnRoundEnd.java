package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 3472)
public class ReqReturnRoundEnd extends Message
{
    private String recordId;
    private int roleId;
    private int round;
    
    public String getRecordId() {
        return this.recordId;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getRound() {
        return this.round;
    }
    
    public void setRecordId(final String recordId) {
        this.recordId = recordId;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setRound(final int round) {
        this.round = round;
    }
    
    public ReqReturnRoundEnd(final String recordId, final int roleId, final int round) {
        this.recordId = recordId;
        this.roleId = roleId;
        this.round = round;
    }
    
    public ReqReturnRoundEnd() {
    }
}
