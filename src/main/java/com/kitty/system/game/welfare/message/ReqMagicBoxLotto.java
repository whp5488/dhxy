package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45389)
public class ReqMagicBoxLotto extends Message
{
    private int affiliation;
    private byte type;
    
    public ReqMagicBoxLotto() {
    }
    
    public int getAffiliation() {
        return this.affiliation;
    }
    
    public byte getType() {
        return this.type;
    }
}
