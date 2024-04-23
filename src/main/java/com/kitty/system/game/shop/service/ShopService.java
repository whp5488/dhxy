package com.kitty.system.game.shop.service;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.enums.common.Mall;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.card.domain.CardChange;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.shop.domain.ItemInfo;
import com.kitty.system.game.shop.message.RespGoodsList;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.init.domain.SysShopNpcArena;
import com.kitty.system.init.domain.SysShopNpcBrac;
import com.kitty.system.init.domain.SysShopNpcDi;
import com.kitty.system.init.domain.SysShopNpcDrug;
import com.kitty.system.init.domain.SysShopNpcEquip;
import com.kitty.system.init.domain.SysShopNpcFeats;
import com.kitty.system.init.domain.SysShopNpcGrocery;
import com.kitty.system.init.domain.SysShopNpcGuo;
import com.kitty.system.init.domain.SysShopNpcGuoRecycle;
import com.kitty.system.init.domain.SysShopNpcLong;
import com.kitty.system.init.domain.SysShopNpcNao;
import com.kitty.system.init.domain.SysShopNpcNice;
import com.kitty.system.init.domain.SysShopNpcPet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopService {
  @Autowired
  public RoleService roleService;
  
  @Autowired
  public BagService bagService;
  
  public void sendNpcPet(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcPet> petMap = MallDataPool.shop_npc_pet;
    for (Integer key : petMap.keySet()) {
      SysShopNpcPet pet = petMap.get(key);
      if (pet != null) {
        ItemInfo itemInfo = new ItemInfo(pet.getSerial(), pet.getNum(), pet.getType(), pet.getPrice(), pet.getDiscount(), -1);
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcDrug(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcDrug> drugMap = MallDataPool.shop_npc_drug;
    for (Integer key : drugMap.keySet()) {
      SysShopNpcDrug drug = drugMap.get(key);
      if (drug != null) {
        ItemInfo itemInfo = new ItemInfo(drug.getSerial().intValue(), drug.getNum().intValue(), drug.getType().intValue(), drug.getPrice().intValue(), drug.getDiscount().intValue(), -1);
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcEquip(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcEquip> equipMap = MallDataPool.shop_npc_equip;
    for (Integer key : equipMap.keySet()) {
      SysShopNpcEquip equip = equipMap.get(key);
      if (equip != null) {
        ItemInfo itemInfo = new ItemInfo(equip.getSerial().intValue(), equip.getNum().intValue(), equip.getType().intValue(), equip.getPrice().intValue(), equip.getDiscount().intValue(), -1);
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcGrocery(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcGrocery> groceryMap = MallDataPool.shop_npc_grocery;
    Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
    for (Integer key : groceryMap.keySet()) {
      SysShopNpcGrocery grocery = groceryMap.get(key);
      if (grocery != null) {
        ItemInfo itemInfo = sendQuota(grocery.getSerial().intValue(), grocery.getNum().intValue(), grocery.getType().intValue(), grocery.getPrice().intValue(), grocery.getDiscount().intValue(), grocery.getQuotaLimit().intValue(), quotaMap, Mall.NPC_SHOP_GROCERY.getValue());
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcGuo(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcGuo> guoMap = MallDataPool.shop_npc_guo;
    Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
    for (Integer key : guoMap.keySet()) {
      SysShopNpcGuo shop = guoMap.get(key);
      if (shop != null) {
        ItemInfo itemInfo = sendQuota(shop.getSerial().intValue(), shop.getNum(), Const.RESTYPE_GUO.intValue(), shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_GUO.getValue());
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcBrac(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcBrac> map = MallDataPool.shop_npc_brac;
    Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
    for (Integer key : map.keySet()) {
      SysShopNpcBrac shop = map.get(key);
      if (shop != null) {
        ItemInfo itemInfo = sendQuota(shop.getSerial().intValue(), shop.getNum(), Const.RESTYPE_BRAC.intValue(), shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_BRAC.getValue());
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcNao(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcNao> map = MallDataPool.shop_npc_nao;
    Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
    for (Integer key : map.keySet()) {
      SysShopNpcNao shop = map.get(key);
      if (shop != null) {
        ItemInfo itemInfo = sendQuota(shop.getSerial().intValue(), shop.getNum(), Const.RESTYPE_NAO.intValue(), shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_NAO.getValue());
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcDi(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcDi> map = MallDataPool.shop_npc_di;
    Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
    for (Integer key : map.keySet()) {
      SysShopNpcDi shop = map.get(key);
      if (shop != null) {
        ItemInfo itemInfo = sendQuota(shop.getSerial().intValue(), shop.getNum(), Const.RESTYPE_DI.intValue(), shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_DI.getValue());
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcArena(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcArena> map = MallDataPool.shop_npc_arena;
    Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
    for (Integer key : map.keySet()) {
      SysShopNpcArena shop = map.get(key);
      if (shop != null) {
        ItemInfo itemInfo = sendQuota(shop.getSerial().intValue(), shop.getNum(), Const.RESTYPE_ARENA.intValue(), shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_ARENA.getValue());
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcLong(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcLong> map = MallDataPool.shop_npc_long;
    Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
    for (Integer key : map.keySet()) {
      SysShopNpcLong shop = map.get(key);
      if (shop != null) {
        ItemInfo itemInfo = sendQuota(shop.getSerial().intValue(), shop.getNum(), Const.RESTYPE_LONG.intValue(), shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_LONG.getValue());
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcNice(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcNice> map = MallDataPool.shop_npc_nice;
    Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
    for (Integer key : map.keySet()) {
      SysShopNpcNice shop = map.get(key);
      if (shop != null) {
        ItemInfo itemInfo = sendQuota(shop.getSerial().intValue(), shop.getNum(), Const.RESTYPE_NICE.intValue(), shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_NICE.getValue());
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void sendNpcFeats(Role role, Integer serial) {
    List<ItemInfo> list = new ArrayList<>();
    RespGoodsList respGoodsList = new RespGoodsList();
    Map<Integer, SysShopNpcFeats> map = MallDataPool.shop_npc_feats;
    Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
    for (Integer key : map.keySet()) {
      SysShopNpcFeats shop = map.get(key);
      if (shop != null) {
        ItemInfo itemInfo = sendQuota(shop.getSerial().intValue(), shop.getNum(), Const.RES_TYPE_FEATS.intValue(), shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_FEATS.getValue());
        list.add(itemInfo);
      } 
    } 
    respGoodsList.setId(serial.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public ItemInfo sendQuota(int serial, int num, int type, int price, int discount, int quotaLimit, Map<Integer, Map<Integer, Integer>> quotaMap, int mall) {
    ItemInfo itemInfo = new ItemInfo(serial, num, type, price, discount, -1);
    if (quotaLimit > -1) {
      Map<Integer, Integer> map = quotaMap.get(Integer.valueOf(mall));
      Integer purchased = map.get(Integer.valueOf(serial));
      if (purchased != null) {
        itemInfo.setQuotaLimit(quotaLimit - purchased.intValue());
      } else {
        itemInfo.setQuotaLimit(quotaLimit);
      } 
    } 
    return itemInfo;
  }
  
  public void buyNpcPet(Role role, int serial, int num) {
    SysShopNpcPet pet = (SysShopNpcPet)MallDataPool.shop_npc_pet.get(Integer.valueOf(serial));
    if (pet != null) {
      String msg = DataGlobalFunc.needGradeJudge(pet.getZsLimit(), pet.getLvLimit(), role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
      if (msg != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg(msg));
        return;
      } 
      buy(role, pet.getSerial(), num, pet.getDiscount(), pet.getPrice(), pet.getType(), pet.getNum(), 0);
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public void buyNpcDrug(Role role, int serial, int num) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcDrug drug = (SysShopNpcDrug)MallDataPool.shop_npc_drug.get(Integer.valueOf(serial));
    if (drug != null) {
      buy(role, drug.getSerial().intValue(), num, drug.getDiscount().intValue(), drug.getPrice().intValue(), drug.getType().intValue(), drug.getNum().intValue(), 0);
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public void buyNpcGrocery(Role role, int serial, int num, int shipper) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcGrocery shop = (SysShopNpcGrocery)MallDataPool.shop_npc_grocery.get(Integer.valueOf(serial));
    if (shop != null) {
      num = checkQuotaLimit(role, Mall.NPC_SHOP_GROCERY.getValue(), shop.getSerial().intValue(), shop.getQuotaLimit().intValue(), num);
      if (num > 0) {
        boolean buy = buy(role, shop.getSerial().intValue(), num, shop.getDiscount().intValue(), shop.getPrice().intValue(), shop.getType().intValue(), shop.getNum().intValue(), shop.getBd().intValue());
        if (buy && shop.getQuotaLimit().intValue() > -1)
          buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), shop.getQuotaLimit(), Integer.valueOf(num), shop.getType(), Integer.valueOf(Mall.NPC_SHOP_GROCERY.getValue()), shop.getPrice(), shop.getDiscount()); 
      } 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public void buyNpcGuo(Role role, int serial, int num, int shipper) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcGuo shop = (SysShopNpcGuo)MallDataPool.shop_npc_guo.get(Integer.valueOf(serial));
    if (shop != null) {
      num = checkQuotaLimit(role, Mall.NPC_SHOP_GUO.getValue(), shop.getSerial().intValue(), shop.getQuotaLimit(), num);
      if (num > 0) {
        boolean buy = buy(role, shop.getSerial().intValue(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_GUO.intValue(), shop.getNum(), 0);
        if (buy && shop.getQuotaLimit() > -1)
          buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_GUO, Integer.valueOf(Mall.NPC_SHOP_GUO.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount())); 
      } 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public void buyNpcBrac(Role role, int serial, int num, int shipper) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcBrac shop = (SysShopNpcBrac)MallDataPool.shop_npc_brac.get(Integer.valueOf(serial));
    if (shop != null) {
      num = checkQuotaLimit(role, Mall.NPC_SHOP_BRAC.getValue(), shop.getSerial().intValue(), shop.getQuotaLimit(), num);
      if (num > 0) {
        boolean buy = buy(role, shop.getSerial().intValue(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_BRAC.intValue(), shop.getNum(), 0);
        if (buy && shop.getQuotaLimit() > -1)
          buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_BRAC, Integer.valueOf(Mall.NPC_SHOP_BRAC.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount())); 
      } 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public void buyNpcNao(Role role, int serial, int num, int shipper) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcNao shop = (SysShopNpcNao)MallDataPool.shop_npc_nao.get(Integer.valueOf(serial));
    if (shop != null) {
      num = checkQuotaLimit(role, Mall.NPC_SHOP_NAO.getValue(), shop.getSerial().intValue(), shop.getQuotaLimit(), num);
      if (num > 0) {
        boolean buy = buy(role, shop.getSerial().intValue(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_NAO.intValue(), shop.getNum(), 0);
        if (buy && shop.getQuotaLimit() > -1)
          buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_NAO, Integer.valueOf(Mall.NPC_SHOP_NAO.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount())); 
      } 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public void buyNpcDi(Role role, int serial, int num, int shipper) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcDi shop = (SysShopNpcDi)MallDataPool.shop_npc_di.get(Integer.valueOf(serial));
    if (shop != null) {
      num = checkQuotaLimit(role, Mall.NPC_SHOP_DI.getValue(), shop.getSerial().intValue(), shop.getQuotaLimit(), num);
      if (num > 0) {
        boolean buy = buy(role, shop.getSerial().intValue(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_DI.intValue(), shop.getNum(), 0);
        if (buy && shop.getQuotaLimit() > -1)
          buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_DI, Integer.valueOf(Mall.NPC_SHOP_DI.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount())); 
      } 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public void buyNpcArena(Role role, int serial, int num, int shipper) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcArena shop = (SysShopNpcArena)MallDataPool.shop_npc_arena.get(Integer.valueOf(serial));
    if (shop != null) {
      num = checkQuotaLimit(role, Mall.NPC_SHOP_ARENA.getValue(), shop.getSerial().intValue(), shop.getQuotaLimit(), num);
      if (num > 0) {
        boolean buy = buy(role, shop.getSerial().intValue(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_ARENA.intValue(), shop.getNum(), 0);
        if (buy && shop.getQuotaLimit() > -1)
          buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_ARENA, Integer.valueOf(Mall.NPC_SHOP_ARENA.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount())); 
      } 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public void buyNpcLong(Role role, int serial, int num, int shipper) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcLong shop = (SysShopNpcLong)MallDataPool.shop_npc_long.get(Integer.valueOf(serial));
    if (shop != null) {
      num = checkQuotaLimit(role, Mall.NPC_SHOP_LONG.getValue(), shop.getSerial().intValue(), shop.getQuotaLimit(), num);
      if (num > 0) {
        boolean buy = buy(role, shop.getSerial().intValue(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_LONG.intValue(), shop.getNum(), 0);
        if (buy && shop.getQuotaLimit() > -1)
          buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_LONG, Integer.valueOf(Mall.NPC_SHOP_LONG.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount())); 
      } 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public void buyNpcNice(Role role, int serial, int num, int shipper) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcNice shop = (SysShopNpcNice)MallDataPool.shop_npc_nice.get(Integer.valueOf(serial));
    if (shop != null) {
      num = checkQuotaLimit(role, Mall.NPC_SHOP_NICE.getValue(), shop.getSerial().intValue(), shop.getQuotaLimit(), num);
      if (num > 0) {
        boolean buy = buy(role, shop.getSerial().intValue(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_NICE.intValue(), shop.getNum(), 0);
        if (buy && shop.getQuotaLimit() > -1)
          buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_NICE, Integer.valueOf(Mall.NPC_SHOP_NICE.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount())); 
      } 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public void buyNpcFeats(Role role, int serial, int num, int shipper) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcFeats shop = (SysShopNpcFeats)MallDataPool.shop_npc_feats.get(Integer.valueOf(serial));
    if (shop != null) {
      num = checkQuotaLimit(role, Mall.NPC_SHOP_FEATS.getValue(), shop.getSerial().intValue(), shop.getQuotaLimit(), num);
      if (num > 0) {
        boolean buy = buy(role, shop.getSerial().intValue(), num, shop.getDiscount(), shop.getPrice(), Const.RES_TYPE_FEATS.intValue(), shop.getNum(), 0);
        if (buy && shop.getQuotaLimit() > -1)
          buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RES_TYPE_FEATS, Integer.valueOf(Mall.NPC_SHOP_FEATS.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount())); 
      } 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("商品已售馨"));
    } 
  }
  
  public int checkQuotaLimit(Role role, int mall, int serial, int quotaLimit, int num) {
    if (quotaLimit > -1) {
      Map<Integer, Integer> map = (Map<Integer, Integer>)role.getExtendBox().getQuotaLimit().get(Integer.valueOf(mall));
      Integer integer = map.get(Integer.valueOf(serial));
      if (integer != null && integer.intValue() >= quotaLimit) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("该道具购买以上限"));
        num = 0;
      } else {
        Integer count = Integer.valueOf(num + ((integer == null) ? 0 : integer.intValue()));
        if (count.intValue() > quotaLimit)
          num = quotaLimit - integer.intValue(); 
      } 
    } 
    return num;
  }
  
  public void buyNpcEquip(Role role, int serial, int num) {
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return; 
    SysShopNpcEquip equip = (SysShopNpcEquip)MallDataPool.shop_npc_equip.get(Integer.valueOf(serial));
    if (equip != null) {
      if (this.bagService.getPos(role, false) > -1) {
        RoleService roleService = SpringUtils.getRoleService();
        double discount = (equip.getDiscount().intValue() > 0) ? (equip.getDiscount().intValue() / 100.0D) : 1.0D;
        int sum_price = (int)(equip.getPrice().intValue() * discount * num);
        if (roleService.checkCurrency(role, equip.getType(), sum_price))
          if (roleService.deductCurrency(role, equip.getType(), sum_price, "武器店"))
            ((EquipService)SpringUtils.getBean(EquipService.class)).upgradeEquip(role, 0, 11, String.valueOf(equip.getSerial()), 0, false, "武器店");  
      } 
    } else {
      RespMsg respMsg = new RespMsg();
      respMsg.setMsg("商品已售馨");
      MessagePusher.pushMessage(role, (Message)respMsg);
    } 
  }
  
  public boolean buy(Role role, int serial, int num, int discount, int price, int type, int once, int bd) {
    num = Math.max(1, num);
    if (this.bagService.checkPos(role, serial, num)) {
      RoleService roleService = SpringUtils.getRoleService();
      double dis = (discount > 0) ? (discount / 100.0D) : 1.0D;
      int sum_price = (int)(price * dis * num);
      if (roleService.checkCurrency(role, Integer.valueOf(type), sum_price)) {
        roleService.deductCurrency(role, Integer.valueOf(type), sum_price, "执行物品购买");
        ((BagService)SpringUtils.getBean(BagService.class)).addGoods(role, serial, once * num, bd, true, true, "商店购买");
        return true;
      } 
      return false;
    } 
    return false;
  }
  
  public void buyLimit(Role role, Integer shipper, Integer serial, Integer quotaLimit, Integer num, Integer resType, Integer type, Integer price, Integer discount) {
    Map<Integer, Integer> map = (Map<Integer, Integer>)role.getExtendBox().getQuotaLimit().get(type);
    num = Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), map.get(serial)) + num.intValue());
    map.put(serial, num);
    role.save();
    List<ItemInfo> list = new ArrayList<>();
    ItemInfo itemInfo = new ItemInfo(serial.intValue(), quotaLimit.intValue(), resType.intValue(), price.intValue(), discount.intValue(), quotaLimit.intValue() - ((Integer)map.get(serial)).intValue());
    list.add(itemInfo);
    RespGoodsList respGoodsList = new RespGoodsList();
    respGoodsList.setType(1);
    respGoodsList.setId(shipper.intValue());
    respGoodsList.setList(list);
    MessagePusher.pushMessage(role, (Message)respGoodsList);
  }
  
  public void recycleNpcGuo(Role role, String param, int serial, int num) {
    num = Math.max(1, num);
    Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(serial));
    if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_CARD.getValue()))) {
      SysDataCard dataCard = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(serial));
      if (dataCard != null) {
        CardService cardService = (CardService)SpringUtils.getBean(CardService.class);
        Map<Long, CardChange> cardMap = cardService.getCard(role);
        CardChange cardChange = cardMap.values().stream().filter(e -> (e.getSerial() == serial)).findFirst().orElse(null);
        if (cardChange == null)
          return; 
        int recycleNpcGuoCardPrice = recycleNpcGuoCardPrice(dataCard);
        if (recycleNpcGuoCardPrice <= 0)
          return; 
        boolean detach = cardService.cardDetach(role, serial, num);
        if (detach) {
          cardService.refresh(role, cardChange);
          this.roleService.addCurrency(role, Const.RESTYPE_GUO, Long.valueOf((recycleNpcGuoCardPrice * num)), "出售成功，获得", true, true);
        } 
        return;
      } 
    } else {
      if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue())) || type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()))) {
        if (NumberUtil.isNumber(param)) {
          BagEquip bagEquip = (BagEquip)this.bagService.getBagEquip(role).get(Long.valueOf(param));
          if (bagEquip == null)
            return; 
          if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()))) {
            if (DataGlobalFunc.getPj(bagEquip.getType()) == 1) {
              boolean flag = this.bagService.deleteRoleEquip(role, bagEquip, "郭氏回收");
              if (flag)
                this.roleService.addCurrency(role, Const.RESTYPE_GUO, Long.valueOf(90000L), "出售成功，获得", false, false); 
            } else {
              MessagePusher.pushMessage(role, (Message)new RespMsg("无法回收"));
            } 
          } else if (bagEquip.getProductOrder() == ACC_GRADE.PRICELESS.getValue()) {
            boolean flag = this.bagService.deleteRoleEquip(role, bagEquip, "郭氏回收");
            if (flag)
              this.roleService.addCurrency(role, Const.RESTYPE_GUO, Long.valueOf(120000L), "出售成功，获得", false, false); 
          } else {
            MessagePusher.pushMessage(role, (Message)new RespMsg("无法回收"));
          } 
        } 
        return;
      } 
      Integer goods = this.bagService.getBagGoods(role, serial, num);
      if (goods != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("背包中物品数量不足"));
        return;
      } 
      SysShopNpcGuoRecycle recycle = (SysShopNpcGuoRecycle)MallDataPool.shop_npc_guo_recycle.get(Integer.valueOf(serial));
      if (recycle != null) {
        if (this.bagService.consumeBagGoods(role, serial, num, true, true, "郭氏回收") != null)
          return; 
        this.roleService.addCurrency(role, Const.RESTYPE_GUO, Long.valueOf((recycle.getPrice() * num)), "出售成功，获得", true, true);
        return;
      } 
    } 
    MessagePusher.pushMessage(role, (Message)new RespMsg("该物品无法回收"));
  }
  
  public int recycleNpcGuoCardPrice(SysDataCard dataCard) {
    if (dataCard.getMorph() > 0) {
      if (dataCard.getRarity() == 2)
        return 150; 
      if (dataCard.getRarity() == 3)
        return 600; 
      if (dataCard.getRarity() == 4)
        return 3000; 
      if (dataCard.getRarity() == 5) {
        if (dataCard.getSerial() > 184060)
          return 20250; 
        return 12000;
      } 
    } else {
      if (dataCard.getRarity() == 2)
        return 100; 
      if (dataCard.getRarity() == 3)
        return 400; 
      if (dataCard.getRarity() == 4)
        return 2000; 
      if (dataCard.getRarity() == 5) {
        if (dataCard.getSerial() > 184060)
          return 13500; 
        return 8000;
      } 
    } 
    return 0;
  }
}
