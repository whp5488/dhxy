
package com.kitty.system.game.store.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65465)
public class ReqExcuteStorePara extends Message
{
    private byte type;
    private byte storeType;
    private String pos;
    
    public byte getType() {
        return this.type;
    }
    
    public byte getStoreType() {
        return this.storeType;
    }
    
    public String getPos() {
        return this.pos;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setStoreType(final byte storeType) {
        this.storeType = storeType;
    }
    
    public void setPos(final String pos) {
        this.pos = pos;
    }
    
    @ConstructorProperties({ "type", "storeType", "pos" })
    public ReqExcuteStorePara(final byte type, final byte storeType, final String pos) {
        this.storeType = 0;
        this.type = type;
        this.storeType = storeType;
        this.pos = pos;
    }
    
    public ReqExcuteStorePara() {
        this.storeType = 0;
    }
}
