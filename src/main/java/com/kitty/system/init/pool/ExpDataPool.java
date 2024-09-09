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
    
    public ExpDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        Map<String, Map<Integer, SysLevelExp>> levelExpMap = initService.initLevelExp();
        ExpDataPool.heroLevelExpMap = (Map)levelExpMap.get("heroMap");
        ExpDataPool.petLevelExpMap = (Map)levelExpMap.get("petMap");
        ExpDataPool.partnerLevelExpMap = (Map)levelExpMap.get("partnerMap");
        ExpDataPool.horseLevelExpMap = (Map)levelExpMap.get("horseMap");
        ExpDataPool.skillUpGradeConsume = initService.initSkillUpGradeConsume();
        ExpDataPool.partyPractice = initService.initPartyPractice();
    }
    
    static {
        ExpDataPool.heroLevelExpMap = new HashMap();
        ExpDataPool.petLevelExpMap = new HashMap();
        ExpDataPool.partnerLevelExpMap = new HashMap();
        ExpDataPool.horseLevelExpMap = new HashMap();
        ExpDataPool.skillUpGradeConsume = new HashMap();
        ExpDataPool.partyPractice = new HashMap();
    }
}
