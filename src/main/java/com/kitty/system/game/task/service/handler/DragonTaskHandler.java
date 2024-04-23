package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataTaskDragon;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DragonTaskHandler extends TaskHandler {
  Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final String TASK_DIST = "DRAGON";
  
  private static final int NEED_RELIVE = 0;
  
  private static final int NEED_LEVEL = 60;
  
  public TaskType getTaskType() {
    return TaskType.DRAGON;
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
    boolean flag = this.taskService.delItemTaskEvent(role, taskSet);
    if (flag)
      this.taskService.updateSingleTargetProgress(role, taskSet); 
  }
  
  public void acceptSchoolTask(Role role, Npc npc) {
    String content = getNpcContentNotAccept(role);
    if (content != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(content));
      return;
    } 
    acceptTask(role);
  }
  
  public String getNpcContentNotAccept(Role role) {
    String msg = DataGlobalFunc.needGradeJudge(0, 60, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
    if (msg != null)
      return msg; 
    boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.DRAGON);
    if (!isHave)
      return "你今日为端午节做了很多事，辛苦你了，前去休息吧"; 
    if (isWaitGiveUpCD(role))
      return "你刚才已经放弃端午节任务，还是休息一阵再来吧"; 
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
    Random random = new Random();
    List<SysDataTaskDragon> taskList = TaskDataPool.taskDragonList;
    int index = random.nextInt(taskList.size());
    Integer taskId = Integer.valueOf(((SysDataTaskDragon)taskList.get(index)).getSerial());
    TaskSet taskSet = this.taskService.getTaskSet(taskId.intValue());
    return taskSet;
  }
  
  private boolean canAcceptTask(Role role) {
    boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.DRAGON);
    if (!isHave)
      return false; 
    if (isAcceptedSchoolTask(role))
      return false; 
    if (isWaitGiveUpCD(role))
      return false; 
    return true;
  }
  
  public boolean isAcceptedSchoolTask(Role role) {
    List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.DRAGON);
    if (taskList == null || taskList.isEmpty())
      return false; 
    return true;
  }
  
  private boolean isWaitGiveUpCD(Role role) {
    long giveUpTime = role.getTask().getGiveUpTime(TaskType.DRAGON);
    if (giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L)
      return true; 
    return false;
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
    if (this.taskService.isMatchTaskType(taskSet, TaskType.DRAGON)) {
      int step = role.getTaskProgress().getFinishCounts().getInt(ActivityType.DRAGON.getActivityId(), 0) + 1;
      taskInfo.setCounter((short)step);
    } 
    return taskInfo;
  }
  
  public boolean canGiveUpTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public void endTaskTips(Role role) {}
  
  public void giveReward(Role role, TaskSet taskSet) {
    int step = role.getTaskProgress().getFinishCounts().getInt(ActivityType.DRAGON.getActivityId(), 0) + 1;
    this.roleService.addExp(role, calculate(role.getLevel().intValue(), role.getRelive().intValue(), 1, 1), "任务", true);
    Integer combatPet = role.getExtendBox().getCombatPet();
    if (combatPet.intValue() > 0) {
      Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
      this.petService.addExp(role, pet, calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), 1, 2), true);
    } 
    this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf(calculate(role.getLevel().intValue(), role.getRelive().intValue(), 1, 3)), "任务", true, false);
    addActivityCount(role, taskSet);
    List<Param> list = (List<Param>)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
    if (list != null)
      ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "", 0); 
    if (step >= ActivityType.DRAGON.getTotalCount()) {
      int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(TaskDataPool.dragonRewardList.size() - 1));
      Param param = TaskDataPool.dragonRewardList.get(round);
      boolean flag = SkillAi.canAffectOnPro(param.getR().intValue() / 1000.0D);
      if (flag)
        ((BagService)SpringUtils.getBean(BagService.class)).addGoods(role, param.getK().intValue(), param.getV().intValue(), 0, true, true, "端午最后一环"); 
    } 
  }
  
  private void addActivityCount(Role role, TaskSet taskSet) {
    if (this.activityService.isHaveRemainCount(role, ActivityType.DRAGON)) {
      int step = 1;
      this.activityService.addFinishCount(role, ActivityType.DRAGON, step);
      ActivityLivenessSet activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.DRAGON);
      if (activityLivenessSet != null && activityLivenessSet.getPerLiveness() < 1.0F) {
        int c = (int)(1.0F / activityLivenessSet.getPerLiveness());
        int anInt = role.getTaskProgress().getFinishCounts().getInt(ActivityType.DRAGON.getActivityId(), 1) - 1;
        if (anInt % c == 0)
          activationTips(role, step); 
      } 
    } 
  }
  
  private static long calculate(int grade, int rei, int cnt, int type) {
    double fold = ActivityType.DRAGON.getFold();
    double exp = 0.0D;
    if (type == 1) {
      exp = 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 28.13D * (0.2D + 0.1D * cnt + Math.pow(cnt, 0.45D)) * 1.5D;
    } else if (type == 2) {
      exp = 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 28.13D * (0.2D + 0.1D * cnt + Math.pow(cnt, 0.45D)) * 1.5D * 1.2D;
    } else if (type == 3) {
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(7109.847138286527D * (0.2D + 0.1D * cnt + Math.pow(cnt, 0.45D)))).longValue();
    } 
    return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0D) ? (exp * fold) : exp)).longValue();
  }
}
