package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12490)
public class ReqBuyFromStall extends Message
{
    private String id;
    private short amount;
    private String para;
    
    public ReqBuyFromStall() {
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setAmount(final short amount) {
        this.amount = amount;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    public String getId() {
        return this.id;
    }
    
    public short getAmount() {
        return this.amount;
    }
    
    public String getPara() {
        return this.para;
    }
}
