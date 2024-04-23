package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataTaskFive;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class FiveTaskHandler extends TaskHandler {
  public static final String TASK_DIST = "FIVE_NOM";
  
  private static final int NEED_RELIVE = 0;
  
  private static final int NEED_LEVEL = 33;
  
  private static final Float NEED_LIVENESS = Float.valueOf(0.0F);
  
  private static final String NEED_LIVENESS_TIPS = "我需要精力特别旺盛的人来做五环任务，请少侠今天的活跃度达到%d再来吧#<E:17>#";
  
  ActivityLivenessSet activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.FIVE_TASK);
  
  public TaskType getTaskType() {
    return TaskType.FIVE;
  }
  
  public boolean canResetTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public void acceptFiveTask(Role role, Npc npc) {
    String msg = DataGlobalFunc.needGradeJudge(0, 33, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
    if (msg != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(msg));
      return;
    } 
    if (this.activityService.getLiveness(role) < NEED_LIVENESS.floatValue()) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(String.format("我需要精力特别旺盛的人来做五环任务，请少侠今天的活跃度达到%d再来吧#<E:17>#", new Object[] { Integer.valueOf(NEED_LIVENESS.intValue()) })));
      return;
    } 
    boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.FIVE_TASK);
    if (!isHave) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("#<E:17>#英雄今天已经帮忙很多了,请明天再来吧"));
      return;
    } 
    randomAcceptTask(role);
    this.activityService.increaseFinishCount(role, ActivityType.FIVE_TASK, 1);
    MessagePusher.pushMessage(role, (Message)new RespMsg("成功领取五环任务"));
  }
  
  public void randomAcceptTask(Role role) {
    if (!canAcceptTask(role))
      return; 
    List<SysDataTaskFive> list = (List<SysDataTaskFive>)TaskDataPool.taskFiveList.stream().filter(e -> (e.getType() == 0)).collect(Collectors.toList());
    int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
    Integer taskId = Integer.valueOf(((SysDataTaskFive)list.get(random)).getSerial());
    TaskSet taskSet = this.taskService.getTaskSet(taskId.intValue());
    if (taskSet == null)
      return; 
    acceptTask(role, taskSet, -1);
  }
  
  private boolean canAcceptTask(Role role) {
    if (isAcceptedSchoolTask(role))
      return false; 
    if (isWaitGiveUpCD(role))
      return false; 
    return true;
  }
  
  public boolean isAcceptedSchoolTask(Role role) {
    List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.FIVE);
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
  }
  
  private void addActivity(Role role, TaskSet taskSet) {
    int sum = sum();
    int count = count(role);
    if (count < sum && 
      taskSet.getNeedCounter() == 1) {
      if (this.activityLivenessSet != null)
        activationTips(role, (int)this.activityLivenessSet.getPerLiveness()); 
      count++;
      role.getTaskProgress().getFinishRecord().setv("FIVE_NOM", Integer.valueOf(count));
      giveReward(role, taskSet, count, sum);
    } 
    if (count < sum)
      randomAcceptTask(role); 
  }
  
  private int sum() {
    return (int)(this.activityLivenessSet.getMaxLiveness() / this.activityLivenessSet.getPerLiveness());
  }
  
  private int count(Role role) {
    return role.getTaskProgress().getFinishRecord().getInt("FIVE_NOM", 0);
  }
  
  public void doAfterSubmit(Role role, TaskSet taskSet) {
    if (taskSet.getNeedCounter() == 1)
      addActivity(role, taskSet); 
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = this.taskService.getTaskInfo(role, taskSet);
    if (this.taskService.isMatchTaskType(taskSet, TaskType.FIVE)) {
      int count = count(role) + 1;
      taskInfo.setCounter((short)count);
    } 
    return taskInfo;
  }
  
  public boolean canGiveUpTask(Role role, TaskSet taskSet) {
    return false;
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    super.doFightFail(role, fightEndEvent);
  }
  
  public void giveReward(Role role, TaskSet taskSet, int cun, int sum) {
    if (cun <= sum) {
      this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf(calculate(role.getLevel().intValue(), role.getRelive().intValue(), cun, ItemClassify.RESTYPE_COIN)), "五环任务", true, false);
      this.roleService.addCurrency(role, Const.RESTYPE_SILVER, Long.valueOf(calculate(role.getLevel().intValue(), role.getRelive().intValue(), cun, ItemClassify.RESTYPE_SILVER)), "五环任务", true, false);
      List<Param> list = (List<Param>)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
      if (list != null)
        ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, " 五环单人", 0); 
    } 
  }
  
  private static long calculate(int grade, int rei, int cnt, ItemClassify type) {
    if (ItemClassify.RESTYPE_COIN == type) {
      double randomDouble = RandomUtil.getRandomDouble(Double.valueOf(70.0D), Double.valueOf(80.0D));
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(8000.0D * cnt + BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(850.0D / (80.0D + grade) * grade * randomDouble * cnt)).longValue())).longValue();
    } 
    if (ItemClassify.RESTYPE_SILVER == type) {
      double randomDouble = RandomUtil.getRandomDouble(Double.valueOf(13.0D), Double.valueOf(14.0D));
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(8000.0D * cnt + BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(850.0D / (80.0D + grade) * grade * randomDouble * cnt)).longValue())).longValue();
    } 
    return 0L;
  }
}
