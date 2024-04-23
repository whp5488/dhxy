package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.NumberUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.message.RespPetEquipDisplace;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("PetEquipDisplace")
public class PetEquipDisplace extends UpgradeHandler {
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip(role.getId().longValue());
    if (petEquipMap == null || petEquipMap.size() <= 0)
      return null; 
    String[] split = param.split("-");
    if (split.length < 1)
      return null; 
    if (!NumberUtil.isNumber(split[0]) || !NumberUtil.isNumber(split[1]))
      return null; 
    long main = Long.valueOf(split[0]).longValue();
    long second = Long.valueOf(split[1]).longValue();
    PetEquip equipMain = petEquipMap.get(Long.valueOf(main));
    PetEquip equipSecond = petEquipMap.get(Long.valueOf(second));
    if (equipMain == null || equipSecond == null)
      return null; 
    List<FiedEquip> fieldEquipMain = equipMain.getField();
    List<FiedEquip> fieldEquipSecond = equipSecond.getField();
    List<FiedEquip> equipMainDisplace = displace(fieldEquipMain, advAttrLib(fieldEquipSecond));
    List<FiedEquip> equipSecondDisplace = displace(fieldEquipMain, advAttrLib(fieldEquipMain));
    equipMain.setField(equipMainDisplace);
    equipSecond.setField(equipSecondDisplace);
    equipMain.setField(MakeAttribute.calEquipScore(equipMain));
    equipSecond.setField(MakeAttribute.calEquipScore(equipSecond));
    equipMain.save();
    equipSecond.save();
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespPetEquipDisplace(split[0], split[1]));
    this.equipService.refreshPetEquip(role, equipMain);
    this.equipService.refreshPetEquip(role, equipSecond);
    return null;
  }
  
  public List<FiedEquip> displace(List<FiedEquip> field, FiedEquip fiedEquip) {
    Map<Integer, SysPetAcsryAdvAttrLib> map = EquipDataPool.petAcsryAdvAttrLibMap;
    List<FiedEquip> fieldEquipList = new ArrayList<>();
    for (FiedEquip obj : field) {
      if (map.get(obj.getType()) == null && !obj.getType().equals(Integer.valueOf(6672)))
        fieldEquipList.add(obj); 
    } 
    if (fiedEquip != null)
      fieldEquipList.add(fiedEquip); 
    return fieldEquipList;
  }
  
  public FiedEquip advAttrLib(List<FiedEquip> field) {
    FiedEquip fiedEquip = null;
    for (FiedEquip obj : field) {
      if (EquipDataPool.petAcsryAdvAttrLibMap.get(obj.getType()) != null) {
        fiedEquip = obj;
        break;
      } 
    } 
    return fiedEquip;
  }
}
