
package com.kitty.system.game.onlinemall.service;

import com.kitty.system.game.vo.FieldProp;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.init.domain.SysGiftItemOpen;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.game.onlinemall.message.RespTreasureAwardInfo;
import com.kitty.common.utils.RandomUtil;
import java.util.Arrays;
import com.kitty.system.common.message.RespMsg;
import java.util.Iterator;
import com.kitty.system.game.onlinemall.message.vo.WeekTreasureInfo;
import com.kitty.system.game.activity.domain.WeekTreasure;
import com.kitty.mina.cache.Deploy;
import com.kitty.system.game.onlinemall.message.RespTreasureInfo;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.activity.service.GamePlayService;
import java.util.List;
import com.kitty.system.game.onlinemall.domain.OnlineMallInfo;
import java.util.ArrayList;
import com.kitty.system.game.onlinemall.message.RespOnlineMall;
import java.util.HashMap;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.common.utils.BigDecimalUtil;
import java.util.Map;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.init.domain.SysBoundShop;
import com.kitty.system.init.domain.SysDataLimitShop;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.domain.SysShopOther;
import com.kitty.system.init.domain.SysDataRecommendShop;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.system.enums.common.Mall;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.onlinemall.message.ReqBuyFromOnlineMall;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.bag.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class MallService
{
    @Autowired
    private RoleService roleService;
    @Autowired
    private BagService bagService;
    
    public boolean buyProp(final Role role, final ReqBuyFromOnlineMall buy) {
        if (!SafeUtil.isUnlock(role)) {
            return false;
        }
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return false;
        }
        if (buy.getType() == Mall.RECOMMEND.getValue()) {
            final Map<Integer, SysDataRecommendShop> recommendShopMap = MallDataPool.data_recommend_shop;
            final SysDataRecommendShop shop = recommendShopMap.get(buy.getSerial());
            if (shop != null) {
                return this.deductAndSend(role, buy.getSerial(), shop.getPrice(), buy.getAmount(), shop.getDiscount(), shop.getType(), shop.getLvLimit(), shop.getZsLimit(), shop.getMustVip(), shop.getQuotaLimit(), Mall.RECOMMEND.getValue());
            }
        }
        else if (buy.getType() == Mall.TREND_NEW.getValue()) {
            final Map<Integer, SysShopOther> shopOtherMap = MallDataPool.data_shopother;
            final SysShopOther shop2 = shopOtherMap.get(buy.getSerial());
            if (shop2 != null) {
                return this.deductAndSend(role, buy.getSerial(), shop2.getPrice(), buy.getAmount(), shop2.getDiscount(), shop2.getType(), shop2.getLvLimit(), shop2.getZsLimit(), shop2.getMustVip(), -1, Mall.TREND_NEW.getValue());
            }
        }
        else if (buy.getType() == Mall.STRENGTHEN_MATERIAL.getValue()) {
            final Map<Integer, SysShopStrengthenMaterial> shopStrengthenMaterialMap = MallDataPool.data_shopStrengthenMaterial;
            final SysShopStrengthenMaterial shop3 = shopStrengthenMaterialMap.get(buy.getSerial());
            if (shop3 != null) {
                return this.deductAndSend(role, buy.getSerial(), shop3.getPrice(), buy.getAmount(), shop3.getDiscount(), shop3.getType(), shop3.getLvLimit(), shop3.getZsLimit(), shop3.getMustVip(), -1, Mall.STRENGTHEN_MATERIAL.getValue());
            }
        }
        else if (buy.getType() == Mall.WEEKLY_QUOTA.getValue()) {
            final Map<Integer, SysDataLimitShop> limitShop = MallDataPool.t_data_limit_shop;
            final SysDataLimitShop shop4 = limitShop.get(buy.getSerial());
            if (shop4 != null) {
                return this.deductAndSend(role, buy.getSerial(), shop4.getPrice(), buy.getAmount(), shop4.getDiscount(), shop4.getType(), shop4.getLvLimit(), shop4.getZsLimit(), shop4.getMustVip(), shop4.getQuotaLimit(), Mall.WEEKLY_QUOTA.getValue());
            }
        }
        else if (buy.getType() == Mall.BOUND_SHOP.getValue()) {
            final Map<Integer, SysBoundShop> map = MallDataPool.boundShopMap;
            final SysBoundShop shop5 = map.get(buy.getSerial());
            if (shop5 != null) {
                return this.deductAndSend(role, buy.getSerial(), shop5.getPrice(), buy.getAmount(), shop5.getDiscount(), shop5.getType(), shop5.getLvLimit(), shop5.getZsLimit(), shop5.getMustVip(), shop5.getQuotaLimit(), Mall.BOUND_SHOP.getValue());
            }
        }
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "商城中未获取到该道具", false));
        return false;
    }
    
    public boolean deductAndSend(final Role role, final Integer serial, final double price, Integer num, Integer discount, final Integer type, final Integer lv, final Integer zs, final Integer mustVip, final Integer quotaLimit, final Integer mall) {
        final boolean checkPos = this.bagService.checkPos(role, serial, num);
        if (!checkPos) {
            return false;
        }
        final int num_ = num;
        if (discount <= 0) {
            discount = 100;
        }
        if (quotaLimit > -1) {
            Integer integer = 0;
            if (mall.equals(Mall.BOUND_SHOP.getValue())) {
                integer = role.getExtendBox().getDayQuotaLimit().getInt(serial.toString(), 0);
            }
            else {
                final Map<Integer, Integer> map = role.getExtendBox().getQuotaLimit().get(mall);
                integer = ((map.get(serial) == null) ? Integer.valueOf(0) : map.get(serial));
            }
            if (integer >= quotaLimit) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "该道具购买以上限", false));
                return false;
            }
            final Integer count = num + integer;
            if (count > quotaLimit) {
                num = quotaLimit - integer;
            }
        }
        final String priceMoney = String.valueOf((int)(price * num));
        final Integer sum_price = BigDecimalUtil.decimalScaleString(0, BigDecimalUtil.multiply(priceMoney, BigDecimalUtil.divide(discount.toString(), "100")));
        if (this.roleService.checkCurrency(role, type, sum_price)) {
            if (mustVip == 1 && role.getVip() <= 0) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "该道具仅限VIP才可购买", false));
                return false;
            }
            if (role.getRelive() < zs) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "转生等级不足" + zs + "转无法购买", false));
                return false;
            }
            if (role.getLevel() < lv) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "等级不足" + lv + "无法购买", false));
                return false;
            }
            final boolean flag = this.roleService.deductCurrency(role, type, sum_price, serial + ",buyProp," + mall);
            if (flag) {
                int bd = 0;
                if (type.equals(Const.RES_TYPE_BOUND_GOLD) || mall.equals(Mall.BOUND_SHOP.getValue())) {
                    bd = 2;
                }
                this.bagService.addGoods(role, serial, num, bd, true, true, "多宝购买");
                this.buyLimit(role, serial, quotaLimit, num_, mall);
                return true;
            }
        }
        return false;
    }
    
    public void buyLimit(final Role role, final Integer serial, final Integer quotaLimit, final Integer num, final Integer type) {
        if (quotaLimit > -1) {
            int already = 0;
            if (type.equals(Mall.BOUND_SHOP.getValue())) {
                already = role.getExtendBox().getDayQuotaLimit().getInt(serial.toString(), 0) + num;
                role.getExtendBox().getDayQuotaLimit().setv(serial.toString(), (Object)already);
            }
            else {
                Map<Integer, Integer> map = role.getExtendBox().getQuotaLimit().get(type);
                if (map == null) {
                    role.getExtendBox().getQuotaLimit().put(type, new HashMap<Integer, Integer>());
                    map = role.getExtendBox().getQuotaLimit().get(type);
                }
                if (map.size() <= 0) {
                    already = num;
                }
                else {
                    already = map.get(serial) + num;
                }
                map.put(serial, already);
            }
            role.save();
            final RespOnlineMall respOnlineMall = new RespOnlineMall();
            final List<OnlineMallInfo> list = new ArrayList<OnlineMallInfo>();
            final OnlineMallInfo mall = new OnlineMallInfo();
            mall.setType(type);
            mall.setSerial(serial);
            mall.setQuotaLimit(quotaLimit - already);
            list.add(mall);
            respOnlineMall.setType(1);
            respOnlineMall.setList(list);
            MessagePusher.pushMessage(role, respOnlineMall);
        }
    }
    
    public void openTreasure(final Role role) {
        final Map<Long, WeekTreasure> map = SpringUtils.getBean(GamePlayService.class).getWeekTreasure(role.getId());
        final RespTreasureInfo treasureInfo = new RespTreasureInfo();
        if (Deploy.treasure <= 0) {
            treasureInfo.setCount(Deploy.treasure);
        }
        else {
            final int count = role.getExtendBox().getDayQuotaLimit().getInt("treasure_1+1", 0);
            treasureInfo.setCount(Deploy.treasure - count);
        }
        for (final Long key : map.keySet()) {
            treasureInfo.getList().add(new WeekTreasureInfo(map.get(key)));
        }
        MessagePusher.pushMessage(role, treasureInfo);
    }
    
    public void buyTreasure(final Role role) {
        if (this.bagService.getPos(role, false) < 0) {
            MessagePusher.pushMessage(role, new RespMsg("你的物品栏已满"));
            return;
        }
        if (!this.roleService.checkCurrency(role, Const.RESTYPE_GOLD, Deploy.treasurePrice)) {
            return;
        }
        if (Deploy.treasure > 0) {
            final int count = role.getExtendBox().getDayQuotaLimit().getInt("treasure_1+1", 0);
            if (count >= Deploy.treasure) {
                MessagePusher.pushMessage(role, new RespMsg("购买以上限"));
                return;
            }
            role.getExtendBox().getDayQuotaLimit().setv("treasure_1+1", (Object)(count + 1));
        }
        if (this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, Deploy.treasurePrice, "天天挖宝")) {
            this.bagService.addGoods(role, 90061, 1, 0, true, true, "天天挖宝");
            final List<Integer> list = Arrays.asList(90058, 90059, 90060);
            final int index = RandomUtil.round(0, list.size() - 1);
            this.bagService.addGoods(role, list.get(index), 1, 0, true, true, "天天挖宝");
            this.openTreasure(role);
            String name = "红色原石";
            if (index == 1) {
                name = "蓝色原石";
            }
            else if (index == 2) {
                name = "黄色原石";
            }
            MessagePusher.pushMessage(role.getId(), new RespMsg("花费#<LG1>" + Deploy.treasurePrice + "#仙玉，获得" + String.format("#<II%d>#", 90061) + "#<P>[超级藏宝图]#"));
            MessagePusher.pushMessage(role.getId(), new RespMsg("获得#<LB1>" + name + "#1个"));
        }
    }
    
    public void openTreasureAward(final Role role) {
        final RespTreasureAwardInfo treasureAwardInfo = new RespTreasureAwardInfo();
        final List<SysGiftItemOpen> list = ItemDataPool.data_gift_item_open;
        final List<SysGiftItemOpen> normalList = list.stream().filter(item -> item.getGiftSerial().equals(30026)).collect(Collectors.toList());
        for (final SysGiftItemOpen obj : normalList) {
            treasureAwardInfo.getNormal().add(new FieldProp(obj.getSerial(), obj.getNum()));
        }
        final List<SysGiftItemOpen> seniorList = list.stream().filter(item -> item.getGiftSerial().equals(30027)).collect(Collectors.toList());
        for (final SysGiftItemOpen obj2 : seniorList) {
            treasureAwardInfo.getSenior().add(new FieldProp(obj2.getSerial(), obj2.getNum()));
        }
        final List<SysGiftItemOpen> supremeList = list.stream().filter(item -> item.getGiftSerial().equals(30028)).collect(Collectors.toList());
        for (final SysGiftItemOpen obj3 : supremeList) {
            treasureAwardInfo.getSupreme().add(new FieldProp(obj3.getSerial(), obj3.getNum()));
        }
        MessagePusher.pushMessage(role, treasureAwardInfo);
    }
    
    public void openTreasure(final Role role, final Long id) {
        final Map<Long, WeekTreasure> map = SpringUtils.getBean(GamePlayService.class).getWeekTreasure(role.getId());
        final WeekTreasure weekTreasure = map.get(id);
        if (weekTreasure == null) {
            MessagePusher.pushMessage(role, new RespMsg("没有该秘宝信息"));
            return;
        }
        if (this.bagService.getPos(role, false) < 0) {
            MessagePusher.pushMessage(role, new RespMsg("你的物品栏已满"));
            return;
        }
        boolean flag = false;
        if (weekTreasure.getSerial() == 30026 && weekTreasure.getShare() >= 2) {
            flag = true;
        }
        else if (weekTreasure.getSerial() == 30027 && weekTreasure.getShare() >= 4) {
            flag = true;
        }
        else if (weekTreasure.getSerial() == 30028 && weekTreasure.getShare() >= 6) {
            flag = true;
        }
        weekTreasure.setDelete(true);
        weekTreasure.save();
        map.remove(weekTreasure.getId());
        this.bagService.addGoods(role, weekTreasure.getSerial(), 1, 0, true, true, "开启秘宝奖励：" + weekTreasure.getSerial());
        this.openTreasure(role);
    }
}
