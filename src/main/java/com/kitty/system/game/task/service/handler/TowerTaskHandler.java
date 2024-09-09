package com.kitty.system.game.task.service.handler;

import java.io.Serializable;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.init.domain.SysDataTaskTower;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.task.message.vo.TaskInfo;
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
public class TowerTaskHandler extends TaskHandler
{
    private static final int FIRST_TASK_ID = 800002;
    private static final int MIN_TEAM_COUNT = 3;
    private static final int MIN_LEVEL = 50;
    private static final String FDUGEON_NAME = "大雁塔";
    private static final int ENTER_MAP = 1111;
    private static final int ENTER_X = 24;
    private static final int ENTER_Y = 18;
    private static final int EXIT_MAP = 1011;
    private static final int EXIT_X = 120;
    private static final int EXIT_Y = 102;
    private static final int TOWER_NICE = 20;
    
    public TowerTaskHandler() {
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.TOWER;
    }
    
    @Override
    public boolean resetTask(Role role, TaskSet taskSet) {
        return true;
    }
    
    @PostConstruct
    public void init() {
        for (int mapId : Const.DUGEON_DYT_MAP) {
            MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(mapId), role -> (Long)DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L)));
        }
    }
    
    public void createTowerDugeon(Role role, Npc npc) {
        String talk = this.checkCreateDugeon(role);
        if (talk != null) {
            this.npcService.sendNpcContent(role, npc, "", talk);
            return;
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
        this.createDungeon(team);
        TaskSet taskSet = this.taskService.getTaskSet(800002);
        this.teamService.memberHandleThreadLocal(team, memberRole -> this.acceptTask(memberRole, taskSet, -1));
        this.doEnterDugeon(leaderRole);
        return;
    }
    
    private String checkCreateDugeon(Role role) {
        if (!this.teamService.isTeamLeader(role)) {
            return "你不是队长，不能创建副本。#<R>(至少需要3人组队才能进入副本)#";
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        String names = this.teamService.getNotInTeam(team);
        if (names != null) {
            return String.format("队伍中#<Y>%s#处于暂离状态，不能创建副本", names);
        }
        for (int i = 0; i < team.getList().size(); ++i) {
            Member member = (Member)team.getList().get(i);
            if (member != null && member.isInTeam()) {
            	Role memberRole = this.roleService.getPlayer((int)member.getRoleId());
                if (memberRole != null) {}
            }
        }
        if ((int)role.getLevel() < 50) {
            return String.format("你等级不足#<R>%s#级，无法创建该副本。", Integer.valueOf(50));
        }
        if (this.teamService.getTeamCount(role) < 3) {
            return String.format("#<R>%s#中妖怪众多，少于#<R>%d#人的队伍进去是非常危险的。", "大雁塔", Integer.valueOf(3));
        }
        names = this.teamService.checkMember(role, memberRole -> (int)memberRole.getLevel() < 50);
        if (names != null) {
            return String.format("#<Y>%s#的等级不足#<Y>%d#级，无法创建该副本。", names, Integer.valueOf(50));
        }
        return null;
    }
    
    private boolean isDugeonMember(Role role) {
        return role != null && DataCache.ROLE_DUNGEON.containsKey(role.getId());
    }
    
    @Override
    public void doAfterSubmit(Role role, TaskSet taskSet) {
        super.doAfterSubmit(role, taskSet);
        this.taskService.updateTargetProgress(role, taskSet);
        this.addActivityCount(role, taskSet);
    }
    
    private void addActivityCount(Role role, TaskSet taskSet) {
        if (this.activityService.isHaveRemainCount(role, ActivityType.TOWER_DUGEON) && role.getTaskProgress().getFinishRecord().getInt(ActivityType.TOWER_DUGEON.getActivityId() + taskSet.getSerial(), 0) == 0) {
            role.getTaskProgress().getFinishRecord().setv(ActivityType.TOWER_DUGEON.getActivityId() + taskSet.getSerial(), Integer.valueOf(1));
            this.activityService.increaseFinishCount(role, ActivityType.TOWER_DUGEON, 1);
            int totalCount = ActivityType.TOWER_DUGEON.getTotalCount();
            int finishCount = this.activityService.getFinishCount(role, ActivityType.TOWER_DUGEON, 1);
            if (totalCount - 1 == finishCount || finishCount == 1) {
                ActivityLivenessSet activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.TOWER_DUGEON);
                if (activityLivenessSet != null) {
                    this.activationTips(role, (int)activityLivenessSet.getPerLiveness());
                }
            }
        }
    }
    
    private boolean isAcceptedTask(Role role) {
        return this.getTaskSetByType(role) != null;
    }
    
    private void doEnterDugeon(Role role) {
        ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId(1111);
        reqTelePort.setX(24);
        reqTelePort.setY(18);
        this.mapService.changeMap(role, reqTelePort, null);
    }
    
    public void startFight(Role role, Npc npc) {
        String content = this.getNpcContentNotFight(role, npc);
        if (content != null) {
            MessagePusher.pushMessage(role, new RespMsg(content));
            return;
        }
        TaskSet taskSet = this.getTaskSetByType(role);
        this.taskService.taskTriggerFight(role, taskSet, -1);
        return;
    }
    
    private String getNpcContentNotFight(Role role, Npc npc) {
        if (SpringUtils.getFightNewService().isInFight(role)) {
            return "请老老实实打完你的战斗";
        }
        if (this.teamService.getTeamCount(role) < 3) {
            return String.format("队伍人数不足%d人，不能挑战", Integer.valueOf(3));
        }
        return null;
    }
    
    @Override
    public void handleLogin(Role role) {
        super.handleLogin(role);
        if (this.isInDungeon(role, Const.DUGEON_DYT_MAP) && !SpringUtils.getFightNewService().isInFight(role)) {
            this.leaveDungeon(role, 1011, 120, 102, null);
        }
        if (this.isAcceptedTask(role) && this.getDungeon(role) == null) {
            TaskSet taskSet = this.getTaskSetByType(role);
            this.forceResetTask(role, taskSet);
        }
    }
    
    private void deleteTaskAndLeave(Role role) {
        if (role == null) {
            return;
        }
        TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        this.forceResetTask(role, taskSet);
        if (this.isInDungeon(role, Const.DUGEON_DYT_MAP)) {
            this.leaveDungeon(role, 1011, 120, 102, null);
        }
        return;
    }
    
    @Override
    public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
        TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        return taskInfo;
    }
    
    @Override
    public void doFightFail(Role role, FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
    }
    
    public boolean isRequestJoinTeam(Role role, Team team) {
        Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (leaderRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受申请哦。"));
            return false;
        }
        if (!this.isCanTeam(role, leaderRole, Const.DUGEON_DYT_MAP)) {
            if (this.isInDungeon(role, Const.DUGEON_DYT_MAP)) {
                MessagePusher.pushMessage(role, new RespMsg("本地图无法申请。"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受申请。"));
            }
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
        Role invitRole = this.roleService.getPlayer(inviteRoleId);
        if (invitRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请哦。"));
            return false;
        }
        if (!this.isCanTeam(role, invitRole, Const.DUGEON_DYT_MAP)) {
            if (this.isInDungeon(role, Const.DUGEON_DYT_MAP)) {
                MessagePusher.pushMessage(role, new RespMsg("本地图无法邀请。"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请。"));
            }
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean isAcceptJoinTeam(Role role, int joinRoleId) {
        Role joinRole = this.roleService.getPlayer(joinRoleId);
        if (!this.isCanTeam(role, joinRole, Const.DUGEON_DYT_MAP)) {
            if (this.isInDungeon(role, Const.DUGEON_DYT_MAP)) {
                MessagePusher.pushMessage(role, new RespMsg("本地图无法操作申请队员。"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受操作。"));
            }
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean isBackToTeam(Team team, Role memberRole) {
        Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (!this.isCanTeam(leaderRole, memberRole, Const.DUGEON_DYT_MAP)) {
            if (this.isInDungeon(leaderRole, Const.DUGEON_DYT_MAP)) {
                MessagePusher.pushMessage(memberRole, new RespMsg("队长所在地图无法直接进入。"));
            }
            else {
                MessagePusher.pushMessage(memberRole, new RespMsg("本地图无法直接进入。"));
            }
            return false;
        }
        else {
            return true;
        }
    }
    
    @Override
    public void giveReward(Role role, TaskSet taskSet) {
        int record = role.getTaskProgress().getFinishRecord().getInt(ActivityType.TOWER_DUGEON.getActivityId() + taskSet.getSerial(), 0);
        if (record == 0) {
            this.roleService.addCurrency(role, Const.RESTYPE_BRAC, Long.valueOf(calculate((int)role.getLevel(), (int)role.getRelive(), 3)), "大雁塔", true, false);
            if (this.teamService.isTeamLeader(role)) {
                long num = this.activityService.addNice(role, 5L);
                if (num > 0L) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "获得好心值#<IR" + Const.RESTYPE_NICE + ">#" + MoneyUtil.colours(Long.valueOf(num)) + "，队长带队辛苦了#<E:23>#", true));
                }
                this.activityService.teamLeaderNice(role, taskSet, ActivityType.TOWER_DUGEON.getActivityId(), 20L);
            }
            this.roleService.addExp(role, calculate((int)role.getLevel(), (int)role.getRelive(), 1), "大雁塔", true);
            Integer combatPet = role.getExtendBox().getCombatPet();
            if ((int)combatPet > 0) {
                Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
                if (pet != null) {
                    this.petService.addExp(role, pet, calculate((int)pet.getPetLv(), (int)pet.getRebirth(), 2), true);
                }
            }
            List<Param> list = (List)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
            if (list != null) {
                ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "大雁塔", 0);
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("你今日已获得本层大雁塔奖励,不能再次获得"));
            if (this.teamService.isTeamLeader(role)) {
                this.activityService.teamLeaderNice(role, taskSet, ActivityType.TOWER_DUGEON.getActivityId(), 20L);
            }
        }
        MessagePusher.pushMessage(role, new RespMsg("挑战成功!#<E:145>#可以进入下一层继续挑战！#<E:86>#"));
    }
    
    private static long calculate(int grade, int rei, int type) {
        double fold = ActivityType.TOWER_DUGEON.getFold();
        double exp = 0.0;
        if (type == 1) {
            exp = 1.6E7 * (1.0 + (Math.min((double)grade, 180.0) + Math.min((double)rei, 3.0) * 15.0) / 140.0 * 2.5) / 30.0 * 0.6 * 1.3;
        }
        else if (type == 2) {
            exp = 1.6E7 * (1.0 + (Math.min((double)grade, 180.0) + Math.min((double)rei, 3.0) * 15.0) / 140.0 * 2.5) / 30.0 * 0.6 * 1.3 * 1.2;
        }
        else if (type == 3) {
            if (grade - 180 >= 50) {
                return 37L;
            }
            if (grade - 180 >= 20) {
                return 75L;
            }
            return 150L;
        }
        return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0) ? (exp * fold) : exp)).longValue();
    }
    
    @EventHandler({ EventType.LEAVE_MAP })
    public void handleLeaveMapEvent(LeaveMapEvent leaveMapEvent) {
        Role role = leaveMapEvent.getRole();
        int mapId = (int)role.getPos().getMapId();
        int oldMapId = leaveMapEvent.getMapId();
        if (Const.DUGEON_DYT_MAP.contains(Integer.valueOf(oldMapId)) && !Const.DUGEON_DYT_MAP.contains(Integer.valueOf(mapId))) {
            List<SysDataTaskTower> taskTowerList = TaskDataPool.taskTowerList;
            this.clear(role, taskTowerList);
            role.save();
        }
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        boolean flag = false;
        List<SysDataTaskTower> taskTowerList = TaskDataPool.taskTowerList;
        if (Const.DUGEON_DYT_MAP.contains(role.getPos().getMapId())) {
            for (SysDataTaskTower taskTower : taskTowerList) {
                if (role.getTask().isAccepted(taskTower.getSerial()) && this.isDugeonMember(role)) {
                    flag = true;
                    break;
                }
                continue;
            }
            if (!flag) {
                this.clear(role, taskTowerList);
                this.leaveDungeon(role, 1011, 120, 102, null);
                role.save();
            }
        }
    }
    
    public void clear(Role role, List<SysDataTaskTower> taskTowerList) {
        for (SysDataTaskTower taskTower : taskTowerList) {
            TaskSet taskSet = this.taskService.getTaskSet(taskTower.getSerial());
            if (this.taskService.isTask(role, taskSet)) {
                this.delTask(role, taskSet);
                this.deleteDungeon(this.getDungeon(role));
            }
            else {
                continue;
            }
        }
    }
    
    @Override
    public boolean canGiveUpTask(Role role, TaskSet taskSet) {
        return false;
    }
}
