
package com.kitty.system.game.fightnew.book.acsrypet.utils;

import java.util.ArrayList;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.Map;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.acsrypet.pojo.AcsryPetSkillParam;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;

public class AcsryPetSkillUtil
{
    public static double getPval(final int skillId, final FightObject fightObject) {
        try {
            if (fightObject != null && fightObject.isPet()) {
                double pval = 0.0;
                final PetFightObject petFightObject = (PetFightObject)fightObject;
                final AcsryPetSkillParam acsryPetSkillParam = petFightObject.getAcsryPetSkillMap().get(skillId);
                if (acsryPetSkillParam != null) {
                    pval = acsryPetSkillParam.getPval();
                }
                return pval;
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    public static boolean isTrigger(final int skillId, final FightObject fightObject) {
        try {
            if (fightObject != null && fightObject.isPet()) {
                final PetFightObject petFightObject = (PetFightObject)fightObject;
                final AcsryPetSkillParam acsryPetSkillParam = petFightObject.getAcsryPetSkillMap().get(skillId);
                if (acsryPetSkillParam != null) {
                    final double pval = acsryPetSkillParam.getPval();
                    if (pval > 0.0) {
                        return SkillAi.canAffectOnPro(pval);
                    }
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void addPetAcsry(final Role role, final Pet pet, final NutMap propertyMap, final Map<Integer, AcsryPetSkillParam> acsryPetSkillMap) {
        final List<PetEquip> petEffective = SpringUtils.getPetService().getPetEffective(role.getId(), pet);
        if (petEffective != null && petEffective.size() > 0) {
            final Map<Integer, Integer> map = PRO_DICT_TYPE.toMap();
            for (final PetEquip petEquip : petEffective) {
                final List<FiedEquip> fields = petEquip.getField();
                if (fields != null && fields.size() > 0) {
                    for (final FiedEquip fiedEquip : fields) {
                        if (fiedEquip.getType() != null && fiedEquip.getType() > 0) {
                            final int attrSerial = fiedEquip.getType() % PRO_DICT_SERIAL.PET_PS.getSerial();
                            if (attrSerial > 0 && map.get(attrSerial) != null) {
                                addAttrValue(fiedEquip, propertyMap, attrSerial);
                            }
                            if (petEquip.getProductOrder() < ACC_GRADE.PRICELESS.getValue()) {
                                continue;
                            }
                            addAcsryPetSkill(fiedEquip, acsryPetSkillMap);
                        }
                    }
                }
            }
        }
    }
    
    private static void addAttrValue(final FiedEquip fiedEquip, final NutMap propertyMap, final int attrSerial) {
        if (attrSerial > 0) {
            if (isBasicAttr(attrSerial)) {
                return;
            }
            final double value = propertyMap.getDouble(String.valueOf(attrSerial), 0.0) + fiedEquip.getValue() / 1000.0;
            propertyMap.setv(String.valueOf(attrSerial), (Object)value);
        }
    }
    
    private static boolean isBasicAttr(final int attrSerial) {
        boolean flag = false;
        if (attrSerial == PRO_DICT_TYPE.HP_CAP.getDist() || attrSerial == PRO_DICT_TYPE.MP_CAP.getDist() || attrSerial == PRO_DICT_TYPE.ATTACK.getDist() || attrSerial == PRO_DICT_TYPE.SPEED.getDist() || attrSerial == PRO_DICT_TYPE.HP_PCT.getDist() || attrSerial == PRO_DICT_TYPE.HP_PCT.getDist() || attrSerial == PRO_DICT_TYPE.ATTACK_PCT.getDist() || attrSerial == PRO_DICT_TYPE.SPEED_PCT.getDist()) {
            flag = true;
        }
        return flag;
    }
    
    private static void addAcsryPetSkill(final FiedEquip fiedEquip, final Map<Integer, AcsryPetSkillParam> acsryPetSkillMap) {
        final SysPetAcsryAdvAttrLib sysPetAcsryAdvAttrLib = EquipDataPool.petAcsryAdvAttrLibMap.get(fiedEquip.getType());
        if (sysPetAcsryAdvAttrLib != null) {
            for (final Integer skillSerial : sysPetAcsryAdvAttrLib.getSkillList()) {
                final AcsryPetSkillParam acsryPetSkillParam = new AcsryPetSkillParam(skillSerial, fiedEquip.getValue() / 1000.0);
                if (82002 == skillSerial || 82015 == skillSerial || 81004 == skillSerial) {
                    acsryPetSkillParam.setPval(1.0);
                }
                acsryPetSkillMap.put(skillSerial, acsryPetSkillParam);
            }
        }
    }
    
    public static int getAttrSpeed(final Pet pet) {
        final BagService bagService = SpringUtils.getBean(BagService.class);
        final Role role = SpringUtils.getRoleService().getPlayer(pet.getRoleId());
        if (role == null) {
            return 0;
        }
        final Map<Long, PetEquip> petBagEquip = bagService.getPetBagEquip(role.getId());
        if (petBagEquip != null && petBagEquip.size() > 0) {
            int returnData = 0;
            for (final Map.Entry<Long, PetEquip> entry : petBagEquip.entrySet()) {
                final PetEquip petEquip = entry.getValue();
                if (petEquip.getPId() == pet.getId()) {
                    for (final FiedEquip fiedEquip : petEquip.getField()) {
                        if (fiedEquip.getType() != null) {
                            final int attrSerial = fiedEquip.getType() % PRO_DICT_SERIAL.PET_PS.getSerial();
                            if (attrSerial <= 0 || attrSerial != PRO_DICT_TYPE.SPEED.getDist()) {
                                continue;
                            }
                            returnData += fiedEquip.getValue();
                        }
                    }
                }
            }
            final NutMap nutMap = AwakenSkillUtils.additionSuit(role, pet);
            if (nutMap != null) {
                final int suitSpeed = nutMap.getInt(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()));
                if (suitSpeed > 0) {
                    returnData += suitSpeed;
                }
                return returnData;
            }
        }
        return 0;
    }
    
    public static double getAttrSpeedPct(final Pet pet) {
        final BagService bagService = SpringUtils.getBean(BagService.class);
        final Role role = SpringUtils.getRoleService().getPlayer(pet.getRoleId());
        if (role == null) {
            return 0.0;
        }
        final Map<Long, PetEquip> petBagEquip = bagService.getPetBagEquip(role.getId());
        if (petBagEquip != null && petBagEquip.size() > 0) {
            double returnData = 0.0;
            for (final Map.Entry<Long, PetEquip> entry : petBagEquip.entrySet()) {
                final PetEquip petEquip = entry.getValue();
                if (petEquip.getPId() == pet.getId()) {
                    for (final FiedEquip fiedEquip : petEquip.getField()) {
                        if (fiedEquip.getType() != null) {
                            final int attrSerial = fiedEquip.getType() % PRO_DICT_SERIAL.PET_PS.getSerial();
                            if (attrSerial <= 0 || attrSerial != PRO_DICT_TYPE.SPEED_PCT.getDist()) {
                                continue;
                            }
                            returnData += fiedEquip.getValue() / 1000.0;
                        }
                    }
                }
            }
            return returnData;
        }
        return 0.0;
    }
    
    public static List<PetEquip> getWearAcsrys(final Role role, final Pet pet) {
        final BagService bagService = SpringUtils.getBean(BagService.class);
        final Map<Long, PetEquip> petBagEquip = bagService.getPetBagEquip(role.getId());
        final List<PetEquip> returnList = new ArrayList<PetEquip>();
        if (petBagEquip != null && petBagEquip.size() > 0) {
            for (final Map.Entry<Long, PetEquip> entry : petBagEquip.entrySet()) {
                final PetEquip petEquip = entry.getValue();
                if (petEquip.getPId() == pet.getId()) {
                    returnList.add(petEquip);
                }
            }
        }
        return returnList;
    }
    
    public static byte getSuitLev(final Role role, final Pet pet) {
        final List<PetEquip> wearAcsrys = getWearAcsrys(role, pet);
        if (wearAcsrys.size() >= 3) {
            byte lev = ACC_GRADE.TRENDS.getValue();
            for (final PetEquip petEquip : wearAcsrys) {
                if (lev < petEquip.getProductOrder()) {
                    lev = petEquip.getProductOrder();
                }
            }
            return lev;
        }
        return 0;
    }
    
    public static byte getSuitLevParam(final Role role, final Pet pet) {
        final byte suitLev = getSuitLev(role, pet);
        if (suitLev > 0) {
            byte levParam = 0;
            if (suitLev == ACC_GRADE.PLAY_WITH.getValue()) {
                levParam = 4;
            }
            else if (suitLev == ACC_GRADE.RESERVE.getValue()) {
                levParam = 8;
            }
            else if (suitLev == ACC_GRADE.PRICELESS.getValue()) {
                levParam = 14;
            }
            else if (suitLev == ACC_GRADE.TRENDS.getValue()) {
                levParam = 14;
            }
            return levParam;
        }
        return 0;
    }
}
