
package com.kitty.system.game.party.message;

import com.kitty.system.game.party.message.vo.RecvRedBagInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32849)
public class RespPartyRedBagRecord extends Message
{
    private byte type;
    private int count;
    private int total;
    private List<RecvRedBagInfo> recvRedBagInfoList;
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setCount(final int count) {
        this.count = count;
    }
    
    public void setTotal(final int total) {
        this.total = total;
    }
    
    public void setRecvRedBagInfoList(final List<RecvRedBagInfo> recvRedBagInfoList) {
        this.recvRedBagInfoList = recvRedBagInfoList;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public int getTotal() {
        return this.total;
    }
    
    public List<RecvRedBagInfo> getRecvRedBagInfoList() {
        return this.recvRedBagInfoList;
    }
}
