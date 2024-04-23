package com.kitty.system.game.bag.helper;

import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.pool.ItemDataPool;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("GemDetach")
public class GemDetachHandler extends BagHandler {
  private static final String LACK = "该宝石需要大于3颗方可进行降解~";
  
  private static final String SUCCEED = "消耗#<II%d>##<%s>[%d级%s宝石]#X%d得到了#<II%d>##<LG1>[1级%s宝石]#X%d";
  
  private static final String[] GEM_TYPE = new String[] { "红", "绿", "蓝", "紫", "橙" };
  
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
    BagGoods goods = bagGoodsMap.values().stream().filter(item -> pos.equals(Integer.valueOf(item.getPos()))).findFirst().orElse(null);
    if (goods == null)
      return null; 
    List<Integer> gemList = gemList();
    if (!gemList.contains(Integer.valueOf(goods.getSerial())))
      return null; 
    String str = String.valueOf(goods.getSerial());
    int level = Integer.valueOf(str.substring(str.length() - 1)).intValue();
    if (level < 2 || level > 6)
      return null; 
    int count = DataGlobalFunc.unrestricted(goods.getRests());
    if (count <= 0)
      return null; 
    int position = goods.getSerial() % 70000 / 100;
    Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
    List<BagEquip> equips = (List<BagEquip>)bagEquipMap.values().stream().filter(e -> (e.getPosition() == position)).collect(Collectors.toList());
    for (BagEquip equip : equips)
      count += count(equip.getField(), position, level); 
    List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
    WearEquip wearEquip = wearEquips.stream().filter(e -> (e.getPosition() == position)).findFirst().orElse(null);
    if (wearEquip != null)
      count += count(wearEquip.getField(), position, level); 
    if (count < 3)
      return "该宝石需要大于3颗方可进行降解~"; 
    int lowGemNum = DataGlobalFunc.gem2lowGemNum(goods.getSerial());
    this.bagService.deleteRoleItem(role, pos.intValue(), "宝石降解");
    int goodsNum = num.intValue() * lowGemNum;
    this.bagService.addGoods(role, 70000 + position * 100 + 1, goodsNum, 0, true, false, "宝石降解");
    SysDataStuff stuff = (SysDataStuff)ItemDataPool.data_stuff.get(Integer.valueOf(goods.getSerial()));
    return String.format("消耗#<II%d>##<%s>[%d级%s宝石]#X%d得到了#<II%d>##<LG1>[1级%s宝石]#X%d", new Object[] { Integer.valueOf(goods.getSerial()), DataGlobalFunc.getColours(stuff.getItemPJ().intValue(), true), Integer.valueOf(level), GEM_TYPE[position - 1], num, Integer.valueOf(70000 + position * 100 + 1), GEM_TYPE[position - 1], Integer.valueOf(goodsNum) });
  }
  
  public static List<Integer> gemList() {
    List<Integer> list = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      for (int j = 1; j <= 7; j++)
        list.add(Integer.valueOf(70000 + i * 100 + j)); 
    } 
    return list;
  }
  
  public static int count(List<FiedEquip> field, int pos, int lv) {
    FiedEquip equip = field.stream().filter(e -> e.getType().equals(Integer.valueOf(6670))).findFirst().orElse(null);
    Map<Integer, Integer> map = new LinkedHashMap<>();
    int level = 1;
    for (int i = 1; i <= equip.getValue().intValue(); i++) {
      int num = 1;
      int serial = 70000 + pos * 100 + level;
      Integer value = map.get(Integer.valueOf(serial));
      if (value != null)
        num += value.intValue(); 
      map.put(Integer.valueOf(serial), Integer.valueOf(num));
      int cost = i % 3;
      if (cost == 0) {
        level++;
        level = (level > 7) ? 7 : level;
      } 
    } 
    return (map.get(Integer.valueOf(lv)) == null) ? 0 : ((Integer)map.get(Integer.valueOf(lv))).intValue();
  }
}
