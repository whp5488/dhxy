
package com.kitty.system.init.pool;

import java.util.HashMap;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysPartyPractice;
import com.kitty.system.init.domain.SysSkillUpGradeConsume;
import com.kitty.system.init.domain.SysLevelExp;
import java.util.Map;

public class ExpDataPool
{
    public static Map<Integer, SysLevelExp> heroLevelExpMap;
    public static Map<Integer, SysLevelExp> petLevelExpMap;
    public static Map<Integer, SysLevelExp> partnerLevelExpMap;
    public static Map<Integer, SysLevelExp> horseLevelExpMap;
    public static Map<Integer, SysSkillUpGradeConsume> skillUpGradeConsume;
    public static Map<Integer, SysPartyPractice> partyPractice;
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        final Map<String, Map<Integer, SysLevelExp>> levelExpMap = initService.initLevelExp();
        ExpDataPool.heroLevelExpMap = levelExpMap.get("heroMap");
        ExpDataPool.petLevelExpMap = levelExpMap.get("petMap");
        ExpDataPool.partnerLevelExpMap = levelExpMap.get("partnerMap");
        ExpDataPool.horseLevelExpMap = levelExpMap.get("horseMap");
        ExpDataPool.skillUpGradeConsume = initService.initSkillUpGradeConsume();
        ExpDataPool.partyPractice = initService.initPartyPractice();
    }
    
    static {
        ExpDataPool.heroLevelExpMap = new HashMap<Integer, SysLevelExp>();
        ExpDataPool.petLevelExpMap = new HashMap<Integer, SysLevelExp>();
        ExpDataPool.partnerLevelExpMap = new HashMap<Integer, SysLevelExp>();
        ExpDataPool.horseLevelExpMap = new HashMap<Integer, SysLevelExp>();
        ExpDataPool.skillUpGradeConsume = new HashMap<Integer, SysSkillUpGradeConsume>();
        ExpDataPool.partyPractice = new HashMap<Integer, SysPartyPractice>();
    }
}
