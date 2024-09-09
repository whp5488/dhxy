package com.kitty.system.game.task.pool;

import java.util.HashMap;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.init.domain.SysDataTaskSurround;
import com.kitty.common.utils.CalculateUtils;
import com.kitty.system.game.vo.Npc;
import com.kitty.mina.cache.DataCache;
import com.kitty.common.utils.bean.BeanUtils;
import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysProvinceQuestion;
import com.kitty.system.init.domain.SysHallQuestion;
import com.kitty.system.init.domain.SysCountryQuestion;
import com.kitty.system.init.domain.vo.Question;
import com.kitty.system.init.domain.vo.surround.SysDataTaskCrusadeRenegade;
import com.kitty.system.init.domain.SysDataThreeScopeDemon;
import com.kitty.system.init.domain.SysDataSealDemon;
import com.kitty.system.init.domain.SysDataWorldBoos;
import com.kitty.system.init.domain.SysDataStarOfficer;
import com.kitty.system.init.domain.SysDataPartyBandit;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.init.domain.SysDataTaskForeign;
import com.kitty.system.init.domain.SysDataTaskRewardDynamic;
import com.kitty.system.init.domain.SysDataTaskReward;
import com.kitty.system.init.domain.SysDataTaskEventForeign;
import com.kitty.system.init.domain.SysDataTaskEvent;
import com.kitty.system.init.domain.SysDataTaskCondition;
import com.kitty.system.init.domain.SysDataTaskFight;
import com.kitty.system.init.domain.SysDataTaskFormula;
import com.kitty.system.init.domain.SysDataTaskNpcNameType;
import com.kitty.system.init.domain.SysDataTaskNpcName;
import com.kitty.system.init.domain.SysDataTaskNpc;
import com.kitty.system.init.domain.SysDataTaskMoAward;
import com.kitty.system.init.domain.SysDataTaskKeju;
import com.kitty.system.init.domain.SysDataTaskMo;
import com.kitty.system.init.domain.SysDataTaskDragon;
import com.kitty.system.init.domain.SysDataTaskCarnival;
import com.kitty.system.init.domain.SysDataTaskGodsCourt;
import com.kitty.system.init.domain.SysDataTaskCatchesGhosts;
import com.kitty.system.init.domain.SysDataTaskFive;
import com.kitty.system.init.domain.SysDataTaskXm;
import com.kitty.system.init.domain.SysDataTaskLookFor;
import com.kitty.system.init.domain.SysDataTaskTower;
import com.kitty.system.init.domain.SysDataTaskLoop;
import com.kitty.system.init.domain.SysDataTaskParty;
import com.kitty.system.init.domain.SysDataTaskTreasureMap;
import com.kitty.system.init.domain.SysDataTaskSchool;
import java.util.List;
import com.kitty.system.init.domain.SysDataTaskMain;
import com.kitty.system.game.config.TaskSet;
import java.util.Map;

public class TaskDataPool
{
    public static Map<Integer, TaskSet> taskMap;
    public static Map<Integer, SysDataTaskMain> taskMainMap;
    public static List<SysDataTaskSchool> taskSchoolElse;
    public static List<SysDataTaskSchool> taskSchoolPurchase;
    public static List<SysDataTaskTreasureMap> taskTreasureMap;
    public static List<SysDataTaskParty> taskPartyList;
    public static List<SysDataTaskLoop> taskLoopList;
    public static List<SysDataTaskTower> taskTowerList;
    public static List<SysDataTaskLookFor> taskLookForList;
    public static List<SysDataTaskXm> taskXmList;
    public static List<SysDataTaskFive> taskFiveList;
    public static List<SysDataTaskCatchesGhosts> taskCatchesGhosts;
    public static List<SysDataTaskGodsCourt> taskGodsCourts;
    public static List<SysDataTaskCarnival> taskCarnivalList;
    public static List<SysDataTaskDragon> taskDragonList;
    public static List<SysDataTaskMo> taskMoList;
    public static List<SysDataTaskKeju> taskKjList;
    public static Map<Integer, List<SysDataTaskMoAward>> moAwardMap;
    public static Map<Integer, SysDataTaskNpc> taskNpcMap;
    public static List<SysDataTaskNpcName> taskNpcNameList;
    public static Map<String, SysDataTaskNpcNameType> taskNpcNameTypeMap;
    public static Map<Integer, SysDataTaskFormula> taskFormulaMap;
    public static Map<Integer, SysDataTaskFight> taskFightMap;
    public static Map<Integer, SysDataTaskCondition> taskConditionMap;
    public static Map<Integer, SysDataTaskEvent> taskEventMap;
    public static List<SysDataTaskEventForeign> taskEventForeign;
    public static Map<String, SysDataTaskReward> taskRewardMap;
    public static List<SysDataTaskRewardDynamic> taskRewardDynamicList;
    public static Map<Integer, Integer> taskRewardDynamicNumList;
    public static Map<Integer, Double> taskRewardDynamicRateList;
    public static List<SysDataTaskForeign> taskRewardForeign;
    public static List<Param> dragonRewardList;
    public static List<SysDataPartyBandit> partyBanditList;
    public static List<SysDataStarOfficer> starOfficerList;
    public static List<SysDataWorldBoos> worldBoosList;
    public static List<SysDataSealDemon> sealDemonList;
    public static List<SysDataThreeScopeDemon> threeScopeDemonList;
    public static List<SysDataTaskCrusadeRenegade> crusadeRenegadeList;
    public static List<SysDataTaskCrusadeRenegade> conquerAsuraList;
    public static Map<Integer, Question> questionMap;
    public static Map<Integer, SysCountryQuestion> countryQuestion;
    public static Map<Integer, SysHallQuestion> hallQuestion;
    public static Map<Integer, SysProvinceQuestion> provinceQuestion;
    
    public TaskDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        initTaskMain(initService);
        initTaskSchool(initService);
        initTaskTreasureMap(initService);
        initTaskParty(initService);
        initTaskLoop(initService);
        initTaskTower(initService);
        initTaskLookFor(initService);
        initTaskXm(initService);
        initTaskFive(initService);
        initTaskCatchesGhosts(initService);
        initTaskGodsCourt(initService);
        initTaskCarnival(initService);
        initTaskDragon(initService);
        initTaskMo(initService);
        initTaskMoAward(initService);
        initTaskKj(initService);
        initTaskNpc(initService);
        initNpcName(initService);
        initNpcNameType(initService);
        initTaskFormula(initService);
        initTaskFight(initService);
        initTaskCondition(initService);
        initTaskEvent(initService);
        initTaskEventForeign(initService);
        initTaskReward(initService);
        initTaskRewardDynamic(initService);
        initTaskRewardForeign(initService);
        initWorldBoosList(initService);
        initPartyBanditList(initService);
        initStarOfficerList(initService);
        initSealDemonList(initService);
        initThreeScopeDemonList(initService);
        initTaskSurround(initService);
        initCountryQuestion(initService);
        initProvinceQuestion(initService);
        initHallQuestion(initService);
        initDragonReward();
    }
    
    private static void initTaskMoAward(InitService initService) {
        List<SysDataTaskMoAward> awardList = initService.initTaskMoAward();
        for (SysDataTaskMoAward award : awardList) {
            List<SysDataTaskMoAward> list = (List)TaskDataPool.moAwardMap.get(Integer.valueOf(award.getDungeon()));
            if (list == null) {
                list = new ArrayList<>();
                TaskDataPool.moAwardMap.put(Integer.valueOf(award.getDungeon()), list);
            }
            list.add(award);
        }
    }
    
    private static void initTaskKj(InitService initService) {
        TaskDataPool.taskKjList = initService.initTaskKj();
        for (SysDataTaskKeju object : TaskDataPool.taskKjList) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskMain(InitService initService) {
        TaskDataPool.taskMainMap = initService.initTaskMain();
        SysDataTaskMain nextTask = null;
        for (Map.Entry<Integer, SysDataTaskMain> entry : TaskDataPool.taskMainMap.entrySet()) {
            nextTask = (SysDataTaskMain)TaskDataPool.taskMainMap.get(Integer.valueOf(((SysDataTaskMain)entry.getValue()).getNextTaskSerial()));
            if (nextTask == null) {
                continue;
            }
            nextTask.setPreviousId(((SysDataTaskMain)entry.getValue()).getSerial());
        }
        for (Integer key : TaskDataPool.taskMainMap.keySet()) {
            SysDataTaskMain taskMain = (SysDataTaskMain)TaskDataPool.taskMainMap.get(key);
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, taskMain);
            TaskDataPool.taskMap.put(Integer.valueOf(taskMain.getSerial()), taskSet);
        }
    }
    
    public static void initTaskSchool(InitService initService) {
        Map<Integer, SysDataTaskSchool> taskSchoolMap = initService.initTaskSchool();
        for (Integer key : taskSchoolMap.keySet()) {
            SysDataTaskSchool taskSchool = (SysDataTaskSchool)taskSchoolMap.get(key);
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, taskSchool);
            TaskDataPool.taskMap.put(Integer.valueOf(taskSchool.getSerial()), taskSet);
            String value = String.valueOf(taskSchool.getSerial());
            int type = (int)Integer.valueOf(value.substring(1));
            if (type == 3) {
                TaskDataPool.taskSchoolPurchase.add(taskSchool);
            }
            else {
                TaskDataPool.taskSchoolElse.add(taskSchool);
            }
        }
    }
    
    public static void initTaskTreasureMap(InitService initService) {
        TaskDataPool.taskTreasureMap = initService.initTaskTreasureMap();
        for (SysDataTaskTreasureMap object : TaskDataPool.taskTreasureMap) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskParty(InitService initService) {
        TaskDataPool.taskPartyList = initService.initTaskParty();
        for (SysDataTaskParty object : TaskDataPool.taskPartyList) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskLoop(InitService initService) {
        TaskDataPool.taskLoopList = initService.initTaskLoop();
        for (SysDataTaskLoop object : TaskDataPool.taskLoopList) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskTower(InitService initService) {
        TaskDataPool.taskTowerList = initService.initTaskTower();
        for (SysDataTaskTower object : TaskDataPool.taskTowerList) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskLookFor(InitService initService) {
        TaskDataPool.taskLookForList = initService.initTaskLookFor();
        for (SysDataTaskLookFor object : TaskDataPool.taskLookForList) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskXm(InitService initService) {
        TaskDataPool.taskXmList = initService.initTaskXm();
        for (SysDataTaskXm object : TaskDataPool.taskXmList) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskFive(InitService initService) {
        TaskDataPool.taskFiveList = initService.initTaskFive();
        for (SysDataTaskFive object : TaskDataPool.taskFiveList) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskCatchesGhosts(InitService initService) {
        TaskDataPool.taskCatchesGhosts = initService.initTaskCatchesGhosts();
        for (SysDataTaskCatchesGhosts object : TaskDataPool.taskCatchesGhosts) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskGodsCourt(InitService initService) {
        TaskDataPool.taskGodsCourts = initService.initTaskGodsCourt();
        for (SysDataTaskGodsCourt object : TaskDataPool.taskGodsCourts) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskCarnival(InitService initService) {
        TaskDataPool.taskCarnivalList = initService.initTaskCarnival();
        for (SysDataTaskCarnival object : TaskDataPool.taskCarnivalList) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskDragon(InitService initService) {
        TaskDataPool.taskDragonList = initService.initTaskDragon();
        for (SysDataTaskDragon object : TaskDataPool.taskDragonList) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskMo(InitService initService) {
        TaskDataPool.taskMoList = initService.initTaskMo();
        for (SysDataTaskMo object : TaskDataPool.taskMoList) {
            TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
        }
    }
    
    public static void initTaskNpc(InitService initService) {
        TaskDataPool.taskNpcMap = initService.initTaskNpc();
        for (SysDataTaskNpc value : TaskDataPool.taskNpcMap.values()) {
            if (value.getIsScene() == 1) {
                DataCache.SCENE_TASK_NPC.put(Integer.valueOf(value.getSerial()), new Npc(value));
            }
            else {
                continue;
            }
        }
    }
    
    public static void initNpcName(InitService initService) {
        TaskDataPool.taskNpcNameList = initService.initNpcName();
    }
    
    public static void initNpcNameType(InitService initService) {
        TaskDataPool.taskNpcNameTypeMap = initService.initNpcNameType();
    }
    
    public static void initTaskFormula(InitService initService) {
        TaskDataPool.taskFormulaMap = initService.initTaskFormula();
    }
    
    public static void initTaskFight(InitService initService) {
        TaskDataPool.taskFightMap = initService.initTaskFight();
    }
    
    public static void initTaskCondition(InitService initService) {
        TaskDataPool.taskConditionMap = initService.initTaskCondition();
    }
    
    public static void initTaskEvent(InitService initService) {
        TaskDataPool.taskEventMap = initService.initTaskEvent();
    }
    
    public static void initTaskEventForeign(InitService initService) {
        TaskDataPool.taskEventForeign = initService.initTaskEventForeign();
    }
    
    public static void initTaskReward(InitService initService) {
        TaskDataPool.taskRewardMap = initService.initTaskReward();
    }
    
    public static void initTaskRewardDynamic(InitService initService) {
        TaskDataPool.taskRewardDynamicList = initService.initTaskRewardDynamic();
        for (SysDataTaskRewardDynamic obj : TaskDataPool.taskRewardDynamicList) {
            TaskDataPool.taskRewardDynamicNumList.put(Integer.valueOf(obj.getIdentify()), Integer.valueOf(CalculateUtils.calculate(obj.getNumCalculate()).intValue()));
            TaskDataPool.taskRewardDynamicRateList.put(Integer.valueOf(obj.getIdentify()), Double.valueOf(CalculateUtils.calculate(obj.getRateCalculate()).doubleValue()));
        }
    }
    
    public static void initTaskRewardForeign(InitService initService) {
        TaskDataPool.taskRewardForeign = initService.initTaskRewardForeign();
    }
    
    public static void initWorldBoosList(InitService initService) {
        TaskDataPool.worldBoosList = initService.initWorldBoos();
    }
    
    public static void initPartyBanditList(InitService initService) {
        TaskDataPool.partyBanditList = initService.initPartyBanditList();
    }
    
    public static void initStarOfficerList(InitService initService) {
        TaskDataPool.starOfficerList = initService.initStarOfficerList();
    }
    
    public static void initSealDemonList(InitService initService) {
        TaskDataPool.sealDemonList = initService.initSealDemonList();
    }
    
    public static void initThreeScopeDemonList(InitService initService) {
        TaskDataPool.threeScopeDemonList = initService.initThreeScopeDemonList();
    }
    
    public static void initTaskSurround(InitService initService) {
        List<SysDataTaskSurround> list = initService.initTaskSurround();
        for (SysDataTaskSurround object : list) {
            boolean flag = false;
            if (TaskType.CRUSADE_RENEGADE.equals(object.getTaskType())) {
                SysDataTaskCrusadeRenegade renegade = new SysDataTaskCrusadeRenegade();
                BeanUtils.copyBeanProp(renegade, object);
                TaskDataPool.crusadeRenegadeList.add(renegade);
                flag = true;
            }
            else if (TaskType.XL.equals(object.getTaskType())) {
                SysDataTaskCrusadeRenegade renegade = new SysDataTaskCrusadeRenegade();
                BeanUtils.copyBeanProp(renegade, object);
                TaskDataPool.conquerAsuraList.add(renegade);
                flag = true;
            }
            if (flag) {
                TaskSet taskSet = new TaskSet();
                BeanUtils.copyBeanProp(taskSet, object);
                TaskDataPool.taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
            }
            else {
                continue;
            }
        }
    }
    
    public static void initCountryQuestion(InitService initService) {
        TaskDataPool.countryQuestion = initService.initCountryQuestionLib();
        for (Integer key : TaskDataPool.countryQuestion.keySet()) {
            SysCountryQuestion question = (SysCountryQuestion)TaskDataPool.countryQuestion.get(key);
            TaskDataPool.questionMap.put(key, new Question(question.getSerial(), question.getAnswer()));
        }
    }
    
    public static void initProvinceQuestion(InitService initService) {
        TaskDataPool.provinceQuestion = initService.initProvinceQuestion();
        for (Integer key : TaskDataPool.provinceQuestion.keySet()) {
            SysProvinceQuestion question = (SysProvinceQuestion)TaskDataPool.provinceQuestion.get(key);
            TaskDataPool.questionMap.put(key, new Question(question.getSerial(), question.getAnswer(), question.getType()));
        }
    }
    
    public static void initHallQuestion(InitService initService) {
        TaskDataPool.hallQuestion = initService.initHallQuestion();
        for (Integer key : TaskDataPool.hallQuestion.keySet()) {
            SysHallQuestion question = (SysHallQuestion)TaskDataPool.hallQuestion.get(key);
            TaskDataPool.questionMap.put(key, new Question(question.getSerial(), question.getAnswer()));
        }
    }
    
    public static void initDragonReward() {
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21064), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21070), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21071), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21067), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21069), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21068), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21066), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21065), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21063), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21062), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21061), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21059), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21060), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21058), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21057), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
        TaskDataPool.dragonRewardList.add(new Param(Integer.valueOf(21056), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
    }
    
    static {
        TaskDataPool.taskMap = new HashMap();
        TaskDataPool.taskMainMap = new HashMap();
        TaskDataPool.taskSchoolElse = new ArrayList<>();
        TaskDataPool.taskSchoolPurchase = new ArrayList<>();
        TaskDataPool.taskTreasureMap = new ArrayList<>();
        TaskDataPool.taskPartyList = new ArrayList<>();
        TaskDataPool.taskLoopList = new ArrayList<>();
        TaskDataPool.taskTowerList = new ArrayList<>();
        TaskDataPool.taskLookForList = new ArrayList<>();
        TaskDataPool.taskXmList = new ArrayList<>();
        TaskDataPool.taskFiveList = new ArrayList<>();
        TaskDataPool.taskCatchesGhosts = new ArrayList<>();
        TaskDataPool.taskGodsCourts = new ArrayList<>();
        TaskDataPool.taskCarnivalList = new ArrayList<>();
        TaskDataPool.taskDragonList = new ArrayList<>();
        TaskDataPool.taskMoList = new ArrayList<>();
        TaskDataPool.taskKjList = new ArrayList<>();
        TaskDataPool.moAwardMap = new HashMap();
        TaskDataPool.taskNpcMap = new HashMap();
        TaskDataPool.taskNpcNameList = new ArrayList<>();
        TaskDataPool.taskNpcNameTypeMap = new HashMap();
        TaskDataPool.taskFormulaMap = new HashMap();
        TaskDataPool.taskFightMap = new HashMap();
        TaskDataPool.taskConditionMap = new HashMap();
        TaskDataPool.taskEventMap = new HashMap();
        TaskDataPool.taskEventForeign = new ArrayList<>();
        TaskDataPool.taskRewardMap = new HashMap();
        TaskDataPool.taskRewardDynamicList = new ArrayList<>();
        TaskDataPool.taskRewardDynamicNumList = new HashMap();
        TaskDataPool.taskRewardDynamicRateList = new HashMap();
        TaskDataPool.taskRewardForeign = new ArrayList<>();
        TaskDataPool.dragonRewardList = new ArrayList<>();
        TaskDataPool.partyBanditList = new ArrayList<>();
        TaskDataPool.starOfficerList = new ArrayList<>();
        TaskDataPool.worldBoosList = new ArrayList<>();
        TaskDataPool.sealDemonList = new ArrayList<>();
        TaskDataPool.threeScopeDemonList = new ArrayList<>();
        TaskDataPool.crusadeRenegadeList = new ArrayList<>();
        TaskDataPool.conquerAsuraList = new ArrayList<>();
        TaskDataPool.questionMap = new HashMap();
        TaskDataPool.countryQuestion = new HashMap();
        TaskDataPool.hallQuestion = new HashMap();
        TaskDataPool.provinceQuestion = new HashMap();
    }
}
