
package com.kitty.system.game.task.service.handler.dungeon;

import java.io.Serializable;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.thread.IdGenerator;
import java.util.ArrayList;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.activity.model.Dugeon;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import com.kitty.system.game.task.message.RespViewTaskAward;
import com.kitty.system.init.domain.SysDataTaskXm;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.vo.Npc;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import javax.annotation.PostConstruct;
import java.util.Iterator;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;
import com.kitty.system.game.task.service.handler.TaskHandler;

@Component
public class DungeonXmHandler extends TaskHandler
{
    private static final int FIRST_TASK_ID = 1620001;
    private static final int MIN_TEAM_COUNT = 1;
    private static final int MIN_LEVEL = 1080;
    private static final String FDUGEON_NAME = "心魔";
    private static final int ENTER_MAP = 8001;
    private static final int ENTER_X = 36;
    private static final int ENTER_Y = 23;
    private static final int EXIT_MAP = 1209;
    private static final int EXIT_X = 34;
    private static final int EXIT_Y = 28;
    private static final int XM_NICE = 10;
    
    @Override
    public boolean canResetTask(final Role role, final TaskSet taskSet) {
        return true;
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.XM;
    }
    
    @PostConstruct
    public void init() {
        for (final int mapId : Const.DUGEON_XM_MAP) {
            MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(mapId), role -> DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), 0L));
        }
    }
    
    @Override
    public void canSubmitTask(final Role role, final TaskSet taskSet) {
        final int submitNpc = taskSet.getSubmitNpc();
        if (submitNpc > 0) {
            final Npc npc = this.mapService.getNpc(submitNpc);
            if (npc != null) {
                final boolean b = this.npcService.canClickNpc(role, npc);
                if (!b) {
                    MessagePusher.pushMessage(role, new RespMsg("请前往" + npc.getName() + "处提交任务"));
                    return;
                }
            }
        }
        final int submitCondition = taskSet.getSubmitCondition();
        if (submitCondition > 0 && !this.taskService.condition(role, taskSet, (byte)1)) {
            return;
        }
        this.taskService.updateSingleTargetProgress(role, taskSet);
    }
    
    public void createXmDugeon(final Role role, final Npc npc) {
        final String talk = this.checkCreateDugeon(role);
        if (talk != null) {
            this.npcService.sendNpcContent(role, npc, "", talk);
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        final Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
        this.createDungeon(team);
        final TaskSet taskSet = this.taskService.getTaskSet(1620001);
        this.teamService.memberHandleThreadLocal(team, memberRole -> this.acceptTask(memberRole, taskSet, -1));
        this.doEnterDugeon(leaderRole);
    }
    
    private String checkCreateDugeon(final Role role) {
        if (!this.teamService.isTeamLeader(role)) {
            return String.format("心魔试炼之路艰险，准备不足何以自保？请招募%d人以上再来挑战吧!#<E:99>#", 1);
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
        if (role.getRelive() * 1000 + role.getLevel() < 1080) {
            return String.format("你等级不足#<R>%s#，无法创建该副本。", String.format("%d转%d级", 1, 80));
        }
        if (this.teamService.getTeamCount(role) < 1) {
            return String.format("#<R>%s#中妖怪众多，少于#<R>%d#人的队伍进去是非常危险的。", "心魔", 1);
        }
        names = this.teamService.checkMember(role, memberRole -> memberRole.getRelive() * 1000 + memberRole.getLevel() < 1080);
        if (names != null) {
            return String.format("#<Y>%s#的等级不足#<Y>%s#，无法创建该副本。", names, String.format("%d转%d级", 1, 80));
        }
        return null;
    }
    
    public void viewDugeonAward(final Role role) {
        for (final SysDataTaskXm obj : TaskDataPool.taskXmList) {
            final TaskSet taskSet = this.taskService.getTaskSet(obj.getSerial());
            if (this.taskService.isTask(role, taskSet)) {
                final int level = obj.getGrade() % 1000;
                final RespViewTaskAward viewTaskAward = new RespViewTaskAward();
                viewTaskAward.setSerial(taskSet.getSerial());
                int brac = 0;
                final List<Param> list = JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
                if (list != null) {
                    for (final Param param : list) {
                        if (Const.RESTYPE_BRAC.equals(param.getK())) {
                            brac = param.getV();
                            break;
                        }
                    }
                }
                viewTaskAward.getList().add(new FiedValue(Const.RESTYPE_BRAC.toString(), (byte)3, brac));
                long exp = 0L;
                final Integer combatPet = role.getExtendBox().getCombatPet();
                if (combatPet > 0) {
                    final Pet pet = this.petService.selectPet(role).get(combatPet);
                    if (pet != null) {
                        exp = calculate(level, pet.getRebirth(), obj.getPower(), ItemClassify.PET_EXP);
                    }
                }
                viewTaskAward.getList().add(new FiedValue(ItemClassify.PET_EXP.toString(), (byte)3, exp));
                viewTaskAward.getList().add(new FiedValue(ItemClassify.HERO_EXP.toString(), (byte)3, calculate(level, role.getRelive(), obj.getPower(), ItemClassify.HERO_EXP)));
                MessagePusher.pushMessage(role.getId(), viewTaskAward);
                break;
            }
        }
    }
    
    @Override
    public void doAfterSubmit(final Role role, final TaskSet taskSet) {
        super.doAfterSubmit(role, taskSet);
        this.taskService.updateTargetProgress(role, taskSet);
        this.addActivityCount(role, taskSet);
    }
    
    private void addActivityCount(final Role role, final TaskSet taskSet) {
        if (this.activityService.isHaveRemainCount(role, ActivityType.XM_DUGEON) && role.getTaskProgress().getFinishRecord().getInt(ActivityType.XM_DUGEON.getActivityId() + taskSet.getSerial(), 0) == 0) {
            role.getTaskProgress().getFinishRecord().setv(ActivityType.XM_DUGEON.getActivityId() + taskSet.getSerial(), 1);
            this.activityService.increaseFinishCount(role, ActivityType.XM_DUGEON, 1);
            final int totalCount = ActivityType.XM_DUGEON.getTotalCount();
            final int finishCount = this.activityService.getFinishCount(role, ActivityType.XM_DUGEON, 1);
            if (totalCount - 1 == finishCount || finishCount == 1) {
                final ActivityLivenessSet activityLivenessSet = ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.XM_DUGEON);
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
        this.mapService.changeMap(role, new ReqTelePort(8001, 36, 23), null);
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
    
    public void leave(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null && !team.isLeader(role.getRoleId())) {
            MessagePusher.pushMessage(role, new RespMsg("既然是集体行动，还是老老实实听从队长的指挥啦。"));
            return;
        }
        final List<SysDataTaskXm> taskXmList = TaskDataPool.taskXmList;
        this.closeTaskIcon(team, role, taskXmList);
        this.clear(role, taskXmList);
        this.leaveDungeon(role, 1209, 34, 28, null);
    }
    
    public void leaveDugeon(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null && team.isLeader(role.getRoleId())) {
            this.closeTaskIcon(team, role, TaskDataPool.taskXmList);
        }
        else {
            this.closeTaskIcon(null, role, TaskDataPool.taskXmList);
        }
        SpringUtils.getMapService().changeMap(role, 1209, 34, 28, null);
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
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            this.teamService.memberHandleThreadLocal(team, memberRole -> this.taskScreenIcon(memberRole, taskSet.getSerial(), "", (byte)1));
        }
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
        if (!this.isCanTeam(role, leaderRole, Const.DUGEON_XM_MAP)) {
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
        return role != null && Const.DUGEON_XM_MAP.contains(role.getPos().getMapId());
    }
    
    public boolean isInviteJoinTeam(final Role role, final int inviteRoleId) {
        final Role invitRole = this.roleService.getPlayer(inviteRoleId);
        if (invitRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请哦。"));
            return false;
        }
        if (!this.isCanTeam(role, invitRole, Const.DUGEON_XM_MAP)) {
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
        if (!this.isCanTeam(role, joinRole, Const.DUGEON_XM_MAP)) {
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
        if (!this.isCanTeam(leaderRole, memberRole, Const.DUGEON_XM_MAP)) {
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
        final SysDataTaskXm taskXm = TaskDataPool.taskXmList.stream().filter(e -> e.getSerial() == taskSet.getSerial()).findFirst().orElse(null);
        if (taskXm != null && taskXm.getPower() > 0.0) {
            final int record = role.getTaskProgress().getFinishRecord().getInt(ActivityType.XM_DUGEON.getActivityId() + taskSet.getSerial(), 0);
            if (record == 0) {
                if (this.teamService.isTeamLeader(role)) {
                    final long num = this.activityService.addNice(role, 5L);
                    if (num > 0L) {
                        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "获得好心值#<IR" + Const.RESTYPE_NICE + ">#" + MoneyUtil.colours(num) + "，队长带队辛苦了#<E:23>#", true));
                    }
                    this.activityService.teamLeaderNice(role, taskSet, ActivityType.XM_DUGEON.getActivityId(), 10L);
                }
                final int level = taskXm.getGrade() % 1000;
                final List<Param> list = JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
                if (list != null) {
                    SpringUtils.getBean(BagService.class).sendReward(role, list, "心魔副本", 0);
                }
                this.roleService.addExp(role, calculate(level, role.getRelive(), taskXm.getPower(), ItemClassify.HERO_EXP), "心魔", true);
                final Integer combatPet = role.getExtendBox().getCombatPet();
                if (combatPet > 0) {
                    final Pet pet = this.petService.selectPet(role).get(combatPet);
                    if (pet != null) {
                        this.petService.addExp(role, pet, calculate(level, pet.getRebirth(), taskXm.getPower(), ItemClassify.PET_EXP), true);
                    }
                }
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("你今日已获得本关卡奖励,不能再次获得"));
            }
        }
    }
    
    public void closeTaskIcon(final Team team, final Role role, final List<SysDataTaskXm> list) {
        if (team != null) {
            if (team.isLeader(role.getRoleId())) {
                for (final Member member : team.getList()) {
                    final Role player = this.roleService.getPlayer(member.getId());
                    if (player != null) {
                        this.closeTaskIcon(player, this.getDungeonTask(list, role));
                    }
                }
            }
        }
        else {
            this.closeTaskIcon(role, this.getDungeonTask(list, role));
        }
    }
    
    public DoingTask getDungeonTask(final List<SysDataTaskXm> list, final Role role) {
        DoingTask doingTask = null;
        for (final SysDataTaskXm task : list) {
            if (role.getTask().isAccepted(task.getSerial())) {
                doingTask = role.getTask().getDoingTask().get(task.getSerial());
                break;
            }
        }
        return doingTask;
    }
    
    public void closeTaskIcon(final Role role, final DoingTask dungeonTask) {
        int serial = 0;
        String difficulty = null;
        if (dungeonTask != null) {
            serial = dungeonTask.getTaskId();
            difficulty = dungeonTask.getPara();
        }
        this.taskScreenIcon(role, serial, difficulty, (byte)0);
    }
    
    private boolean isDungeonMember(final Role role) {
        return role != null && DataCache.ROLE_DUNGEON.containsKey(role.getId());
    }
    
    @EventHandler({ EventType.LEAVE_MAP })
    public void handleLeaveMapEvent(final LeaveMapEvent leaveMapEvent) {
        final Role role = leaveMapEvent.getRole();
        final int mapId = role.getPos().getMapId();
        final int oldMapId = leaveMapEvent.getMapId();
        if (Const.DUGEON_XM_MAP.contains(oldMapId) && !Const.DUGEON_XM_MAP.contains(mapId)) {
            final List<SysDataTaskXm> list = TaskDataPool.taskXmList;
            this.clear(role, list);
            this.closeTaskIcon(this.teamService.getTeam(role.getRoleId()), role, list);
            role.save();
        }
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        boolean flag = false;
        final List<SysDataTaskXm> list = TaskDataPool.taskXmList;
        if (Const.DUGEON_XM_MAP.contains(role.getPos().getMapId())) {
            DoingTask doingTask = null;
            for (final SysDataTaskXm task : list) {
                if (role.getTask().isAccepted(task.getSerial())) {
                    flag = true;
                    doingTask = role.getTask().getDoingTask().get(task.getSerial());
                    break;
                }
            }
            if (flag) {
                if (doingTask != null) {
                    if (!this.isDungeonMember(role)) {
                        final List<Long> memberList = new ArrayList<Long>();
                        memberList.add(role.getId());
                        final Dugeon dugeon = new Dugeon(IdGenerator.getNextId(), memberList);
                        DataCache.DUNGEON_MAP.put(dugeon.getId(), dugeon);
                        for (final long uid : memberList) {
                            DataCache.ROLE_DUNGEON.put(uid, dugeon.getId());
                        }
                    }
                    this.init();
                    this.taskScreenIcon(role, doingTask.getTaskId(), doingTask.getPara(), (byte)1);
                }
                else {
                    this.leave(role);
                }
            }
            else {
                this.leaveDungeon(role, 1209, 34, 28, null);
            }
        }
    }
    
    public void clear(final Role role, final List<SysDataTaskXm> list) {
        for (final SysDataTaskXm obj : list) {
            final TaskSet taskSet = this.taskService.getTaskSet(obj.getSerial());
            if (this.taskService.isTask(role, taskSet)) {
                this.delTask(role, taskSet);
                this.deleteDugeon(this.getDugeon(role));
            }
        }
    }
    
    private static long calculate(final int grade, final int rei, final double power, final ItemClassify type) {
        final double fold = ActivityType.XM_DUGEON.getFold();
        double exp = 0.0;
        if (ItemClassify.HERO_EXP == type) {
            exp = (double)BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (grade + rei * 15) / 140 * 2.5) / 30.0 * 0.6 * power).longValue();
        }
        else if (ItemClassify.PET_EXP == type) {
            exp = (double)BigDecimalUtil.decimalScaleDown(0, BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (grade + rei * 15) / 140 * 2.5) / 30.0 * 0.6 * power * 1.2).longValue()).longValue();
        }
        return BigDecimalUtil.decimalScaleDown(0, (fold > 1.0) ? (exp * fold) : exp).longValue();
    }
}
