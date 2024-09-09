package com.kitty.system.common.message;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45060)
public class RespStateRefresh extends Message
{
    private List<FiedValue> list;
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    public RespStateRefresh(final List<FiedValue> list) {
        this.list = list;
    }
    
    public RespStateRefresh() {
    }
}
