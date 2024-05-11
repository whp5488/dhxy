
package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41191)
public class RespOperateResult extends Message
{
    private boolean isSuccess;
    private String type;
    
    public boolean isSuccess() {
        return this.isSuccess;
    }
    
    public void setSuccess(final boolean success) {
        this.isSuccess = success;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
}
