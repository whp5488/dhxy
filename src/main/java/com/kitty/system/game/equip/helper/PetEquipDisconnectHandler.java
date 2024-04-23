package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.equip.message.RespPetEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysPetAcsry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("PetEquipDisconnect")
public class PetEquipDisconnectHandler extends UpgradeHandler {
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip(role.getId().longValue());
    if (petEquipMap == null || petEquipMap.size() <= 0)
      return null; 
    if (type.equals(Integer.valueOf(1))) {
      if (!NumberUtil.isNumber(param))
        return null; 
      Long id = Long.valueOf(param);
      PetEquip equip = petEquipMap.get(id);
      if (equip == null)
        return null; 
      if (equip.getPId() > 0L)
        return "请先卸下装备，再分解"; 
      if (equip.getLock() > 0)
        return "该物品已被锁定，请解锁后再试"; 
      SysPetAcsry sysPetAcsry = (SysPetAcsry)EquipDataPool.petAcsryMap.get(Integer.valueOf(equip.getSerial()));
      if (sysPetAcsry == null)
        return null; 
      Map<Integer, Long> decompose = decompose(sysPetAcsry.getGrade(), equip.getProductOrder());
      if (decompose == null && decompose.size() > 0)
        return null; 
      petEquipMap.remove(equip.getId());
      equip.setDelete(true);
      equip.save();
      List<String> objects = new ArrayList<>(1);
      objects.add(String.valueOf(equip.getId()));
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespPetEquip(objects));
      send(role, decompose);
    } else {
      String[] split = param.split("-");
      List<PetEquip> objects = new ArrayList<>();
      for (String str : split) {
        if (NumberUtil.isNumber(str)) {
          Byte order = Byte.valueOf(str);
          List<PetEquip> collect = (List<PetEquip>)petEquipMap.values().stream().filter(e -> (e.getProductOrder() == order.byteValue() && e.getPId() <= 0L && e.getLock() <= 0)).collect(Collectors.toList());
          if (collect.size() > 0)
            objects.addAll(collect); 
        } 
      } 
      Map<Integer, Long> map = new HashMap<>();
      List<String> delList = new ArrayList<>(objects.size());
      for (PetEquip equip : objects) {
        SysPetAcsry sysPetAcsry = (SysPetAcsry)EquipDataPool.petAcsryMap.get(Integer.valueOf(equip.getSerial()));
        if (sysPetAcsry == null)
          continue; 
        Map<Integer, Long> decompose = decompose(sysPetAcsry.getGrade(), equip.getProductOrder());
        if (decompose == null && decompose.size() > 0)
          continue; 
        petEquipMap.remove(equip.getId());
        equip.setDelete(true);
        equip.save();
        for (Integer key : decompose.keySet()) {
          Long value = map.get(key);
          if (value != null) {
            value = Long.valueOf(value.longValue() + ((Long)decompose.get(key)).longValue());
          } else {
            value = decompose.get(key);
          } 
          map.put(key, value);
        } 
        delList.add(String.valueOf(equip.getId()));
      } 
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespPetEquip(delList));
      send(role, map);
    } 
    return null;
  }
  
  public void send(Role role, Map<Integer, Long> map) {
    for (Integer key : map.keySet()) {
      Long num = map.get(key);
      if (Const.RESTYPE_COIN.equals(key)) {
        this.roleService.addCurrency(role, Const.RESTYPE_COIN, num, "分解宠物配饰", true, false);
        continue;
      } 
      this.bagService.addGoods(role, key.intValue(), num.intValue(), 0, true, true, "分解宠物配饰");
    } 
  }
  
  public Map<Integer, Long> decompose(int grade, byte suit) {
    Map<Integer, Long> map = new HashMap<>();
    if (suit == ACC_GRADE.PLAY_WITH.getValue()) {
      if (grade == 1) {
        map.put(Const.RESTYPE_COIN, Long.valueOf(5000L));
      } else if (grade == 2) {
        map.put(Const.RESTYPE_COIN, Long.valueOf(10000L));
        if (RandomUtil.round(Integer.valueOf(0), Integer.valueOf(100)) <= 33)
          map.put(Integer.valueOf(75401), Long.valueOf(1L)); 
      } else if (grade == 3) {
        map.put(Integer.valueOf(75401), Long.valueOf(1L));
        map.put(Const.RESTYPE_COIN, Long.valueOf(15000L));
      } 
    } else if (suit == ACC_GRADE.RESERVE.getValue()) {
      if (grade == 1) {
        map.put(Const.RESTYPE_COIN, Long.valueOf(15000L));
      } else if (grade == 2) {
        if (RandomUtil.round(Integer.valueOf(0), Integer.valueOf(100)) <= 33)
          map.put(Integer.valueOf(75401), Long.valueOf(1L)); 
        map.put(Const.RESTYPE_COIN, Long.valueOf(30000L));
      } else if (grade == 3) {
        map.put(Integer.valueOf(75401), Long.valueOf(1L));
        map.put(Const.RESTYPE_COIN, Long.valueOf(45000L));
      } 
    } else if (suit == ACC_GRADE.PRICELESS.getValue()) {
      if (grade == 1) {
        map.put(Const.RESTYPE_COIN, Long.valueOf(50000L));
      } else if (grade == 2) {
        if (RandomUtil.round(Integer.valueOf(0), Integer.valueOf(100)) <= 33)
          map.put(Integer.valueOf(75401), Long.valueOf(1L)); 
        map.put(Const.RESTYPE_COIN, Long.valueOf(100000L));
      } else if (grade == 3) {
        map.put(Integer.valueOf(75401), Long.valueOf(1L));
        map.put(Const.RESTYPE_COIN, Long.valueOf(150000L));
      } 
    } 
    return map;
  }
}
