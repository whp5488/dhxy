
package com.kitty.system.game.task.pool;

import java.util.HashMap;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.init.domain.SysDataTaskSurround;
import com.kitty.common.utils.CalculateUtils;
import com.kitty.system.game.vo.Npc;
import com.kitty.mina.cache.DataCache;
import com.kitty.common.utils.bean.BeanUtils;

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
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
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
    
    private static void initTaskMoAward(final InitService initService) {
        final List<SysDataTaskMoAward> awardList = initService.initTaskMoAward();
        for (final SysDataTaskMoAward award : awardList) {
            List<SysDataTaskMoAward> list = TaskDataPool.moAwardMap.get(award.getDungeon());
            if (list == null) {
                list = new ArrayList<>();
                TaskDataPool.moAwardMap.put(award.getDungeon(), list);
            }
            list.add(award);
        }
    }
    
    private static void initTaskKj(final InitService initService) {
        TaskDataPool.taskKjList = initService.initTaskKj();
        for (final SysDataTaskKeju object : TaskDataPool.taskKjList) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskMain(final InitService initService) {
        TaskDataPool.taskMainMap = initService.initTaskMain();
        SysDataTaskMain nextTask = null;
        for (final Map.Entry<Integer, SysDataTaskMain> entry : TaskDataPool.taskMainMap.entrySet()) {
            nextTask = TaskDataPool.taskMainMap.get(entry.getValue().getNextTaskSerial());
            if (nextTask == null) {
                continue;
            }
            nextTask.setPreviousId(entry.getValue().getSerial());
        }
        for (final Integer key : TaskDataPool.taskMainMap.keySet()) {
            final SysDataTaskMain taskMain = TaskDataPool.taskMainMap.get(key);
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, taskMain);
            TaskDataPool.taskMap.put(taskMain.getSerial(), taskSet);
        }
    }
    
    public static void initTaskSchool(final InitService initService) {
        final Map<Integer, SysDataTaskSchool> taskSchoolMap = initService.initTaskSchool();
        for (final Integer key : taskSchoolMap.keySet()) {
            final SysDataTaskSchool taskSchool = taskSchoolMap.get(key);
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, taskSchool);
            TaskDataPool.taskMap.put(taskSchool.getSerial(), taskSet);
            final String value = String.valueOf(taskSchool.getSerial());
            final int type = Integer.valueOf(value.substring(1));
            if (type == 3) {
                TaskDataPool.taskSchoolPurchase.add(taskSchool);
            }
            else {
                TaskDataPool.taskSchoolElse.add(taskSchool);
            }
        }
    }
    
    public static void initTaskTreasureMap(final InitService initService) {
        TaskDataPool.taskTreasureMap = initService.initTaskTreasureMap();
        for (final SysDataTaskTreasureMap object : TaskDataPool.taskTreasureMap) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskParty(final InitService initService) {
        TaskDataPool.taskPartyList = initService.initTaskParty();
        for (final SysDataTaskParty object : TaskDataPool.taskPartyList) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskLoop(final InitService initService) {
        TaskDataPool.taskLoopList = initService.initTaskLoop();
        for (final SysDataTaskLoop object : TaskDataPool.taskLoopList) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskTower(final InitService initService) {
        TaskDataPool.taskTowerList = initService.initTaskTower();
        for (final SysDataTaskTower object : TaskDataPool.taskTowerList) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskLookFor(final InitService initService) {
        TaskDataPool.taskLookForList = initService.initTaskLookFor();
        for (final SysDataTaskLookFor object : TaskDataPool.taskLookForList) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskXm(final InitService initService) {
        TaskDataPool.taskXmList = initService.initTaskXm();
        for (final SysDataTaskXm object : TaskDataPool.taskXmList) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskFive(final InitService initService) {
        TaskDataPool.taskFiveList = initService.initTaskFive();
        for (final SysDataTaskFive object : TaskDataPool.taskFiveList) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskCatchesGhosts(final InitService initService) {
        TaskDataPool.taskCatchesGhosts = initService.initTaskCatchesGhosts();
        for (final SysDataTaskCatchesGhosts object : TaskDataPool.taskCatchesGhosts) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskGodsCourt(final InitService initService) {
        TaskDataPool.taskGodsCourts = initService.initTaskGodsCourt();
        for (final SysDataTaskGodsCourt object : TaskDataPool.taskGodsCourts) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskCarnival(final InitService initService) {
        TaskDataPool.taskCarnivalList = initService.initTaskCarnival();
        for (final SysDataTaskCarnival object : TaskDataPool.taskCarnivalList) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskDragon(final InitService initService) {
        TaskDataPool.taskDragonList = initService.initTaskDragon();
        for (final SysDataTaskDragon object : TaskDataPool.taskDragonList) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskMo(final InitService initService) {
        TaskDataPool.taskMoList = initService.initTaskMo();
        for (final SysDataTaskMo object : TaskDataPool.taskMoList) {
            final TaskSet taskSet = new TaskSet();
            BeanUtils.copyBeanProp(taskSet, object);
            TaskDataPool.taskMap.put(object.getSerial(), taskSet);
        }
    }
    
    public static void initTaskNpc(final InitService initService) {
        TaskDataPool.taskNpcMap = initService.initTaskNpc();
        for (final SysDataTaskNpc value : TaskDataPool.taskNpcMap.values()) {
            if (value.getIsScene() == 1) {
                DataCache.SCENE_TASK_NPC.put(value.getSerial(), new Npc(value));
            }
        }
    }
    
    public static void initNpcName(final InitService initService) {
        TaskDataPool.taskNpcNameList = initService.initNpcName();
    }
    
    public static void initNpcNameType(final InitService initService) {
        TaskDataPool.taskNpcNameTypeMap = initService.initNpcNameType();
    }
    
    public static void initTaskFormula(final InitService initService) {
        TaskDataPool.taskFormulaMap = initService.initTaskFormula();
    }
    
    public static void initTaskFight(final InitService initService) {
        TaskDataPool.taskFightMap = initService.initTaskFight();
    }
    
    public static void initTaskCondition(final InitService initService) {
        TaskDataPool.taskConditionMap = initService.initTaskCondition();
    }
    
    public static void initTaskEvent(final InitService initService) {
        TaskDataPool.taskEventMap = initService.initTaskEvent();
    }
    
    public static void initTaskEventForeign(final InitService initService) {
        TaskDataPool.taskEventForeign = initService.initTaskEventForeign();
    }
    
    public static void initTaskReward(final InitService initService) {
        TaskDataPool.taskRewardMap = initService.initTaskReward();
    }
    
    public static void initTaskRewardDynamic(final InitService initService) {
        TaskDataPool.taskRewardDynamicList = initService.initTaskRewardDynamic();
        for (final SysDataTaskRewardDynamic obj : TaskDataPool.taskRewardDynamicList) {
            TaskDataPool.taskRewardDynamicNumList.put(obj.getIdentify(), CalculateUtils.calculate(obj.getNumCalculate()).intValue());
            TaskDataPool.taskRewardDynamicRateList.put(obj.getIdentify(), CalculateUtils.calculate(obj.getRateCalculate()).doubleValue());
        }
    }
    
    public static void initTaskRewardForeign(final InitService initService) {
        TaskDataPool.taskRewardForeign = initService.initTaskRewardForeign();
    }
    
    public static void initWorldBoosList(final InitService initService) {
        TaskDataPool.worldBoosList = initService.initWorldBoos();
    }
    
    public static void initPartyBanditList(final InitService initService) {
        TaskDataPool.partyBanditList = initService.initPartyBanditList();
    }
    
    public static void initStarOfficerList(final InitService initService) {
        TaskDataPool.starOfficerList = initService.initStarOfficerList();
    }
    
    public static void initSealDemonList(final InitService initService) {
        TaskDataPool.sealDemonList = initService.initSealDemonList();
    }
    
    public static void initThreeScopeDemonList(final InitService initService) {
        TaskDataPool.threeScopeDemonList = initService.initThreeScopeDemonList();
    }
    
    public static void initTaskSurround(final InitService initService) {
        final List<SysDataTaskSurround> list = initService.initTaskSurround();
        for (final SysDataTaskSurround object : list) {
            boolean flag = false;
            if (TaskType.CRUSADE_RENEGADE.equals(object.getTaskType())) {
                final SysDataTaskCrusadeRenegade renegade = new SysDataTaskCrusadeRenegade();
                BeanUtils.copyBeanProp(renegade, object);
                TaskDataPool.crusadeRenegadeList.add(renegade);
                flag = true;
            } else if (TaskType.XL.equals(object.getTaskType())) {
                final SysDataTaskCrusadeRenegade renegade = new SysDataTaskCrusadeRenegade();
                BeanUtils.copyBeanProp(renegade, object);
                TaskDataPool.conquerAsuraList.add(renegade);
                flag = true;
            }
            if (flag) {
                final TaskSet taskSet = new TaskSet();
                BeanUtils.copyBeanProp(taskSet, object);
                TaskDataPool.taskMap.put(object.getSerial(), taskSet);
            }
        }
    }

    public static void initCountryQuestion(final InitService initService) {
        TaskDataPool.countryQuestion = initService.initCountryQuestionLib();
        for (final Integer key : TaskDataPool.countryQuestion.keySet()) {
            final SysCountryQuestion question = TaskDataPool.countryQuestion.get(key);
            TaskDataPool.questionMap.put(key, new Question(question.getSerial(), question.getAnswer()));
        }
    }
    
    public static void initProvinceQuestion(final InitService initService) {
        TaskDataPool.provinceQuestion = initService.initProvinceQuestion();
        for (final Integer key : TaskDataPool.provinceQuestion.keySet()) {
            final SysProvinceQuestion question = TaskDataPool.provinceQuestion.get(key);
            TaskDataPool.questionMap.put(key, new Question(question.getSerial(), question.getAnswer(), question.getType()));
        }
    }
    
    public static void initHallQuestion(final InitService initService) {
        TaskDataPool.hallQuestion = initService.initHallQuestion();
        for (final Integer key : TaskDataPool.hallQuestion.keySet()) {
            final SysHallQuestion question = TaskDataPool.hallQuestion.get(key);
            TaskDataPool.questionMap.put(key, new Question(question.getSerial(), question.getAnswer()));
        }
    }
    
    public static void initDragonReward() {
        TaskDataPool.dragonRewardList.add(new Param(21064, 1, 1, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21070, 1, 1, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21071, 1, 1, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21067, 1, 2, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21069, 1, 1, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21068, 1, 1, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21066, 1, 2, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21065, 1, 1, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21063, 1, 5, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21062, 1, 5, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21061, 1, 5, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21059, 1, 5, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21060, 1, 5, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21058, 1, 5, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21057, 1, 5, 0, null));
        TaskDataPool.dragonRewardList.add(new Param(21056, 1, 5, 0, null));
    }
    
    static {
        TaskDataPool.taskMap = new HashMap<Integer, TaskSet>();
        TaskDataPool.taskMainMap = new HashMap<Integer, SysDataTaskMain>();
        TaskDataPool.taskSchoolElse = new ArrayList<SysDataTaskSchool>();
        TaskDataPool.taskSchoolPurchase = new ArrayList<SysDataTaskSchool>();
        TaskDataPool.taskTreasureMap = new ArrayList<SysDataTaskTreasureMap>();
        TaskDataPool.taskPartyList = new ArrayList<SysDataTaskParty>();
        TaskDataPool.taskLoopList = new ArrayList<SysDataTaskLoop>();
        TaskDataPool.taskTowerList = new ArrayList<SysDataTaskTower>();
        TaskDataPool.taskLookForList = new ArrayList<SysDataTaskLookFor>();
        TaskDataPool.taskXmList = new ArrayList<SysDataTaskXm>();
        TaskDataPool.taskFiveList = new ArrayList<SysDataTaskFive>();
        TaskDataPool.taskCatchesGhosts = new ArrayList<SysDataTaskCatchesGhosts>();
        TaskDataPool.taskGodsCourts = new ArrayList<SysDataTaskGodsCourt>();
        TaskDataPool.taskCarnivalList = new ArrayList<SysDataTaskCarnival>();
        TaskDataPool.taskDragonList = new ArrayList<SysDataTaskDragon>();
        TaskDataPool.taskMoList = new ArrayList<SysDataTaskMo>();
        TaskDataPool.taskKjList = new ArrayList<SysDataTaskKeju>();
        TaskDataPool.moAwardMap = new HashMap<Integer, List<SysDataTaskMoAward>>();
        TaskDataPool.taskNpcMap = new HashMap<Integer, SysDataTaskNpc>();
        TaskDataPool.taskNpcNameList = new ArrayList<SysDataTaskNpcName>();
        TaskDataPool.taskNpcNameTypeMap = new HashMap<String, SysDataTaskNpcNameType>();
        TaskDataPool.taskFormulaMap = new HashMap<Integer, SysDataTaskFormula>();
        TaskDataPool.taskFightMap = new HashMap<Integer, SysDataTaskFight>();
        TaskDataPool.taskConditionMap = new HashMap<Integer, SysDataTaskCondition>();
        TaskDataPool.taskEventMap = new HashMap<Integer, SysDataTaskEvent>();
        TaskDataPool.taskEventForeign = new ArrayList<SysDataTaskEventForeign>();
        TaskDataPool.taskRewardMap = new HashMap<String, SysDataTaskReward>();
        TaskDataPool.taskRewardDynamicList = new ArrayList<SysDataTaskRewardDynamic>();
        TaskDataPool.taskRewardDynamicNumList = new HashMap<Integer, Integer>();
        TaskDataPool.taskRewardDynamicRateList = new HashMap<Integer, Double>();
        TaskDataPool.taskRewardForeign = new ArrayList<SysDataTaskForeign>();
        TaskDataPool.dragonRewardList = new ArrayList<Param>();
        TaskDataPool.partyBanditList = new ArrayList<SysDataPartyBandit>();
        TaskDataPool.starOfficerList = new ArrayList<SysDataStarOfficer>();
        TaskDataPool.worldBoosList = new ArrayList<SysDataWorldBoos>();
        TaskDataPool.sealDemonList = new ArrayList<SysDataSealDemon>();
        TaskDataPool.threeScopeDemonList = new ArrayList<>();
        TaskDataPool.crusadeRenegadeList = new ArrayList<>();
        TaskDataPool.conquerAsuraList = new ArrayList<>();
        TaskDataPool.questionMap = new HashMap<Integer, Question>();
        TaskDataPool.countryQuestion = new HashMap<Integer, SysCountryQuestion>();
        TaskDataPool.hallQuestion = new HashMap<Integer, SysHallQuestion>();
        TaskDataPool.provinceQuestion = new HashMap<Integer, SysProvinceQuestion>();
    }
}
