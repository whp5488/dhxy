package com.kitty.system.game.task.pool;

import com.kitty.common.utils.CalculateUtils;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.init.domain.SysCountryQuestion;
import com.kitty.system.init.domain.SysDataPartyBandit;
import com.kitty.system.init.domain.SysDataSealDemon;
import com.kitty.system.init.domain.SysDataStarOfficer;
import com.kitty.system.init.domain.SysDataTaskCarnival;
import com.kitty.system.init.domain.SysDataTaskCatchesGhosts;
import com.kitty.system.init.domain.SysDataTaskCondition;
import com.kitty.system.init.domain.SysDataTaskDragon;
import com.kitty.system.init.domain.SysDataTaskEvent;
import com.kitty.system.init.domain.SysDataTaskEventForeign;
import com.kitty.system.init.domain.SysDataTaskFight;
import com.kitty.system.init.domain.SysDataTaskFive;
import com.kitty.system.init.domain.SysDataTaskForeign;
import com.kitty.system.init.domain.SysDataTaskFormula;
import com.kitty.system.init.domain.SysDataTaskGodsCourt;
import com.kitty.system.init.domain.SysDataTaskKeju;
import com.kitty.system.init.domain.SysDataTaskLookFor;
import com.kitty.system.init.domain.SysDataTaskLoop;
import com.kitty.system.init.domain.SysDataTaskMain;
import com.kitty.system.init.domain.SysDataTaskMo;
import com.kitty.system.init.domain.SysDataTaskMoAward;
import com.kitty.system.init.domain.SysDataTaskNpc;
import com.kitty.system.init.domain.SysDataTaskNpcName;
import com.kitty.system.init.domain.SysDataTaskNpcNameType;
import com.kitty.system.init.domain.SysDataTaskParty;
import com.kitty.system.init.domain.SysDataTaskReward;
import com.kitty.system.init.domain.SysDataTaskRewardDynamic;
import com.kitty.system.init.domain.SysDataTaskSchool;
import com.kitty.system.init.domain.SysDataTaskSurround;
import com.kitty.system.init.domain.SysDataTaskTower;
import com.kitty.system.init.domain.SysDataTaskTreasureMap;
import com.kitty.system.init.domain.SysDataTaskXm;
import com.kitty.system.init.domain.SysDataThreeScopeDemon;
import com.kitty.system.init.domain.SysDataWorldBoos;
import com.kitty.system.init.domain.SysHallQuestion;
import com.kitty.system.init.domain.SysProvinceQuestion;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.init.domain.vo.Question;
import com.kitty.system.init.domain.vo.surround.SysDataTaskCrusadeRenegade;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDataPool {
  public static Map<Integer, TaskSet> taskMap = new HashMap<>();
  
  public static Map<Integer, SysDataTaskMain> taskMainMap = new HashMap<>();
  
  public static List<SysDataTaskSchool> taskSchoolElse = new ArrayList<>();
  
  public static List<SysDataTaskSchool> taskSchoolPurchase = new ArrayList<>();
  
  public static List<SysDataTaskTreasureMap> taskTreasureMap = new ArrayList<>();
  
  public static List<SysDataTaskParty> taskPartyList = new ArrayList<>();
  
  public static List<SysDataTaskLoop> taskLoopList = new ArrayList<>();
  
  public static List<SysDataTaskTower> taskTowerList = new ArrayList<>();
  
  public static List<SysDataTaskLookFor> taskLookForList = new ArrayList<>();
  
  public static List<SysDataTaskXm> taskXmList = new ArrayList<>();
  
  public static List<SysDataTaskFive> taskFiveList = new ArrayList<>();
  
  public static List<SysDataTaskCatchesGhosts> taskCatchesGhosts = new ArrayList<>();
  
  public static List<SysDataTaskGodsCourt> taskGodsCourts = new ArrayList<>();
  
  public static List<SysDataTaskCarnival> taskCarnivalList = new ArrayList<>();
  
  public static List<SysDataTaskDragon> taskDragonList = new ArrayList<>();
  
  public static List<SysDataTaskMo> taskMoList = new ArrayList<>();
  
  public static List<SysDataTaskKeju> taskKjList = new ArrayList<>();
  
  public static Map<Integer, List<SysDataTaskMoAward>> moAwardMap = new HashMap<>();
  
  public static Map<Integer, SysDataTaskNpc> taskNpcMap = new HashMap<>();
  
  public static List<SysDataTaskNpcName> taskNpcNameList = new ArrayList<>();
  
  public static Map<String, SysDataTaskNpcNameType> taskNpcNameTypeMap = new HashMap<>();
  
  public static Map<Integer, SysDataTaskFormula> taskFormulaMap = new HashMap<>();
  
  public static Map<Integer, SysDataTaskFight> taskFightMap = new HashMap<>();
  
  public static Map<Integer, SysDataTaskCondition> taskConditionMap = new HashMap<>();
  
  public static Map<Integer, SysDataTaskEvent> taskEventMap = new HashMap<>();
  
  public static List<SysDataTaskEventForeign> taskEventForeign = new ArrayList<>();
  
  public static Map<String, SysDataTaskReward> taskRewardMap = new HashMap<>();
  
  public static List<SysDataTaskRewardDynamic> taskRewardDynamicList = new ArrayList<>();
  
  public static Map<Integer, Integer> taskRewardDynamicNumList = new HashMap<>();
  
  public static Map<Integer, Double> taskRewardDynamicRateList = new HashMap<>();
  
  public static List<SysDataTaskForeign> taskRewardForeign = new ArrayList<>();
  
  public static List<Param> dragonRewardList = new ArrayList<>();
  
  public static List<SysDataPartyBandit> partyBanditList = new ArrayList<>();
  
  public static List<SysDataStarOfficer> starOfficerList = new ArrayList<>();
  
  public static List<SysDataWorldBoos> worldBoosList = new ArrayList<>();
  
  public static List<SysDataSealDemon> sealDemonList = new ArrayList<>();
  
  public static List<SysDataThreeScopeDemon> threeScopeDemonList = new ArrayList<>();
  
  public static List<SysDataTaskCrusadeRenegade> crusadeRenegadeList = new ArrayList<>();
  
  public static Map<Integer, Question> questionMap = new HashMap<>();
  
  public static Map<Integer, SysCountryQuestion> countryQuestion = new HashMap<>();
  
  public static Map<Integer, SysHallQuestion> hallQuestion = new HashMap<>();
  
  public static Map<Integer, SysProvinceQuestion> provinceQuestion = new HashMap<>();
  
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
      List<SysDataTaskMoAward> list = moAwardMap.get(Integer.valueOf(award.getDungeon()));
      if (list == null) {
        list = new ArrayList<>();
        moAwardMap.put(Integer.valueOf(award.getDungeon()), list);
      } 
      list.add(award);
    } 
  }
  
  private static void initTaskKj(InitService initService) {
    taskKjList = initService.initTaskKj();
    for (SysDataTaskKeju object : taskKjList) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskMain(InitService initService) {
    taskMainMap = initService.initTaskMain();
    SysDataTaskMain nextTask = null;
    for (Map.Entry<Integer, SysDataTaskMain> entry : taskMainMap.entrySet()) {
      nextTask = taskMainMap.get(Integer.valueOf(((SysDataTaskMain)entry.getValue()).getNextTaskSerial()));
      if (nextTask == null)
        continue; 
      nextTask.setPreviousId(((SysDataTaskMain)entry.getValue()).getSerial());
    } 
    for (Integer key : taskMainMap.keySet()) {
      SysDataTaskMain taskMain = taskMainMap.get(key);
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, taskMain);
      taskMap.put(Integer.valueOf(taskMain.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskSchool(InitService initService) {
    Map<Integer, SysDataTaskSchool> taskSchoolMap = initService.initTaskSchool();
    for (Integer key : taskSchoolMap.keySet()) {
      SysDataTaskSchool taskSchool = taskSchoolMap.get(key);
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, taskSchool);
      taskMap.put(Integer.valueOf(taskSchool.getSerial()), taskSet);
      String value = String.valueOf(taskSchool.getSerial());
      int type = Integer.valueOf(value.substring(1)).intValue();
      if (type == 3) {
        taskSchoolPurchase.add(taskSchool);
        continue;
      } 
      taskSchoolElse.add(taskSchool);
    } 
  }
  
  public static void initTaskTreasureMap(InitService initService) {
    taskTreasureMap = initService.initTaskTreasureMap();
    for (SysDataTaskTreasureMap object : taskTreasureMap) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskParty(InitService initService) {
    taskPartyList = initService.initTaskParty();
    for (SysDataTaskParty object : taskPartyList) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskLoop(InitService initService) {
    taskLoopList = initService.initTaskLoop();
    for (SysDataTaskLoop object : taskLoopList) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskTower(InitService initService) {
    taskTowerList = initService.initTaskTower();
    for (SysDataTaskTower object : taskTowerList) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskLookFor(InitService initService) {
    taskLookForList = initService.initTaskLookFor();
    for (SysDataTaskLookFor object : taskLookForList) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskXm(InitService initService) {
    taskXmList = initService.initTaskXm();
    for (SysDataTaskXm object : taskXmList) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskFive(InitService initService) {
    taskFiveList = initService.initTaskFive();
    for (SysDataTaskFive object : taskFiveList) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskCatchesGhosts(InitService initService) {
    taskCatchesGhosts = initService.initTaskCatchesGhosts();
    for (SysDataTaskCatchesGhosts object : taskCatchesGhosts) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskGodsCourt(InitService initService) {
    taskGodsCourts = initService.initTaskGodsCourt();
    for (SysDataTaskGodsCourt object : taskGodsCourts) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskCarnival(InitService initService) {
    taskCarnivalList = initService.initTaskCarnival();
    for (SysDataTaskCarnival object : taskCarnivalList) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskDragon(InitService initService) {
    taskDragonList = initService.initTaskDragon();
    for (SysDataTaskDragon object : taskDragonList) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskMo(InitService initService) {
    taskMoList = initService.initTaskMo();
    for (SysDataTaskMo object : taskMoList) {
      TaskSet taskSet = new TaskSet();
      BeanUtils.copyBeanProp(taskSet, object);
      taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
    } 
  }
  
  public static void initTaskNpc(InitService initService) {
    taskNpcMap = initService.initTaskNpc();
    for (SysDataTaskNpc value : taskNpcMap.values()) {
      if (value.getIsScene() == 1)
        DataCache.SCENE_TASK_NPC.put(Integer.valueOf(value.getSerial()), new Npc(value)); 
    } 
  }
  
  public static void initNpcName(InitService initService) {
    taskNpcNameList = initService.initNpcName();
  }
  
  public static void initNpcNameType(InitService initService) {
    taskNpcNameTypeMap = initService.initNpcNameType();
  }
  
  public static void initTaskFormula(InitService initService) {
    taskFormulaMap = initService.initTaskFormula();
  }
  
  public static void initTaskFight(InitService initService) {
    taskFightMap = initService.initTaskFight();
  }
  
  public static void initTaskCondition(InitService initService) {
    taskConditionMap = initService.initTaskCondition();
  }
  
  public static void initTaskEvent(InitService initService) {
    taskEventMap = initService.initTaskEvent();
  }
  
  public static void initTaskEventForeign(InitService initService) {
    taskEventForeign = initService.initTaskEventForeign();
  }
  
  public static void initTaskReward(InitService initService) {
    taskRewardMap = initService.initTaskReward();
  }
  
  public static void initTaskRewardDynamic(InitService initService) {
    taskRewardDynamicList = initService.initTaskRewardDynamic();
    for (SysDataTaskRewardDynamic obj : taskRewardDynamicList) {
      taskRewardDynamicNumList.put(Integer.valueOf(obj.getIdentify()), Integer.valueOf(CalculateUtils.calculate(obj.getNumCalculate()).intValue()));
      taskRewardDynamicRateList.put(Integer.valueOf(obj.getIdentify()), Double.valueOf(CalculateUtils.calculate(obj.getRateCalculate()).doubleValue()));
    } 
  }
  
  public static void initTaskRewardForeign(InitService initService) {
    taskRewardForeign = initService.initTaskRewardForeign();
  }
  
  public static void initWorldBoosList(InitService initService) {
    worldBoosList = initService.initWorldBoos();
  }
  
  public static void initPartyBanditList(InitService initService) {
    partyBanditList = initService.initPartyBanditList();
  }
  
  public static void initStarOfficerList(InitService initService) {
    starOfficerList = initService.initStarOfficerList();
  }
  
  public static void initSealDemonList(InitService initService) {
    sealDemonList = initService.initSealDemonList();
  }
  
  public static void initThreeScopeDemonList(InitService initService) {
    threeScopeDemonList = initService.initThreeScopeDemonList();
  }
  
  public static void initTaskSurround(InitService initService) {
    List<SysDataTaskSurround> list = initService.initTaskSurround();
    for (SysDataTaskSurround object : list) {
      boolean flag = false;
      if (TaskType.CRUSADE_RENEGADE.equals(object.getTaskType())) {
        SysDataTaskCrusadeRenegade renegade = new SysDataTaskCrusadeRenegade();
        BeanUtils.copyBeanProp(renegade, object);
        crusadeRenegadeList.add(renegade);
        flag = true;
      } 
      if (flag) {
        TaskSet taskSet = new TaskSet();
        BeanUtils.copyBeanProp(taskSet, object);
        taskMap.put(Integer.valueOf(object.getSerial()), taskSet);
      } 
    } 
  }
  
  public static void initCountryQuestion(InitService initService) {
    countryQuestion = initService.initCountryQuestionLib();
    for (Integer key : countryQuestion.keySet()) {
      SysCountryQuestion question = countryQuestion.get(key);
      questionMap.put(key, new Question(question.getSerial(), question.getAnswer()));
    } 
  }
  
  public static void initProvinceQuestion(InitService initService) {
    provinceQuestion = initService.initProvinceQuestion();
    for (Integer key : provinceQuestion.keySet()) {
      SysProvinceQuestion question = provinceQuestion.get(key);
      questionMap.put(key, new Question(question.getSerial(), question.getAnswer(), question.getType()));
    } 
  }
  
  public static void initHallQuestion(InitService initService) {
    hallQuestion = initService.initHallQuestion();
    for (Integer key : hallQuestion.keySet()) {
      SysHallQuestion question = hallQuestion.get(key);
      questionMap.put(key, new Question(question.getSerial(), question.getAnswer()));
    } 
  }
  
  public static void initDragonReward() {
    dragonRewardList.add(new Param(Integer.valueOf(21064), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21070), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21071), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21067), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21069), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21068), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21066), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21065), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21063), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21062), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21061), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21059), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21060), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21058), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21057), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
    dragonRewardList.add(new Param(Integer.valueOf(21056), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(0), null));
  }
}
