package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.common.ADORN;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.init.pool.ItemDataPool;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component("EquipUpgrade")
public class EquipUpgradeHandler extends UpgradeHandler {
  public static final String NOT_FIND_STUFF = "升级材料数量不足";
  
  public static final String SUCCESS = "消耗{0}合成{1}";
  
  public static final String NOT_LEVEL = "等级升到{0}级才能升级该装备";
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    Long id = Long.valueOf(param);
    EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
    if (equipInfo.getTips() != null)
      return equipInfo.getTips(); 
    BagEquip equip = equipInfo.getEquip();
    Integer order = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(equip.getType()));
    int nextSerial = 0;
    if (order.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue()) {
      SysNewBieEq sysNewBieEq = (SysNewBieEq)EquipDataPool.newBieEq.get(Integer.valueOf(equip.getType()));
      nextSerial = nextSerial(sysNewBieEq.getNeedGrade(), sysNewBieEq.getSerial(), sysNewBieEq.getPos());
    } else if (order.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue()) {
      SysSeniorBieEq sysSeniorBieEq = (SysSeniorBieEq)EquipDataPool.seniorBieEq.get(Integer.valueOf(equip.getType()));
      nextSerial = nextSerial(sysSeniorBieEq.getNeedGrade(), sysSeniorBieEq.getSerial(), sysSeniorBieEq.getPos());
    } 
    SysSeniorBieEq bieEq = (SysSeniorBieEq)EquipDataPool.seniorBieEq.get(Integer.valueOf(nextSerial));
    if (nextSerial <= 0 || bieEq == null)
      return "该装备不可以升级"; 
    int needGrade = bieEq.getNeedGrade();
    int needRei = bieEq.getNeedRei();
    String msg = needRei + "转";
    if (role.getRelive().intValue() < needRei)
      return MessageFormat.format("等级升到{0}级才能升级该装备", new Object[] { ((needRei > 0) ? msg : "") + needGrade }); 
    if (role.getLevel().intValue() < needGrade)
      return MessageFormat.format("等级升到{0}级才能升级该装备", new Object[] { ((needRei > 0) ? msg : "") + needGrade }); 
    Map<Integer, Integer> consume = consume(needGrade);
    for (Integer key : consume.keySet()) {
      Integer amount = consume.get(key);
      if (key.equals(Const.RESTYPE_COIN)) {
        if (role.getCoin().longValue() < amount.intValue())
          return "升级材料数量不足"; 
        continue;
      } 
      Integer num = this.bagService.getBagGoods(role, key.intValue(), amount.intValue());
      if (num != null)
        return "升级材料数量不足"; 
    } 
    String materials = "";
    for (Integer key : consume.keySet()) {
      Integer amount = consume.get(key);
      if (key.equals(Const.RESTYPE_COIN)) {
        materials = materials + "#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(Long.valueOf(amount.longValue())) + ",";
        this.roleService.deductCurrency(role, Const.RESTYPE_COIN, amount.intValue(), "EquipUpgrade");
        continue;
      } 
      SysDataStuff stuff = (SysDataStuff)ItemDataPool.data_stuff.get(key);
      materials = materials + String.format("#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", new Object[] { stuff.getSerial(), stuff.getSerial(), stuff.getSerial(), stuff.getName() });
      if (amount.intValue() > 1) {
        materials = materials + "x" + amount + ",";
      } else {
        materials = materials + ",";
      } 
      Integer integer = this.bagService.consumeBagGoods(role, key.intValue(), amount.intValue(), true, true, "装备升级");
      if (integer != null)
        return null; 
    } 
    materials = materials.substring(0, materials.length() - 1);
    int bn = 0;
    Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6670))).findFirst();
    if (first.isPresent()) {
      FiedEquip fiedEquip = first.get();
      bn = fiedEquip.getValue().intValue();
    } 
    BagEquip newEq = new BagEquip();
    BeanUtils.copyBeanProp(newEq, equip);
    List<FiedEquip> field = MakeAttribute.createHighBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bieEq.getAttrFactor(), bn);
    newEq = clearHistory(newEq);
    newEq.setField(field);
    newEq.setType(bieEq.getSerial());
    newEq.setName(bieEq.getName());
    newEq.setField(field);
    Integer score = MakeAttribute.calEquipScore(newEq);
    field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(score.intValue() + bieEq.getBaseScore())));
    newEq.setField(field);
    if (pos.intValue() < 0)
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(String.format("穿上装备#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", new Object[] { Integer.valueOf(newEq.getType()), Integer.valueOf(newEq.getType()), Integer.valueOf(newEq.getType()), newEq.getName() }))); 
    refresh(role, newEq, pos.intValue(), id, true);
    role.save();
    animation(role, equip, newEq, ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue());
    MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), MessageFormat.format("消耗{0}合成{1}", new Object[] { materials, newEq.getName() }), false));
    return null;
  }
  
  public int nextSerial(int grade, int serial, int pos) {
    if (grade == 1) {
      serial += 1040000;
    } else if (grade < 100) {
      serial -= 10000;
    } else if (grade == 100) {
      serial += 40000;
      if (ADORN.SHOES.getValue() == pos) {
        int round = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(1000));
        if (round < 500)
          serial++; 
      } 
    } else {
      serial += 10000;
      if (ADORN.SHOES.getValue() == pos) {
        int round = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(1000));
        if (round < 500)
          serial++; 
      } 
    } 
    return serial;
  }
  
  public Map<Integer, Integer> consume(int grade) {
    Map<Integer, Integer> map = new LinkedHashMap<>();
    if (grade == 40) {
      map.put(Integer.valueOf(75030), Integer.valueOf(1));
      map.put(Const.RESTYPE_COIN, Integer.valueOf(50000));
    } else if (grade == 60) {
      map.put(Integer.valueOf(75030), Integer.valueOf(1));
      map.put(Const.RESTYPE_COIN, Integer.valueOf(100000));
    } else if (grade == 80) {
      map.put(Integer.valueOf(75030), Integer.valueOf(1));
      map.put(Const.RESTYPE_COIN, Integer.valueOf(200000));
    } else if (grade == 100) {
      map.put(Integer.valueOf(75030), Integer.valueOf(1));
      map.put(Const.RESTYPE_COIN, Integer.valueOf(500000));
    } else if (grade == 120) {
      map.put(Integer.valueOf(75030), Integer.valueOf(10));
      map.put(Integer.valueOf(75047), Integer.valueOf(10));
      map.put(Const.RESTYPE_COIN, Integer.valueOf(800000));
    } else if (grade == 140) {
      map.put(Integer.valueOf(75030), Integer.valueOf(50));
      map.put(Integer.valueOf(75047), Integer.valueOf(50));
      map.put(Const.RESTYPE_COIN, Integer.valueOf(2000000));
    } 
    return map;
  }
}
