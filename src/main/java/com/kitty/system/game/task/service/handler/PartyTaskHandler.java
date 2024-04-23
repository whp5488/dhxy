package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.party.event.QuitPartyEvent;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataTaskParty;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PartyTaskHandler extends TaskHandler {
  Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final String TASK_DIST = "PARTY";
  
  private static final int NEED_RELIVE = 0;
  
  private static final int NEED_LEVEL = 30;
  
  public TaskType getTaskType() {
    return TaskType.PARTY;
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
  
  public void acceptPartyTask(Role role, Npc npc) {
    String content = getNpcContentNotAccept(role);
    if (content != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(content));
      return;
    } 
    acceptTask(role);
  }
  
  public String getNpcContentNotAccept(Role role) {
    String msg = DataGlobalFunc.needGradeJudge(0, 30, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
    if (msg != null)
      return msg; 
    if (SpringUtils.getPartyService().getParty(role.getFaction()) == null)
      return "你还没有帮派哦#<E:51>#"; 
    ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
    boolean isHave = activityService.isHaveRemainCount(role, ActivityType.PARTY_TASK);
    if (!isHave)
      return "英雄今天已经打了很多，请明天再来吧"; 
    if (isWaitGiveUpCD(role))
      return "你刚才已经放弃帮派任务，还是休息一阵再来吧"; 
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
    List<SysDataTaskParty> list = TaskDataPool.taskPartyList;
    int random = (int)(Math.random() * list.size());
    Integer taskId = Integer.valueOf(((SysDataTaskParty)list.get(random)).getSerial());
    return this.taskService.getTaskSet(taskId.intValue());
  }
  
  private boolean canAcceptTask(Role role) {
    ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
    boolean isHave = activityService.isHaveRemainCount(role, ActivityType.PARTY_TASK);
    if (!isHave)
      return false; 
    if (isAcceptedSchoolTask(role))
      return false; 
    if (isWaitGiveUpCD(role))
      return false; 
    return true;
  }
  
  public boolean isAcceptedSchoolTask(Role role) {
    List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.PARTY);
    if (taskList == null || taskList.isEmpty())
      return false; 
    return true;
  }
  
  private boolean isWaitGiveUpCD(Role role) {
    long giveUpTime = role.getTask().getGiveUpTime(TaskType.PARTY);
    if (giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L)
      return true; 
    return false;
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
    if (this.taskService.isMatchTaskType(taskSet, TaskType.PARTY)) {
      int step = role.getTaskProgress().getFinishTallyCounts().getInt("PARTY", 1);
      if (step > 10) {
        step = 1;
        role.getTaskProgress().getFinishTallyCounts().setv("PARTY", Integer.valueOf(step));
      } 
      taskInfo.setCounter((short)step);
    } 
    return taskInfo;
  }
  
  public boolean canGiveUpTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public void giveReward(Role role, TaskSet taskSet) {
    int doStep = role.getTaskProgress().getFinishTallyCounts().getInt("PARTY", 1);
    this.roleService.addExp(role, calculate(role.getLevel().intValue(), role.getRelive().intValue(), doStep, 1), "帮派任务", true);
    Integer combatPet = role.getExtendBox().getCombatPet();
    if (combatPet.intValue() > 0) {
      Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
      this.petService.addExp(role, pet, calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), doStep, 2), true);
    } 
    long calculate = 0L;
    if (taskSet.getTaskReward() != null) {
      calculate = calculate(role.getLevel().intValue(), role.getRelive().intValue(), doStep, 3);
      this.roleService.addCurrency(role, Const.RESTYPE_TRIBUTE, Long.valueOf(calculate), "帮派任务", false, false);
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "你获得了帮贡#<G>" + calculate + "#", true));
    } 
    if (doStep % 10 == 0)
      this.roleService.addCurrency(role, Const.RES_TYPE_ACHIEVEMENT, Long.valueOf(3000L), "帮派任务", true, false); 
    addActivityCount(role, taskSet, calculate);
    if (doStep < ActivityType.PARTY_TASK.getTotalCount())
      acceptTask(role); 
  }
  
  private void addActivityCount(Role role, TaskSet taskSet, long calculate) {
    int step = 1;
    if (this.activityService.isHaveRemainCount(role, ActivityType.PARTY_TASK)) {
      ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
      activityService.addFinishCount(role, ActivityType.PARTY_TASK, step);
      int doStep = activityService.getFinishCount(role, ActivityType.PARTY_TASK, 1);
      if (doStep % 2 == 0)
        activationTips(role, step); 
      int count = role.getTaskProgress().getFinishTallyCounts().getInt("PARTY", 1);
      role.getTaskProgress().getFinishTallyCounts().setv("PARTY", Integer.valueOf(count + 1));
      SpringUtils.getPartyService().updatePartyTask(role, doStep, calculate);
    } 
  }
  
  private static long calculate(int grade, int rei, int cnt, int type) {
    double fold = ActivityType.PARTY_TASK.getFold();
    if (type == 1) {
      double exp = 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 25.59D * (0.1D + 0.13D * cnt + Math.pow(cnt, 0.35D)) * 1.5D;
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0D) ? (exp * fold) : exp)).longValue();
    } 
    if (type == 2) {
      double exp = 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 25.59D * (0.1D + 0.13D * cnt + Math.pow(cnt, 0.35D)) * 1.5D * 1.2D;
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0D) ? (exp * fold) : exp)).longValue();
    } 
    if (type == 3)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Integer.valueOf(69 + (cnt - 1) * 18)).longValue() * 2L; 
    return 0L;
  }
  
  @EventHandler({EventType.QUIT_PARTY})
  public void handleQuitPartyEvent(QuitPartyEvent quitPartyEvent) {
    Role role = quitPartyEvent.getRole();
    List<SysDataTaskParty> list = TaskDataPool.taskPartyList;
    for (SysDataTaskParty obj : list) {
      if (role.getTask().isAccepted(obj.getSerial()))
        delTask(role, this.taskService.getTaskSet(obj.getSerial())); 
    } 
    role.save();
  }
}
