package com.kitty.system.game.activity.service;

import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessRewardSet;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.player.bean.TaskProgress;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.task.message.RespLivenessInfo;
import com.kitty.system.game.task.message.vo.ActivityData;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.task.service.handler.TaskActivityHandler;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.vo.Param;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
  @Autowired
  private RoleService roleService;
  
  @Autowired
  private TeamService teamService;
  
  @Autowired
  private TaskService taskService;
  
  public void handleFightEnd(FightEndEvent fightEndEvent) {
    ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleFightEnd(fightEndEvent));
  }
  
  public void handleLeaveTeam(Role role, boolean leader, Team team) {
    ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleTeamLeave(role, leader, team));
  }
  
  public void handleTeamLeaveMoment(Role role) {
    ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleTeamLeaveMoment(role));
  }
  
  public void handleTeamJoinEvent(Role role) {
    ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleTeamJoinEvent(role));
  }
  
  public void handleTeamChangeLeader(Role role, List<Member> memberList) {
    ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleTeamChangeLeader(role, memberList));
  }
  
  public void handleLogout(Role role) {
    ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleLogout(role));
  }
  
  public void handleLogin(Role role) {
    ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleLogin(role));
  }
  
  public void handleHeartBeat(Role role, long prevHeartTime) {
    ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleHeartBeat(role, prevHeartTime));
  }
  
  public void handleRoleExpire(Role role) {
    ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleRoleExpire(role));
  }
  
  public void addFinishCount(Role role, ActivityType activityType, int addCount) {
    if (role != null) {
      TaskProgress taskProgress = role.getTaskProgress();
      int activityCount = taskProgress.getFinishCounts().getInt(activityType.getActivityId(), 0);
      int count = activityCount + addCount;
      taskProgress.getFinishCounts().setv(activityType.getActivityId(), Integer.valueOf(count));
      role.save();
    } 
  }
  
  public void increaseFinishCount(Role role, ActivityType activityType, int addCount) {
    int activityCount = role.getTaskProgress().getFinishCounts().getInt(activityType.getActivityId(), 0);
    int count = activityCount + addCount;
    role.getTaskProgress().getFinishCounts().put(activityType.getActivityId(), Integer.valueOf(count));
  }
  
  public void teamLeaderNice(Role role, TaskSet taskSet, String activityId, long nice) {
    ArrayList<Role> objects = new ArrayList<>();
    ArrayList<Role> objectsNot = new ArrayList<>();
    this.teamService.memberHandleThreadLocal(role, memberRole -> {
          if (memberRole.getTaskProgress().getFinishCounts().getInt(activityId + taskSet.getSerial(), 0) == 1) {
            objects.add(memberRole);
          } else {
            objectsNot.add(memberRole);
          } 
        });
    if (objectsNot.size() > 0)
      for (Role object : objects) {
        long num = addNice(object, nice);
        if (num > 0L) {
          object.save();
          MessagePusher.pushMessage(object, (Message)new RespNotifyMiscEx(object.getId(), "获得好心值#<IR" + Const.RESTYPE_NICE + ">#" + MoneyUtil.colours(Long.valueOf(num)), true));
        } 
      }  
  }
  
  public long addNice(Role role, long nice) {
    int anInt = role.getTaskProgress().getEveryDayUpperLimit().getInt(Const.RESTYPE_NICE.toString(), 0);
    if (anInt < 300L) {
      long num = (anInt + nice > 300L) ? (300L - nice) : nice;
      this.roleService.addCurrency(role, Const.RESTYPE_NICE, Long.valueOf(num), "活动", false, false);
      role.getTaskProgress().getEveryDayUpperLimit().setv(Const.RESTYPE_NICE.toString(), Long.valueOf(anInt + num));
      return num;
    } 
    return 0L;
  }
  
  public boolean isHaveRemainCount(Role role, ActivityType activityType) {
    int count = getRemainCount(role, activityType);
    if (count <= 0)
      return false; 
    return true;
  }
  
  public boolean isLevel(Role role, int level) {
    if (role.getLevel().intValue() <= level)
      return false; 
    return true;
  }
  
  public int getRemainCount(Role role, ActivityType activityType) {
    int finishCount = getFinishCount(role, activityType, 0);
    int totalCount = activityType.getTotalCount();
    if (finishCount > totalCount)
      return 0; 
    return totalCount - finishCount;
  }
  
  public int getFinishCount(Role role, ActivityType activityType, int dft) {
    return role.getTaskProgress().getFinishCounts().getInt(activityType.getActivityId(), dft);
  }
  
  public RespLivenessInfo getRespLivenessInfo(Role role) {
    RespLivenessInfo respLivenessInfo = new RespLivenessInfo(getActivityData(role), getRewardStatus(role));
    return respLivenessInfo;
  }
  
  private byte getRewardStatus(Role role) {
    StringBuffer sb = new StringBuffer();
    for (Integer key : ActivityDataPool.activityLivenessRewardSetMap.keySet()) {
      if (isFetchLivenessReward(role, key.intValue())) {
        sb.append('1');
        continue;
      } 
      sb.append("0");
    } 
    return Byte.parseByte(sb.reverse().toString(), 2);
  }
  
  private List<ActivityData> getActivityData(Role role) {
    List<ActivityType> activityTypeList = ActivityType.getActivityLiveness();
    List<ActivityData> activityDataList = new ArrayList<>(activityTypeList.size());
    for (ActivityType activityType : activityTypeList) {
      ActivityData activityData = new ActivityData();
      activityData.setSerial(Short.valueOf(activityType.getActivityId()).shortValue());
      int finishCount = getFinishCount(role, activityType, 0);
      activityData.setNum((short)finishCount);
      ActivityLivenessSet activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(activityType);
      if (activityLivenessSet != null) {
        int liveness = (int)(finishCount * activityLivenessSet.getPerLiveness());
        activityData.setLiveness((byte)((liveness > activityLivenessSet.getMaxLiveness()) ? activityLivenessSet.getMaxLiveness() : liveness));
      } 
      if (activityType == ActivityType.LOOP && activityData.getNum() > 0)
        activityData.setLiveness((byte)1); 
      if (activityType == ActivityType.FIVE_TASK && activityData.getNum() > 0) {
        byte Liveness = (byte)role.getTaskProgress().getFinishRecord().getInt("FIVE_NOM", 0);
        activityData.setLiveness(Liveness);
      } 
      activityDataList.add(activityData);
    } 
    return activityDataList;
  }
  
  public void fetchLivenessReward(Role role, int index) {
    boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
    if (!clicker)
      return; 
    ActivityLivenessRewardSet activityLivenessRewardSet = getActivityLivenessRewardSet(index);
    if (activityLivenessRewardSet == null)
      return; 
    if (isFetchLivenessReward(role, index))
      return; 
    if (getLiveness(role) < activityLivenessRewardSet.getLivenessCount())
      return; 
    role.getTaskProgress().getLivenessRewardFetchStatus().put(Integer.valueOf(index), Boolean.valueOf(true));
    List<Param> list = activityLivenessRewardSet.getList();
    ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "活跃度", 0);
    role.save();
    RespLivenessInfo respLivenessInfo = getRespLivenessInfo(role);
    MessagePusher.pushMessage(role, (Message)respLivenessInfo);
  }
  
  private ActivityLivenessRewardSet getActivityLivenessRewardSet(int index) {
    return (ActivityLivenessRewardSet)ActivityDataPool.activityLivenessRewardSetMap.get(Integer.valueOf(index));
  }
  
  public int getLiveness(Role role) {
    int liveness = 0;
    List<ActivityData> activityDataList = getActivityData(role);
    for (ActivityData datum : activityDataList)
      liveness += datum.getLiveness(); 
    return liveness;
  }
  
  private boolean isFetchLivenessReward(Role role, int index) {
    Boolean isFetched = (Boolean)role.getTaskProgress().getLivenessRewardFetchStatus().get(Integer.valueOf(index));
    if (isFetched == null)
      return false; 
    return isFetched.booleanValue();
  }
  
  public void dailyResetActivity(Role role) {
    role.getTaskProgress().dailyReset(this.roleService.isCanWeeklyReset(role));
    resetActivityTask(role);
  }
  
  private void resetActivityTask(Role role) {
    ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> {
          if (activityHandler instanceof TaskActivityHandler) {
            TaskSet taskSet = ((TaskActivityHandler)activityHandler).getTaskSetByType(role);
            if (taskSet == null)
              return; 
            role.getTask().resetTask(taskSet.getSerial());
          } 
        });
    role.save();
  }
}
