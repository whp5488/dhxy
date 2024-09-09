package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41482)
public class RespPrizeDraw extends Message
{
    private short count;
    
    public RespPrizeDraw() {
        this.count = 0;
    }
    
    public short getCount() {
        return this.count;
    }
    
    public void setCount(final short count) {
        this.count = count;
    }
}
