package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 6666)
public class RespCherish extends Message
{
    private int serial;
    private short num;
    
    public int getSerial() {
        return this.serial;
    }
    
    public short getNum() {
        return this.num;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final short num) {
        this.num = num;
    }

    public RespCherish() {
        this.serial = 0;
        this.num = 0;
    }
    
    public RespCherish(final int serial, final short num) {
        this.serial = 0;
        this.num = 0;
        this.serial = serial;
        this.num = num;
    }
}
