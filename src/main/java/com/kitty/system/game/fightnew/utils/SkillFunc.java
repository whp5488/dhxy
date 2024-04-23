package com.kitty.system.game.fightnew.utils;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.warskill.skilldef.TARGETTYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.vo.SkillParameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class SkillFunc {
  public static final Integer DefineRelativePetAddPos = Integer.valueOf(100);
  
  public static List<RoleSkill> initPetSkill(List<PetSkill> list) {
    Map<Integer, SysPetSkill> data_pet_skill = SkillDataPool.petSkillMap;
    List<RoleSkill> objects = new ArrayList<>();
    for (PetSkill petSkill : list) {
      RoleSkill skill = new RoleSkill();
      skill.setSkillId(Integer.valueOf(petSkill.getSerial()));
      SysPetSkill skill1 = data_pet_skill.get(Integer.valueOf(petSkill.getSerial()));
      if (skill1 != null)
        skill.setType(skill1.getAttr()); 
      objects.add(skill);
    } 
    return objects;
  }
  
  public static Map<Integer, HorseSkillParam> initHorseSkill(long roleId, long horseId) {
    Map<Integer, HorseSkillParam> horseSkillMap = new HashMap<>();
    Map<Long, Horse> horseMap = (Map<Long, Horse>)DataCache.HORSE_MAP.get(Long.valueOf(roleId));
    Horse horse = horseMap.get(Long.valueOf(horseId));
    if (horse != null) {
      IdentityHashMap<Integer, Long> skillMap = horse.getSkill();
      if (skillMap != null && skillMap.size() > 0)
        for (Map.Entry<Integer, Long> entry : skillMap.entrySet()) {
          Integer key = entry.getKey();
          Long value = entry.getValue();
          HorseSkillParam horseSkillParam = new HorseSkillParam();
          horseSkillParam.setSerial(key.intValue());
          horseSkillParam.setProficient(value.longValue());
          horseSkillParam.setGrowUp(horse.getGrowUp());
          horseSkillParam.setLevel(horse.getLevel());
          horseSkillMap.put(Integer.valueOf(horseSkillParam.getSerial()), horseSkillParam);
        }  
    } 
    return horseSkillMap;
  }
  
  public static List<RoleSkill> initMonsterSkill(List<SkillParameter> list, Integer proficient, Integer grade) {
    Map<Integer, SysSkill> data_skill = SkillDataPool.skillMap;
    proficient = Integer.valueOf((proficient.intValue() <= 0) ? 1 : proficient.intValue());
    List<RoleSkill> objects = new ArrayList<>();
    Integer serial = Integer.valueOf(0);
    for (SkillParameter skillParameter : list) {
      serial = Integer.valueOf(skillParameter.getSerial());
      RoleSkill skill = new RoleSkill();
      skill.setSkillId(serial);
      skill.setProficient(proficient);
      SysSkill sysSkill = data_skill.get(serial);
      if (sysSkill != null)
        skill.setType(sysSkill.getAttr()); 
      objects.add(skill);
    } 
    return objects;
  }
  
  public static Integer getPetPosByMasterPos(Integer pos) {
    return Integer.valueOf(pos.intValue() + DefineRelativePetAddPos.intValue());
  }
  
  public static Integer getSkillTargeNum(Integer skillId, Integer skillExp, Integer roleType) {
    Integer num = Integer.valueOf(1);
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skillId);
    if (sysSkill != null);
    return num;
  }
  
  public static Integer getPetSkillSubType(Integer skillID) {
    Integer subType = Integer.valueOf(skillID.intValue() % 10000 / 1000);
    return subType;
  }
  
  public static List<Integer> getSkillTargetType(Integer skillId, int logic) {
    if (logic == LOGIC.LOGICTYPE_HERO.getValue()) {
      SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skillId);
      return sysSkill.getTargetType();
    } 
    if (logic == LOGIC.LOGICTYPE_PET.getValue()) {
      SysPetSkill skill = (SysPetSkill)SkillDataPool.petSkillMap.get(skillId);
      if (skill != null)
        return skill.getTargetType(); 
    } else {
      SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skillId);
      if (sysSkill != null)
        return sysSkill.getTargetType(); 
      SysPetSkill skill = (SysPetSkill)SkillDataPool.petSkillMap.get(skillId);
      if (skill != null)
        return skill.getTargetType(); 
    } 
    return new ArrayList<>();
  }
  
  public static boolean isAssistSkill(Integer skillId, int logic) {
    List<Integer> list = getSkillTargetType(skillId, logic);
    if (!list.contains(Integer.valueOf(TARGETTYPE.ALL_ENEMY.getValue())) && !list.contains(Integer.valueOf(TARGETTYPE.ALL_ENEMY_SUMMON.getValue())) && !list.contains(Integer.valueOf(TARGETTYPE.ALL_ENEMY_PARTNER.getValue())) && !list.contains(Integer.valueOf(TARGETTYPE.ALL_ENEMY_USER.getValue())))
      return true; 
    return false;
  }
  
  public static double getWuXingKeZhiXiuZheng(FightObject attacker, FightObject target, int typeId) {
    double kz = 1.0D;
    double qk = 1.0D;
    if (FIGHT_TYPE.MONSTER.getValue() == typeId) {
      Map<String, Double> monsterCoeff = DataGlobalFunc.data_getMonsterKeZhiXiuZhengCoeff(target.isMonster());
      kz = ((Double)monsterCoeff.get("kzMonsterCoeff")).doubleValue();
      qk = ((Double)monsterCoeff.get("qkzMonsterCoeff")).doubleValue();
    } 
    double wxjin = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_J.getDist());
    double wxmu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_M.getDist());
    double wxshui = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_S.getDist());
    double wxhuo = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_H.getDist());
    double wxtu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_T.getDist());
    double kewxjin = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KJ.getDist()) * 100.0D;
    double kewxmu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KM.getDist()) * 100.0D;
    double kewxshui = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KS.getDist()) * 100.0D;
    double kewxhuo = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KH.getDist()) * 100.0D;
    double kewxtu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KT.getDist()) * 100.0D;
    double wxjin_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_J.getDist());
    double wxmu_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_M.getDist());
    double wxshui_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_S.getDist());
    double wxhuo_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_H.getDist());
    double wxtu_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_T.getDist());
    double coeff = ((wxjin * wxmu_e + wxmu * wxtu_e + wxshui * wxhuo_e + wxhuo * wxjin_e + wxtu * wxmu_e) * 4.0E-5D * kz - (wxjin_e * wxmu + wxmu_e * wxtu + wxshui_e * wxhuo + wxhuo_e * wxjin + wxtu_e * wxmu) * 4.0E-5D + 1.0D) * ((kewxjin * wxjin_e + kewxmu * wxmu_e + kewxshui * wxshui_e + kewxhuo * wxhuo_e + kewxtu * wxtu_e) * 1.0E-4D * qk + 1.0D);
    coeff = BigDecimalUtil.decimalScaleDouble(Integer.valueOf(3), Double.valueOf(coeff));
    return (coeff == 0.0D) ? 1.0D : coeff;
  }
  
  public static void main(String[] args) {
    double kz = 1.0D;
    double qk = 1.0D;
    double wxjin = 0.0D;
    double wxmu = 0.0D;
    double wxshui = 0.0D;
    double wxhuo = 0.0D;
    double wxtu = 0.0D;
    double kewxjin = 0.0D;
    double kewxmu = 0.0D;
    double kewxshui = 0.0D;
    double kewxhuo = 0.0D;
    double kewxtu = 0.0D;
    double wxjin_e = 0.0D;
    double wxmu_e = 0.0D;
    double wxshui_e = 0.0D;
    double wxhuo_e = 0.0D;
    double wxtu_e = 0.0D;
    double wx = ((wxjin * wxmu_e + wxmu * wxtu_e + wxshui * wxhuo_e + wxhuo * wxjin_e + wxtu * wxmu_e) * 4.0E-5D * kz - (wxjin_e * wxmu + wxmu_e * wxtu + wxshui_e * wxhuo + wxhuo_e * wxjin + wxtu_e * wxmu) * 4.0E-5D + 1.0D) * ((kewxjin * wxjin_e + kewxmu * wxmu_e + kewxshui * wxshui_e + kewxhuo * wxhuo_e + kewxtu * wxtu_e) * 1.0E-4D * qk + 1.0D);
    double harm = 27980.0D;
    double kx = 3.7D;
    double fk = 0.0D;
    double extra = 0.0D;
    double js = 0.0D;
    double dk = 0.0D;
    double weak = js / 1000.0D - dk / 1000.0D;
    harm *= 1.0D + fk / 1000.0D * 3.0D - kx / 1000.0D * 2.0D + extra / 1000.0D * 0.28D + weak * 10.0D;
    harm *= wx;
    harm = Math.max(harm, 1.0D);
    System.out.println("最终伤害 = " + harm);
  }
}
