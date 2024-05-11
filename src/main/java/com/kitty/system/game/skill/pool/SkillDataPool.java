
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
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        final Object[] objects = initService.initSkill();
        SkillDataPool.skillMap = (Map)objects[0];
        SkillDataPool.groupSkillMap = (Map)objects[1];
        SkillDataPool.petSkillMap = initService.initPetSkill();
        final Object[] tianFuObjects = initService.initTianFuSkill();
        SkillDataPool.tianFuSkillMap = (Map)tianFuObjects[0];
        SkillDataPool.triggerTianFuSkillMap = (Map)tianFuObjects[1];
        SkillDataPool.noTriggerTianFuSkillMap = (Map)tianFuObjects[2];
        SkillDataPool.dayInCePracticeMap = initService.initDayInCePractice();
        SkillDataPool.acsrySkillMap = initService.initAcsrySkill();
        for (final ACSRY_SKILL_TRIGGER_DICT dict : ACSRY_SKILL_TRIGGER_DICT.values()) {
            SkillDataPool.acsrySkillSerialType.put(dict.getValue(), new HashSet<Integer>());
        }
        for (final SysDataAcsrySkill sysDataAcsrySkill : SkillDataPool.acsrySkillMap.values()) {
            if (sysDataAcsrySkill != null) {
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
                    for (final String triggerType : sysDataAcsrySkill.getTriggerType()) {
                        if (SkillDataPool.acsrySkillSerialType.get(triggerType) != null) {
                            SkillDataPool.acsrySkillSerialType.get(triggerType).add(sysDataAcsrySkill.getSerial());
                        }
                    }
                }
                if (sysDataAcsrySkill.getForthwithType().contains(1)) {
                    SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.WEAR_TAKE_EFFECT.getValue()).add(sysDataAcsrySkill.getSerial());
                }
                if (!sysDataAcsrySkill.getForthwithType().contains(2)) {
                    continue;
                }
                SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.INITIALIZE_FIGHT_OBJECT.getValue()).add(sysDataAcsrySkill.getSerial());
            }
        }
    }
    
    static {
        SkillDataPool.skillMap = new HashMap<Integer, SysSkill>();
        SkillDataPool.groupSkillMap = new HashMap<Integer, Map<Integer, Map<Integer, List<SysSkill>>>>();
        SkillDataPool.petSkillMap = new HashMap<Integer, SysPetSkill>();
        SkillDataPool.tianFuSkillMap = new HashMap<Integer, SysTianFuSkill>();
        SkillDataPool.triggerTianFuSkillMap = new HashMap<Integer, SysTianFuSkill>();
        SkillDataPool.noTriggerTianFuSkillMap = new HashMap<Integer, SysTianFuSkill>();
        SkillDataPool.dayInCePracticeMap = new HashMap<Short, SysDayInCePractice>();
        SkillDataPool.acsrySkillMap = new HashMap<Integer, SysDataAcsrySkill>();
        SkillDataPool.acsrySkillSerialType = new HashMap<String, Set<Integer>>();
    }
}
