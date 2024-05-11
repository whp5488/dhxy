
package com.kitty.system.game.pk.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45227)
public class ReqTraceRole extends Message
{
    private int roleId;
    
    public int getRoleId() {
        return this.roleId;
    }
}
