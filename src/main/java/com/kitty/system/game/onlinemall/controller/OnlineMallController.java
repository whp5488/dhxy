package com.kitty.system.game.onlinemall.controller;

import java.util.Iterator;
import java.util.List;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.init.domain.SysDataChargeScoreShop;
import com.kitty.system.init.domain.SysBoundShop;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.init.domain.SysDataLimitShop;
import java.util.Date;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.domain.SysShopOther;
import java.util.Map;
import com.kitty.system.game.onlinemall.domain.OnlineMallInfo;
import com.kitty.system.init.domain.SysDataRecommendShop;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.system.enums.common.Mall;
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
    public void buyProp(IoSession session, ReqBuyFromOnlineMall buy) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        int max = Math.max(1, buy.getAmount());
        if (buy.getAmount() <= 0) {
            this.logger.error("玩家购买物品非法操作，{}，玩家ID{}，封装数量为{}，进行强转后数量为{}", new Object[] { buy.toString(), role.getRoleId(), Integer.valueOf(buy.getAmount()), Integer.valueOf(max) });
        }
        buy.setAmount(max);
        this.mallService.buyProp(role, buy);
        return;
    }
    
    @RequestMapping
    public void openMall(IoSession session, ReqOpenOnlineMall reqOpenOnlineMall) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        if (quotaMap.size() <= 0) {
            quotaMap = DataGlobalFunc.setQuota();
            role.getExtendBox().setQuotaLimit(quotaMap);
        }
        RespOnlineMall respOnlineMall = new RespOnlineMall();
        List<OnlineMallInfo> list = new ArrayList<>();
        if (Mall.RECOMMEND.getState() == 1) {
            Map<Integer, SysDataRecommendShop> recommendShopMap = MallDataPool.data_recommend_shop;
            for (Integer key : recommendShopMap.keySet()) {
                SysDataRecommendShop shop = (SysDataRecommendShop)recommendShopMap.get(key);
                OnlineMallInfo mall = new OnlineMallInfo();
                mall.setSerial((int)key);
                mall.setType(Mall.RECOMMEND.getValue());
                mall.setGive((int)shop.getNum());
                mall.setSort((int)shop.getSortNo());
                mall.setPrice((int)shop.getPrice());
                mall.setCostType((int)shop.getType());
                mall.setDiscount((int)shop.getDiscount());
                mall.setMustVip((int)shop.getMustVip());
                mall.setLvLimit((int)shop.getLvLimit());
                mall.setZsLimit((int)shop.getZsLimit());
                mall.setRecommend(shop.getRecommend());
                Map<Integer, Integer> map = (Map)quotaMap.get(Integer.valueOf(Mall.RECOMMEND.getValue()));
                Integer num = (Integer)map.get(key);
                if (num != null) {
                    mall.setQuotaLimit((int)shop.getQuotaLimit() - (int)num);
                }
                else {
                    mall.setQuotaLimit((int)shop.getQuotaLimit());
                }
                list.add(mall);
            }
        }
        if (Mall.TREND_NEW.getState() == 1) {
            Map<Integer, SysShopOther> shopOtherMap = MallDataPool.data_shopother;
            for (Integer key : shopOtherMap.keySet()) {
                SysShopOther shopOther = (SysShopOther)shopOtherMap.get(key);
                OnlineMallInfo mall = new OnlineMallInfo();
                mall.setSerial((int)key);
                mall.setType(Mall.TREND_NEW.getValue());
                mall.setGive((int)shopOther.getNum());
                mall.setSort((int)shopOther.getSortNo());
                mall.setPrice((int)shopOther.getPrice());
                mall.setCostType((int)shopOther.getType());
                mall.setDiscount((int)shopOther.getDiscount());
                mall.setMustVip((int)shopOther.getMustVip());
                mall.setLvLimit((int)shopOther.getLvLimit());
                mall.setZsLimit((int)shopOther.getZsLimit());
                list.add(mall);
            }
        }
        if (Mall.STRENGTHEN_MATERIAL.getState() == 1) {
            Map<Integer, SysShopStrengthenMaterial> shopStrengthenMaterialMap = MallDataPool.data_shopStrengthenMaterial;
            for (Integer key : shopStrengthenMaterialMap.keySet()) {
                SysShopStrengthenMaterial shop2 = (SysShopStrengthenMaterial)shopStrengthenMaterialMap.get(key);
                OnlineMallInfo mall = new OnlineMallInfo();
                mall.setSerial((int)key);
                mall.setType(Mall.STRENGTHEN_MATERIAL.getValue());
                mall.setGive((int)shop2.getNum());
                mall.setSort((int)shop2.getSortNo());
                mall.setPrice((int)shop2.getPrice());
                mall.setCostType((int)shop2.getType());
                mall.setDiscount((int)shop2.getDiscount());
                mall.setMustVip((int)shop2.getMustVip());
                mall.setLvLimit((int)shop2.getLvLimit());
                mall.setZsLimit((int)shop2.getZsLimit());
                list.add(mall);
            }
        }
        if (Mall.WEEKLY_QUOTA.getState() == 1) {
            Map<Integer, SysDataLimitShop> limitShop = MallDataPool.t_data_limit_shop;
            Date date = new Date();
            for (Integer key2 : limitShop.keySet()) {
                SysDataLimitShop shop3 = (SysDataLimitShop)limitShop.get(key2);
                if ((int)TimeUtil.timeCalculate(date, shop3.getStartTime()) <= 0 && (int)TimeUtil.timeCalculate(date, shop3.getEndTime()) >= 0) {
                    OnlineMallInfo mall2 = new OnlineMallInfo();
                    mall2.setSerial((int)key2);
                    mall2.setType(Mall.WEEKLY_QUOTA.getValue());
                    mall2.setGive((int)shop3.getNum());
                    mall2.setSort((int)shop3.getSortNo());
                    mall2.setPrice((int)shop3.getPrice());
                    mall2.setCostType((int)shop3.getType());
                    mall2.setDiscount((int)shop3.getDiscount());
                    mall2.setMustVip((int)shop3.getMustVip());
                    mall2.setLvLimit((int)shop3.getLvLimit());
                    mall2.setZsLimit((int)shop3.getZsLimit());
                    Map<Integer, Integer> map2 = (Map)quotaMap.get(Integer.valueOf(Mall.WEEKLY_QUOTA.getValue()));
                    Integer num2 = (Integer)map2.get(key2);
                    if (num2 != null) {
                        mall2.setQuotaLimit((int)shop3.getQuotaLimit() - (int)num2);
                    }
                    else {
                        mall2.setQuotaLimit((int)shop3.getQuotaLimit());
                    }
                    list.add(mall2);
                }
                else {
                    continue;
                }
            }
        }
        if (Mall.BOUND_SHOP.getState() == 1) {
            Map<Integer, SysBoundShop> boundShopMap = MallDataPool.boundShopMap;
            for (Integer key : boundShopMap.keySet()) {
                SysBoundShop shop4 = (SysBoundShop)boundShopMap.get(key);
                OnlineMallInfo mall = new OnlineMallInfo();
                mall.setSerial((int)key);
                mall.setType(Mall.BOUND_SHOP.getValue());
                mall.setGive((int)shop4.getNum());
                mall.setSort((int)shop4.getSortNo());
                mall.setPrice((int)shop4.getPrice());
                mall.setCostType((int)shop4.getType());
                mall.setDiscount((int)shop4.getDiscount());
                mall.setMustVip((int)shop4.getMustVip());
                mall.setLvLimit((int)shop4.getLvLimit());
                mall.setZsLimit((int)shop4.getZsLimit());
                if ((int)shop4.getQuotaLimit() > 0) {
                    Integer num3 = Integer.valueOf(role.getExtendBox().getDayQuotaLimit().getInt(shop4.getSerial().toString(), 0));
                    mall.setQuotaLimit((int)shop4.getQuotaLimit() - (int)num3);
                }
                list.add(mall);
            }
        }
        if (Mall.CHARGE_SCORE_SHOP.getState() == 1) {
            Map<Integer, SysDataChargeScoreShop> chargeScoreShopMap = MallDataPool.chargeScoreShopMap;
            for (Integer key : chargeScoreShopMap.keySet()) {
                SysDataChargeScoreShop shop5 = (SysDataChargeScoreShop)chargeScoreShopMap.get(key);
                OnlineMallInfo mall = new OnlineMallInfo();
                mall.setSerial((int)key);
                mall.setType(Mall.CHARGE_SCORE_SHOP.getValue());
                mall.setGive((int)shop5.getNum());
                mall.setSort((int)shop5.getSortNo());
                mall.setPrice((int)shop5.getPrice());
                mall.setCostType((int)shop5.getType());
                mall.setDiscount((int)shop5.getDiscount());
                mall.setMustVip((int)shop5.getMustVip());
                mall.setLvLimit((int)shop5.getLvLimit());
                mall.setZsLimit((int)shop5.getZsLimit());
                if ((int)shop5.getQuotaLimit() > 0) {
                    Integer num3 = Integer.valueOf(role.getExtendBox().getDayQuotaLimit().getInt(shop5.getSerial().toString(), 0));
                    mall.setQuotaLimit((int)shop5.getQuotaLimit() - (int)num3);
                }
                list.add(mall);
            }
        }
        respOnlineMall.setType(0);
        respOnlineMall.setList(list);
        MessagePusher.pushMessage(role, respOnlineMall);
        return;
    }
}
