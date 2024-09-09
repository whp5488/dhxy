package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32842)
public class ReqPartyRecvRedBag extends Message
{
    private String redBagGid;
    
    public ReqPartyRecvRedBag() {
    }
    
    public String getRedBagGid() {
        return this.redBagGid;
    }
}
