package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.init.domain.SysDataTaskTreasureMap;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TreasureMapHandler extends TaskHandler {
  Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final int NEED_RELIVE = 0;
  
  private static final int NEED_LEVEL = 33;
  
  public TaskType getTaskType() {
    return TaskType.TREASURE_MAP;
  }
  
  public void canSubmitTask(Role role, TaskSet taskSet) {
    addActivityCount(role, taskSet);
  }
  
  public void acceptTreasureMapTask(Role role, Npc npc) {
    String content = getNpcContentNotAccept(role);
    if (content != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(content));
      return;
    } 
    acceptTask(role);
  }
  
  public String getNpcContentNotAccept(Role role) {
    String msg = DataGlobalFunc.needGradeJudge(0, 33, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
    if (msg != null)
      return msg; 
    ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
    boolean isHave = activityService.isHaveRemainCount(role, ActivityType.TREASURE_MAP_TASK);
    if (!isHave)
      return "英雄今天已经打了很多，请明天再来吧"; 
    if (isWaitGiveUpCD(role))
      return "你刚才已经放弃宝图任务，还是休息一阵再来吧"; 
    return null;
  }
  
  public void acceptTask(Role role) {
    if (!canAcceptTask(role))
      return; 
    TaskSet randTaskSet = getRandomSchoolTask(role);
    if (randTaskSet == null)
      return; 
    acceptTask(role, randTaskSet, -1);
  }
  
  private TaskSet getRandomSchoolTask(Role role) {
    List<SysDataTaskTreasureMap> list = TaskDataPool.taskTreasureMap;
    int random = (int)(Math.random() * list.size());
    Integer taskId = Integer.valueOf(((SysDataTaskTreasureMap)list.get(random)).getSerial());
    return this.taskService.getTaskSet(taskId.intValue());
  }
  
  private boolean canAcceptTask(Role role) {
    ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
    boolean isHave = activityService.isHaveRemainCount(role, ActivityType.TREASURE_MAP_TASK);
    if (!isHave)
      return false; 
    if (isAcceptedSchoolTask(role))
      return false; 
    if (isWaitGiveUpCD(role))
      return false; 
    return true;
  }
  
  public boolean isAcceptedSchoolTask(Role role) {
    List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.TREASURE_MAP);
    if (taskList == null || taskList.isEmpty())
      return false; 
    return true;
  }
  
  private boolean isWaitGiveUpCD(Role role) {
    long giveUpTime = role.getTask().getGiveUpTime(TaskType.TREASURE_MAP);
    if (giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L)
      return true; 
    return false;
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
    if (this.taskService.isMatchTaskType(taskSet, TaskType.TREASURE_MAP)) {
      int doStep = this.activityService.getFinishCount(role, ActivityType.TREASURE_MAP_TASK, 1);
      taskInfo.setCounter((short)doStep);
    } 
    return taskInfo;
  }
  
  public boolean canGiveUpTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public void giveReward(Role role, TaskSet taskSet) {
    addActivityCount(role, taskSet);
    int doStep = this.activityService.getFinishCount(role, ActivityType.TREASURE_MAP_TASK, 1);
    if (doStep <= ActivityType.TREASURE_MAP_TASK.getTotalCount())
      acceptTask(role); 
    List<Param> list = (List<Param>)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
    ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "藏宝图", 0);
  }
  
  private void addActivityCount(Role role, TaskSet taskSet) {
    int step = 1;
    if (this.activityService.isHaveRemainCount(role, ActivityType.TREASURE_MAP_TASK)) {
      ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
      activityService.addFinishCount(role, ActivityType.TREASURE_MAP_TASK, step);
      activationTips(role, step);
    } 
  }
}
