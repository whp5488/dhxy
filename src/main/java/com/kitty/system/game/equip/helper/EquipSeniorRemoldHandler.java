package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysSeniorBieEq;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("EquipSeniorRemold")
public class EquipSeniorRemoldHandler extends UpgradeHandler {
  public static final String NOT_FIND_STUFF = "重铸材料数量不足";
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    Long id = Long.valueOf(param);
    EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
    if (equipInfo.getTips() != null) {
      this.logger.error("高级装备重铸：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
      return equipInfo.getTips();
    } 
    BagEquip equip = equipInfo.getEquip();
    SysSeniorBieEq bieEq = (SysSeniorBieEq)EquipDataPool.seniorBieEq.get(Integer.valueOf(equip.getType()));
    Map<Integer, Integer> consume = consume(bieEq.getNeedGrade(), type.intValue());
    for (Integer key : consume.keySet()) {
      Integer amount = consume.get(key);
      if (key.equals(Const.RESTYPE_SILVER)) {
        if (role.getSilver().longValue() < amount.intValue())
          return "重铸材料数量不足"; 
        continue;
      } 
      Integer num = this.bagService.getBagGoods(role, key.intValue(), amount.intValue());
      if (num != null)
        return "重铸材料数量不足"; 
    } 
    boolean deduct = deduct(role, consume);
    if (!deduct)
      return "道具或者货币扣除失败"; 
    List<EquipNeo> list = new ArrayList<>();
    BagEquip calculate = new BagEquip();
    BeanUtils.copyBeanProp(calculate, equipInfo.getEquip());
    int count = expend(type.intValue());
    int bn = DataGlobalFunc.getGemNum(equip);
    List<List<FiedEquip>> redo = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      List<FiedEquip> field = MakeAttribute.createHighBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bieEq.getAttrFactor(), bn);
      calculate.setField(field);
      Integer score = MakeAttribute.calEquipScore(calculate);
      field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(score.intValue() + bieEq.getBaseScore())));
      list.add(new EquipNeo(field));
      redo.add(field);
    } 
    MessagePusher.pushMessage(role, (Message)new RespPreUpgradeEquip((byte)0, param, list, (byte)type.intValue()));
    equip = remoldSave(equip, redo, type.intValue());
    refresh(role, equip, pos.intValue(), id, false);
    return null;
  }
  
  public Map<Integer, Integer> consume(int grade, int type) {
    int num = expend(type);
    Map<Integer, Integer> map = new LinkedHashMap<>();
    if (grade == 40) {
      map.put(Integer.valueOf(75030), Integer.valueOf(1 * num));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(20000 * num));
    } else if (grade == 60) {
      map.put(Integer.valueOf(75030), Integer.valueOf(1 * num));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(40000 * num));
    } else if (grade == 80) {
      map.put(Integer.valueOf(75030), Integer.valueOf(1 * num));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(100000 * num));
    } else if (grade == 100) {
      map.put(Integer.valueOf(75030), Integer.valueOf(1 * num));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(200000 * num));
    } else if (grade == 120) {
      map.put(Integer.valueOf(75030), Integer.valueOf(5 * num));
      map.put(Integer.valueOf(75047), Integer.valueOf(2 * num));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(250000 * num));
    } else if (grade == 140) {
      map.put(Integer.valueOf(75030), Integer.valueOf(10 * num));
      map.put(Integer.valueOf(75047), Integer.valueOf(5 * num));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(500000 * num));
    } 
    return map;
  }
}
