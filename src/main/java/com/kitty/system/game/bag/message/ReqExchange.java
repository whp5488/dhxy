
package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20654)
public class ReqExchange extends Message
{
    private int serial;
    private short num;
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public short getNum() {
        return this.num;
    }
    
    public void setNum(final short num) {
        this.num = num;
    }
}
