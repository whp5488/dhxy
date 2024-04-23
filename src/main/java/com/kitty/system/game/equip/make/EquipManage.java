package com.kitty.system.game.equip.make;

import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.init.domain.SysSinBieEq;
import java.util.List;

public class EquipManage {
  public static BagEquip createEquip(Integer serial) {
    Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(serial);
    BagEquip equip = null;
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue()) {
      equip = createNormalEquip(serial);
    } else if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue()) {
      equip = createSeniorEquip(serial);
    } else if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()) {
      equip = createMagicalEquip(serial);
    } else if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
      equip = createFairyEquip(serial);
    } 
    return equip;
  }
  
  public static BagEquip createNormalEquip(Integer serial) {
    SysNewBieEq bieEq = (SysNewBieEq)EquipDataPool.newBieEq.get(serial);
    if (bieEq != null)
      return createBeiEquip(bieEq.getSerial(), bieEq.getPos(), bieEq.getMaxEndure(), bieEq.getBaseScore(), bieEq.getBaseAttr(), bieEq.getName()); 
    return null;
  }
  
  public static BagEquip createSeniorEquip(Integer serial) {
    SysSeniorBieEq bieEq = (SysSeniorBieEq)EquipDataPool.seniorBieEq.get(serial);
    if (bieEq != null) {
      BagEquip equip = new BagEquip();
      equip.setPosition(bieEq.getPos());
      List<FiedEquip> field = MakeAttribute.createHighBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bieEq.getAttrFactor(), 0);
      equip.setType(serial.intValue());
      equip.setName(bieEq.getName());
      equip.setField(field);
      Integer score = MakeAttribute.calEquipScore(equip);
      field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(score.intValue() + bieEq.getBaseScore())));
      equip.setField(field);
      return equip;
    } 
    return null;
  }
  
  public static BagEquip createMagicalEquip(Integer serial) {
    SysMagicalBieEq bieEq = (SysMagicalBieEq)EquipDataPool.magicalBieEq.get(serial);
    if (bieEq != null) {
      BagEquip equip = new BagEquip();
      equip.setPosition(bieEq.getPos());
      List<FiedEquip> field = MakeAttribute.createBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bieEq.getRndRange(), 0);
      equip.setType(serial.intValue());
      equip.setName(bieEq.getName());
      equip.setField(field);
      Integer score = MakeAttribute.calEquipScore(equip);
      field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(score.intValue() + bieEq.getBaseScore())));
      equip.setField(field);
      return equip;
    } 
    return null;
  }
  
  public static BagEquip createFairyEquip(Integer serial) {
    SysSinBieEq bieEq = (SysSinBieEq)EquipDataPool.sinBieEq.get(serial);
    if (bieEq != null)
      return createBeiEquip(bieEq.getSerial(), bieEq.getPos(), bieEq.getMaxEndure(), bieEq.getBaseScore(), bieEq.getBaseAttr(), bieEq.getName()); 
    return null;
  }
  
  public static BagEquip createBeiEquip(int serial, int pos, int maxEndure, int baseScore, String baseAttr, String name) {
    BagEquip equip = new BagEquip();
    equip.setPosition(pos);
    List<FiedEquip> field = MakeAttribute.createBaseBieEq(baseAttr, maxEndure, serial, 0, 0, 0.0D);
    equip.setType(serial);
    equip.setName(name);
    equip.setField(field);
    Integer score = MakeAttribute.calEquipScore(equip);
    field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(score.intValue() + baseScore)));
    equip.setField(field);
    return equip;
  }
}
