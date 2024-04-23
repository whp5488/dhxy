package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.utils.EquipUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("EquipRefineReplace")
public class EquipRefineReplaceHandler extends UpgradeHandler {
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    Long id = Long.valueOf(param);
    EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
    if (equipInfo.getTips() != null) {
      this.logger.error("炼化属性替换：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
      return equipInfo.getTips();
    } 
    BagEquip equip = equipInfo.getEquip();
    int index = type.intValue() / 1000;
    int genre = type.intValue() % 1000;
    if (genre == 1) {
      if (equip.getArtificeGeneralHistory().size() <= 0)
        return null; 
      equip.setArtifice(field(equip.getArtificeGeneralHistory()));
      equip.setArtificeGeneralHistory(new ArrayList());
    } else if (genre == 2) {
      if (equip.getArtificeHighHistory().size() <= 0)
        return null; 
      equip.setArtifice(field(equip.getArtificeHighHistory().get(index)));
      equip.setArtificeHighHistory(new ArrayList());
    } else if (genre == 3) {
      if (equip.getArtificeSuperHistory().size() <= 0)
        return null; 
      equip.setArtifice(field(equip.getArtificeSuperHistory().get(index)));
      equip.setArtificeSuperHistory(new ArrayList());
    } 
    EquipAlike alike = DataGlobalFunc.getEquipData(Integer.valueOf(equip.getType()));
    Integer score = MakeAttribute.calEquipScore(equip);
    equip = updateSore(equip, score.intValue() + alike.getBaseScore());
    equip = EquipUtils.scoreUpdate(equip, 4, genre, alike.getBaseScore());
    refresh(role, equip, pos.intValue(), id, true);
    RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)0, (byte)3, "装备炼化替换" + JsonUtils.object2String(equip), role.getMarking());
    return null;
  }
  
  public BagEquip updateSore(BagEquip equip, int score) {
    try {
      equip.getField().forEach(e -> {
            if (e.getType().equals(Integer.valueOf(6679))) {
              e.setValue(Integer.valueOf(score));
              return;
            } 
          });
    } catch (ConcurrentModificationException e) {
      e.getMessage();
    } 
    return equip;
  }
  
  public List<FiedEquip> field(List<FiedEquip> field) {
    try {
      field.forEach(e -> {
            if (e.getType().equals(Integer.valueOf(6679))) {
              field.remove(e);
              return;
            } 
          });
    } catch (ConcurrentModificationException e) {
      e.getMessage();
    } 
    return field;
  }
}
