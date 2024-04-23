package com.kitty.system.game.fightnew.book.acsrypet.utils;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.acsrypet.pojo.AcsryPetSkillParam;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.nutz.lang.util.NutMap;

public class AcsryPetSkillUtil {
  public static double getPval(int skillId, FightObject fightObject) {
    try {
      if (fightObject != null && fightObject.isPet()) {
        double pval = 0.0D;
        PetFightObject petFightObject = (PetFightObject)fightObject;
        AcsryPetSkillParam acsryPetSkillParam = (AcsryPetSkillParam)petFightObject.getAcsryPetSkillMap().get(Integer.valueOf(skillId));
        if (acsryPetSkillParam != null)
          pval = acsryPetSkillParam.getPval(); 
        return pval;
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return 0.0D;
  }
  
  public static boolean isTrigger(int skillId, FightObject fightObject) {
    try {
      if (fightObject != null && fightObject.isPet()) {
        PetFightObject petFightObject = (PetFightObject)fightObject;
        AcsryPetSkillParam acsryPetSkillParam = (AcsryPetSkillParam)petFightObject.getAcsryPetSkillMap().get(Integer.valueOf(skillId));
        if (acsryPetSkillParam != null) {
          double pval = acsryPetSkillParam.getPval();
          if (pval > 0.0D)
            return SkillAi.canAffectOnPro(pval); 
        } 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return false;
  }
  
  public static void addPetAcsry(Role role, Pet pet, NutMap propertyMap, Map<Integer, AcsryPetSkillParam> acsryPetSkillMap) {
    List<PetEquip> petEffective = SpringUtils.getPetService().getPetEffective(role.getId().longValue(), pet);
    if (petEffective != null && petEffective.size() > 0) {
      Map<Integer, Integer> map = PRO_DICT_TYPE.toMap();
      for (PetEquip petEquip : petEffective) {
        List<FiedEquip> fields = petEquip.getField();
        if (fields != null && fields.size() > 0)
          for (FiedEquip fiedEquip : fields) {
            if (fiedEquip.getType() != null && fiedEquip.getType().intValue() > 0) {
              int attrSerial = fiedEquip.getType().intValue() % PRO_DICT_SERIAL.PET_PS.getSerial();
              if (attrSerial > 0 && map.get(Integer.valueOf(attrSerial)) != null)
                addAttrValue(fiedEquip, propertyMap, attrSerial); 
              if (petEquip.getProductOrder() >= ACC_GRADE.PRICELESS.getValue())
                addAcsryPetSkill(fiedEquip, acsryPetSkillMap); 
            } 
          }  
      } 
    } 
  }
  
  private static void addAttrValue(FiedEquip fiedEquip, NutMap propertyMap, int attrSerial) {
    if (attrSerial > 0) {
      if (isBasicAttr(attrSerial))
        return; 
      double value = propertyMap.getDouble(String.valueOf(attrSerial), 0.0D) + fiedEquip.getValue().intValue() / 1000.0D;
      propertyMap.setv(String.valueOf(attrSerial), Double.valueOf(value));
    } 
  }
  
  private static boolean isBasicAttr(int attrSerial) {
    boolean flag = false;
    if (attrSerial == PRO_DICT_TYPE.HP_CAP.getDist() || attrSerial == PRO_DICT_TYPE.MP_CAP.getDist() || attrSerial == PRO_DICT_TYPE.ATTACK.getDist() || attrSerial == PRO_DICT_TYPE.SPEED.getDist() || attrSerial == PRO_DICT_TYPE.HP_PCT
      .getDist() || attrSerial == PRO_DICT_TYPE.HP_PCT.getDist() || attrSerial == PRO_DICT_TYPE.ATTACK_PCT.getDist() || attrSerial == PRO_DICT_TYPE.SPEED_PCT.getDist())
      flag = true; 
    return flag;
  }
  
  private static void addAcsryPetSkill(FiedEquip fiedEquip, Map<Integer, AcsryPetSkillParam> acsryPetSkillMap) {
    SysPetAcsryAdvAttrLib sysPetAcsryAdvAttrLib = (SysPetAcsryAdvAttrLib)EquipDataPool.petAcsryAdvAttrLibMap.get(fiedEquip.getType());
    if (sysPetAcsryAdvAttrLib != null)
      for (Integer skillSerial : sysPetAcsryAdvAttrLib.getSkillList()) {
        AcsryPetSkillParam acsryPetSkillParam = new AcsryPetSkillParam(skillSerial.intValue(), fiedEquip.getValue().intValue() / 1000.0D);
        if (82002 == skillSerial.intValue() || 82015 == skillSerial.intValue() || 81004 == skillSerial.intValue())
          acsryPetSkillParam.setPval(1.0D); 
        acsryPetSkillMap.put(skillSerial, acsryPetSkillParam);
      }  
  }
  
  public static int getAttrSpeed(Pet pet) {
    BagService bagService = (BagService)SpringUtils.getBean(BagService.class);
    Role role = SpringUtils.getRoleService().getPlayer(pet.getRoleId().intValue());
    if (role == null)
      return 0; 
    Map<Long, PetEquip> petBagEquip = bagService.getPetBagEquip(role.getId().longValue());
    if (petBagEquip != null && petBagEquip.size() > 0) {
      int returnData = 0;
      for (Map.Entry<Long, PetEquip> entry : petBagEquip.entrySet()) {
        PetEquip petEquip = entry.getValue();
        if (petEquip.getPId() == pet.getId().longValue())
          for (FiedEquip fiedEquip : petEquip.getField()) {
            if (fiedEquip.getType() != null) {
              int attrSerial = fiedEquip.getType().intValue() % PRO_DICT_SERIAL.PET_PS.getSerial();
              if (attrSerial > 0)
                if (attrSerial == PRO_DICT_TYPE.SPEED.getDist())
                  returnData += fiedEquip.getValue().intValue();  
            } 
          }  
      } 
      NutMap nutMap = AwakenSkillUtils.additionSuit(role, pet);
      if (nutMap != null) {
        int suitSpeed = nutMap.getInt(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()));
        if (suitSpeed > 0)
          returnData += suitSpeed; 
        return returnData;
      } 
    } 
    return 0;
  }
  
  public static double getAttrSpeedPct(Pet pet) {
    BagService bagService = (BagService)SpringUtils.getBean(BagService.class);
    Role role = SpringUtils.getRoleService().getPlayer(pet.getRoleId().intValue());
    if (role == null)
      return 0.0D; 
    Map<Long, PetEquip> petBagEquip = bagService.getPetBagEquip(role.getId().longValue());
    if (petBagEquip != null && petBagEquip.size() > 0) {
      double returnData = 0.0D;
      for (Map.Entry<Long, PetEquip> entry : petBagEquip.entrySet()) {
        PetEquip petEquip = entry.getValue();
        if (petEquip.getPId() == pet.getId().longValue())
          for (FiedEquip fiedEquip : petEquip.getField()) {
            if (fiedEquip.getType() != null) {
              int attrSerial = fiedEquip.getType().intValue() % PRO_DICT_SERIAL.PET_PS.getSerial();
              if (attrSerial > 0)
                if (attrSerial == PRO_DICT_TYPE.SPEED_PCT.getDist())
                  returnData += fiedEquip.getValue().intValue() / 1000.0D;  
            } 
          }  
      } 
      return returnData;
    } 
    return 0.0D;
  }
  
  public static List<PetEquip> getWearAcsrys(Role role, Pet pet) {
    BagService bagService = (BagService)SpringUtils.getBean(BagService.class);
    Map<Long, PetEquip> petBagEquip = bagService.getPetBagEquip(role.getId().longValue());
    List<PetEquip> returnList = new ArrayList<>();
    if (petBagEquip != null && petBagEquip.size() > 0)
      for (Map.Entry<Long, PetEquip> entry : petBagEquip.entrySet()) {
        PetEquip petEquip = entry.getValue();
        if (petEquip.getPId() == pet.getId().longValue())
          returnList.add(petEquip); 
      }  
    return returnList;
  }
  
  public static byte getSuitLev(Role role, Pet pet) {
    List<PetEquip> wearAcsrys = getWearAcsrys(role, pet);
    if (wearAcsrys.size() >= 3) {
      byte lev = ACC_GRADE.TRENDS.getValue();
      for (PetEquip petEquip : wearAcsrys) {
        if (lev < petEquip.getProductOrder())
          lev = petEquip.getProductOrder(); 
      } 
      return lev;
    } 
    return 0;
  }
  
  public static byte getSuitLevParam(Role role, Pet pet) {
    byte suitLev = getSuitLev(role, pet);
    if (suitLev > 0) {
      byte levParam = 0;
      if (suitLev == ACC_GRADE.PLAY_WITH.getValue()) {
        levParam = 4;
      } else if (suitLev == ACC_GRADE.RESERVE.getValue()) {
        levParam = 8;
      } else if (suitLev == ACC_GRADE.PRICELESS.getValue()) {
        levParam = 14;
      } else if (suitLev == ACC_GRADE.TRENDS.getValue()) {
        levParam = 14;
      } 
      return levParam;
    } 
    return 0;
  }
}
