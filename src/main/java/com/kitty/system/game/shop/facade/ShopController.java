
package com.kitty.system.game.shop.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.map.service.MapService;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.shop.message.ReqBuyItem;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.shop.service.ShopService;
import org.springframework.stereotype.Controller;

@Controller
public class ShopController
{
    @Autowired
    private ShopService shopService;
    
    @RequestMapping
    public void buyItem(final IoSession session, final ReqBuyItem reqBuyItem) {
        final Role role = SessionUtils.getRoleBySession(session);
        final MapService mapService = SpringUtils.getBean(MapService.class);
        final Npc npc = mapService.getNpc(reqBuyItem.getShipper());
        if (npc != null && npc.getShipper() != null && role != null) {
            final String shipper = npc.getShipper();
            switch (shipper) {
                case "buy_npc_pet": {
                    this.shopService.buyNpcPet(role, reqBuyItem.getSerial(), reqBuyItem.getAmount());
                    break;
                }
                case "buy_npc_drug": {
                    this.shopService.buyNpcDrug(role, reqBuyItem.getSerial(), reqBuyItem.getAmount());
                    break;
                }
                case "buy_npc_equip": {
                    this.shopService.buyNpcEquip(role, reqBuyItem.getSerial(), reqBuyItem.getAmount());
                    break;
                }
                case "buy_npc_grocery": {
                    this.shopService.buyNpcGrocery(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case "buy_npc_guo": {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    if (reqBuyItem.getType() == 0) {
                        this.shopService.buyNpcGuo(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                        break;
                    }
                    if (reqBuyItem.getType() == 1) {
                        this.shopService.recycleNpcGuo(role, reqBuyItem.getParam(), reqBuyItem.getSerial(), reqBuyItem.getAmount());
                        break;
                    }
                    break;
                }
                case "buy_npc_brac": {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcBrac(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case "buy_npc_nao": {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcNao(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case "buy_npc_di": {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcDi(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case "buy_npc_arena": {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcArena(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case "buy_npc_long": {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcLong(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case "buy_npc_nice": {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcNice(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case "buy_npc_feats": {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcFeats(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
            }
        }
    }
}
