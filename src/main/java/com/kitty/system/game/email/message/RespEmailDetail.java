
package com.kitty.system.game.email.message;

import com.kitty.system.game.email.domain.EmailSystemAccessory;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4320)
public class RespEmailDetail extends Message
{
    private String emailId;
    private String emailTitle;
    private String senderName;
    private String content;
    List<EmailSystemAccessory> accessorys;
    
    public String getEmailId() {
        return this.emailId;
    }
    
    public String getEmailTitle() {
        return this.emailTitle;
    }
    
    public String getSenderName() {
        return this.senderName;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public List<EmailSystemAccessory> getAccessorys() {
        return this.accessorys;
    }
    
    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }
    
    public void setEmailTitle(final String emailTitle) {
        this.emailTitle = emailTitle;
    }
    
    public void setSenderName(final String senderName) {
        this.senderName = senderName;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    public void setAccessorys(final List<EmailSystemAccessory> accessorys) {
        this.accessorys = accessorys;
    }
}
