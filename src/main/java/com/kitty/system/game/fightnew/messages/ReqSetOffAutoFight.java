package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 3473)
public class ReqSetOffAutoFight extends Message
{
    private byte autoFight;
    
    public byte getAutoFight() {
        return this.autoFight;
    }
    
    public void setAutoFight(final byte autoFight) {
        this.autoFight = autoFight;
    }
    
    public ReqSetOffAutoFight(final byte autoFight) {
        this.autoFight = autoFight;
    }
    
    public ReqSetOffAutoFight() {
    }
}
