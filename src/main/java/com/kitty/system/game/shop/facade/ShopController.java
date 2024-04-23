package com.kitty.system.game.shop.facade;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.shop.message.ReqBuyItem;
import com.kitty.system.game.shop.service.ShopService;
import com.kitty.system.game.vo.Npc;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ShopController {
  @Autowired
  private ShopService shopService;
  
  @RequestMapping
  public void buyItem(IoSession session, ReqBuyItem reqBuyItem) {
    Role role = SessionUtils.getRoleBySession(session);
    MapService mapService = (MapService)SpringUtils.getBean(MapService.class);
    Npc npc = mapService.getNpc(reqBuyItem.getShipper());
    if (npc != null && npc.getShipper() != null && role != null) {
      switch (npc.getShipper()) {
        default:
          return;
        case "buy_npc_pet":
          this.shopService.buyNpcPet(role, reqBuyItem.getSerial(), reqBuyItem.getAmount());
        case "buy_npc_drug":
          this.shopService.buyNpcDrug(role, reqBuyItem.getSerial(), reqBuyItem.getAmount());
        case "buy_npc_equip":
          this.shopService.buyNpcEquip(role, reqBuyItem.getSerial(), reqBuyItem.getAmount());
        case "buy_npc_grocery":
          this.shopService.buyNpcGrocery(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
        case "buy_npc_guo":
          if (!SafeUtil.isUnlock(role))
            return; 
          if (reqBuyItem.getType() == 0) {
            this.shopService.buyNpcGuo(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
          } else if (reqBuyItem.getType() == 1) {
            this.shopService.recycleNpcGuo(role, reqBuyItem.getParam(), reqBuyItem.getSerial(), reqBuyItem.getAmount());
          } 
        case "buy_npc_brac":
          if (!SafeUtil.isUnlock(role))
            return; 
          this.shopService.buyNpcBrac(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
        case "buy_npc_nao":
          if (!SafeUtil.isUnlock(role))
            return; 
          this.shopService.buyNpcNao(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
        case "buy_npc_di":
          if (!SafeUtil.isUnlock(role))
            return; 
          this.shopService.buyNpcDi(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
        case "buy_npc_arena":
          if (!SafeUtil.isUnlock(role))
            return; 
          this.shopService.buyNpcArena(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
        case "buy_npc_long":
          if (!SafeUtil.isUnlock(role))
            return; 
          this.shopService.buyNpcLong(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
        case "buy_npc_nice":
          if (!SafeUtil.isUnlock(role))
            return; 
          this.shopService.buyNpcNice(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
        case "buy_npc_feats":
          break;
      } 
      if (!SafeUtil.isUnlock(role))
        return; 
      this.shopService.buyNpcFeats(role, reqBuyItem.getSerial(), reqBuyItem.getAmount(), reqBuyItem.getShipper());
    } 
  }
}
