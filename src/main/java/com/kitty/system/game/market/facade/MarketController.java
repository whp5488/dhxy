
package com.kitty.system.game.market.facade;

import com.kitty.system.game.market.message.ReqGoodInfo;
import com.kitty.system.game.market.message.ReqBuyFromStall;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.market.message.ReqSetStallGoods;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.market.service.MarketService;
import org.springframework.stereotype.Controller;

@Controller
public class MarketController
{
    @Autowired
    private MarketService marketService;
    
    @RequestMapping
    public void upShell(final IoSession session, final ReqSetStallGoods reqSetStallGoods) {
        this.marketService.upShell(session, reqSetStallGoods);
    }
    
    @RequestMapping
    public void buyGoods(final IoSession session, final ReqBuyFromStall reqBuyFromStall) {
        this.marketService.buyGoods(session, reqBuyFromStall);
    }
    
    @RequestMapping
    public void goodsInfo(final IoSession session, final ReqGoodInfo reqGoodInfo) {
        this.marketService.goodsInfo(session, reqGoodInfo);
    }
}
