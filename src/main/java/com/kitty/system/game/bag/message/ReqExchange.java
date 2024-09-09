package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20654)
public class ReqExchange extends Message
{
    private int serial;
    private short num;
    
    public ReqExchange() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setSerial(int serial) {
        this.serial = serial;
    }
    
    public short getNum() {
        return this.num;
    }
    
    public void setNum(short num) {
        this.num = num;
    }
}
