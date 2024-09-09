package com.kitty.system.game.fightnew.spectator.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4241)
public class ReqLookOn extends Message
{
    private String objId;
    private byte isNo;
    private boolean isA;
    
    public ReqLookOn() {
        this.isA = true;
    }
    
    public String getObjId() {
        return this.objId;
    }
    
    public byte getIsNo() {
        return this.isNo;
    }
    
    public boolean isA() {
        return this.isA;
    }
    
    public void setObjId(final String objId) {
        this.objId = objId;
    }
    
    public void setIsNo(final byte isNo) {
        this.isNo = isNo;
    }
    
    public void setA(final boolean isA) {
        this.isA = isA;
    }
}
