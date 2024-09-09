package com.kitty.system.game.skill.pool;

import java.util.HashMap;
import java.util.Iterator;
import com.kitty.system.init.service.InitService;
import java.util.HashSet;
import com.kitty.system.game.fightnew.book.acsry.def.ACSRY_SKILL_TRIGGER_DICT;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.Set;
import com.kitty.system.init.domain.SysDataAcsrySkill;
import com.kitty.system.game.tianfu.domain.SysDayInCePractice;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.List;
import com.kitty.system.init.domain.SysSkill;
import java.util.Map;

public class SkillDataPool
{
    public static Map<Integer, SysSkill> skillMap;
    public static Map<Integer, Map<Integer, Map<Integer, List<SysSkill>>>> groupSkillMap;
    public static Map<Integer, SysPetSkill> petSkillMap;
    public static Map<Integer, SysTianFuSkill> tianFuSkillMap;
    public static Map<Integer, SysTianFuSkill> triggerTianFuSkillMap;
    public static Map<Integer, SysTianFuSkill> noTriggerTianFuSkillMap;
    public static Map<Short, SysDayInCePractice> dayInCePracticeMap;
    public static Map<Integer, SysDataAcsrySkill> acsrySkillMap;
    public static Map<String, Set<Integer>> acsrySkillSerialType;
    
    public SkillDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        Object[] objects = initService.initSkill();
        SkillDataPool.skillMap = (Map)objects[0];
        SkillDataPool.groupSkillMap = (Map)objects[1];
        SkillDataPool.petSkillMap = initService.initPetSkill();
        Object[] tianFuObjects = initService.initTianFuSkill();
        SkillDataPool.tianFuSkillMap = (Map)tianFuObjects[0];
        SkillDataPool.triggerTianFuSkillMap = (Map)tianFuObjects[1];
        SkillDataPool.noTriggerTianFuSkillMap = (Map)tianFuObjects[2];
        SkillDataPool.dayInCePracticeMap = initService.initDayInCePractice();
        SkillDataPool.acsrySkillMap = initService.initAcsrySkill();
        for (ACSRY_SKILL_TRIGGER_DICT dict : ACSRY_SKILL_TRIGGER_DICT.values()) {
            SkillDataPool.acsrySkillSerialType.put(dict.getValue(), new HashSet());
        }
        for (SysDataAcsrySkill sysDataAcsrySkill : SkillDataPool.acsrySkillMap.values()) {
            if (sysDataAcsrySkill == null) {
                continue;
            }
            if (sysDataAcsrySkill.getSerial() <= 0) {
                continue;
            }
            if (sysDataAcsrySkill != null && sysDataAcsrySkill.getTriggerType() != null) {
                if (sysDataAcsrySkill.getTriggerType() == null) {
                    continue;
                }
                if (sysDataAcsrySkill.getTriggerType().size() <= 0) {
                    continue;
                }
                for (String triggerType : sysDataAcsrySkill.getTriggerType()) {
                    if (SkillDataPool.acsrySkillSerialType.get(triggerType) != null) {
                        ((Set)SkillDataPool.acsrySkillSerialType.get(triggerType)).add(Integer.valueOf(sysDataAcsrySkill.getSerial()));
                    }
                    else {
                        continue;
                    }
                }
            }
            if (sysDataAcsrySkill.getForthwithType().contains(Integer.valueOf(1))) {
                ((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.WEAR_TAKE_EFFECT.getValue())).add(Integer.valueOf(sysDataAcsrySkill.getSerial()));
            }
            if (sysDataAcsrySkill.getForthwithType().contains(Integer.valueOf(2))) {
                ((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.INITIALIZE_FIGHT_OBJECT.getValue())).add(Integer.valueOf(sysDataAcsrySkill.getSerial()));
            }
            else {
                continue;
            }
        }
    }
    
    static {
        SkillDataPool.skillMap = new HashMap();
        SkillDataPool.groupSkillMap = new HashMap();
        SkillDataPool.petSkillMap = new HashMap();
        SkillDataPool.tianFuSkillMap = new HashMap();
        SkillDataPool.triggerTianFuSkillMap = new HashMap();
        SkillDataPool.noTriggerTianFuSkillMap = new HashMap();
        SkillDataPool.dayInCePracticeMap = new HashMap();
        SkillDataPool.acsrySkillMap = new HashMap();
        SkillDataPool.acsrySkillSerialType = new HashMap();
    }
}
