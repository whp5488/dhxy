package com.kitty.system.game.train.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20171)
public class RespMagicCircle extends Message
{
    private int magicCircle;
    private int t;
    
    public int getMagicCircle() {
        return this.magicCircle;
    }
    
    public int getT() {
        return this.t;
    }
    
    public void setMagicCircle(final int magicCircle) {
        this.magicCircle = magicCircle;
    }
    
    public void setT(final int t) {
        this.t = t;
    }
    
    public RespMagicCircle(final int magicCircle, final int t) {
        this.magicCircle = 0;
        this.t = 0;
        this.magicCircle = magicCircle;
        this.t = t;
    }
    
    public RespMagicCircle() {
        this.magicCircle = 0;
        this.t = 0;
    }
}
