
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45144)
public class ReqRequestLineInfo extends Message
{
    private String accountId;
    
    public String getAccountId() {
        return this.accountId;
    }
    
    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }
    
    @Override
    public String toString() {
        return "ReqRequestLineInfo{accountId='" + this.accountId + '\'' + '}';
    }
}
