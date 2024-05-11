
package com.kitty.system.game.task.service.handler;

import java.io.Serializable;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.init.domain.SysDataTaskLookFor;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.activity.model.Dugeon;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.vo.Npc;
import javax.annotation.PostConstruct;
import java.util.Iterator;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.task.TaskType;
import org.springframework.stereotype.Component;

@Component
public class LookForTaskHandler extends TaskHandler
{
    private static final int FIRST_TASK_ID = 1600001;
    private static final int MIN_TEAM_COUNT = 1;
    private static final int MIN_LEVEL = 40;
    private static final String FDUGEON_NAME = "寻芳";
    private static final int ENTER_MAP = 5001;
    private static final int ENTER_X = 52;
    private static final int ENTER_Y = 11;
    private static final int EXIT_MAP = 1011;
    private static final int EXIT_X = 120;
    private static final int EXIT_Y = 102;
    private static final int LOOK_FOR_NICE = 10;
    
    @Override
    public TaskType getTaskType() {
        return TaskType.LOOK_FOR;
    }
    
    @Override
    public boolean resetTask(final Role role, final TaskSet taskSet) {
        return true;
    }
    
    @PostConstruct
    public void init() {
        for (final int mapId : Const.DUGEON_LOOK_FOR_MAP) {
            MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(mapId), role -> DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), 0L));
        }
    }
    
    public void createLookForDugeon(final Role role, final Npc npc) {
        final String talk = this.checkCreateDugeon(role);
        if (talk != null) {
            this.npcService.sendNpcContent(role, npc, "", talk);
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        final Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
        this.createDungeon(team);
        final TaskSet taskSet = this.taskService.getTaskSet(1600001);
        this.teamService.memberHandleThreadLocal(team, memberRole -> this.acceptTask(memberRole, taskSet, -1));
        this.doEnterDugeon(leaderRole);
    }
    
    private String checkCreateDugeon(final Role role) {
        if (!this.teamService.isTeamLeader(role)) {
            return String.format("要风流得先保证安全呐，请至少组满%d位大侠再来挑战吧!#<E:99>#", 1);
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        String names = this.teamService.getNotInTeam(team);
        if (names != null) {
            return "队伍中#<Y>%s#处于暂离状态，不能创建副本";
        }
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            if (member != null) {
                if (member.isInTeam()) {
                    Role memberRole = SpringUtils.getRoleService().getOnlinePlayer(member.getRoleId());
                    if (memberRole == null) {}
                }
            }
        }
        if (role.getLevel() < 40) {
            return String.format("你等级不足#<R>%s#级，无法创建该副本。", 40);
        }
        if (this.teamService.getTeamCount(role) < 1) {
            return String.format("#<R>%s#中妖怪众多，少于#<R>%d#人的队伍进去是非常危险的。", "寻芳", 1);
        }
        names = this.teamService.checkMember(role, memberRole -> memberRole.getLevel() < 40);
        if (names != null) {
            return String.format("#<Y>%s#的等级不足#<Y>%d#级，无法创建该副本。", names, 40);
        }
        return null;
    }
    
    private boolean isDugeonMember(final Role role) {
        return role != null && DataCache.ROLE_DUNGEON.containsKey(role.getId());
    }
    
    @Override
    public void doAfterSubmit(final Role role, final TaskSet taskSet) {
        super.doAfterSubmit(role, taskSet);
        this.taskService.updateTargetProgress(role, taskSet);
        this.addActivityCount(role, taskSet);
    }
    
    private void addActivityCount(final Role role, final TaskSet taskSet) {
        if (this.activityService.isHaveRemainCount(role, ActivityType.LOOK_FOR_DUGEON) && role.getTaskProgress().getFinishRecord().getInt(ActivityType.LOOK_FOR_DUGEON.getActivityId() + taskSet.getSerial(), 0) == 0) {
            role.getTaskProgress().getFinishRecord().setv(ActivityType.LOOK_FOR_DUGEON.getActivityId() + taskSet.getSerial(), 1);
            this.activityService.increaseFinishCount(role, ActivityType.LOOK_FOR_DUGEON, 1);
            final int totalCount = ActivityType.LOOK_FOR_DUGEON.getTotalCount();
            final int finishCount = this.activityService.getFinishCount(role, ActivityType.LOOK_FOR_DUGEON, 1);
            if (totalCount - 1 == finishCount || finishCount == 1) {
                final ActivityLivenessSet activityLivenessSet = ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.LOOK_FOR_DUGEON);
                if (activityLivenessSet != null) {
                    this.activationTips(role, (int)activityLivenessSet.getPerLiveness());
                }
            }
        }
    }
    
    private boolean isAcceptedTask(final Role role) {
        return this.getTaskSetByType(role) != null;
    }
    
    private void doEnterDugeon(final Role role) {
        final ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId(5001);
        reqTelePort.setX(52);
        reqTelePort.setY(11);
        this.mapService.changeMap(role, reqTelePort, null);
    }
    
    public void startFight(final Role role, final Npc npc) {
        final String content = this.getNpcContentNotFight(role, npc);
        if (content != null) {
            MessagePusher.pushMessage(role, new RespMsg(content));
            return;
        }
        final TaskSet taskSet = this.getTaskSetByType(role);
        this.taskService.taskTriggerFight(role, taskSet, -1);
    }
    
    private String getNpcContentNotFight(final Role role, final Npc npc) {
        if (SpringUtils.getFightNewService().isInFight(role)) {
            return "请老老实实打完你的战斗";
        }
        if (this.teamService.getTeamCount(role) < 1) {
            return String.format("队伍人数不足%d人，不能挑战", 1);
        }
        return null;
    }
    
    public void leaveDugeon(final Role role) {
        SpringUtils.getMapService().changeMap(role, 1011, 120, 102, null);
    }
    
    @Override
    public void handleLogin(final Role role) {
        super.handleLogin(role);
        if (this.isInDugeon(role) && !SpringUtils.getFightNewService().isInFight(role)) {
            this.leaveDugeon(role);
        }
        if (this.isAcceptedTask(role) && this.getDugeon(role) == null) {
            final TaskSet taskSet = this.getTaskSetByType(role);
            this.forceResetTask(role, taskSet);
        }
    }
    
    private void deleteDugeon(final Dugeon dugeon) {
        if (dugeon != null) {
            DataCache.DUNGEON_MAP.remove(dugeon.getId());
            for (final long uid : dugeon.getMemberList()) {
                DataCache.ROLE_DUNGEON.remove(uid);
            }
        }
    }
    
    private void deleteTaskAndLeave(final Role role) {
        if (role == null) {
            return;
        }
        final TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        this.forceResetTask(role, taskSet);
        if (this.isInDugeon(role)) {
            this.leaveDugeon(role);
        }
    }
    
    public Dugeon getDugeon(final Role role) {
        final Long dugeonId = DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), 0L);
        if (dugeonId > 0L) {
            return DataCache.DUNGEON_MAP.get(dugeonId);
        }
        return null;
    }
    
    @Override
    public TaskInfo getTaskInfo(final Role role, final TaskSet taskSet) {
        final TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        return taskInfo;
    }
    
    @Override
    public boolean canGiveUpTask(final Role role, final TaskSet taskSet) {
        return false;
    }
    
    @Override
    public void doFightFail(final Role role, final FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
    }
    
    public boolean isRequestJoinTeam(final Role role, final Team team) {
        final Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
        if (leaderRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受申请哦。"));
            return false;
        }
        if (!this.isCanTeam(role, leaderRole, Const.DUGEON_LOOK_FOR_MAP)) {
            if (this.isInDugeon(role)) {
                MessagePusher.pushMessage(role, new RespMsg("本地图无法申请。"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受申请。"));
            }
            return false;
        }
        return true;
    }
    
    public boolean isInDugeon(final Role role) {
        return role != null && Const.DUGEON_LOOK_FOR_MAP.contains(role.getPos().getMapId());
    }
    
    public boolean isInviteJoinTeam(final Role role, final int inviteRoleId) {
        final Role invitRole = this.roleService.getOnlinePlayer(inviteRoleId);
        if (invitRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请哦。"));
            return false;
        }
        if (!this.isCanTeam(role, invitRole, Const.DUGEON_LOOK_FOR_MAP)) {
            if (this.isInDugeon(role)) {
                MessagePusher.pushMessage(role, new RespMsg("本地图无法邀请。"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请。"));
            }
            return false;
        }
        return true;
    }
    
    public boolean isAcceptJoinTeam(final Role role, final int joinRoleId) {
        final Role joinRole = SpringUtils.getRoleService().getOnlinePlayer(joinRoleId);
        if (!this.isCanTeam(role, joinRole, Const.DUGEON_LOOK_FOR_MAP)) {
            if (this.isInDugeon(role)) {
                MessagePusher.pushMessage(role, new RespMsg("本地图无法操作申请队员。"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受操作。"));
            }
            return false;
        }
        return true;
    }
    
    public boolean isBackToTeam(final Team team, final Role memberRole) {
        final Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
        if (!this.isCanTeam(leaderRole, memberRole, Const.DUGEON_LOOK_FOR_MAP)) {
            if (this.isInDugeon(leaderRole)) {
                MessagePusher.pushMessage(memberRole, new RespMsg("队长所在地图无法直接进入。"));
            }
            else {
                MessagePusher.pushMessage(memberRole, new RespMsg("本地图无法直接进入。"));
            }
            return false;
        }
        return true;
    }
    
    @Override
    public void giveReward(final Role role, final TaskSet taskSet) {
        final int record = role.getTaskProgress().getFinishRecord().getInt(ActivityType.LOOK_FOR_DUGEON.getActivityId() + taskSet.getSerial(), 0);
        if (record == 0) {
            if (this.teamService.isTeamLeader(role)) {
                final long num = this.activityService.addNice(role, 5L);
                if (num > 0L) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "获得好心值#<IR" + Const.RESTYPE_NICE + ">#" + MoneyUtil.colours(num) + "，队长带队辛苦了#<E:23>#", true));
                }
                this.activityService.teamLeaderNice(role, taskSet, ActivityType.LOOK_FOR_DUGEON.getActivityId(), 10L);
            }
            final List<Param> list = JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
            if (list != null) {
                SpringUtils.getBean(BagService.class).sendReward(role, list, "寻芳副本", 0);
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("你今日已获得本层奖励,不能再次获得"));
            if (this.teamService.isTeamLeader(role)) {
                this.activityService.teamLeaderNice(role, taskSet, ActivityType.LOOK_FOR_DUGEON.getActivityId(), 10L);
            }
        }
        MessagePusher.pushMessage(role, new RespMsg("挑战成功!#<E:145>#可以进入下一层继续挑战！#<E:86>#"));
    }
    
    @EventHandler({ EventType.LEAVE_MAP })
    public void handleLeaveMapEvent(final LeaveMapEvent leaveMapEvent) {
        final Role role = leaveMapEvent.getRole();
        final int mapId = role.getPos().getMapId();
        final int oldMapId = leaveMapEvent.getMapId();
        if (Const.DUGEON_LOOK_FOR_MAP.contains(oldMapId) && !Const.DUGEON_LOOK_FOR_MAP.contains(mapId)) {
            final List<SysDataTaskLookFor> list = TaskDataPool.taskLookForList;
            this.clear(role, list);
            role.save();
        }
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        boolean flag = false;
        final List<SysDataTaskLookFor> list = TaskDataPool.taskLookForList;
        if (Const.DUGEON_LOOK_FOR_MAP.contains(role.getPos().getMapId())) {
            for (final SysDataTaskLookFor obj : list) {
                if (role.getTask().isAccepted(obj.getSerial()) && this.isDugeonMember(role)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.clear(role, list);
                this.leaveDugeon(role);
                role.save();
            }
        }
    }
    
    public void clear(final Role role, final List<SysDataTaskLookFor> list) {
        for (final SysDataTaskLookFor obj : list) {
            final TaskSet taskSet = this.taskService.getTaskSet(obj.getSerial());
            if (this.taskService.isTask(role, taskSet)) {
                this.delTask(role, taskSet);
                this.deleteDugeon(this.getDugeon(role));
            }
        }
    }
}
