package com.kitty.system.game.tianfu.message;

import java.util.ArrayList;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 44004)
public class RespTianFuDelete extends Message
{
    List<String> tianFuIds;
    
    public List<String> getTianFuIds() {
        return this.tianFuIds;
    }
    
    public void setTianFuIds(final List<String> tianFuIds) {
        this.tianFuIds = tianFuIds;
    }
    
    public RespTianFuDelete(final List<String> tianFuIds) {
        this.tianFuIds = new ArrayList<>();
        this.tianFuIds = tianFuIds;
    }
    
    public RespTianFuDelete() {
        this.tianFuIds = new ArrayList<>();
    }
}
