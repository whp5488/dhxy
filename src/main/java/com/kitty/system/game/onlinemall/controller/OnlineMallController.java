
package com.kitty.system.game.onlinemall.controller;

import java.util.Iterator;
import java.util.List;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.init.domain.SysBoundShop;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.init.domain.SysDataLimitShop;
import java.util.Date;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.domain.SysShopOther;
import java.util.Map;
import com.kitty.system.enums.common.Mall;
import com.kitty.system.init.domain.SysDataRecommendShop;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.system.game.onlinemall.domain.OnlineMallInfo;
import java.util.ArrayList;
import com.kitty.system.game.onlinemall.message.RespOnlineMall;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.onlinemall.message.ReqOpenOnlineMall;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.onlinemall.message.ReqBuyFromOnlineMall;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.onlinemall.service.MallService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class OnlineMallController
{
    Logger logger;
    @Autowired
    private MallService mallService;
    
    public OnlineMallController() {
        this.logger = LoggerFactory.getLogger(OnlineMallController.class);
    }
    
    @RequestMapping
    public void buyProp(final IoSession session, final ReqBuyFromOnlineMall buy) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final int max = Math.max(1, buy.getAmount());
        if (buy.getAmount() <= 0) {
            this.logger.error("玩家购买物品非法操作，{}，玩家ID{}，封装数量为{}，进行强转后数量为{}", new Object[] { buy.toString(), role.getRoleId(), buy.getAmount(), max });
        }
        buy.setAmount(max);
        this.mallService.buyProp(role, buy);
    }
    
    @RequestMapping
    public void openMall(final IoSession session, final ReqOpenOnlineMall reqOpenOnlineMall) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        if (quotaMap.size() <= 0) {
            quotaMap = DataGlobalFunc.setQuota();
            role.getExtendBox().setQuotaLimit(quotaMap);
        }
        final RespOnlineMall respOnlineMall = new RespOnlineMall();
        final List<OnlineMallInfo> list = new ArrayList<OnlineMallInfo>();
        final Map<Integer, SysDataRecommendShop> recommendShopMap = MallDataPool.data_recommend_shop;
        for (final Integer key : recommendShopMap.keySet()) {
            final SysDataRecommendShop shop = recommendShopMap.get(key);
            final OnlineMallInfo mall = new OnlineMallInfo();
            mall.setSerial(key);
            mall.setType(Mall.RECOMMEND.getValue());
            mall.setSort(shop.getSortNo());
            mall.setPrice((int)shop.getPrice());
            mall.setCostType(shop.getType());
            mall.setDiscount(shop.getDiscount());
            mall.setMustVip(shop.getMustVip());
            mall.setLvLimit(shop.getLvLimit());
            mall.setZsLimit(shop.getZsLimit());
            mall.setRecommend(shop.getRecommend());
            final Map<Integer, Integer> map = quotaMap.get(Mall.RECOMMEND.getValue());
            final Integer num = map.get(key);
            if (num != null) {
                mall.setQuotaLimit(shop.getQuotaLimit() - num);
            }
            else {
                mall.setQuotaLimit(shop.getQuotaLimit());
            }
            list.add(mall);
        }
        final Map<Integer, SysShopOther> shopOtherMap = MallDataPool.data_shopother;
        for (final Integer key2 : shopOtherMap.keySet()) {
            final SysShopOther shopOther = shopOtherMap.get(key2);
            final OnlineMallInfo mall2 = new OnlineMallInfo();
            mall2.setSerial(key2);
            mall2.setType(Mall.TREND_NEW.getValue());
            mall2.setSort(shopOther.getSortNo());
            mall2.setPrice((int)shopOther.getPrice());
            mall2.setCostType(shopOther.getType());
            mall2.setDiscount(shopOther.getDiscount());
            mall2.setMustVip(shopOther.getMustVip());
            mall2.setLvLimit(shopOther.getLvLimit());
            mall2.setZsLimit(shopOther.getZsLimit());
            list.add(mall2);
        }
        final Map<Integer, SysShopStrengthenMaterial> shopStrengthenMaterialMap = MallDataPool.data_shopStrengthenMaterial;
        for (final Integer key3 : shopStrengthenMaterialMap.keySet()) {
            final SysShopStrengthenMaterial shop2 = shopStrengthenMaterialMap.get(key3);
            final OnlineMallInfo mall3 = new OnlineMallInfo();
            mall3.setSerial(key3);
            mall3.setType(Mall.STRENGTHEN_MATERIAL.getValue());
            mall3.setSort(shop2.getSortNo());
            mall3.setPrice((int)shop2.getPrice());
            mall3.setCostType(shop2.getType());
            mall3.setDiscount(shop2.getDiscount());
            mall3.setMustVip(shop2.getMustVip());
            mall3.setLvLimit(shop2.getLvLimit());
            mall3.setZsLimit(shop2.getZsLimit());
            list.add(mall3);
        }
        final Map<Integer, SysDataLimitShop> limitShop = MallDataPool.t_data_limit_shop;
        final Date date = new Date();
        for (final Integer key4 : limitShop.keySet()) {
            final SysDataLimitShop shop3 = limitShop.get(key4);
            if (TimeUtil.timeCalculate(date, shop3.getStartTime()) <= 0 && TimeUtil.timeCalculate(date, shop3.getEndTime()) >= 0) {
                final OnlineMallInfo mall4 = new OnlineMallInfo();
                mall4.setSerial(key4);
                mall4.setType(Mall.WEEKLY_QUOTA.getValue());
                mall4.setSort(shop3.getSortNo());
                mall4.setPrice((int)shop3.getPrice());
                mall4.setCostType(shop3.getType());
                mall4.setDiscount(shop3.getDiscount());
                mall4.setMustVip(shop3.getMustVip());
                mall4.setLvLimit(shop3.getLvLimit());
                mall4.setZsLimit(shop3.getZsLimit());
                final Map<Integer, Integer> map2 = quotaMap.get(Mall.WEEKLY_QUOTA.getValue());
                final Integer num2 = map2.get(key4);
                if (num2 != null) {
                    mall4.setQuotaLimit(shop3.getQuotaLimit() - num2);
                }
                else {
                    mall4.setQuotaLimit(shop3.getQuotaLimit());
                }
                list.add(mall4);
            }
        }
        final Map<Integer, SysBoundShop> boundShopMap = MallDataPool.boundShopMap;
        for (final Integer key5 : boundShopMap.keySet()) {
            final SysBoundShop shop4 = boundShopMap.get(key5);
            final OnlineMallInfo mall5 = new OnlineMallInfo();
            mall5.setSerial(key5);
            mall5.setType(Mall.BOUND_SHOP.getValue());
            mall5.setSort(shop4.getSortNo());
            mall5.setPrice((int)shop4.getPrice());
            mall5.setCostType(shop4.getType());
            mall5.setDiscount(shop4.getDiscount());
            mall5.setMustVip(shop4.getMustVip());
            mall5.setLvLimit(shop4.getLvLimit());
            mall5.setZsLimit(shop4.getZsLimit());
            if (shop4.getQuotaLimit() > 0) {
                final Integer num2 = role.getExtendBox().getDayQuotaLimit().getInt(shop4.getSerial().toString(), 0);
                mall5.setQuotaLimit(shop4.getQuotaLimit() - num2);
            }
            list.add(mall5);
        }
        respOnlineMall.setType(0);
        respOnlineMall.setList(list);
        MessagePusher.pushMessage(role, respOnlineMall);
    }
}
