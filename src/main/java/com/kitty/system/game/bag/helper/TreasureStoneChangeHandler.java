package com.kitty.system.game.bag.helper;

import com.kitty.common.utils.ArraysUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.utils.Const;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.springframework.stereotype.Component;

@Component("TreasureStoneChange")
public class TreasureStoneChangeHandler extends BagHandler {
  private static final String LACK = "少侠所拥有的宝石种类数量不足两款，无法熔炼";
  
  private static final List<Integer> NEED = Arrays.asList(new Integer[] { Integer.valueOf(90058), Integer.valueOf(90059), Integer.valueOf(90060) });
  
  private static final long SILVER = 100000L;
  
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    NutMap nutMap = null;
    try {
      nutMap = (NutMap)Json.fromJson(NutMap.class, param);
    } catch (Exception e) {
      return null;
    } 
    ArrayList<Integer> list = new ArrayList<>();
    boolean flag = true;
    int count = 0;
    for (String key : nutMap.keySet()) {
      Integer index = Integer.valueOf(key);
      if (index.intValue() < 0 || index.intValue() > 2)
        return null; 
      list.add(NEED.get(index.intValue()));
      count += nutMap.getInt(key);
      if (this.bagService.getBagGoods(role, ((Integer)NEED.get(index.intValue())).intValue(), nutMap.getInt(key)) != null)
        flag = false; 
    } 
    if (!this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, 100000L))
      return null; 
    if (count < 2 || count > 2 || !flag)
      return "少侠所拥有的宝石种类数量不足两款，无法熔炼"; 
    if (this.bagService.getPos(role, false) < 0)
      return "你的物品栏已满"; 
    if (!this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 100000L, "原石转换"))
      return null; 
    for (String key : nutMap.keySet()) {
      if (this.bagService.consumeBagGoods(role, ((Integer)NEED.get(Integer.valueOf(key).intValue())).intValue(), nutMap.getInt(key), true, true, "原石转换") != null)
        return null; 
    } 
    List<Integer> objects = new ArrayList<>();
    if (nutMap.size() == 2) {
      int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(NEED.size() - 1));
      objects.add(NEED.get(index));
      List<Integer> notMatchList = new ArrayList<>(ArraysUtil.getDifferListByMapPlus(NEED, list));
      if (notMatchList != null && notMatchList.size() > 0) {
        objects.add(notMatchList.get(0));
      } else {
        objects.add(NEED.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(NEED.size() - 1))));
      } 
    } else {
      objects.add(NEED.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(NEED.size() - 1))));
      objects.add(NEED.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(NEED.size() - 1))));
    } 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("转换成功"));
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespHint(30, (short)1));
    for (Integer serial : objects) {
      String name = "红色原石";
      if (serial.intValue() == 90059) {
        name = "蓝色原石";
      } else if (serial.intValue() == 90060) {
        name = "黄色原石";
      } 
      this.bagService.addGoods(role, serial.intValue(), 1, 0, false, true, "原石转换");
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("获得#<LB1>" + name + "#1个"));
    } 
    return null;
  }
}
