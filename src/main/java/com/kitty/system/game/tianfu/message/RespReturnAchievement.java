
package com.kitty.system.game.tianfu.message;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 44003)
public class RespReturnAchievement extends Message
{
    private List<FiedValue> list;
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @ConstructorProperties({ "list" })
    public RespReturnAchievement(final List<FiedValue> list) {
        this.list = new ArrayList<FiedValue>();
        this.list = list;
    }
    
    public RespReturnAchievement() {
        this.list = new ArrayList<FiedValue>();
    }
}
