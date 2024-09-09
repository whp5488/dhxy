package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32846)
public class ReqShowRedBag extends Message
{
    private String redBagGid;
    
    public ReqShowRedBag() {
    }
    
    public String getRedBagGid() {
        return this.redBagGid;
    }
}
