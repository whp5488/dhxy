package com.kitty.system.game.onlinemall.service;

import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.init.domain.SysGiftItemOpen;
import java.util.stream.Collectors;
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
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.init.domain.SysDataRecommendShop;
import com.kitty.system.init.domain.SysShopOther;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.domain.SysDataLimitShop;
import com.kitty.system.init.domain.SysBoundShop;
import com.kitty.system.init.domain.SysDataChargeScoreShop;
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
    
    public MallService() {
    }
    
    public boolean buyProp(Role role, ReqBuyFromOnlineMall buy) {
        if (!SafeUtil.isUnlock(role)) {
            return false;
        }
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return false;
        }
        if (buy.getType() == Mall.RECOMMEND.getValue() && Mall.RECOMMEND.getState() == 1) {
            Map<Integer, SysDataRecommendShop> recommendShopMap = MallDataPool.data_recommend_shop;
            SysDataRecommendShop shop6 = (SysDataRecommendShop)recommendShopMap.get(Integer.valueOf(buy.getSerial()));
            if (shop6 != null) {
                return this.deductAndSend(role, Integer.valueOf(buy.getSerial()), shop6.getPrice(), Integer.valueOf(buy.getAmount()), shop6.getDiscount(), shop6.getType(), shop6.getLvLimit(), shop6.getZsLimit(), shop6.getMustVip(), shop6.getQuotaLimit(), (int)shop6.getNum(), Integer.valueOf(Mall.RECOMMEND.getValue()));
            }
        }
        else if (buy.getType() == Mall.TREND_NEW.getValue() && Mall.TREND_NEW.getState() == 1) {
            Map<Integer, SysShopOther> shopOtherMap = MallDataPool.data_shopother;
            SysShopOther shop5 = (SysShopOther)shopOtherMap.get(Integer.valueOf(buy.getSerial()));
            if (shop5 != null) {
                return this.deductAndSend(role, Integer.valueOf(buy.getSerial()), shop5.getPrice(), Integer.valueOf(buy.getAmount()), shop5.getDiscount(), shop5.getType(), shop5.getLvLimit(), shop5.getZsLimit(), shop5.getMustVip(), Integer.valueOf(-1), (int)shop5.getNum(), Integer.valueOf(Mall.TREND_NEW.getValue()));
            }
        }
        else if (buy.getType() == Mall.STRENGTHEN_MATERIAL.getValue() && Mall.STRENGTHEN_MATERIAL.getState() == 1) {
            Map<Integer, SysShopStrengthenMaterial> shopStrengthenMaterialMap = MallDataPool.data_shopStrengthenMaterial;
            SysShopStrengthenMaterial shop4 = (SysShopStrengthenMaterial)shopStrengthenMaterialMap.get(Integer.valueOf(buy.getSerial()));
            if (shop4 != null) {
                return this.deductAndSend(role, Integer.valueOf(buy.getSerial()), shop4.getPrice(), Integer.valueOf(buy.getAmount()), shop4.getDiscount(), shop4.getType(), shop4.getLvLimit(), shop4.getZsLimit(), shop4.getMustVip(), Integer.valueOf(-1), (int)shop4.getNum(), Integer.valueOf(Mall.STRENGTHEN_MATERIAL.getValue()));
            }
        }
        else if (buy.getType() == Mall.WEEKLY_QUOTA.getValue() && Mall.WEEKLY_QUOTA.getState() == 1) {
            Map<Integer, SysDataLimitShop> limitShop = MallDataPool.t_data_limit_shop;
            SysDataLimitShop shop3 = (SysDataLimitShop)limitShop.get(Integer.valueOf(buy.getSerial()));
            if (shop3 != null) {
                return this.deductAndSend(role, Integer.valueOf(buy.getSerial()), shop3.getPrice(), Integer.valueOf(buy.getAmount()), shop3.getDiscount(), shop3.getType(), shop3.getLvLimit(), shop3.getZsLimit(), shop3.getMustVip(), shop3.getQuotaLimit(), (int)shop3.getNum(), Integer.valueOf(Mall.WEEKLY_QUOTA.getValue()));
            }
        }
        else if (buy.getType() == Mall.BOUND_SHOP.getValue() && Mall.BOUND_SHOP.getState() == 1) {
            Map<Integer, SysBoundShop> map2 = MallDataPool.boundShopMap;
            SysBoundShop shop2 = (SysBoundShop)map2.get(Integer.valueOf(buy.getSerial()));
            if (shop2 != null) {
                return this.deductAndSend(role, Integer.valueOf(buy.getSerial()), shop2.getPrice(), Integer.valueOf(buy.getAmount()), shop2.getDiscount(), shop2.getType(), shop2.getLvLimit(), shop2.getZsLimit(), shop2.getMustVip(), shop2.getQuotaLimit(), (int)shop2.getNum(), Integer.valueOf(Mall.BOUND_SHOP.getValue()));
            }
        }
        else if (buy.getType() == Mall.CHARGE_SCORE_SHOP.getValue() && Mall.CHARGE_SCORE_SHOP.getState() == 1) {
            Map<Integer, SysDataChargeScoreShop> map = MallDataPool.chargeScoreShopMap;
            SysDataChargeScoreShop shop = (SysDataChargeScoreShop)map.get(Integer.valueOf(buy.getSerial()));
            if (shop != null) {
                return this.deductAndSend(role, Integer.valueOf(buy.getSerial()), shop.getPrice(), Integer.valueOf(buy.getAmount()), shop.getDiscount(), shop.getType(), shop.getLvLimit(), shop.getZsLimit(), shop.getMustVip(), shop.getQuotaLimit(), (int)shop.getNum(), Integer.valueOf(Mall.CHARGE_SCORE_SHOP.getValue()));
            }
        }
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "商城中未获取到该道具", false));
        return false;
    }
    
    public boolean deductAndSend(Role role, Integer serial, double price, Integer num, Integer discount, Integer type, Integer lv, Integer zs, Integer mustVip, Integer quotaLimit, int give, Integer mall) {
        boolean checkPos = this.bagService.checkPos(role, (int)serial, (int)num * give);
        if (!checkPos) {
            return false;
        }
        int num_ = (int)num;
        if ((int)discount <= 0) {
            discount = Integer.valueOf(100);
        }
        if ((int)quotaLimit > -1) {
            Integer integer = Integer.valueOf(0);
            if (mall.equals(Integer.valueOf(Mall.BOUND_SHOP.getValue()))) {
                integer = Integer.valueOf(role.getExtendBox().getDayQuotaLimit().getInt(serial.toString(), 0));
            }
            else {
                Map<Integer, Integer> map = (Map)role.getExtendBox().getQuotaLimit().get(mall);
                integer = ((map.get(serial) == null) ? Integer.valueOf(0) : ((Integer)map.get(serial)));
            }
            if ((int)integer >= (int)quotaLimit) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "该道具购买以上限", false));
                return false;
            }
            Integer count = Integer.valueOf((int)num + (int)integer);
            if ((int)count > (int)quotaLimit) {
                num = Integer.valueOf((int)quotaLimit - (int)integer);
            }
        }
        String priceMoney = String.valueOf((int)(price * (double)(int)num));
        Integer sum_price = Integer.valueOf(BigDecimalUtil.decimalScaleString(Integer.valueOf(0), BigDecimalUtil.multiply(priceMoney, BigDecimalUtil.divide(discount.toString(), "100"))));
        if (this.roleService.checkCurrency(role, type, (long)(int)sum_price)) {
            if ((int)mustVip == 1 && (int)role.getVip() <= 0) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "该道具仅限VIP才可购买", false));
                return false;
            }
            if ((int)role.getRelive() < (int)zs) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "转生等级不足" + zs + "转无法购买", false));
                return false;
            }
            if ((int)role.getLevel() < (int)lv) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "等级不足" + lv + "无法购买", false));
                return false;
            }
            boolean flag = this.roleService.deductCurrency(role, type, (long)(int)sum_price, serial + ",buyProp," + mall);
            if (flag) {
                int bd = 0;
                if (type.equals(Const.RES_TYPE_BOUND_GOLD) || mall.equals(Integer.valueOf(Mall.BOUND_SHOP.getValue()))) {
                    bd = 2;
                }
                this.bagService.addGoods(role, (int)serial, (int)num * give, bd, true, true, "多宝购买");
                this.buyLimit(role, serial, quotaLimit, Integer.valueOf(num_), mall);
                return true;
            }
        }
        return false;
    }
    
    public void buyLimit(Role role, Integer serial, Integer quotaLimit, Integer num, Integer type) {
        if ((int)quotaLimit > -1) {
            int already = 0;
            if (type.equals(Integer.valueOf(Mall.BOUND_SHOP.getValue()))) {
                already = role.getExtendBox().getDayQuotaLimit().getInt(serial.toString(), 0) + (int)num;
                role.getExtendBox().getDayQuotaLimit().setv(serial.toString(), Integer.valueOf(already));
            }
            else {
                Map<Integer, Integer> map = (Map)role.getExtendBox().getQuotaLimit().get(type);
                if (map == null) {
                    role.getExtendBox().getQuotaLimit().put(type, new HashMap());
                    map = (Map)role.getExtendBox().getQuotaLimit().get(type);
                }
                if (map.size() <= 0) {
                    already = (int)num;
                }
                else {
                    Integer integer = (Integer)map.get(serial);
                    if (integer == null) {
                        already = (int)num;
                    }
                    else {
                        already = (int)integer + (int)num;
                    }
                }
                map.put(serial, Integer.valueOf(already));
            }
            role.save();
            RespOnlineMall respOnlineMall = new RespOnlineMall();
            List<OnlineMallInfo> list = new ArrayList<>();
            OnlineMallInfo mall = new OnlineMallInfo();
            mall.setType((int)type);
            mall.setSerial((int)serial);
            mall.setQuotaLimit((int)quotaLimit - already);
            list.add(mall);
            respOnlineMall.setType(1);
            respOnlineMall.setList(list);
            MessagePusher.pushMessage(role, respOnlineMall);
        }
    }
    
    public void openTreasure(Role role) {
        Map<Long, WeekTreasure> map = ((GamePlayService)SpringUtils.getBean(GamePlayService.class)).getWeekTreasure((long)role.getId());
        RespTreasureInfo treasureInfo = new RespTreasureInfo();
        if (Deploy.treasure <= 0) {
            treasureInfo.setCount(Deploy.treasure);
        }
        else {
            int count = role.getExtendBox().getDayQuotaLimit().getInt("treasure_1+1", 0);
            treasureInfo.setCount(Deploy.treasure - count);
        }
        for (Long key : map.keySet()) {
            treasureInfo.getList().add(new WeekTreasureInfo((WeekTreasure)map.get(key)));
        }
        MessagePusher.pushMessage(role, treasureInfo);
    }
    
    public void buyTreasure(Role role) {
        if (this.bagService.getPos(role, false) < 0) {
            MessagePusher.pushMessage(role, new RespMsg("你的物品栏已满"));
            return;
        }
        if (!this.roleService.checkCurrency(role, Const.RESTYPE_GOLD, (long)Deploy.treasurePrice)) {
            return;
        }
        if (Deploy.treasure > 0) {
            int count = role.getExtendBox().getDayQuotaLimit().getInt("treasure_1+1", 0);
            if (count >= Deploy.treasure) {
                MessagePusher.pushMessage(role, new RespMsg("购买以上限"));
                return;
            }
            role.getExtendBox().getDayQuotaLimit().setv("treasure_1+1", Integer.valueOf(count + 1));
        }
        if (this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, (long)Deploy.treasurePrice, "天天挖宝")) {
            this.bagService.addGoods(role, 90061, 1, 0, true, true, "天天挖宝");
            List<Integer> list = Arrays.asList(Integer.valueOf(90058), Integer.valueOf(90059), Integer.valueOf(90060));
            int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
            this.bagService.addGoods(role, (int)list.get(index), 1, 0, true, true, "天天挖宝");
            this.openTreasure(role);
            String name = "红色原石";
            if (index == 1) {
                name = "蓝色原石";
            }
            else if (index == 2) {
                name = "黄色原石";
            }
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("花费#<LG1>" + Deploy.treasurePrice + "#仙玉，获得" + String.format("#<II%d>#", Integer.valueOf(90061)) + "#<P>[超级藏宝图]#"));
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("获得#<LB1>" + name + "#1个"));
        }
        return;
    }
    
    public void openTreasureAward(Role role) {
        RespTreasureAwardInfo treasureAwardInfo = new RespTreasureAwardInfo();
        List<SysGiftItemOpen> list = ItemDataPool.data_gift_item_open;
        List<SysGiftItemOpen> normalList = (List)list.stream().filter(item -> item.getGiftSerial().equals(Integer.valueOf(30026))).collect(Collectors.toList());
        for (SysGiftItemOpen obj : normalList) {
            treasureAwardInfo.getNormal().add(new FieldProp(obj.getSerial(), obj.getNum()));
        }
        List<SysGiftItemOpen> seniorList = (List)list.stream().filter(item -> item.getGiftSerial().equals(Integer.valueOf(30027))).collect(Collectors.toList());
        for (SysGiftItemOpen obj2 : seniorList) {
            treasureAwardInfo.getSenior().add(new FieldProp(obj2.getSerial(), obj2.getNum()));
        }
        List<SysGiftItemOpen> supremeList = (List)list.stream().filter(item -> item.getGiftSerial().equals(Integer.valueOf(30028))).collect(Collectors.toList());
        for (SysGiftItemOpen obj3 : supremeList) {
            treasureAwardInfo.getSupreme().add(new FieldProp(obj3.getSerial(), obj3.getNum()));
        }
        MessagePusher.pushMessage(role, treasureAwardInfo);
    }
    
    public void openTreasure(Role role, Long id) {
        Map<Long, WeekTreasure> map = ((GamePlayService)SpringUtils.getBean(GamePlayService.class)).getWeekTreasure((long)role.getId());
        WeekTreasure weekTreasure = (WeekTreasure)map.get(id);
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
        return;
    }
}
