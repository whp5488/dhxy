package com.kitty.system.game.fightRecord.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4240)
public class ReqFightRecords extends Message
{
    private byte type;
    private String objId;
    
    public ReqFightRecords() {
    }
    
    public byte getType() {
        return this.type;
    }
    
    public String getObjId() {
        return this.objId;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setObjId(final String objId) {
        this.objId = objId;
    }
}
