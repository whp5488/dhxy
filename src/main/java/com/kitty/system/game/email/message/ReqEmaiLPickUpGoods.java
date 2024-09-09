package com.kitty.system.game.email.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4312)
public class ReqEmaiLPickUpGoods extends Message
{
    private byte type;
    private String emailId;
    
    public ReqEmaiLPickUpGoods() {
    }
    
    public byte getType() {
        return this.type;
    }
    
    public String getEmailId() {
        return this.emailId;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }
}
