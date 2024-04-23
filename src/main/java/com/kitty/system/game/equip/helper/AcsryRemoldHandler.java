package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataAcsry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("AcsryRemold")
public class AcsryRemoldHandler extends UpgradeHandler {
  public static final List<Integer> integers = Arrays.asList(new Integer[] { Integer.valueOf(92), Integer.valueOf(7), Integer.valueOf(1) });
  
  public static final String NOT_FIND_STUFF = "所需材料不足";
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    Long id = Long.valueOf(param);
    EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
    if (equipInfo.getTips() != null) {
      this.logger.error("配饰重铸：获取物品所在位置为 = {}，获取物品ID = {}", pos, id);
      return equipInfo.getTips();
    } 
    BagEquip equip = equipInfo.getEquip();
    SysDataAcsry acsry = (SysDataAcsry)EquipDataPool.acsryMap.get(Integer.valueOf(equip.getType()));
    if (acsry == null)
      return null; 
    Map<Integer, Integer> consume = consume(type.intValue());
    for (Integer key : consume.keySet()) {
      Integer amount = consume.get(key);
      if (key.equals(Const.RESTYPE_SILVER)) {
        if (role.getSilver().longValue() < amount.intValue())
          return "所需银两不足"; 
        continue;
      } 
      Integer num = this.bagService.getBagGoods(role, key.intValue(), amount.intValue());
      if (num != null)
        return "所需材料不足"; 
    } 
    boolean deduct = deduct(role, consume);
    if (!deduct)
      return "道具或者货币扣除失败"; 
    List<EquipNeo> list = new ArrayList<>();
    BagEquip calculate = new BagEquip();
    BeanUtils.copyBeanProp(calculate, equipInfo.getEquip());
    int count = expend(type.intValue());
    List<List<FiedEquip>> redo = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      List<FiedEquip> field = MakeAttribute.createBaseAcsry(acsry, equip.getField(), integers);
      list.add(new EquipNeo(field));
      redo.add(field);
    } 
    MessagePusher.pushMessage(role, (Message)new RespPreUpgradeEquip((byte)0, param, list, (byte)type.intValue()));
    equip = remoldSave(equip, redo, type.intValue());
    refresh(role, equip, pos.intValue(), id, false);
    return null;
  }
  
  public Map<Integer, Integer> consume(int type) {
    Map<Integer, Integer> map = new LinkedHashMap<>();
    if (type == 1) {
      map.put(Integer.valueOf(75031), Integer.valueOf(1));
      map.put(Integer.valueOf(75048), Integer.valueOf(5));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(300000));
    } else if (type == 2) {
      map.put(Integer.valueOf(75031), Integer.valueOf(5));
      map.put(Integer.valueOf(75048), Integer.valueOf(25));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(1425000));
    } else if (type == 3) {
      map.put(Integer.valueOf(75031), Integer.valueOf(20));
      map.put(Integer.valueOf(75048), Integer.valueOf(100));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(5400000));
    } 
    return map;
  }
}
