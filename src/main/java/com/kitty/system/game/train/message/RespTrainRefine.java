package com.kitty.system.game.train.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20170)
public class RespTrainRefine extends Message
{
    private int refine;
    
    public int getRefine() {
        return this.refine;
    }
    
    public void setRefine(final int refine) {
        this.refine = refine;
    }
    
    public RespTrainRefine(final int refine) {
        this.refine = 0;
        this.refine = refine;
    }
    
    public RespTrainRefine() {
        this.refine = 0;
    }
}
