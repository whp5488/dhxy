
package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45385)
public class ReqLuckDraw extends Message
{
    private byte type;
    private byte classify;
    
    public byte getType() {
        return this.type;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public byte getClassify() {
        return this.classify;
    }
    
    public void setClassify(final byte classify) {
        this.classify = classify;
    }
}
