
package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32840)
public class ReqPartySendRedBag extends Message
{
    private short money;
    private short num;
    private String msg;
    private String format;
    private String actName;
    private String actId;
    
    public short getMoney() {
        return this.money;
    }
    
    public short getNum() {
        return this.num;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public String getFormat() {
        return this.format;
    }
    
    public String getActName() {
        return this.actName;
    }
    
    public String getActId() {
        return this.actId;
    }
}
