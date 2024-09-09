package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32848)
public class ReqPartyRedBagRecord extends Message
{
    private byte type;
    
    public ReqPartyRedBagRecord() {
    }
    
    public byte getType() {
        return this.type;
    }
}
