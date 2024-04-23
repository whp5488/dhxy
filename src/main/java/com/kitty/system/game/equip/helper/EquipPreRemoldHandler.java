package com.kitty.system.game.equip.helper;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.FieldProp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("EquipPreRemold")
public class EquipPreRemoldHandler extends UpgradeHandler {
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    List<EquipNeo> list = new ArrayList<>();
    Long id = Long.valueOf(param);
    EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
    if (equipInfo.getTips() != null) {
      this.logger.error("装备重铸预览：获取物品所在位置为 = {}，获取物品ID = {}", pos, id);
      return equipInfo.getTips();
    } 
    BagEquip equip = equipInfo.getEquip();
    int classify = (type.intValue() < 1000) ? 0 : (type.intValue() / 1000);
    if (classify == 0) {
      if (type.intValue() == 1) {
        List<FiedEquip> equips = equip.getRedoGeneralHistory();
        list.add(field(equips));
      } else if (type.intValue() == 2) {
        List<List<FiedEquip>> history = equip.getRedoHighHistory();
        for (List<FiedEquip> fieldEquips : history)
          list.add(field(fieldEquips)); 
      } else if (type.intValue() == 3) {
        List<List<FiedEquip>> superHistory = equip.getRedoSuperHistory();
        for (List<FiedEquip> fieldEquips : superHistory)
          list.add(field(fieldEquips)); 
      } 
    } else if (classify == 2) {
      type = Integer.valueOf(type.intValue() % 1000);
      if (type.intValue() == 1) {
        List<FiedEquip> equips = equip.getPurifyGeneralHistory();
        list.add(field(equips));
      } else if (type.intValue() == 2) {
        List<List<FiedEquip>> history = equip.getPurifyHighHistory();
        for (List<FiedEquip> fieldEquips : history)
          list.add(field(fieldEquips)); 
      } else if (type.intValue() == 3) {
        List<List<FiedEquip>> superHistory = equip.getPurifySuperHistory();
        for (List<FiedEquip> fieldEquips : superHistory)
          list.add(field(fieldEquips)); 
      } 
    } 
    MessagePusher.pushMessage(role, (Message)new RespPreUpgradeEquip((byte)classify, param, list, type.byteValue()));
    return null;
  }
  
  public EquipNeo field(List<FiedEquip> field) {
    EquipNeo equipNeo = new EquipNeo();
    List<FieldProp> list = new ArrayList<>();
    for (int i = 0; i < field.size(); i++) {
      FiedEquip fiedEquip = field.get(i);
      if (fiedEquip.getType().intValue() != 6672 && fiedEquip.getType().intValue() != 6670 && fiedEquip.getType().intValue() != 6680)
        list.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
    } 
    equipNeo.setList(list);
    return equipNeo;
  }
}
