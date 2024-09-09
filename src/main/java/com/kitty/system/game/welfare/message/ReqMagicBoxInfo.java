package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45386)
public class ReqMagicBoxInfo extends Message
{
    private int affiliation;
    
    public ReqMagicBoxInfo() {
    }
    
    public int getAffiliation() {
        return this.affiliation;
    }
}
