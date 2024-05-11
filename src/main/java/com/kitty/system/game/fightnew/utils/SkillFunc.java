
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
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.horse.domain.Horse;
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
    
    public static List<RoleSkill> initPetSkill(final List<PetSkill> list) {
        final Map<Integer, SysPetSkill> data_pet_skill = SkillDataPool.petSkillMap;
        final List<RoleSkill> objects = new ArrayList<RoleSkill>();
        for (final PetSkill petSkill : list) {
            final RoleSkill skill = new RoleSkill();
            skill.setSkillId(petSkill.getSerial());
            final SysPetSkill skill2 = data_pet_skill.get(petSkill.getSerial());
            if (skill2 != null) {
                skill.setType(skill2.getAttr());
            }
            objects.add(skill);
        }
        return objects;
    }
    
    public static Map<Integer, HorseSkillParam> initHorseSkill(final long roleId, final long horseId) {
        final Map<Integer, HorseSkillParam> horseSkillMap = new HashMap<Integer, HorseSkillParam>();
        final Map<Long, Horse> horseMap = DataCache.HORSE_MAP.get(roleId);
        final Horse horse = horseMap.get(horseId);
        if (horse != null) {
            final IdentityHashMap<Integer, Long> skillMap = horse.getSkill();
            if (skillMap != null && skillMap.size() > 0) {
                for (final Map.Entry<Integer, Long> entry : skillMap.entrySet()) {
                    final Integer key = entry.getKey();
                    final Long value = entry.getValue();
                    final HorseSkillParam horseSkillParam = new HorseSkillParam();
                    horseSkillParam.setSerial(key);
                    horseSkillParam.setProficient(value);
                    horseSkillParam.setGrowUp(horse.getGrowUp());
                    horseSkillParam.setLevel(horse.getLevel());
                    horseSkillMap.put(horseSkillParam.getSerial(), horseSkillParam);
                }
            }
        }
        return horseSkillMap;
    }
    
    public static List<RoleSkill> initMonsterSkill(final List<SkillParameter> list, Integer proficient, final Integer grade) {
        final Map<Integer, SysSkill> data_skill = SkillDataPool.skillMap;
        proficient = ((proficient <= 0) ? 1 : proficient);
        final List<RoleSkill> objects = new ArrayList<RoleSkill>();
        Integer serial = 0;
        for (final SkillParameter skillParameter : list) {
            serial = skillParameter.getSerial();
            final RoleSkill skill = new RoleSkill();
            skill.setSkillId(serial);
            skill.setProficient(proficient);
            final SysSkill sysSkill = data_skill.get(serial);
            if (sysSkill != null) {
                skill.setType(sysSkill.getAttr());
            }
            objects.add(skill);
        }
        return objects;
    }
    
    public static Integer getPetPosByMasterPos(final Integer pos) {
        return pos + SkillFunc.DefineRelativePetAddPos;
    }
    
    public static Integer getSkillTargeNum(final Integer skillId, final Integer skillExp, final Integer roleType) {
        final Integer num = 1;
        final SysSkill sysSkill = SkillDataPool.skillMap.get(skillId);
        if (sysSkill != null) {}
        return num;
    }
    
    public static Integer getPetSkillSubType(final Integer skillID) {
        final Integer subType = skillID % 10000 / 1000;
        return subType;
    }
    
    public static List<Integer> getSkillTargetType(final Integer skillId, final int logic) {
        if (logic == LOGIC.LOGICTYPE_HERO.getValue()) {
            final SysSkill sysSkill = SkillDataPool.skillMap.get(skillId);
            return sysSkill.getTargetType();
        }
        if (logic == LOGIC.LOGICTYPE_PET.getValue()) {
            final SysPetSkill skill = SkillDataPool.petSkillMap.get(skillId);
            if (skill != null) {
                return skill.getTargetType();
            }
        }
        else {
            final SysSkill sysSkill = SkillDataPool.skillMap.get(skillId);
            if (sysSkill != null) {
                return sysSkill.getTargetType();
            }
            final SysPetSkill skill2 = SkillDataPool.petSkillMap.get(skillId);
            if (skill2 != null) {
                return skill2.getTargetType();
            }
        }
        return new ArrayList<Integer>();
    }
    
    public static boolean isAssistSkill(final Integer skillId, final int logic) {
        final List<Integer> list = getSkillTargetType(skillId, logic);
        return !list.contains(TARGETTYPE.ALL_ENEMY.getValue()) && !list.contains(TARGETTYPE.ALL_ENEMY_SUMMON.getValue()) && !list.contains(TARGETTYPE.ALL_ENEMY_PARTNER.getValue()) && !list.contains(TARGETTYPE.ALL_ENEMY_USER.getValue());
    }
    
    public static double getWuXingKeZhiXiuZheng(final FightObject attacker, final FightObject target, final int typeId) {
        double kz = 1.0;
        double qk = 1.0;
        if (FIGHT_TYPE.MONSTER.getValue() == typeId) {
            final Map<String, Double> monsterCoeff = DataGlobalFunc.data_getMonsterKeZhiXiuZhengCoeff(target.isMonster());
            kz = monsterCoeff.get("kzMonsterCoeff");
            qk = monsterCoeff.get("qkzMonsterCoeff");
        }
        final double wxjin = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_J.getDist());
        final double wxmu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_M.getDist());
        final double wxshui = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_S.getDist());
        final double wxhuo = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_H.getDist());
        final double wxtu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.WX_T.getDist());
        final double kewxjin = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KJ.getDist()) * 100.0;
        final double kewxmu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KM.getDist()) * 100.0;
        final double kewxshui = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KS.getDist()) * 100.0;
        final double kewxhuo = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KH.getDist()) * 100.0;
        final double kewxtu = attacker.getSelfAllTempPro(PRO_DICT_TYPE.Q_KT.getDist()) * 100.0;
        final double wxjin_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_J.getDist());
        final double wxmu_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_M.getDist());
        final double wxshui_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_S.getDist());
        final double wxhuo_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_H.getDist());
        final double wxtu_e = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_T.getDist());
        double coeff = ((wxjin * wxmu_e + wxmu * wxtu_e + wxshui * wxhuo_e + wxhuo * wxjin_e + wxtu * wxmu_e) * 4.0E-5 * kz - (wxjin_e * wxmu + wxmu_e * wxtu + wxshui_e * wxhuo + wxhuo_e * wxjin + wxtu_e * wxmu) * 4.0E-5 + 1.0) * ((kewxjin * wxjin_e + kewxmu * wxmu_e + kewxshui * wxshui_e + kewxhuo * wxhuo_e + kewxtu * wxtu_e) * 1.0E-4 * qk + 1.0);
        coeff = BigDecimalUtil.decimalScaleDouble(3, coeff);
        return (coeff == 0.0) ? 1.0 : coeff;
    }
    
    public static void main(final String[] args) {
        final double kz = 1.0;
        final double qk = 1.0;
        final double wxjin = 0.0;
        final double wxmu = 0.0;
        final double wxshui = 0.0;
        final double wxhuo = 0.0;
        final double wxtu = 0.0;
        final double kewxjin = 0.0;
        final double kewxmu = 0.0;
        final double kewxshui = 0.0;
        final double kewxhuo = 0.0;
        final double kewxtu = 0.0;
        final double wxjin_e = 0.0;
        final double wxmu_e = 0.0;
        final double wxshui_e = 0.0;
        final double wxhuo_e = 0.0;
        final double wxtu_e = 0.0;
        final double wx = ((wxjin * wxmu_e + wxmu * wxtu_e + wxshui * wxhuo_e + wxhuo * wxjin_e + wxtu * wxmu_e) * 4.0E-5 * kz - (wxjin_e * wxmu + wxmu_e * wxtu + wxshui_e * wxhuo + wxhuo_e * wxjin + wxtu_e * wxmu) * 4.0E-5 + 1.0) * ((kewxjin * wxjin_e + kewxmu * wxmu_e + kewxshui * wxshui_e + kewxhuo * wxhuo_e + kewxtu * wxtu_e) * 1.0E-4 * qk + 1.0);
        double harm = 27980.0;
        final double kx = 3.7;
        final double fk = 0.0;
        final double extra = 0.0;
        final double js = 0.0;
        final double dk = 0.0;
        final double weak = js / 1000.0 - dk / 1000.0;
        harm *= 1.0 + fk / 1000.0 * 3.0 - kx / 1000.0 * 2.0 + extra / 1000.0 * 0.28 + weak * 10.0;
        harm *= wx;
        harm = Math.max(harm, 1.0);
        System.out.println("最终伤害 = " + harm);
    }
    
    static {
        DefineRelativePetAddPos = 100;
    }
}
