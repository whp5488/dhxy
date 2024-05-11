
package com.kitty.system.game.market.message;

import com.kitty.system.game.market.message.vo.MarketDealVo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49185)
public class ResMarketRecords extends Message
{
    private List<MarketDealVo> buyList;
    private List<MarketDealVo> sellList;
    
    public void setBuyList(final List<MarketDealVo> buyList) {
        this.buyList = buyList;
    }
    
    public void setSellList(final List<MarketDealVo> sellList) {
        this.sellList = sellList;
    }
    
    public List<MarketDealVo> getBuyList() {
        return this.buyList;
    }
    
    public List<MarketDealVo> getSellList() {
        return this.sellList;
    }
}
