package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataTaskLoop;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoopTaskHandler extends TaskHandler {
  private static final int NEED_RELIVE = 1;
  
  private static final int NEED_LEVEL = 80;
  
  Logger logger = LoggerFactory.getLogger(getClass());
  
  public TaskType getTaskType() {
    return TaskType.LOOP;
  }
  
  public boolean resetTask(Role role, TaskSet taskSet) {
    if (this.roleService.isCanWeeklyReset(role))
      return true; 
    return false;
  }
  
  public boolean canResetTask(Role role, TaskSet taskSet) {
    if (this.roleService.isCanWeeklyReset(role))
      return true; 
    return false;
  }
  
  public void canSubmitTask(Role role, TaskSet taskSet) {
    int submitNpc = taskSet.getSubmitNpc();
    if (submitNpc > 0) {
      Npc npc = this.mapService.getNpc(submitNpc);
      if (npc != null) {
        boolean b = this.npcService.canClickNpc(role, npc);
        if (!b) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("请前往" + npc.getName() + "处提交任务"));
          return;
        } 
      } 
    } 
    int submitCondition = taskSet.getSubmitCondition();
    if (submitCondition > 0 && !this.taskService.condition(role, taskSet, (byte)1))
      return; 
    this.taskService.updateSingleTargetProgress(role, taskSet);
    addActivityCount(role, taskSet);
  }
  
  public void acceptLoopTask(Role role, Npc npc) {
    String msg = DataGlobalFunc.needGradeJudge(1, 80, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
    if (msg != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(msg));
      return;
    } 
    String content = getNpcContentNotAccept(role);
    if (content != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(content));
      return;
    } 
    int count = role.getTaskProgress().getLoop() - 1;
    role.getTaskProgress().setLoop((count < 0) ? 0 : count);
    role.getTaskProgress().getFinishCounts().setv(ActivityType.LOOP.getActivityId(), Integer.valueOf(0));
    acceptTask(role);
  }
  
  public String getNpcContentNotAccept(Role role) {
    if (!canAcceptTask(role))
      return "少侠的任务还没有完成啊#<E:32>#，完成后再来找我吧"; 
    if (role.getTaskProgress().getLoop() <= 0)
      return "英雄本周已经打了很多，请下周再来吧"; 
    if (isWaitGiveUpCD(role))
      return "你刚才已经放弃200环任务，还是休息一阵再来吧"; 
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
    List<SysDataTaskLoop> list = (List<SysDataTaskLoop>)TaskDataPool.taskLoopList.stream().filter(e -> (e.getKind() == 0)).collect(Collectors.toList());
    int random = (int)(Math.random() * list.size());
    Integer taskId = Integer.valueOf(((SysDataTaskLoop)list.get(random)).getSerial());
    return this.taskService.getTaskSet(taskId.intValue());
  }
  
  private boolean canAcceptTask(Role role) {
    ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
    boolean isHave = activityService.isHaveRemainCount(role, ActivityType.LOOP);
    if (!isHave)
      return false; 
    if (isAcceptedSchoolTask(role))
      return false; 
    if (isWaitGiveUpCD(role))
      return false; 
    return true;
  }
  
  public boolean isAcceptedSchoolTask(Role role) {
    List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.LOOP);
    if (taskList == null || taskList.isEmpty())
      return false; 
    return true;
  }
  
  private boolean isWaitGiveUpCD(Role role) {
    long giveUpTime = role.getTask().getGiveUpTime(TaskType.LOOP);
    if (giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L)
      return true; 
    return false;
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
    if (this.taskService.isMatchTaskType(taskSet, TaskType.LOOP)) {
      int doStep = this.activityService.getFinishCount(role, ActivityType.LOOP, 0);
      taskInfo.setCounter((short)doStep);
    } 
    return taskInfo;
  }
  
  public boolean canGiveUpTask(Role role, TaskSet taskSet) {
    return false;
  }
  
  public void giveReward(Role role, TaskSet taskSet) {
    int doStep = this.activityService.getFinishCount(role, ActivityType.LOOP, 1);
    if (taskSet.getTaskReward() != null) {
      this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf(calculate(role.getLevel().intValue(), role.getRelive().intValue(), doStep, Const.RESTYPE_COIN.intValue())), "任务奖励", true, false);
      this.roleService.addCurrency(role, Const.RESTYPE_SILVER, Long.valueOf(calculate(role.getLevel().intValue(), role.getRelive().intValue(), doStep, Const.RESTYPE_SILVER.intValue())), "任务奖励", true, false);
    } 
    if (doStep < ActivityType.LOOP.getTotalCount())
      acceptTask(role); 
  }
  
  private void addActivityCount(Role role, TaskSet taskSet) {
    int step = 1;
    if (this.activityService.isHaveRemainCount(role, ActivityType.LOOP))
      this.activityService.addFinishCount(role, ActivityType.LOOP, step); 
  }
  
  private static long calculate(int grade, int rei, int cnt, int type) {
    double fold = ActivityType.LOOP.getFold();
    double exp = 0.0D;
    if (type == Const.RESTYPE_SILVER.intValue()) {
      exp = 30000.0D + Math.floor((cnt - 1) / 10.0D) * 2760.0D + (cnt - Math.floor((cnt - 1) / 10.0D) * 10.0D - 1.0D) * 4166.0D;
    } else if (type == Const.RESTYPE_COIN.intValue()) {
      exp = 30000.0D + Math.floor((cnt - 1) / 10.0D) * 2760.0D + (cnt - Math.floor((cnt - 1) / 10.0D) * 10.0D - 1.0D) * 4166.0D;
    } 
    return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0D) ? (exp * fold) : exp)).longValue();
  }
}
