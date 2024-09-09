package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4290)
public class ReqUseIncense extends Message
{
    private Integer serial;
    private Integer residueTimes;
    
    public ReqUseIncense() {
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
