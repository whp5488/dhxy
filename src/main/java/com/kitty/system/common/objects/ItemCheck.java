package com.kitty.system.common.objects;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.CheckMapInt;
import com.kitty.system.game.vo.init.Item;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class ItemCheck {
  public static CheckMapInt checkAllItemTypes(List<Item> list, Role role) {
    Map<Long, BagGoods> bagGoodsMap = ((BagService)SpringUtils.getBean(BagService.class)).getBagGoods(role);
    Boolean issucc = Boolean.valueOf(true);
    Map<Integer, Integer> lackitems = new HashMap<>();
    Map<Integer, Integer> hasitems = new HashMap<>();
    for (Item item : list) {
      Integer itemType = item.getItem();
      Integer num = item.getNum();
      Integer count = bagGoodsSum(bagGoodsMap, itemType.intValue());
      if (count.intValue() < num.intValue()) {
        issucc = Boolean.valueOf(false);
        hasitems.put(itemType, count);
        lackitems.put(itemType, Integer.valueOf(num.intValue() - count.intValue()));
        continue;
      } 
      hasitems.put(itemType, num);
    } 
    CheckMapInt check = new CheckMapInt(issucc.booleanValue(), lackitems, hasitems);
    return check;
  }
  
  public static Integer bagGoodsSum(Map<Long, BagGoods> bagGoodsMap, int type) {
    int sum = 0;
    List<BagGoods> collect = (List<BagGoods>)bagGoodsMap.values().stream().filter(goods -> (goods.getSerial() == type)).collect(Collectors.toList());
    for (BagGoods goods : collect)
      sum += getBagGoodsNum(goods); 
    return Integer.valueOf(sum);
  }
  
  public static int getBagGoodsNum(BagGoods bagGoods) {
    int num = 0;
    NutMap rests = bagGoods.getRests();
    for (String s : rests.keySet())
      num += rests.getInt(s); 
    return num;
  }
}
