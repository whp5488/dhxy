package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32956)
public class ReqExchangeContactSeller extends Message
{
    private short serial;
    private String id;
    
    public ReqExchangeContactSeller() {
    }
    
    public short getSerial() {
        return this.serial;
    }
    
    public String getId() {
        return this.id;
    }
}
