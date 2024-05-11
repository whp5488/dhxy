
package com.kitty.cross.demo;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = -2)
public class CRespCrossHeartBeat extends Message
{
    private long time;
    
    public CRespCrossHeartBeat() {
        this.time = System.currentTimeMillis();
    }
    
    public long getTime() {
        return this.time;
    }
    
    public void setTime(final long time) {
        this.time = time;
    }
}
