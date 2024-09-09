package com.kitty.system.game.market.message;

import com.kitty.system.game.market.model.Cargo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49179)
public class RespStallMine extends Message
{
    private byte stallTotalNum;
    private List<Cargo> list;
    
    public RespStallMine() {
    }
    
    public void setStallTotalNum(final byte stallTotalNum) {
        this.stallTotalNum = stallTotalNum;
    }
    
    public void setList(final List<Cargo> list) {
        this.list = list;
    }
    
    public byte getStallTotalNum() {
        return this.stallTotalNum;
    }
    
    public List<Cargo> getList() {
        return this.list;
    }
}
