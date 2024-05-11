
package com.kitty.system.game.email.message;

import com.kitty.system.game.email.message.vo.EmailParam;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4321)
public class RespEmailList extends Message
{
    private List<EmailParam> emails;
    
    public RespEmailList(final List<EmailParam> emails) {
        this.emails = emails;
    }
    
    public List<EmailParam> getEmails() {
        return this.emails;
    }
    
    public void setEmails(final List<EmailParam> emails) {
        this.emails = emails;
    }
}
