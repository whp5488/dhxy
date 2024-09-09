package com.kitty.system.game.pk.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45234)
public class RespReleasePrisonerSuccess extends Message
{
    private int id;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public RespReleasePrisonerSuccess() {
    }
    
    public RespReleasePrisonerSuccess(final int id) {
        this.id = id;
    }
}
