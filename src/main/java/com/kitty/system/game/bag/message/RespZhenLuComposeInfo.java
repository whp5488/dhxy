package com.kitty.system.game.bag.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FieldValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20680)
public class RespZhenLuComposeInfo extends Message
{
    private int serial;
    private List<FieldValue> list;
    
    public RespZhenLuComposeInfo() {
        this.list = new ArrayList<>();
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<FieldValue> getList() {
        return this.list;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setList(final List<FieldValue> list) {
        this.list = list;
    }
}
