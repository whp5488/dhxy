package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8405)
public class RespSwitchServer extends Message
{
    private short result;
    private String content;
    
    public RespSwitchServer() {
        this.result = 1;
    }
    
    public short getResult() {
        return this.result;
    }
    
    public void setResult(short result) {
        this.result = result;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}
