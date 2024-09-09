package com.kitty.system.game.train.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20201)
public class ReqMagicCircle extends Message
{
    private int magicCircle;
    
    public ReqMagicCircle() {
        this.magicCircle = 0;
    }
    
    public int getMagicCircle() {
        return this.magicCircle;
    }
    
    public void setMagicCircle(final int magicCircle) {
        this.magicCircle = magicCircle;
    }
}
