package com.kitty.system.game.bag.helper;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.pool.ItemDataPool;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.springframework.stereotype.Component;

@Component("FiveElements")
public class FiveElementsHandler extends BagHandler {
  private static final String LACK = "材料数量不足！";
  
  private static final String SUCCEED = "灵力流转，你获得了1个{0}。";
  
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    int bagServicePos = this.bagService.getPos(role, false);
    if (bagServicePos < 0)
      return "物品栏空间不足"; 
    try {
      NutMap nutMap = (NutMap)Json.fromJson(NutMap.class, param);
      int count = 0;
      for (String key : nutMap.keySet())
        count += nutMap.getInt(key); 
      if (count != 5)
        return "材料数量不足！"; 
      Map<Integer, List<Integer>> map = getFiveData();
      List<Integer> list = new ArrayList<>();
      Integer index = Integer.valueOf(0);
      for (String key : nutMap.keySet()) {
        int anInt = nutMap.getInt(key);
        if (anInt < 1)
          return "材料数量不足！"; 
        Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        BagGoods goods = bagGoodsMap.values().stream().filter(e -> Integer.valueOf(key).equals(Integer.valueOf(e.getPos()))).findFirst().orElse(null);
        if (goods == null)
          return "材料数量不足！"; 
        if (index.intValue() == 0) {
          index = Integer.valueOf(getFiveLevel(map, Integer.valueOf(goods.getSerial())));
          if (index.intValue() == -1)
            return "材料数量不足！"; 
          list = map.get(index);
        } 
        if (!list.contains(Integer.valueOf(goods.getSerial())))
          return "材料数量不足！"; 
        Integer bagGoodsNum = this.bagService.getBagGoodsNum(role, Integer.valueOf(goods.getSerial()));
        if (bagGoodsNum == null || bagGoodsNum.intValue() < anInt)
          return "材料数量不足！"; 
      } 
      for (String key : nutMap.keySet()) {
        Integer integer = this.bagService.consumeBagGoodsByPos(role, nutMap.getInt(key), Integer.valueOf(key).intValue(), true, "五行书熔炼");
        if (integer != null)
          return "非法操作"; 
      } 
      List<Integer> integers = map.get(Integer.valueOf(index.intValue() + 1));
      Random random = new Random();
      Integer serial = integers.get(random.nextInt(integers.size()));
      MessagePusher.pushMessage(role, (Message)new RespHint(12, (short)1));
      this.bagService.addGoods(role, serial.intValue(), 1, 0, true, true, "五行书熔炼");
      SysDataStuff stuff = (SysDataStuff)ItemDataPool.data_stuff.get(serial);
      return MessageFormat.format("灵力流转，你获得了1个{0}。", new Object[] { stuff.getName() });
    } catch (Exception e) {
      return null;
    } 
  }
  
  public int getFiveLevel(Map<Integer, List<Integer>> map, Integer serial) {
    for (Integer key : map.keySet()) {
      if (key.intValue() > 2)
        return -1; 
      List<Integer> list = map.get(key);
      if (list.contains(serial))
        return key.intValue(); 
    } 
    return -1;
  }
  
  public Map<Integer, List<Integer>> getFiveData() {
    Map<Integer, List<Integer>> map = new HashMap<>();
    map.put(Integer.valueOf(1), Arrays.asList(new Integer[] { Integer.valueOf(75001), Integer.valueOf(75002), Integer.valueOf(75003), Integer.valueOf(75004), Integer.valueOf(75005) }));
    map.put(Integer.valueOf(2), Arrays.asList(new Integer[] { Integer.valueOf(75006), Integer.valueOf(75007), Integer.valueOf(75008), Integer.valueOf(75009), Integer.valueOf(75010) }));
    map.put(Integer.valueOf(3), Arrays.asList(new Integer[] { Integer.valueOf(75011), Integer.valueOf(75012), Integer.valueOf(75013), Integer.valueOf(75014), Integer.valueOf(75015) }));
    return map;
  }
}
