
package com.kitty.system.game.email.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4310)
public class ReqEmailDetail extends Message
{
    private byte isSystem;
    private String emailId;
    
    public byte getIsSystem() {
        return this.isSystem;
    }
    
    public String getEmailId() {
        return this.emailId;
    }
    
    public void setIsSystem(final byte isSystem) {
        this.isSystem = isSystem;
    }
    
    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }
}
