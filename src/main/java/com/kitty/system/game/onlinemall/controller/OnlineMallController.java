package com.kitty.system.game.onlinemall.controller;

import com.kitty.common.utils.TimeUtil;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.common.Mall;
import com.kitty.system.game.onlinemall.domain.OnlineMallInfo;
import com.kitty.system.game.onlinemall.message.ReqBuyFromOnlineMall;
import com.kitty.system.game.onlinemall.message.ReqOpenOnlineMall;
import com.kitty.system.game.onlinemall.message.RespOnlineMall;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.system.game.onlinemall.service.MallService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysBoundShop;
import com.kitty.system.init.domain.SysDataLimitShop;
import com.kitty.system.init.domain.SysDataRecommendShop;
import com.kitty.system.init.domain.SysShopOther;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OnlineMallController {
  Logger logger = LoggerFactory.getLogger(OnlineMallController.class);
  
  @Autowired
  private MallService mallService;
  
  @RequestMapping
  public void buyProp(IoSession session, ReqBuyFromOnlineMall buy) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    int max = Math.max(1, buy.getAmount());
    if (buy.getAmount() <= 0)
      this.logger.error("玩家购买物品非法操作，{}，玩家ID{}，封装数量为{}，进行强转后数量为{}", new Object[] { buy.toString(), role.getRoleId(), Integer.valueOf(buy.getAmount()), Integer.valueOf(max) }); 
    buy.setAmount(max);
    this.mallService.buyProp(role, buy);
  }
  
  @RequestMapping
  public void openMall(IoSession session, ReqOpenOnlineMall reqOpenOnlineMall) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
    if (quotaMap.size() <= 0) {
      quotaMap = DataGlobalFunc.setQuota();
      role.getExtendBox().setQuotaLimit(quotaMap);
    } 
    RespOnlineMall respOnlineMall = new RespOnlineMall();
    List<OnlineMallInfo> list = new ArrayList<>();
    Map<Integer, SysDataRecommendShop> recommendShopMap = MallDataPool.data_recommend_shop;
    for (Integer key : recommendShopMap.keySet()) {
      SysDataRecommendShop shop = recommendShopMap.get(key);
      OnlineMallInfo mall = new OnlineMallInfo();
      mall.setSerial(key.intValue());
      mall.setType(Mall.RECOMMEND.getValue());
      mall.setSort(shop.getSortNo().intValue());
      mall.setPrice((int)shop.getPrice());
      mall.setCostType(shop.getType().intValue());
      mall.setDiscount(shop.getDiscount().intValue());
      mall.setMustVip(shop.getMustVip().intValue());
      mall.setLvLimit(shop.getLvLimit().intValue());
      mall.setZsLimit(shop.getZsLimit().intValue());
      mall.setRecommend(shop.getRecommend());
      Map<Integer, Integer> map = quotaMap.get(Integer.valueOf(Mall.RECOMMEND.getValue()));
      Integer num = map.get(key);
      if (num != null) {
        mall.setQuotaLimit(shop.getQuotaLimit().intValue() - num.intValue());
      } else {
        mall.setQuotaLimit(shop.getQuotaLimit().intValue());
      } 
      list.add(mall);
    } 
    Map<Integer, SysShopOther> shopOtherMap = MallDataPool.data_shopother;
    for (Integer key : shopOtherMap.keySet()) {
      SysShopOther shopOther = shopOtherMap.get(key);
      OnlineMallInfo mall = new OnlineMallInfo();
      mall.setSerial(key.intValue());
      mall.setType(Mall.TREND_NEW.getValue());
      mall.setSort(shopOther.getSortNo().intValue());
      mall.setPrice((int)shopOther.getPrice());
      mall.setCostType(shopOther.getType().intValue());
      mall.setDiscount(shopOther.getDiscount().intValue());
      mall.setMustVip(shopOther.getMustVip().intValue());
      mall.setLvLimit(shopOther.getLvLimit().intValue());
      mall.setZsLimit(shopOther.getZsLimit().intValue());
      list.add(mall);
    } 
    Map<Integer, SysShopStrengthenMaterial> shopStrengthenMaterialMap = MallDataPool.data_shopStrengthenMaterial;
    for (Integer key : shopStrengthenMaterialMap.keySet()) {
      SysShopStrengthenMaterial shop = shopStrengthenMaterialMap.get(key);
      OnlineMallInfo mall = new OnlineMallInfo();
      mall.setSerial(key.intValue());
      mall.setType(Mall.STRENGTHEN_MATERIAL.getValue());
      mall.setSort(shop.getSortNo().intValue());
      mall.setPrice((int)shop.getPrice());
      mall.setCostType(shop.getType().intValue());
      mall.setDiscount(shop.getDiscount().intValue());
      mall.setMustVip(shop.getMustVip().intValue());
      mall.setLvLimit(shop.getLvLimit().intValue());
      mall.setZsLimit(shop.getZsLimit().intValue());
      list.add(mall);
    } 
    Map<Integer, SysDataLimitShop> limitShop = MallDataPool.t_data_limit_shop;
    Date date = new Date();
    for (Integer key : limitShop.keySet()) {
      SysDataLimitShop shop = limitShop.get(key);
      if (TimeUtil.timeCalculate(date, shop.getStartTime()).intValue() <= 0 && TimeUtil.timeCalculate(date, shop.getEndTime()).intValue() >= 0) {
        OnlineMallInfo mall = new OnlineMallInfo();
        mall.setSerial(key.intValue());
        mall.setType(Mall.WEEKLY_QUOTA.getValue());
        mall.setSort(shop.getSortNo().intValue());
        mall.setPrice((int)shop.getPrice());
        mall.setCostType(shop.getType().intValue());
        mall.setDiscount(shop.getDiscount().intValue());
        mall.setMustVip(shop.getMustVip().intValue());
        mall.setLvLimit(shop.getLvLimit().intValue());
        mall.setZsLimit(shop.getZsLimit().intValue());
        Map<Integer, Integer> map = quotaMap.get(Integer.valueOf(Mall.WEEKLY_QUOTA.getValue()));
        Integer num = map.get(key);
        if (num != null) {
          mall.setQuotaLimit(shop.getQuotaLimit().intValue() - num.intValue());
        } else {
          mall.setQuotaLimit(shop.getQuotaLimit().intValue());
        } 
        list.add(mall);
      } 
    } 
    Map<Integer, SysBoundShop> boundShopMap = MallDataPool.boundShopMap;
    for (Integer key : boundShopMap.keySet()) {
      SysBoundShop shop = boundShopMap.get(key);
      OnlineMallInfo mall = new OnlineMallInfo();
      mall.setSerial(key.intValue());
      mall.setType(Mall.BOUND_SHOP.getValue());
      mall.setSort(shop.getSortNo().intValue());
      mall.setPrice((int)shop.getPrice());
      mall.setCostType(shop.getType().intValue());
      mall.setDiscount(shop.getDiscount().intValue());
      mall.setMustVip(shop.getMustVip().intValue());
      mall.setLvLimit(shop.getLvLimit().intValue());
      mall.setZsLimit(shop.getZsLimit().intValue());
      if (shop.getQuotaLimit().intValue() > 0) {
        Integer num = Integer.valueOf(role.getExtendBox().getDayQuotaLimit().getInt(shop.getSerial().toString(), 0));
        mall.setQuotaLimit(shop.getQuotaLimit().intValue() - num.intValue());
      } 
      list.add(mall);
    } 
    respOnlineMall.setType(0);
    respOnlineMall.setList(list);
    MessagePusher.pushMessage(role, (Message)respOnlineMall);
  }
}
