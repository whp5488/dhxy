
package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42160)
public class RespUseIncense extends Message
{
    private Integer serial;
    private Integer residueTimes;
    
    public RespUseIncense() {
    }
    
    public RespUseIncense(final Integer serial, final Integer residueTimes) {
        this.serial = serial;
        this.residueTimes = residueTimes;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getResidueTimes() {
        return this.residueTimes;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setResidueTimes(final Integer residueTimes) {
        this.residueTimes = residueTimes;
    }
}
