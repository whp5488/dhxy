package com.kitty.system.game.welfare.message;

import com.kitty.system.game.welfare.domain.GiftsInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49171)
public class RespNewBieGift extends Message
{
    private List<GiftsInfo> gifts;
    
    public RespNewBieGift() {
    }
    
    public List<GiftsInfo> getGifts() {
        return this.gifts;
    }
    
    public void setGifts(List<GiftsInfo> gifts) {
        this.gifts = gifts;
    }
}
