
package com.kitty.system.game.welfare.message;

import com.kitty.system.game.welfare.domain.GiftsInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49172)
public class RespPayBieGift extends Message
{
    private List<GiftsInfo> gifts;
    private String total;
    private byte type;
    
    public List<GiftsInfo> getGifts() {
        return this.gifts;
    }
    
    public void setGifts(final List<GiftsInfo> gifts) {
        this.gifts = gifts;
    }
    
    public String getTotal() {
        return this.total;
    }
    
    public void setTotal(final String total) {
        this.total = total;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public RespPayBieGift(final List<GiftsInfo> gifts, final String total, final byte type) {
        this.type = 0;
        this.gifts = gifts;
        this.total = total;
        this.type = type;
    }
    
    public RespPayBieGift() {
        this.type = 0;
    }
}
