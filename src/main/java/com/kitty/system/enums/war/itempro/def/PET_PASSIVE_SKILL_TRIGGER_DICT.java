
package com.kitty.system.enums.war.itempro.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.Lists;
import java.util.List;

public enum PET_PASSIVE_SKILL_TRIGGER_DICT
{
    ROUND_START("回合开始时", "roundStart"), 
    ROUND_START_ACTION("回合开始时,需要预告", "roundStartAction"), 
    ENTERING_SIDE("进场时", "enteringSide"), 
    ENEMY_ENTERING_SIDE("敌方进场时", "enemyEnteringSide"), 
    ENTERING_SIDE_ACTION("进场时,需要预告", "enteringSideAction"), 
    ENTERING_SIDE_NF("进场时（排除第一回合）", "enteringSideNF"), 
    ENTERING_SIDE_NF_ACTION("进场时（排除第一回合），需要预告", "enteringSideNFAction"), 
    PET_BE("召唤兽在场时", "petBe"), 
    NORMAL_ATTACK("物理攻击时", "normalAttack"), 
    NORMAL_ATTACK_AFTER("物理攻击后", "normalAttackAfter"), 
    NORMAL_ATTACK_DOUBLE_HIT("物理攻击触发连击时", "normalAttackDoubleHit"), 
    AFFTER_NORMAL_ATTACK("物理攻击之后", "affterNormalAttack"), 
    STRENGTHEN_NORMAL_ATTACK_PHY("增强物理攻击基础攻击", "strengthenNormalAttackPhy"), 
    STRENGTHEN_NORMAL_ATTACK("增强物理攻击", "strengthenNormalAttack"), 
    NORMAL_ATTACK_TARGET_FALL_DOWN("物理攻击敌方倒地后", "normalAttackTargetFallDown"), 
    SELF_SELL_END("自己出手结束后", "selfSellEnd"), 
    PASSIVE_CONTROL("被控N回合", "passiveControl"), 
    HERO_FALL_DOWN("主人倒地时", "HeroFallDown"), 
    BEFORE_PET_EXIT("离场前", "beforePetExit"), 
    PET_EXIT("离场时", "petExit"), 
    NON_SELF_PET_EXIT("非自己离场时", "nonSelfPetExit"), 
    NON_SELF_PET_EXIT_ACTION("非自己离场时,需要预告", "nonSelfPetExitAction"), 
    ATTACK_PET("攻击对象是宠物时", "attackPet"), 
    UNIT_MAGIC("单位施法时", "unitMagic"), 
    FRIEND_MAGIC("友方施法时", "friendMagic"), 
    ENEMY_FIRST_MAGIC("敌方首次法术攻击时", "enemyFirstMagic"), 
    ENEMY_MAGIC("敌方施法时", "enemyMagic"), 
    MAGIC_ATTACK_END("法术攻击完毕后", "magicAttackEnd"), 
    UNIT_MAGIC_ACTION("敌我双方施法时,需要预告", "unitMagicAction"), 
    CURR_LIFE_LESS_THAN_40("气血小于40%", "currLifeLessThan40"), 
    INITIATIVE_SKILL_TRIGGER("施展主动技能时触发", "initiativeSkillTrigger"), 
    PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL("触发被动技能的主动技能", "passiveTriggerForInitiativeSkill"), 
    FLASH_BEFORE_ONE("闪现", "flashBeforeOne"), 
    USE_DRUGS("使用药物后触发", "useDrugs"), 
    REVIVE_SKILL("复活技能", "reviveSkill"), 
    STUDY_FINISH("学习完毕后就会", "studyFinish"), 
    INITIATIVE_SKILL("主动技能", "initiativeSkill"), 
    OTHER("其他", "other");
    
    private String name;
    private String value;
    
    public static List<String> toList() {
        final List list = Lists.newArrayList();
        for (final PET_PASSIVE_SKILL_TRIGGER_DICT n : values()) {
            list.add(n.getValue());
        }
        return list;
    }
    
    public static Map<Integer, List<String>> getSkillTypeOther() {
        final Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        final List<String> list83036 = new ArrayList<String>() {
            {
                this.add(PET_PASSIVE_SKILL_TRIGGER_DICT.ROUND_START.getValue());
                this.add(PET_PASSIVE_SKILL_TRIGGER_DICT.MAGIC_ATTACK_END.getValue());
            }
        };
        map.put(83036, list83036);
        return map;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    private PET_PASSIVE_SKILL_TRIGGER_DICT(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
}
