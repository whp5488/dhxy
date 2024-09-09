package com.kitty.system.game.tianfu.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 44002)
public class RespDayInCe extends Message
{
    private short level;
    
    public short getLevel() {
        return this.level;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public RespDayInCe(final short level) {
        this.level = level;
    }
}
