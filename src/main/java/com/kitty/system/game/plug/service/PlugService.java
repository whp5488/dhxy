
package com.kitty.system.game.plug.service;

import com.kitty.system.game.plug.message.RespPlugCease;
import com.kitty.common.model.Pos;
import com.kitty.system.game.plug.message.RespPlugExecuteInfo;
import com.kitty.system.init.domain.SysDataLotterySuper;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import java.util.ArrayList;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.Arrays;
import java.util.Map;
import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.game.bag.domain.BagGoods;
import java.util.Iterator;
import com.kitty.system.game.plug.message.RespPlugExecute;
import com.kitty.system.enums.plug.Plug;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.plug.message.ReqPlugExecuteTreasure;
import com.kitty.system.game.role.domain.Setting;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.plug.message.RespPlugEndTime;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.bag.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.SettingService;
import org.springframework.stereotype.Service;

@Service
public class PlugService
{
    private static String plug;
    @Autowired
    private SettingService settingService;
    @Autowired
    private BagService bagService;
    @Autowired
    private MapService mapService;
    
    public void getPlugEndTime(final Role role) {
        final Setting setting = this.settingService.getSetting(role);
        MessagePusher.pushMessage(role, new RespPlugEndTime(setting.getPlugEndTime()));
    }
    
    public void plugExecute(final Role role, final ReqPlugExecuteTreasure reqPlugExecuteTreasure) {
        if (role.getPos().getMapId().equals(1201)) {
            MessagePusher.pushMessage(role, new RespMsg("天牢重地，不可随意离开！"));
            return;
        }
        final String msg = DataGlobalFunc.needGradeJudge(0, 60, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            MessagePusher.pushMessage(role, new RespMsg(msg));
            return;
        }
        final List<Integer> collect = reqPlugExecuteTreasure.getList().stream().distinct().collect(Collectors.toList());
        final List<Integer> list = Plug.toList();
        for (final Integer serial : collect) {
            if (!list.contains(serial)) {
                return;
            }
        }
        final int time = new Long(System.currentTimeMillis() / 1000L).intValue();
        final Setting setting = this.settingService.getSetting(role);
        final int plugEndTime = setting.getPlugEndTime();
        if (plugEndTime < 0 || plugEndTime < time) {
            MessagePusher.pushMessage(role, new RespMsg("你的小助手已到期，请先补充时间吧！"));
            return;
        }
        final int pos = this.bagService.residueBagPos(role, false);
        if (pos < 2) {
            this.stopPlugExecute(role, "背包空间不足，小助手已停止", Plug.TREASURE_MAP.getSerial());
            return;
        }
        role.pushTempCache(PlugService.plug, collect);
        this.plugExecute(role, collect);
        MessagePusher.pushMessage(role, new RespPlugExecute(collect));
    }
    
    public void plugExecute(final Role role, final List<Integer> list) {
        boolean flag = true;
        if (list.contains(Plug.TREASURE_MAP.getSerial())) {
            final int pos = this.bagService.residueBagPos(role, false);
            if (pos < 2) {
                this.stopPlugExecute(role, "背包空间不足，小助手已停止", Plug.TREASURE_MAP.getSerial());
                return;
            }
            final List<BagGoods> bagItemList = this.bagService.getBagItem(role, 90033);
            if (bagItemList != null && bagItemList.size() > 0) {
                BagGoods goods = bagItemList.stream().filter(e -> role.getPos().getMapId().equals(e.getSpecial().getInt("map"))).findFirst().orElse(null);
                if (goods == null) {
                    goods = bagItemList.get(0);
                }
                final NutConcurrentMap nutConcurrentMap = new NutConcurrentMap();
                nutConcurrentMap.setAll((Map<String, Object>)goods.getSpecial());
                nutConcurrentMap.setv("pos", goods.getPos());
                role.pushTempCache(Plug.TREASURE_MAP.getDict(), nutConcurrentMap);
                this.mapService.mapTarget(role, nutConcurrentMap.getInt("map", 0), nutConcurrentMap.getInt("x", 0), nutConcurrentMap.getInt("y", 0));
                flag = false;
            }
            else if (list.contains(Plug.TREASURE_MAP_BUY.getSerial())) {
                final long gold = DataGlobalFunc.getMallItemGold(Arrays.asList(75049, 75050, 75051));
                if (gold <= 0L) {
                    this.stopPlugExecute(role, "没有可以使用的高级藏宝图了哦，小助手已停止", Plug.TREASURE_MAP.getSerial());
                    return;
                }
                if (SpringUtils.getRoleService().deductCurrency(role, Const.RESTYPE_GOLD, gold, "自动挖宝-购买宝图")) {
                    this.bagService.addGoods(role, 90033, 1, 0, false, false, "自动挖宝-购买宝图");
                    this.plugExecute(role, list);
                    return;
                }
                this.stopPlugExecute(role, "仙玉不足，无法自动购买见闻录合成高级藏宝图，小助手已停止", Plug.TREASURE_MAP.getSerial());
                return;
            }
            else {
                this.stopPlugExecute(role, "没有可以使用的高级藏宝图了哦，小助手已停止", Plug.TREASURE_MAP.getSerial());
            }
        }
        if (flag) {
            role.popTempCache("plug");
        }
    }
    
    public void plugTreasureMap(final Role role) {
        final List<Integer> collect = role.getTempCache(PlugService.plug, (List<Integer>)null);
        if (collect == null) {
            role.popTempCache("plug");
            return;
        }
        final Pos rolePos = role.getPos();
        final int time = new Long(System.currentTimeMillis() / 1000L).intValue();
        final Setting setting = this.settingService.getSetting(role);
        final int plugEndTime = setting.getPlugEndTime();
        if (plugEndTime < 0 || plugEndTime < time) {
            MessagePusher.pushMessage(role, new RespMsg("你的小助手已到期，请先补充时间吧！"));
            return;
        }
        if (collect.contains(Plug.TREASURE_MAP.getSerial())) {
            final NutConcurrentMap nutConcurrentMap = role.getTempCache(Plug.TREASURE_MAP.getDict(), (NutConcurrentMap)null);
            if (nutConcurrentMap == null) {
                this.plugExecute(role, collect);
                return;
            }
            if (!rolePos.getMapId().equals(nutConcurrentMap.getInt("map", 0))) {
                return;
            }
            if (!rolePos.getX().equals(nutConcurrentMap.getInt("x", 0)) || !rolePos.getY().equals(nutConcurrentMap.getInt("y", 0))) {
                return;
            }
            final int pos = nutConcurrentMap.getInt("pos", -1);
            if (pos < 0) {
                this.stopPlugExecute(role, "没有可以使用的高级藏宝图了哦，小助手已停止", Plug.TREASURE_MAP.getSerial());
                return;
            }
            final List<SysDataLotterySuper> randoms = WelfareDataPool.createRandoms(15);
            final List<Lottery> list = new ArrayList<Lottery>();
            randoms.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", itemOpen.getRate())));
            final List<Double> orignalRates = new ArrayList<Double>(list.size());
            for (final Lottery lottery : list) {
                orignalRates.add(lottery.getBili());
            }
            final int index = LotteryUtils.lottery(orignalRates);
            Lottery lottery = list.get(index);
            final SysDataLotterySuper lotterySuper = randoms.stream().filter(e -> lottery.getSerial().equals(e.getId())).findFirst().orElse(null);
            final int serial = WelfareDataPool.getLotterySuperEquip(lotterySuper.getSerial(), role.getRace());
            final Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
            final BagGoods goods = bagGoodsMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
            if (goods != null && goods.getSerial() == 90033) {
                int bd = 0;
                final int anInt = goods.getRests().getInt(String.valueOf(2), 0);
                if (anInt > 0) {
                    bd = 2;
                }
                this.bagService.send(role, serial, lotterySuper.getType(), lotterySuper.getNum(), pos, lotterySuper.getIsNotice(), "#<BB>%s#跟随藏宝图的指引，挖到了强盗珍藏的%s#<O>X%d#，一时间喜笑颜开#<E:86>#", bd);
                MessagePusher.pushMessage(role.getId(), new RespPlugExecuteInfo(serial, lotterySuper.getNum()));
                this.plugExecute(role, collect);
            }
            else {
                this.stopPlugExecute(role, "没有可以使用的高级藏宝图了哦，小助手已停止", Plug.TREASURE_MAP.getSerial());
            }
        }
    }
    
    public void stopPlugExecute(final Role role, final List<Integer> list) {
        if (list != null && list.size() > 0) {
            for (final Integer serial : list) {
                String tips = Plug.getTips(serial);
                if (tips != null) {
                    tips += "小助手已停止";
                }
                this.stopPlugExecute(role, tips, serial);
            }
        }
        else {
            this.stopPlugExecute(role);
        }
    }
    
    public void stopPlugExecute(final Role role) {
        role.popTempCache("plug");
        MessagePusher.pushMessage(role, new RespPlugCease());
        MessagePusher.pushMessage(role, new RespMsg("小助手已停止"));
    }
    
    public void stopPlugExecute(final Role role, String tips, final int serial) {
        final List<Integer> collect = role.getTempCache(PlugService.plug, (List<Integer>)null);
        if (collect == null) {
            role.popTempCache("plug");
            return;
        }
        final List<Integer> list = new ArrayList<Integer>();
        if (Plug.TREASURE_MAP.getSerial() == serial) {
            list.add(Plug.TREASURE_MAP.getSerial());
            list.add(Plug.TREASURE_MAP_BUY.getSerial());
            collect.removeIf(e -> e.equals(Plug.TREASURE_MAP.getSerial()) || e.equals(Plug.TREASURE_MAP_BUY.getSerial()));
        }
        if (collect.size() <= 0) {
            list.clear();
            tips = "小助手已停止";
            role.popTempCache("plug");
        }
        MessagePusher.pushMessage(role, new RespPlugCease(list));
        if (tips != null) {
            MessagePusher.pushMessage(role, new RespMsg(tips));
        }
    }
    
    static {
        PlugService.plug = "plug";
    }
}
