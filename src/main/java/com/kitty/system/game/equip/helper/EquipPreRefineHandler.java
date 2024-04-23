package com.kitty.system.game.equip.helper;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("EquipPreRefine")
public class EquipPreRefineHandler extends UpgradeHandler {
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    List<EquipNeo> list = new ArrayList<>();
    Long id = Long.valueOf(param);
    EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
    if (equipInfo.getTips() != null) {
      this.logger.error("装备炼化预览：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
      return equipInfo.getTips();
    } 
    BagEquip equip = equipInfo.getEquip();
    if (type.intValue() == 1) {
      List<FiedEquip> equips = equip.getArtificeGeneralHistory();
      list.add(new EquipNeo(equips));
    } else if (type.intValue() == 2) {
      List<List<FiedEquip>> history = equip.getArtificeHighHistory();
      for (List<FiedEquip> fieldEquips : history)
        list.add(new EquipNeo(fieldEquips)); 
    } else if (type.intValue() == 3) {
      List<List<FiedEquip>> superHistory = equip.getArtificeSuperHistory();
      for (List<FiedEquip> fieldEquips : superHistory)
        list.add(new EquipNeo(fieldEquips)); 
    } 
    MessagePusher.pushMessage(role, (Message)new RespPreUpgradeEquip((byte)1, param, list, (byte)type.intValue()));
    return null;
  }
}
