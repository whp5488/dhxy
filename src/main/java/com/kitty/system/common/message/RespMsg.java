package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import org.nutz.json.ToJson;
import com.kitty.mina.message.Message;

@ToJson
@MessageMeta(module = 8165)
public class RespMsg extends Message
{
    private String msg;
    private short active;
    
    public RespMsg() {
        this.active = 0;
    }
    
    public RespMsg(String msg) {
        this.active = 0;
        this.msg = msg;
    }
    
    public RespMsg(String msg, short active) {
        this.active = 0;
        this.msg = msg;
        this.active = active;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public short getActive() {
        return this.active;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setActive(final short active) {
        this.active = active;
    }
}
