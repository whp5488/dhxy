
package com.kitty.system.game.activity.service;

import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.game.task.service.handler.TaskActivityHandler;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.game.activity.model.product.ActivityLivenessRewardSet;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.task.message.vo.ActivityData;
import com.kitty.system.game.task.message.RespLivenessInfo;
import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.game.vo.utils.Const;
import java.util.ArrayList;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.player.bean.TaskProgress;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.team.domain.Member;
import java.util.List;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.team.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class ActivityService
{
    @Autowired
    private RoleService roleService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TaskService taskService;
    
    public void handleFightEnd(final FightEndEvent fightEndEvent) {
        ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleFightEnd(fightEndEvent));
    }
    
    public void handleLeaveTeam(final Role role, final boolean leader, final Team team) {
        ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleTeamLeave(role, leader, team));
    }
    
    public void handleTeamLeaveMoment(final Role role) {
        ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleTeamLeaveMoment(role));
    }
    
    public void handleTeamJoinEvent(final Role role) {
        ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleTeamJoinEvent(role));
    }
    
    public void handleTeamChangeLeader(final Role role, final List<Member> memberList) {
        ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleTeamChangeLeader(role, memberList));
    }
    
    public void handleLogout(final Role role) {
        ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleLogout(role));
    }
    
    public void handleLogin(final Role role) {
        ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleLogin(role));
    }
    
    public void handleHeartBeat(final Role role, final long prevHeartTime) {
        ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleHeartBeat(role, prevHeartTime));
    }
    
    public void handleRoleExpire(final Role role) {
        ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> activityHandler.handleRoleExpire(role));
    }
    
    public void addFinishCount(final Role role, final ActivityType activityType, final int addCount) {
        if (role != null) {
            final TaskProgress taskProgress = role.getTaskProgress();
            final int activityCount = taskProgress.getFinishCounts().getInt(activityType.getActivityId(), 0);
            final int count = activityCount + addCount;
            taskProgress.getFinishCounts().setv(activityType.getActivityId(), count);
            role.save();
        }
    }
    
    public void increaseFinishCount(final Role role, final ActivityType activityType, final int addCount) {
        final int activityCount = role.getTaskProgress().getFinishCounts().getInt(activityType.getActivityId(), 0);
        final int count = activityCount + addCount;
        (role.getTaskProgress().getFinishCounts()).put(activityType.getActivityId(), count);
    }
    
    public void teamLeaderNice(final Role role, final TaskSet taskSet, final String activityId, final long nice) {
        final ArrayList<Role> objects = new ArrayList<Role>();
        final ArrayList<Role> objectsNot = new ArrayList<Role>();
        this.teamService.memberHandleThreadLocal(role, memberRole -> {
            if (memberRole.getTaskProgress().getFinishCounts().getInt(activityId + taskSet.getSerial(), 0) == 1) {
                objects.add(memberRole);
            }
            else {
                objectsNot.add(memberRole);
            }
            return;
        });
        if (objectsNot.size() > 0) {
            for (final Role object : objects) {
                final long num = this.addNice(object, nice);
                if (num > 0L) {
                    object.save();
                    MessagePusher.pushMessage(object, new RespNotifyMiscEx(object.getId(), "获得好心值#<IR" + Const.RESTYPE_NICE + ">#" + MoneyUtil.colours(num), true));
                }
            }
        }
    }
    
    public long addNice(final Role role, final long nice) {
        final int anInt = role.getTaskProgress().getEveryDayUpperLimit().getInt(Const.RESTYPE_NICE.toString(), 0);
        if (anInt < 300L) {
            final long num = (anInt + nice > 300L) ? (300L - nice) : nice;
            this.roleService.addCurrency(role, Const.RESTYPE_NICE, num, "活动", false, false);
            role.getTaskProgress().getEveryDayUpperLimit().setv(Const.RESTYPE_NICE.toString(), anInt + num);
            return num;
        }
        return 0L;
    }
    
    public boolean isHaveRemainCount(final Role role, final ActivityType activityType) {
        final int count = this.getRemainCount(role, activityType);
        return count > 0;
    }
    
    public boolean isLevel(final Role role, final int level) {
        return role.getLevel() > level;
    }
    
    public int getRemainCount(final Role role, final ActivityType activityType) {
        final int finishCount = this.getFinishCount(role, activityType, 0);
        final int totalCount = activityType.getTotalCount();
        if (finishCount > totalCount) {
            return 0;
        }
        return totalCount - finishCount;
    }
    
    public int getFinishCount(final Role role, final ActivityType activityType, final int dft) {
        return role.getTaskProgress().getFinishCounts().getInt(activityType.getActivityId(), dft);
    }
    
    public RespLivenessInfo getRespLivenessInfo(final Role role) {
        final RespLivenessInfo respLivenessInfo = new RespLivenessInfo(this.getActivityData(role), this.getRewardStatus(role));
        return respLivenessInfo;
    }
    
    private byte getRewardStatus(final Role role) {
        final StringBuffer sb = new StringBuffer();
        for (final Integer key : ActivityDataPool.activityLivenessRewardSetMap.keySet()) {
            if (this.isFetchLivenessReward(role, key)) {
                sb.append('1');
            }
            else {
                sb.append("0");
            }
        }
        return Byte.parseByte(sb.reverse().toString(), 2);
    }
    
    private List<ActivityData> getActivityData(final Role role) {
        final List<ActivityType> activityTypeList = ActivityType.getActivityLiveness();
        final List<ActivityData> activityDataList = new ArrayList<ActivityData>(activityTypeList.size());
        for (final ActivityType activityType : activityTypeList) {
            final ActivityData activityData = new ActivityData();
            activityData.setSerial(Short.valueOf(activityType.getActivityId()));
            final int finishCount = this.getFinishCount(role, activityType, 0);
            activityData.setNum((short)finishCount);
            final ActivityLivenessSet activityLivenessSet = ActivityDataPool.activityLivenessSetMapByType.get(activityType);
            if (activityLivenessSet != null) {
                final int liveness = (int)(finishCount * activityLivenessSet.getPerLiveness());
                activityData.setLiveness((byte)((liveness > activityLivenessSet.getMaxLiveness()) ? activityLivenessSet.getMaxLiveness() : liveness));
            }
            if (activityType == ActivityType.LOOP && activityData.getNum() > 0) {
                activityData.setLiveness((byte)1);
            }
            if (activityType == ActivityType.FIVE_TASK && activityData.getNum() > 0) {
                final byte Liveness = (byte)role.getTaskProgress().getFinishRecord().getInt("FIVE_NOM", 0);
                activityData.setLiveness(Liveness);
            }
            activityDataList.add(activityData);
        }
        return activityDataList;
    }
    
    public void fetchLivenessReward(final Role role, final int index) {
        final boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
        if (!clicker) {
            return;
        }
        final ActivityLivenessRewardSet activityLivenessRewardSet = this.getActivityLivenessRewardSet(index);
        if (activityLivenessRewardSet == null) {
            return;
        }
        if (this.isFetchLivenessReward(role, index)) {
            return;
        }
        if (this.getLiveness(role) < activityLivenessRewardSet.getLivenessCount()) {
            return;
        }
        role.getTaskProgress().getLivenessRewardFetchStatus().put(index, true);
        final List<Param> list = activityLivenessRewardSet.getList();
        SpringUtils.getBean(BagService.class).sendReward(role, list, "活跃度", 0);
        role.save();
        final RespLivenessInfo respLivenessInfo = this.getRespLivenessInfo(role);
        MessagePusher.pushMessage(role, respLivenessInfo);
    }
    
    private ActivityLivenessRewardSet getActivityLivenessRewardSet(final int index) {
        return ActivityDataPool.activityLivenessRewardSetMap.get(index);
    }
    
    public int getLiveness(final Role role) {
        int liveness = 0;
        final List<ActivityData> activityDataList = this.getActivityData(role);
        for (final ActivityData datum : activityDataList) {
            liveness += datum.getLiveness();
        }
        return liveness;
    }
    
    private boolean isFetchLivenessReward(final Role role, final int index) {
        final Boolean isFetched = role.getTaskProgress().getLivenessRewardFetchStatus().get(index);
        return isFetched != null && isFetched;
    }
    
    public void dailyResetActivity(final Role role) {
        role.getTaskProgress().dailyReset(this.roleService.isCanWeeklyReset(role));
        this.resetActivityTask(role);
    }
    
    private void resetActivityTask(final Role role) {
        ActivityDataPool.activityHandlers.stream().forEach(activityHandler -> {
            if (activityHandler instanceof TaskActivityHandler) {
                final TaskSet taskSet = ((TaskActivityHandler)activityHandler).getTaskSetByType(role);
                if (taskSet != null) {
                    role.getTask().resetTask(taskSet.getSerial());
                }
            }
            return;
        });
        role.save();
    }
}
