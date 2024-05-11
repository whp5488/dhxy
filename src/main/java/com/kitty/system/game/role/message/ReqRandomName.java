
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45073)
public class ReqRandomName extends Message
{
    private Integer gender;
    
    public Integer getSex() {
        return this.gender;
    }
    
    public void setSex(final Integer sex) {
        this.gender = sex;
    }
}
