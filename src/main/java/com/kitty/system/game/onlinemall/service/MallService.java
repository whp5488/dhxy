package com.kitty.system.game.onlinemall.service;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.Deploy;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.common.Mall;
import com.kitty.system.game.activity.domain.WeekTreasure;
import com.kitty.system.game.activity.service.GamePlayService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.onlinemall.domain.OnlineMallInfo;
import com.kitty.system.game.onlinemall.message.ReqBuyFromOnlineMall;
import com.kitty.system.game.onlinemall.message.RespOnlineMall;
import com.kitty.system.game.onlinemall.message.RespTreasureAwardInfo;
import com.kitty.system.game.onlinemall.message.RespTreasureInfo;
import com.kitty.system.game.onlinemall.message.vo.WeekTreasureInfo;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysBoundShop;
import com.kitty.system.init.domain.SysDataLimitShop;
import com.kitty.system.init.domain.SysDataRecommendShop;
import com.kitty.system.init.domain.SysGiftItemOpen;
import com.kitty.system.init.domain.SysShopOther;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.pool.ItemDataPool;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MallService {
  @Autowired
  private RoleService roleService;
  
  @Autowired
  private BagService bagService;
  
  public boolean buyProp(Role role, ReqBuyFromOnlineMall buy) {
    if (!SafeUtil.isUnlock(role))
      return false; 
    boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
    if (!clicker)
      return false; 
    if (buy.getType() == Mall.RECOMMEND.getValue()) {
      Map<Integer, SysDataRecommendShop> recommendShopMap = MallDataPool.data_recommend_shop;
      SysDataRecommendShop shop = recommendShopMap.get(Integer.valueOf(buy.getSerial()));
      if (shop != null)
        return deductAndSend(role, Integer.valueOf(buy.getSerial()), shop.getPrice(), Integer.valueOf(buy.getAmount()), shop.getDiscount(), shop.getType(), shop.getLvLimit(), shop.getZsLimit(), shop.getMustVip(), shop.getQuotaLimit(), Integer.valueOf(Mall.RECOMMEND.getValue())); 
    } else if (buy.getType() == Mall.TREND_NEW.getValue()) {
      Map<Integer, SysShopOther> shopOtherMap = MallDataPool.data_shopother;
      SysShopOther shop = shopOtherMap.get(Integer.valueOf(buy.getSerial()));
      if (shop != null)
        return deductAndSend(role, Integer.valueOf(buy.getSerial()), shop.getPrice(), Integer.valueOf(buy.getAmount()), shop.getDiscount(), shop.getType(), shop.getLvLimit(), shop.getZsLimit(), shop.getMustVip(), Integer.valueOf(-1), Integer.valueOf(Mall.TREND_NEW.getValue())); 
    } else if (buy.getType() == Mall.STRENGTHEN_MATERIAL.getValue()) {
      Map<Integer, SysShopStrengthenMaterial> shopStrengthenMaterialMap = MallDataPool.data_shopStrengthenMaterial;
      SysShopStrengthenMaterial shop = shopStrengthenMaterialMap.get(Integer.valueOf(buy.getSerial()));
      if (shop != null)
        return deductAndSend(role, Integer.valueOf(buy.getSerial()), shop.getPrice(), Integer.valueOf(buy.getAmount()), shop.getDiscount(), shop.getType(), shop.getLvLimit(), shop.getZsLimit(), shop.getMustVip(), Integer.valueOf(-1), Integer.valueOf(Mall.STRENGTHEN_MATERIAL.getValue())); 
    } else if (buy.getType() == Mall.WEEKLY_QUOTA.getValue()) {
      Map<Integer, SysDataLimitShop> limitShop = MallDataPool.t_data_limit_shop;
      SysDataLimitShop shop = limitShop.get(Integer.valueOf(buy.getSerial()));
      if (shop != null)
        return deductAndSend(role, Integer.valueOf(buy.getSerial()), shop.getPrice(), Integer.valueOf(buy.getAmount()), shop.getDiscount(), shop.getType(), shop.getLvLimit(), shop.getZsLimit(), shop.getMustVip(), shop.getQuotaLimit(), Integer.valueOf(Mall.WEEKLY_QUOTA.getValue())); 
    } else if (buy.getType() == Mall.BOUND_SHOP.getValue()) {
      Map<Integer, SysBoundShop> map = MallDataPool.boundShopMap;
      SysBoundShop shop = map.get(Integer.valueOf(buy.getSerial()));
      if (shop != null)
        return deductAndSend(role, Integer.valueOf(buy.getSerial()), shop.getPrice(), Integer.valueOf(buy.getAmount()), shop.getDiscount(), shop.getType(), shop.getLvLimit(), shop.getZsLimit(), shop.getMustVip(), shop.getQuotaLimit(), Integer.valueOf(Mall.BOUND_SHOP.getValue())); 
    } 
    MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "商城中未获取到该道具", false));
    return false;
  }
  
  public boolean deductAndSend(Role role, Integer serial, double price, Integer num, Integer discount, Integer type, Integer lv, Integer zs, Integer mustVip, Integer quotaLimit, Integer mall) {
    boolean checkPos = this.bagService.checkPos(role, serial.intValue(), num.intValue());
    if (!checkPos)
      return false; 
    int num_ = num.intValue();
    if (discount.intValue() <= 0)
      discount = Integer.valueOf(100); 
    if (quotaLimit.intValue() > -1) {
      Integer integer = Integer.valueOf(0);
      if (mall.equals(Integer.valueOf(Mall.BOUND_SHOP.getValue()))) {
        integer = Integer.valueOf(role.getExtendBox().getDayQuotaLimit().getInt(serial.toString(), 0));
      } else {
        Map<Integer, Integer> map = (Map<Integer, Integer>)role.getExtendBox().getQuotaLimit().get(mall);
        integer = (map.get(serial) == null) ? Integer.valueOf(0) : map.get(serial);
      } 
      if (integer.intValue() >= quotaLimit.intValue()) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "该道具购买以上限", false));
        return false;
      } 
      Integer count = Integer.valueOf(num.intValue() + integer.intValue());
      if (count.intValue() > quotaLimit.intValue())
        num = Integer.valueOf(quotaLimit.intValue() - integer.intValue()); 
    } 
    String priceMoney = String.valueOf((int)(price * num.intValue()));
    Integer sum_price = Integer.valueOf(BigDecimalUtil.decimalScaleString(Integer.valueOf(0), BigDecimalUtil.multiply(priceMoney, BigDecimalUtil.divide(discount.toString(), "100"))));
    if (this.roleService.checkCurrency(role, type, sum_price.intValue())) {
      if (mustVip.intValue() == 1 && 
        role.getVip().intValue() <= 0) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "该道具仅限VIP才可购买", false));
        return false;
      } 
      if (role.getRelive().intValue() < zs.intValue()) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "转生等级不足" + zs + "转无法购买", false));
        return false;
      } 
      if (role.getLevel().intValue() < lv.intValue()) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "等级不足" + lv + "无法购买", false));
        return false;
      } 
      boolean flag = this.roleService.deductCurrency(role, type, sum_price.intValue(), serial + ",buyProp," + mall);
      if (flag) {
        int bd = 0;
        if (type.equals(Const.RES_TYPE_BOUND_GOLD) || mall.equals(Integer.valueOf(Mall.BOUND_SHOP.getValue())))
          bd = 2; 
        this.bagService.addGoods(role, serial.intValue(), num.intValue(), bd, true, true, "多宝购买");
        buyLimit(role, serial, quotaLimit, Integer.valueOf(num_), mall);
        return true;
      } 
    } 
    return false;
  }
  
  public void buyLimit(Role role, Integer serial, Integer quotaLimit, Integer num, Integer type) {
    if (quotaLimit.intValue() > -1) {
      int already = 0;
      if (type.equals(Integer.valueOf(Mall.BOUND_SHOP.getValue()))) {
        already = role.getExtendBox().getDayQuotaLimit().getInt(serial.toString(), 0) + num.intValue();
        role.getExtendBox().getDayQuotaLimit().setv(serial.toString(), Integer.valueOf(already));
      } else {
        Map<Integer, Integer> map = (Map<Integer, Integer>)role.getExtendBox().getQuotaLimit().get(type);
        if (map == null) {
          role.getExtendBox().getQuotaLimit().put(type, new HashMap<>());
          map = (Map<Integer, Integer>)role.getExtendBox().getQuotaLimit().get(type);
        } 
        if (map.size() <= 0) {
          already = num.intValue();
        } else {
          already = ((Integer)map.get(serial)).intValue() + num.intValue();
        } 
        map.put(serial, Integer.valueOf(already));
      } 
      role.save();
      RespOnlineMall respOnlineMall = new RespOnlineMall();
      List<OnlineMallInfo> list = new ArrayList<>();
      OnlineMallInfo mall = new OnlineMallInfo();
      mall.setType(type.intValue());
      mall.setSerial(serial.intValue());
      mall.setQuotaLimit(quotaLimit.intValue() - already);
      list.add(mall);
      respOnlineMall.setType(1);
      respOnlineMall.setList(list);
      MessagePusher.pushMessage(role, (Message)respOnlineMall);
    } 
  }
  
  public void openTreasure(Role role) {
    Map<Long, WeekTreasure> map = ((GamePlayService)SpringUtils.getBean(GamePlayService.class)).getWeekTreasure(role.getId().longValue());
    RespTreasureInfo treasureInfo = new RespTreasureInfo();
    if (Deploy.treasure <= 0) {
      treasureInfo.setCount(Deploy.treasure);
    } else {
      int count = role.getExtendBox().getDayQuotaLimit().getInt("treasure_1+1", 0);
      treasureInfo.setCount(Deploy.treasure - count);
    } 
    for (Long key : map.keySet())
      treasureInfo.getList().add(new WeekTreasureInfo(map.get(key))); 
    MessagePusher.pushMessage(role, (Message)treasureInfo);
  }
  
  public void buyTreasure(Role role) {
    if (this.bagService.getPos(role, false) < 0) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你的物品栏已满"));
      return;
    } 
    if (!this.roleService.checkCurrency(role, Const.RESTYPE_GOLD, Deploy.treasurePrice))
      return; 
    if (Deploy.treasure > 0) {
      int count = role.getExtendBox().getDayQuotaLimit().getInt("treasure_1+1", 0);
      if (count >= Deploy.treasure) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("购买以上限"));
        return;
      } 
      role.getExtendBox().getDayQuotaLimit().setv("treasure_1+1", Integer.valueOf(count + 1));
    } 
    if (this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, Deploy.treasurePrice, "天天挖宝")) {
      this.bagService.addGoods(role, 90061, 1, 0, true, true, "天天挖宝");
      List<Integer> list = Arrays.asList(new Integer[] { Integer.valueOf(90058), Integer.valueOf(90059), Integer.valueOf(90060) });
      int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
      this.bagService.addGoods(role, ((Integer)list.get(index)).intValue(), 1, 0, true, true, "天天挖宝");
      openTreasure(role);
      String name = "红色原石";
      if (index == 1) {
        name = "蓝色原石";
      } else if (index == 2) {
        name = "黄色原石";
      } 
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("花费#<LG1>" + Deploy.treasurePrice + "#仙玉，获得" + String.format("#<II%d>#", new Object[] { Integer.valueOf(90061) }) + "#<P>[超级藏宝图]#"));
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("获得#<LB1>" + name + "#1个"));
    } 
  }
  
  public void openTreasureAward(Role role) {
    RespTreasureAwardInfo treasureAwardInfo = new RespTreasureAwardInfo();
    List<SysGiftItemOpen> list = ItemDataPool.data_gift_item_open;
    List<SysGiftItemOpen> normalList = (List<SysGiftItemOpen>)list.stream().filter(item -> item.getGiftSerial().equals(Integer.valueOf(30026))).collect(Collectors.toList());
    for (SysGiftItemOpen obj : normalList)
      treasureAwardInfo.getNormal().add(new FieldProp(obj.getSerial(), obj.getNum())); 
    List<SysGiftItemOpen> seniorList = (List<SysGiftItemOpen>)list.stream().filter(item -> item.getGiftSerial().equals(Integer.valueOf(30027))).collect(Collectors.toList());
    for (SysGiftItemOpen obj : seniorList)
      treasureAwardInfo.getSenior().add(new FieldProp(obj.getSerial(), obj.getNum())); 
    List<SysGiftItemOpen> supremeList = (List<SysGiftItemOpen>)list.stream().filter(item -> item.getGiftSerial().equals(Integer.valueOf(30028))).collect(Collectors.toList());
    for (SysGiftItemOpen obj : supremeList)
      treasureAwardInfo.getSupreme().add(new FieldProp(obj.getSerial(), obj.getNum())); 
    MessagePusher.pushMessage(role, (Message)treasureAwardInfo);
  }
  
  public void openTreasure(Role role, Long id) {
    Map<Long, WeekTreasure> map = ((GamePlayService)SpringUtils.getBean(GamePlayService.class)).getWeekTreasure(role.getId().longValue());
    WeekTreasure weekTreasure = map.get(id);
    if (weekTreasure == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("没有该秘宝信息"));
      return;
    } 
    if (this.bagService.getPos(role, false) < 0) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你的物品栏已满"));
      return;
    } 
    boolean flag = false;
    if (weekTreasure.getSerial() == 30026 && weekTreasure.getShare() >= 2) {
      flag = true;
    } else if (weekTreasure.getSerial() == 30027 && weekTreasure.getShare() >= 4) {
      flag = true;
    } else if (weekTreasure.getSerial() == 30028 && weekTreasure.getShare() >= 6) {
      flag = true;
    } 
    weekTreasure.setDelete(true);
    weekTreasure.save();
    map.remove(weekTreasure.getId());
    this.bagService.addGoods(role, weekTreasure.getSerial(), 1, 0, true, true, "开启秘宝奖励：" + weekTreasure.getSerial());
    openTreasure(role);
  }
}
