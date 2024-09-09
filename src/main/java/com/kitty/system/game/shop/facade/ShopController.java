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
    
    public ShopController() {
    }
    
    @RequestMapping
    public void buyItem(IoSession session, ReqBuyItem reqBuyItem) {
        Role role = SessionUtils.getRoleBySession(session);
        MapService mapService = (MapService)SpringUtils.getBean(MapService.class);
        Npc npc = mapService.getNpc(reqBuyItem.getShipper());
        if (npc != null && npc.getShipper() != null && role != null) {
            String shipper = npc.getShipper();
            int n = -1;
            switch (shipper.hashCode()) {
                case -978226680: {
                    if (shipper.equals("buy_npc_pet")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case -260600873: {
                    if (shipper.equals("buy_npc_drug")) {
                        n = 2;
                        break;
                    }
                    break;
                }
                case 512201433: {
                    if (shipper.equals("buy_npc_equip")) {
                        n = 3;
                        break;
                    }
                    break;
                }
                case 102247798: {
                    if (shipper.equals("buy_npc_grocery")) {
                        n = 4;
                        break;
                    }
                    break;
                }
                case -978234838: {
                    if (shipper.equals("buy_npc_guo")) {
                        n = 5;
                        break;
                    }
                    break;
                }
                case -260661079: {
                    if (shipper.equals("buy_npc_brac")) {
                        n = 6;
                        break;
                    }
                    break;
                }
                case -978228731: {
                    if (shipper.equals("buy_npc_nao")) {
                        n = 7;
                        break;
                    }
                    break;
                }
                case -1001387396: {
                    if (shipper.equals("buy_npc_di")) {
                        n = 8;
                        break;
                    }
                    break;
                }
                case 508521904: {
                    if (shipper.equals("buy_npc_arena")) {
                        n = 9;
                        break;
                    }
                    break;
                }
                case -260365645: {
                    if (shipper.equals("buy_npc_long")) {
                        n = 10;
                        break;
                    }
                    break;
                }
                case -260312172: {
                    if (shipper.equals("buy_npc_nice")) {
                        n = 11;
                        break;
                    }
                    break;
                }
                case 512748586: {
                    if (shipper.equals("buy_npc_feats")) {
                        n = 12;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 1: {
                    this.shopService.buyNpcPet(role, reqBuyItem.getSerial(), reqBuyItem.getAmount());
                    break;
                }
                case 2: {
                    this.shopService.buyNpcDrug(role, reqBuyItem.getSerial(), reqBuyItem.getAmount());
                    break;
                }
                case 3: {
                    this.shopService.buyNpcEquip(role, reqBuyItem.getSerial(), reqBuyItem.getAmount());
                    break;
                }
                case 4: {
                    this.shopService.buyNpcGrocery(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case 5: {
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
                case 6: {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcBrac(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case 7: {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcNao(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case 8: {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcDi(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case 9: {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcArena(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case 10: {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcLong(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case 11: {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcNice(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                case 12: {
                    if (!SafeUtil.isUnlock(role)) {
                        return;
                    }
                    this.shopService.buyNpcFeats(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}
