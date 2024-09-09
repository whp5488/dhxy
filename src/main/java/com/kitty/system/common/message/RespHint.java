package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8866)
public class RespHint extends Message
{
    private int type;
    private short result;
    
    public int getType() {
        return this.type;
    }
    
    public short getResult() {
        return this.result;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setResult(final short result) {
        this.result = result;
    }

    public RespHint() {
        this.result = 0;
    }
    
    public RespHint(final int type, final short result) {
        this.result = 0;
        this.type = type;
        this.result = result;
    }
}
