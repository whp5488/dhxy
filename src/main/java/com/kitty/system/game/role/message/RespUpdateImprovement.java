package com.kitty.system.game.role.message;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65511)
public class RespUpdateImprovement extends Message
{
    private int id;
    private List<FiedValue> list;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setList(List<FiedValue> list) {
        this.list = list;
    }
    
    public RespUpdateImprovement() {
    }
    
    public RespUpdateImprovement(int id, List<FiedValue> list) {
        this.id = id;
        this.list = list;
    }
}
