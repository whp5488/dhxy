package com.kitty.system.game.fightnew.utils;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.enums.war.warskill.skilldef.TARGETTYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.vo.SkillParameter;
import java.util.IdentityHashMap;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.mina.cache.DataCache;
import java.util.HashMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.ArrayList;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.List;

public class SkillFunc
{
    public static final Integer DefineRelativePetAddPos;
    
    public SkillFunc() {
    }
    
    public static List<RoleSkill> initPetSkill(List<PetSkill> list) {
        Map<Integer, SysPetSkill> data_pet_skill = SkillDataPool.petSkillMap;
        List<RoleSkill> objects = new ArrayList<>();
        for (PetSkill petSkill : list) {
            RoleSkill skill = new RoleSkill();
            skill.setSkillId(Integer.valueOf(petSkill.getSerial()));
            SysPetSkill skill2 = (SysPetSkill)data_pet_skill.get(Integer.valueOf(petSkill.getSerial()));
            if (skill2 != null) {
                skill.setType(skill2.getAttr());
            }
            objects.add(skill);
        }
        return objects;
    }
    
    public static Map<Integer, HorseSkillParam> initHorseSkill(long roleId, long horseId) {
        Map<Integer, HorseSkillParam> horseSkillMap = new HashMap();
        Map<Long, Horse> horseMap = (Map)DataCache.HORSE_MAP.get(Long.valueOf(roleId));
        Horse horse = (Horse)horseMap.get(Long.valueOf(horseId));
        if (horse != null) {
            IdentityHashMap<Integer, Long> skillMap = horse.getSkill();
            if (skillMap != null && skillMap.size() > 0) {
                for (Map.Entry<Integer, Long> entry : skillMap.entrySet()) {
                    Integer key = (Integer)entry.getKey();
                    Long value = (Long)entry.getValue();
                    HorseSkillParam horseSkillParam = new HorseSkillParam();
                    horseSkillParam.setSerial((int)key);
                    horseSkillParam.setProficient((long)value);
                    horseSkillParam.setGrowUp(horse.getGrowUp());
                    horseSkillParam.setLevel(horse.getLevel());
                    horseSkillMap.put(Integer.valueOf(horseSkillParam.getSerial()), horseSkillParam);
                }
            }
        }
        return horseSkillMap;
    }
    
    public static List<RoleSkill> initMonsterSkill(List<SkillParameter> list, Integer proficient, Integer grade) {
        Map<Integer, SysSkill> data_skill = SkillDataPool.skillMap;
        proficient = Integer.valueOf(((int)proficient <= 0) ? 1 : ((int)proficient));
        List<RoleSkill> objects = new ArrayList<>();
        Integer serial = Integer.valueOf(0);
        for (SkillParameter skillParameter : list) {
            serial = Integer.valueOf(skillParameter.getSerial());
            RoleSkill skill = new RoleSkill();
            skill.setSkillId(serial);
            skill.setProficient(proficient);
            SysSkill sysSkill = (SysSkill)data_skill.get(serial);
            if (sysSkill != null) {
                skill.setType(sysSkill.getAttr());
            }
            objects.add(skill);
        }
        return objects;
    }
    
    public static Integer getPetPosByMasterPos(Integer pos) {
        return Integer.valueOf((int)pos + (int)SkillFunc.DefineRelativePetAddPos);
    }
    
    public static Integer getSkillTargeNum(Integer skillId, Integer skillExp, Integer roleType) {
        Integer num = Integer.valueOf(1);
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skillId);
        return num;
    }
    
    public static Integer getPetSkillSubType(Integer skillID) {
        Integer subType = Integer.valueOf((int)skillID % 10000 / 1000);
        return subType;
    }
    
    public static List<Integer> getSkillTargetType(Integer skillId, int logic) {
        if (logic == LOGIC.LOGICTYPE_HERO.getValue()) {
            SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skillId);
            return sysSkill.getTargetType();
        }
        if (logic == LOGIC.LOGICTYPE_PET.getValue()) {
            SysPetSkill skill2 = (SysPetSkill)SkillDataPool.petSkillMap.get(skillId);
            if (skill2 != null) {
                return skill2.getTargetType();
            }
        }
        else {
            SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skillId);
            if (sysSkill != null) {
                return sysSkill.getTargetType();
            }
            SysPetSkill skill = (SysPetSkill)SkillDataPool.petSkillMap.get(skillId);
            if (skill != null) {
                return skill.getTargetType();
            }
        }
        return new ArrayList<>();
    }
    
    public static boolean isAssistSkill(Integer skillId, int logic) {
        List<Integer> list = getSkillTargetType(skillId, logic);
        return !list.contains(Integer.valueOf(TARGETTYPE.ALL_ENEMY.getValue())) && !list.contains(Integer.valueOf(TARGETTYPE.ALL_ENEMY_SUMMON.getValue())) && !list.contains(Integer.valueOf(TARGETTYPE.ALL_ENEMY_PARTNER.getValue())) && !list.contains(Integer.valueOf(TARGETTYPE.ALL_ENEMY_USER.getValue()));
    }
    
    public static double getWuXingKeZhiXiuZheng(FightObject attacker, FightObject target, int typeId) {
        double kz = 1.0;
        double qk = 1.0;
        if (FIGHT_TYPE.MONSTER.getValue() == typeId) {
            Map<String, Double> monsterCoeff = DataGlobalFunc.data_getMonsterKeZhiXiuZhengCoeff(target.isMonster());
            kz = (double)monsterCoeff.get("kzMonsterCoeff");
            qk = (double)monsterCoeff.get("qkzMonsterCoeff");
        }
        double wxjin = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_J.getDist());
        double wxmu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_M.getDist());
        double wxshui = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_S.getDist());
        double wxhuo = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_H.getDist());
        double wxtu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_T.getDist());
        double kewxjin = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KJ.getDist()) * 100.0;
        double kewxmu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KM.getDist()) * 100.0;
        double kewxshui = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KS.getDist()) * 100.0;
        double kewxhuo = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KH.getDist()) * 100.0;
        double kewxtu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KT.getDist()) * 100.0;
        double wxjin_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_J.getDist());
        double wxmu_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_M.getDist());
        double wxshui_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_S.getDist());
        double wxhuo_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_H.getDist());
        double wxtu_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_T.getDist());
        double coeff = ((wxjin * wxmu_e + wxmu * wxtu_e + wxshui * wxhuo_e + wxhuo * wxjin_e + wxtu * wxmu_e) * 4.0E-5 * kz - (wxjin_e * wxmu + wxmu_e * wxtu + wxshui_e * wxhuo + wxhuo_e * wxjin + wxshui_e * wxmu) * 4.0E-5 + 1.0) * ((kewxjin * wxjin_e + kewxmu * wxmu_e + kewxshui * wxshui_e + kewxhuo * wxhuo_e + kewxtu * wxtu_e) * 1.0E-4 * qk + 1.0);
        coeff = BigDecimalUtil.decimalScaleDouble(Integer.valueOf(3), Double.valueOf(coeff));
        return (coeff == 0.0) ? 1.0 : coeff;
    }
    
    public static void main(String[] args) {
        double kz = 1.0;
        double qk = 1.0;
        double wxjin = 0.0;
        double wxmu = 0.0;
        double wxshui = 0.0;
        double wxhuo = 0.0;
        double wxtu = 0.0;
        double kewxjin = 0.0;
        double kewxmu = 0.0;
        double kewxshui = 0.0;
        double kewxhuo = 0.0;
        double kewxtu = 0.0;
        double wxjin_e = 0.0;
        double wxmu_e = 0.0;
        double wxshui_e = 0.0;
        double wxhuo_e = 0.0;
        double wxtu_e = 0.0;
        double wx = ((wxjin * wxmu_e + wxmu * wxtu_e + wxshui * wxhuo_e + wxhuo * wxjin_e + wxtu * wxmu_e) * 4.0E-5 * kz - (wxjin_e * wxmu + wxmu_e * wxtu + wxshui_e * wxhuo + wxhuo_e * wxjin + wxtu_e * wxmu) * 4.0E-5 + 1.0) * ((kewxjin * wxjin_e + kewxmu * wxmu_e + kewxshui * wxshui_e + kewxhuo * wxhuo_e + kewxtu * wxtu_e) * 1.0E-4 * qk + 1.0);
        double harm = 27980.0;
        double kx = 3.7;
        double fk = 0.0;
        double extra = 0.0;
        double js = 0.0;
        double dk = 0.0;
        double weak = js / 1000.0 - dk / 1000.0;
        harm *= 1.0 + fk / 1000.0 * 3.0 - kx / 1000.0 * 2.0 + extra / 1000.0 * 0.28 + weak * 10.0;
        harm *= wx;
        harm = Math.max(harm, 1.0);
        System.out.println("最终伤害 = " + harm);
    }
    
    static {
        DefineRelativePetAddPos = Integer.valueOf(100);
    }
}
