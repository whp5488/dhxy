package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.NumberUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.equip.helper.vo.PetEquipNeo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.message.RespPetPreUpgradeEquip;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.utils.Const;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("PetEquipCompose")
public class PetEquipComposeHandler extends UpgradeHandler {
  public static final List<Integer> integers = Arrays.asList(new Integer[] { Integer.valueOf(92), Integer.valueOf(7), Integer.valueOf(1) });
  
  public static final String NOT_STUFF = "材料不足";
  
  public String acceptUpgrade(Role role, Integer position, Integer type, String param, String cause) {
    int residue = this.bagService.residuePetEquipBagPos(role);
    if (residue < expend(type.intValue()))
      return "物品栏空间不足"; 
    if (!NumberUtil.isNumber(param))
      return null; 
    int grade = Integer.valueOf(param).intValue();
    if (grade < 1 || grade > 3)
      return null; 
    if (!checkGrade(role.getRelive().intValue() * 1000 + role.getLevel().intValue(), grade))
      return null; 
    int expend = expend(type.intValue());
    Map<Integer, Integer> consume = consume(grade, position.intValue(), expend);
    for (Integer key : consume.keySet()) {
      Integer amount = consume.get(key);
      if (!key.equals(Const.RESTYPE_SILVER)) {
        Integer num = this.bagService.getBagGoods(role, key.intValue(), amount.intValue());
        if (num != null)
          return "材料不足"; 
        continue;
      } 
      if (role.getSilver().longValue() < amount.intValue())
        return "材料不足"; 
    } 
    boolean deduct = deduct(role, consume);
    if (!deduct)
      return "道具或者货币扣除失败"; 
    List<PetEquipNeo> objects = new ArrayList<>(expend);
    byte productOrder = (position.intValue() <= 0) ? ACC_GRADE.PLAY_WITH.getValue() : ACC_GRADE.PRICELESS.getValue();
    for (int i = 0; i < expend; i++) {
      PetEquip petBaseBieEq = MakeAttribute.createPetBaseBieEq(grade, productOrder, integers, 0);
      petBaseBieEq.setId(Long.valueOf(this.worker.nextId()));
      petBaseBieEq.setRId(role.getId().longValue());
      this.bagService.addPetEquip(role, petBaseBieEq, true, "宠物配饰打造");
      objects.add(new PetEquipNeo(petBaseBieEq.getId().longValue(), petBaseBieEq.getSerial(), petBaseBieEq.getProductOrder(), petBaseBieEq.getField()));
    } 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespPetPreUpgradeEquip(objects, type.byteValue(), position.byteValue(), (byte)grade));
    return null;
  }
  
  public boolean checkGrade(int grade, int type) {
    if (type == 2 && grade < 2120)
      return false; 
    if (type == 3 && grade < 3145)
      return false; 
    return true;
  }
  
  public Map<Integer, Integer> consume(int grade, int position, int num) {
    Map<Integer, Integer> map = new LinkedHashMap<>();
    if (grade == 1) {
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(50000 * num));
    } else if (grade == 2) {
      map.put(Integer.valueOf(75030), Integer.valueOf(1 * num));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(100000 * num));
    } else if (grade == 3) {
      map.put(Integer.valueOf(75030), Integer.valueOf(2 * num));
      map.put(Integer.valueOf(75047), Integer.valueOf(1 * num));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(150000 * num));
    } 
    if (map.size() > 0) {
      int phoenix = (position <= 0) ? 75401 : 75402;
      map.put(Integer.valueOf(phoenix), Integer.valueOf(1 * num));
      map.put(Integer.valueOf(75400), Integer.valueOf(1 * num));
    } 
    return map;
  }
}
