package com.kitty.system.game.plug.service;

import com.kitty.common.bean.Lottery;
import com.kitty.common.model.Pos;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.enums.plug.Plug;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.plug.message.ReqPlugExecuteTreasure;
import com.kitty.system.game.plug.message.RespPlugCease;
import com.kitty.system.game.plug.message.RespPlugEndTime;
import com.kitty.system.game.plug.message.RespPlugExecute;
import com.kitty.system.game.plug.message.RespPlugExecuteInfo;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.domain.Setting;
import com.kitty.system.game.role.service.SettingService;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.SysDataLotterySuper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlugService {
  private static String plug = "plug";
  
  @Autowired
  private SettingService settingService;
  
  @Autowired
  private BagService bagService;
  
  @Autowired
  private MapService mapService;
  
  public void getPlugEndTime(Role role) {
    Setting setting = this.settingService.getSetting(role);
    MessagePusher.pushMessage(role, (Message)new RespPlugEndTime(setting.getPlugEndTime()));
  }
  
  public void plugExecute(Role role, ReqPlugExecuteTreasure reqPlugExecuteTreasure) {
    if (role.getPos().getMapId().equals(Integer.valueOf(1201))) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("天牢重地，不可随意离开！"));
      return;
    } 
    String msg = DataGlobalFunc.needGradeJudge(0, 60, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
    if (msg != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(msg));
      return;
    } 
    List<Integer> collect = (List<Integer>)reqPlugExecuteTreasure.getList().stream().distinct().collect(Collectors.toList());
    List<Integer> list = Plug.toList();
    for (Integer serial : collect) {
      if (!list.contains(serial))
        return; 
    } 
    int time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    Setting setting = this.settingService.getSetting(role);
    int plugEndTime = setting.getPlugEndTime();
    if (plugEndTime < 0 || plugEndTime < time) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你的小助手已到期，请先补充时间吧！"));
      return;
    } 
    int pos = this.bagService.residueBagPos(role, false);
    if (pos < 2) {
      stopPlugExecute(role, "背包空间不足，小助手已停止", Plug.TREASURE_MAP.getSerial());
      return;
    } 
    role.pushTempCache(plug, collect);
    plugExecute(role, collect);
    MessagePusher.pushMessage(role, (Message)new RespPlugExecute(collect));
  }
  
  public void plugExecute(Role role, List<Integer> list) {
    boolean flag = true;
    if (list.contains(Integer.valueOf(Plug.TREASURE_MAP.getSerial()))) {
      int pos = this.bagService.residueBagPos(role, false);
      if (pos < 2) {
        stopPlugExecute(role, "背包空间不足，小助手已停止", Plug.TREASURE_MAP.getSerial());
        return;
      } 
      List<BagGoods> bagItemList = this.bagService.getBagItem(role, 90033);
      if (bagItemList != null && bagItemList.size() > 0) {
        BagGoods goods = bagItemList.stream().filter(e -> role.getPos().getMapId().equals(Integer.valueOf(e.getSpecial().getInt("map")))).findFirst().orElse(null);
        if (goods == null)
          goods = bagItemList.get(0); 
        NutConcurrentMap nutConcurrentMap = new NutConcurrentMap();
        nutConcurrentMap.setAll((Map)goods.getSpecial());
        nutConcurrentMap.setv("pos", Integer.valueOf(goods.getPos()));
        role.pushTempCache(Plug.TREASURE_MAP.getDict(), nutConcurrentMap);
        this.mapService.mapTarget(role, nutConcurrentMap.getInt("map", 0), nutConcurrentMap.getInt("x", 0), nutConcurrentMap.getInt("y", 0));
        flag = false;
      } else {
        if (list.contains(Integer.valueOf(Plug.TREASURE_MAP_BUY.getSerial()))) {
          long gold = DataGlobalFunc.getMallItemGold(Arrays.asList(new Integer[] { Integer.valueOf(75049), Integer.valueOf(75050), Integer.valueOf(75051) }));
          if (gold <= 0L) {
            stopPlugExecute(role, "没有可以使用的高级藏宝图了哦，小助手已停止", Plug.TREASURE_MAP.getSerial());
            return;
          } 
          if (SpringUtils.getRoleService().deductCurrency(role, Const.RESTYPE_GOLD, gold, "自动挖宝-购买宝图")) {
            this.bagService.addGoods(role, 90033, 1, 0, false, false, "自动挖宝-购买宝图");
            plugExecute(role, list);
            return;
          } 
          stopPlugExecute(role, "仙玉不足，无法自动购买见闻录合成高级藏宝图，小助手已停止", Plug.TREASURE_MAP.getSerial());
          return;
        } 
        stopPlugExecute(role, "没有可以使用的高级藏宝图了哦，小助手已停止", Plug.TREASURE_MAP.getSerial());
      } 
    } 
    if (flag)
      role.popTempCache("plug"); 
  }
  
  public void plugTreasureMap(Role role) {
    List<Integer> collect = (List<Integer>)role.getTempCache(plug, null);
    if (collect == null) {
      role.popTempCache("plug");
      return;
    } 
    Pos rolePos = role.getPos();
    int time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    Setting setting = this.settingService.getSetting(role);
    int plugEndTime = setting.getPlugEndTime();
    if (plugEndTime < 0 || plugEndTime < time) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你的小助手已到期，请先补充时间吧！"));
      return;
    } 
    if (collect.contains(Integer.valueOf(Plug.TREASURE_MAP.getSerial()))) {
      NutConcurrentMap nutConcurrentMap = (NutConcurrentMap)role.getTempCache(Plug.TREASURE_MAP.getDict(), null);
      if (nutConcurrentMap == null) {
        plugExecute(role, collect);
        return;
      } 
      if (rolePos.getMapId().equals(Integer.valueOf(nutConcurrentMap.getInt("map", 0)))) {
        if (!rolePos.getX().equals(Integer.valueOf(nutConcurrentMap.getInt("x", 0))) || !rolePos.getY().equals(Integer.valueOf(nutConcurrentMap.getInt("y", 0))))
          return; 
      } else {
        return;
      } 
      int pos = nutConcurrentMap.getInt("pos", -1);
      if (pos < 0) {
        stopPlugExecute(role, "没有可以使用的高级藏宝图了哦，小助手已停止", Plug.TREASURE_MAP.getSerial());
        return;
      } 
      List<SysDataLotterySuper> randoms = WelfareDataPool.createRandoms(15);
      List<Lottery> list = new ArrayList<>();
      randoms.forEach(itemOpen -> list.add(new Lottery(Integer.valueOf(itemOpen.getId()), "", Double.valueOf(itemOpen.getRate()))));
      List<Double> orignalRates = new ArrayList<>(list.size());
      for (Lottery lottery1 : list)
        orignalRates.add(lottery1.getBili()); 
      int index = LotteryUtils.lottery(orignalRates);
      Lottery lottery = list.get(index);
      SysDataLotterySuper lotterySuper = randoms.stream().filter(e -> lottery.getSerial().equals(Integer.valueOf(e.getId()))).findFirst().orElse(null);
      int serial = WelfareDataPool.getLotterySuperEquip(lotterySuper.getSerial(), role.getRace().intValue());
      Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
      BagGoods goods = bagGoodsMap.values().stream().filter(e -> (e.getPos() == pos)).findFirst().orElse(null);
      if (goods != null && goods.getSerial() == 90033) {
        int bd = 0;
        int anInt = goods.getRests().getInt(String.valueOf(2), 0);
        if (anInt > 0)
          bd = 2; 
        this.bagService.send(role, serial, lotterySuper.getType(), lotterySuper.getNum(), pos, lotterySuper.getIsNotice(), "#<BB>%s#跟随藏宝图的指引，挖到了强盗珍藏的%s#<O>X%d#，一时间喜笑颜开#<E:86>#", bd);
        MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespPlugExecuteInfo(serial, lotterySuper.getNum()));
        plugExecute(role, collect);
      } else {
        stopPlugExecute(role, "没有可以使用的高级藏宝图了哦，小助手已停止", Plug.TREASURE_MAP.getSerial());
      } 
    } 
  }
  
  public void stopPlugExecute(Role role, List<Integer> list) {
    if (list != null && list.size() > 0) {
      for (Integer serial : list) {
        String tips = Plug.getTips(serial.intValue());
        if (tips != null)
          tips = tips + "小助手已停止"; 
        stopPlugExecute(role, tips, serial.intValue());
      } 
    } else {
      stopPlugExecute(role);
    } 
  }
  
  public void stopPlugExecute(Role role) {
    role.popTempCache("plug");
    MessagePusher.pushMessage(role, (Message)new RespPlugCease());
    MessagePusher.pushMessage(role, (Message)new RespMsg("小助手已停止"));
  }
  
  public void stopPlugExecute(Role role, String tips, int serial) {
    List<Integer> collect = (List<Integer>)role.getTempCache(plug, null);
    if (collect == null) {
      role.popTempCache("plug");
      return;
    } 
    List<Integer> list = new ArrayList<>();
    if (Plug.TREASURE_MAP.getSerial() == serial) {
      list.add(Integer.valueOf(Plug.TREASURE_MAP.getSerial()));
      list.add(Integer.valueOf(Plug.TREASURE_MAP_BUY.getSerial()));
      collect.removeIf(e -> (e.equals(Integer.valueOf(Plug.TREASURE_MAP.getSerial())) || e.equals(Integer.valueOf(Plug.TREASURE_MAP_BUY.getSerial()))));
    } 
    if (collect.size() <= 0) {
      list.clear();
      tips = "小助手已停止";
      role.popTempCache("plug");
    } 
    MessagePusher.pushMessage(role, (Message)new RespPlugCease(list));
    if (tips != null)
      MessagePusher.pushMessage(role, (Message)new RespMsg(tips)); 
  }
}
