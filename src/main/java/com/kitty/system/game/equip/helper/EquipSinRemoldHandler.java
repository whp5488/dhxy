package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.MapObjectUtil;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysSinBieEq;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("EquipSinRemold")
public class EquipSinRemoldHandler extends UpgradeHandler {
  public static final String NOT_FIND_STUFF = "需要{0}个悔梦石";
  
  public static final int SERIAL = 75026;
  
  public static final int SILVER = 1000000;
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    Long id = Long.valueOf(param);
    EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
    if (equipInfo.getTips() != null) {
      this.logger.error("角色编号：{}，仙器重铸：获取物品所在位置为 = {}，获取物品ID = {}", new Object[] { role.getRoleId(), pos, id });
      return equipInfo.getTips();
    } 
    BagEquip equip = equipInfo.getEquip();
    int position = equip.getPosition();
    int pj = getPj(equip.getType());
    if (pj < 1 || pj > 5)
      return null; 
    int count = expend(type.intValue());
    Integer goods = this.bagService.getBagGoods(role, 75026, count);
    if (goods != null)
      return MessageFormat.format("需要{0}个悔梦石", new Object[] { goods }); 
    int consumeSilver = consumeSilver(pj, count);
    if (consumeSilver < 0)
      return null; 
    boolean flag = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, consumeSilver);
    if (flag) {
      this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, consumeSilver, "EquipSinRemold");
      Integer integer = this.bagService.consumeBagGoods(role, 75026, count, true, true, "仙器重铸");
      if (integer != null)
        return null; 
    } else {
      return null;
    } 
    List<SysSinBieEq> bieEqs = (List<SysSinBieEq>)EquipDataPool.sinBieEq.values().stream().collect(Collectors.toList());
    List<SysSinBieEq> collect = (List<SysSinBieEq>)bieEqs.stream().filter(eq -> (eq.getPos() == position && (eq.getOwnerRole().contains(Integer.valueOf(0)) || eq.getOwnerRole().contains(role.getSerial()) || eq.getOwnerRole().contains(role.getRace())) && (eq.getSex() == 0 || role.getSex().intValue() == eq.getSex()) && (eq.getRace() == 0 || role.getRace().intValue() == eq.getRace()) && getPj(eq.getSerial()) == pj)).collect(Collectors.toList());
    List<SysSinBieEq> sysSinBieEqs = deWeight(collect, equip.getField());
    if (sysSinBieEqs.size() < 1) {
      this.logger.error("重铸仙器获取属性空，装备ID{}", id);
      return null;
    } 
    BagEquip calculate = new BagEquip();
    List<EquipNeo> list = new ArrayList<>();
    BeanUtils.copyBeanProp(calculate, equipInfo.getEquip());
    int bn = DataGlobalFunc.getGemNum(equip);
    FiedEquip purify = getPurify(equip.getField(), pj);
    List<List<FiedEquip>> redo = new ArrayList<>();
    Random random = new Random();
    for (int i = 0; i < count; i++) {
      SysSinBieEq bieEq = getBieEq(random, sysSinBieEqs);
      List<FiedEquip> field = MakeAttribute.createBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bn, bieEq.getCoef(), equip.getRefining());
      if (pj >= 5)
        if (purify != null)
          field.add(purify);  
      calculate.setField(field);
      Integer score = MakeAttribute.calEquipScore(calculate);
      field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(score.intValue() + bieEq.getBaseScore())));
      field.add(new FiedEquip(Integer.valueOf(6680), Integer.valueOf(bieEq.getSerial())));
      list.add(new EquipNeo(field));
      redo.add(field);
    } 
    MessagePusher.pushMessage(role, (Message)new RespPreUpgradeEquip((byte)0, param, list, (byte)type.intValue()));
    equip = remoldSave(equip, redo, type.intValue());
    refresh(role, equip, pos.intValue(), id, false);
    return null;
  }
  
  public SysSinBieEq getBieEq(Random random, List<SysSinBieEq> sysSinBieEqs) {
    SysSinBieEq bieEq = sysSinBieEqs.get(random.nextInt(sysSinBieEqs.size()));
    return bieEq;
  }
  
  public List<SysSinBieEq> deWeight(List<SysSinBieEq> list, List<FiedEquip> field) {
    List<String> fieldList = new ArrayList<>();
    for (FiedEquip fiedEquip : field) {
      String dist = CONVERT_DICT.getPro(fiedEquip.getType().intValue());
      if (dist != null)
        fieldList.add(dist); 
    } 
    list.forEach(e -> {
          Map<String, Integer> baseMap = MapObjectUtil.getStringToMapStrInteger(e.getBaseAttr());
          List<String> strings = new ArrayList<>(baseMap.keySet());
          List<String> diff = getDiffRent(strings, fieldList);
          if (diff.size() <= 0)
            list.remove(e); 
        });
    return list;
  }
  
  public static List<String> getDiffRent(List<String> list1, List<String> list2) {
    Map<String, Integer> map = new HashMap<>(list1.size() + list2.size());
    List<String> diff = new ArrayList<>();
    List<String> maxList = list1;
    List<String> minList = list2;
    if (list2.size() > list1.size()) {
      maxList = list2;
      minList = list1;
    } 
    for (String string : maxList)
      map.put(string, Integer.valueOf(1)); 
    for (String string : minList) {
      Integer cc = map.get(string);
      if (cc != null) {
        map.put(string, cc = Integer.valueOf(cc.intValue() + 1));
        continue;
      } 
      map.put(string, Integer.valueOf(1));
    } 
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      if (((Integer)entry.getValue()).intValue() == 1)
        diff.add(entry.getKey()); 
    } 
    return diff;
  }
  
  public int consumeSilver(int pj, int count) {
    switch (pj) {
      case 1:
        return 1000000 * count;
      case 2:
        return 2000000 * count;
      case 3:
        return 3000000 * count;
      case 4:
        return 4000000 * count;
      case 5:
        return 8000000 * count;
    } 
    return -1;
  }
}
