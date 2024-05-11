
package com.kitty.system.game.task.service.handler;

import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.thread.IdGenerator;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.domain.Member;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.activity.model.Dugeon;
import com.kitty.system.game.task.message.RespTasksDel;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.task.message.RespTaskFightEndNotify;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.init.domain.SysDataTaskFight;
import com.kitty.listener.event.FightEndEvent;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import com.kitty.system.enums.task.TaskType;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.task.message.RespTaskIcon;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.logs.LoggerUtils;
import java.util.List;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.EasyNpc;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.task.domain.RoleTask;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.logs.LoggerFunction;
import com.kitty.cache.RedisService;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.email.service.EmailService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.map.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.task.service.TaskService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class TaskHandler
{
    protected Logger logger;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected MapService mapService;
    @Autowired
    protected NpcService npcService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected EmailService emailService;
    @Autowired
    protected PetService petService;
    @Autowired
    protected TeamService teamService;
    @Autowired
    protected ActivityService activityService;
    @Autowired
    protected RedisService redisService;
    
    public TaskHandler() {
        this.logger = LoggerFunction.TASK.getLogger();
    }
    
    public boolean acceptTask(final Role role, final TaskSet taskSet, final int expire) {
        if (!this.canAcceptTask(role, taskSet)) {
            return false;
        }
        this.doBeforeAccept(role, taskSet);
        this.doAcceptTask(role, taskSet, expire);
        this.doAfterAccept(role, taskSet);
        return true;
    }
    
    public boolean canAcceptTask(final Role role, final TaskSet taskSet) {
        return this.taskService.condition(role, taskSet, (byte)0) && !this.taskService.isTask(role, taskSet);
    }
    
    public void doBeforeAccept(final Role role, final TaskSet taskSet) {
    }
    
    public boolean doAcceptTask(final Role role, final TaskSet taskSet, final int expire) {
        final RoleTask roleTask = role.getTask();
        roleTask.acceptTask(taskSet.getSerial(), taskSet.getPara(), expire);
        this.dstNpc(role, taskSet);
        return true;
    }
    
    public void dstNpc(final Role role, final TaskSet taskSet) {
        if (taskSet.getDstNpc().size() > 0) {
            final int index = RandomUtil.round(0, taskSet.getDstNpc().size() - 1);
            final Integer serial = taskSet.getDstNpc().get(index);
            final Npc npc = this.mapService.getNpc(serial);
            if (npc != null) {
                final DoingTask doingTask = role.getTask().getDoingTask().get(taskSet.getSerial());
                if (doingTask != null) {
                    final List<EasyNpc> npcList = doingTask.getEasyNpcList();
                    npcList.add(new EasyNpc(npc, taskSet.getKind()));
                }
            }
        }
    }
    
    public void doAfterAccept(final Role role, final TaskSet taskSet) {
        final List<Integer> addTaskEvent = taskSet.getAddTaskEvent();
        this.taskService.editTaskEvent(role, addTaskEvent, taskSet, getTaskPara(role, taskSet));
        this.pushTask(role, taskSet, 0);
        role.save();
    }
    
    public static String getTaskPara(final Role role, final TaskSet taskSet) {
        final DoingTask task = role.getTask().getDoingTask().get(taskSet.getSerial());
        if (task == null) {
            return null;
        }
        return task.getPara();
    }
    
    public boolean submitTask(final Role role, final TaskSet taskSet) {
        if (!role.getTask().isCanSubmit(taskSet.getSerial())) {
            return false;
        }
        this.doBeforeSubmit(role, taskSet);
        this.doSubmitTask(role, taskSet);
        this.doAfterSubmit(role, taskSet);
        return true;
    }
    
    public void doBeforeSubmit(final Role role, final TaskSet taskSet) {
    }
    
    public void doSubmitTask(final Role role, final TaskSet taskSet) {
        final String para = getTaskPara(role, taskSet);
        this.taskService.deleteTask(role, taskSet);
        this.taskService.editTaskEvent(role, taskSet.getFinishTaskEvent(), taskSet, para);
        role.save();
    }
    
    public void doAfterSubmit(final Role role, final TaskSet taskSet) {
        this.giveReward(role, taskSet);
    }
    
    public void pushTask(final Role role, final TaskSet taskSet, final int refresh) {
        try {
            final TaskInfo taskInfo = this.getTaskInfo(role, taskSet);
            taskInfo.setRefresh((byte)refresh);
            this.taskService.pushTaskInfo(role, taskInfo);
        }
        catch (final Exception e) {
            e.printStackTrace();
            LoggerUtils.error("推送任务异常：{}", e);
        }
    }
    
    public TaskInfo getTaskInfo(final Role role, final TaskSet taskSet) {
        return this.taskService.getTaskInfo(role, taskSet);
    }
    
    private boolean isPushEnd(final TaskSet taskSet, final TaskSet nextTask) {
        return nextTask == null || nextTask.getNextTaskSerial() != taskSet.getSerial();
    }
    
    public void taskScreenIcon(final Role role, final int serial, final String difficulty, final byte type) {
        MessagePusher.pushMessage(role, new RespTaskIcon(serial, difficulty, type));
    }
    
    public void giveReward(final Role role, final TaskSet taskSet) {
        this.taskService.giveAward(role, taskSet);
    }
    
    public boolean giveUpTask(final Role role, final TaskSet taskSet) {
        if (!this.canGiveUpTask(role, taskSet)) {
            return false;
        }
        if (taskSet.isTeamAccept()) {
            if (!this.teamService.isTeamLeader(role)) {
                return false;
            }
            this.teamService.memberHandleThreadLocal(role, memberRole -> this.giveUpTaskExecute(memberRole, taskSet));
        }
        else {
            this.giveUpTaskExecute(role, taskSet);
        }
        return true;
    }
    
    public void giveUpTaskExecute(final Role role, final TaskSet taskSet) {
        this.doBeforeGiveUpTask(role, taskSet);
        this.doGiveUpTask(role, taskSet);
        this.doAfterGiveUp(role, taskSet, (byte)2);
    }
    
    public boolean canGiveUpTask(final Role role, final TaskSet taskSet) {
        return taskSet.getGiveUp() == 1;
    }
    
    public void doBeforeGiveUpTask(final Role role, final TaskSet taskSet) {
    }
    
    public void doGiveUpTask(final Role role, final TaskSet taskSet) {
        role.getTask().giveUpTask(taskSet.getSerial());
        role.save();
    }
    
    public void doAfterGiveUp(final Role role, final TaskSet taskSet, final byte type) {
        this.pushEndTask(role, taskSet, type);
    }
    
    public boolean resetTask(final Role role, final TaskSet taskSet) {
        if (!this.canResetTask(role, taskSet)) {
            return false;
        }
        this.doBoforeResetTask(role, taskSet);
        this.doResetTask(role, taskSet);
        this.doAfterReset(role, taskSet, (byte)1);
        return true;
    }
    
    public boolean forceResetTask(final Role role, final TaskSet taskSet) {
        this.doBoforeResetTask(role, taskSet);
        this.doResetTask(role, taskSet);
        this.doAfterReset(role, taskSet, (byte)2);
        return true;
    }
    
    public boolean canResetTask(final Role role, final TaskSet taskSet) {
        return false;
    }
    
    public void doBoforeResetTask(final Role role, final TaskSet taskSet) {
    }
    
    public void doResetTask(final Role role, final TaskSet taskSet) {
        role.getTask().resetTask(taskSet.getSerial());
        role.save();
    }
    
    public void doAfterReset(final Role role, final TaskSet taskSet, final byte type) {
        this.pushEndTask(role, taskSet, type);
    }
    
    public void pushEndTask(final Role role, final TaskSet taskSet, final byte type) {
        this.taskService.pushEndTask(role, taskSet, type);
        this.endTaskTips(role);
    }
    
    public void endTaskTips(final Role role) {
    }
    
    public List<TaskSet> getAcceptedTaskListMatchTaskType(final Role role, final TaskType taskType) {
        List<Integer> acceptedTaskList = null;
        try {
            acceptedTaskList = role.getTask().getAcceptedTaskList();
        }
        catch (final ConcurrentModificationException e) {
            LoggerUtils.error("{}({}),taskType:{},sessionId:{}", role.getRoleId(), role.getName(), taskType, e.getMessage(), e);
        }
        if (acceptedTaskList != null) {
            final List<TaskSet> taskList = new ArrayList<TaskSet>(acceptedTaskList.size());
            for (final Integer taskId : acceptedTaskList) {
                final TaskSet taskSet = this.taskService.getTaskSet(taskId);
                if (!this.taskService.isMatchTaskType(taskSet, taskType)) {
                    continue;
                }
                taskList.add(taskSet);
            }
            return taskList;
        }
        return Collections.emptyList();
    }
    
    public abstract TaskType getTaskType();
    
    public void doFightEnd(final Role role, final FightEndEvent fightEndEvent) {
        switch (fightEndEvent.getStatus()) {
            case 1: {
                this.doFightWin(role, fightEndEvent);
                break;
            }
            case 2: {
                this.doFightFail(role, fightEndEvent);
                break;
            }
            case 3: {
                this.doFightRunAway(role, fightEndEvent);
                break;
            }
            case 40: {
                this.doFightMaxRound(role, fightEndEvent);
                break;
            }
        }
    }
    
    public void doFightWin(final Role role, final FightEndEvent fightEndEvent) {
        final RoleTask roleTask = role.getTask();
        final Integer taskId = roleTask.getTaskIdByFightId(fightEndEvent.getFightId());
        if (taskId == null) {
            return;
        }
        final TaskSet taskSet = this.taskService.getTaskSet(taskId);
        final SysDataTaskFight taskFight = TaskDataPool.taskFightMap.get(fightEndEvent.getNpcId());
        if (taskFight != null && taskFight.getTaskSerial() == fightEndEvent.getTaskId()) {
            this.taskService.editTaskEvent(role, taskFight.getWinEvent(), taskSet, getTaskPara(role, taskSet));
            if (roleTask.isAccepted(fightEndEvent.getTaskId())) {
                MessagePusher.pushMessage(role, new RespTaskFightEndNotify(taskId, fightEndEvent.getNpcId(), (byte)1));
                this.pushTask(role, taskSet, 1);
            }
        }
    }
    
    public void doFightFail(final Role role, final FightEndEvent fightEndEvent) {
        final SysDataTaskFight taskFight = TaskDataPool.taskFightMap.get(fightEndEvent.getNpcId());
        if (taskFight != null && taskFight.getTaskSerial() == fightEndEvent.getTaskId()) {
            final TaskSet taskSet = this.taskService.getTaskSet(fightEndEvent.getTaskId());
            this.taskService.editTaskEvent(role, taskFight.getLoseEvent(), taskSet, getTaskPara(role, taskSet));
            MessagePusher.pushMessage(role, new RespTaskFightEndNotify(fightEndEvent.getTaskId(), fightEndEvent.getNpcId(), (byte)2));
        }
    }
    
    public void doFightRunAway(final Role role, final FightEndEvent fightEndEvent) {
        this.doFightFail(role, fightEndEvent);
    }
    
    public void doFightMaxRound(final Role role, final FightEndEvent fightEndEvent) {
        this.doFightFail(role, fightEndEvent);
    }
    
    public void handleLogin(final Role role) {
    }
    
    public void startFight(final Role role, final TaskType taskType, final Npc npc, final int minTeamCount, final int needRei, final int needLv, final String needFightNot, final String needLvNot) {
        if (role == null) {
            return;
        }
        try {
            final TaskSet taskSet = this.getTaskSetByType(role, taskType);
            if (taskSet == null) {
                return;
            }
            final DoingTask doingTask = role.getTask().getDoingTask().get(taskSet.getSerial());
            if (doingTask != null) {
                int i = 0;
                while (i < doingTask.getEasyNpcList().size()) {
                    final EasyNpc easyNpc = doingTask.getEasyNpcList().get(i);
                    if (easyNpc != null && easyNpc.getId() == npc.getId()) {
                        if (this.teamService.getTeamCount(role) < minTeamCount) {
                            MessagePusher.pushMessage(role, new RespMsg(String.format(needFightNot, minTeamCount)));
                            return;
                        }
                        if (!this.canTeamGrade(role, needRei, needLv, needLvNot)) {
                            return;
                        }
                        this.taskService.taskTriggerFight(role, taskSet, -1);
                        break;
                    }
                    else {
                        ++i;
                    }
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public TaskSet getTaskSetByType(final Role role) {
        final List<TaskSet> acceptedTaskListMatchTaskType = this.taskService.getAcceptedTaskListMatchTaskType(role, this.getTaskType());
        if (acceptedTaskListMatchTaskType.isEmpty()) {
            return null;
        }
        return acceptedTaskListMatchTaskType.get(0);
    }
    
    public TaskSet getTaskSetByType(final Role role, final TaskType taskType) {
        final List<TaskSet> acceptedTaskListMatchTaskType = this.taskService.getAcceptedTaskListMatchTaskType(role, taskType);
        if (acceptedTaskListMatchTaskType.isEmpty()) {
            return null;
        }
        return acceptedTaskListMatchTaskType.get(0);
    }
    
    public void syncTeamTask(final Role role, final DoingTask doingTask) {
        final RoleTask task = role.getTask();
        task.getAcceptedTasks().put(doingTask.getTaskId(), (byte)0);
        task.getDoingTask().put(doingTask.getTaskId(), doingTask);
        this.pushTask(role, this.taskService.getTaskSet(doingTask.getTaskId()), 0);
        role.save();
    }
    
    public void delTask(final Role role, final TaskSet taskSet) {
        role.getTask().updateToFinished(taskSet.getSerial());
        final RespTasksDel respTasksDel = new RespTasksDel();
        respTasksDel.setSerial(taskSet.getSerial());
        respTasksDel.setType((byte)2);
        MessagePusher.pushMessage(role, respTasksDel);
    }
    
    public Dugeon getDungeon(final Role role) {
        final Long dugeonId = DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), 0L);
        if (dugeonId > 0L) {
            return DataCache.DUNGEON_MAP.get(dugeonId);
        }
        return null;
    }
    
    public boolean isCanTeam(final Role role, final Role otherRole, final List<Integer> list) {
        if (role == null || otherRole == null) {
            return false;
        }
        if (!list.contains(role.getPos().getMapId()) && !list.contains(otherRole.getPos().getMapId())) {
            return true;
        }
        if (this.isInDungeon(role, list) && this.isInDungeon(otherRole, list)) {
            final long roleDugeonId = DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), 0L);
            final long otherDugeonId = DataCache.ROLE_DUNGEON.getOrDefault(otherRole.getId(), 0L);
            return roleDugeonId == otherDugeonId;
        }
        return false;
    }
    
    public boolean isInDungeon(final Role role, final List<Integer> list) {
        return role != null && list.contains(role.getPos().getMapId());
    }
    
    public void deleteDungeon(final Dugeon dugeon) {
        if (dugeon != null) {
            DataCache.DUNGEON_MAP.remove(dugeon.getId());
            for (final long uid : dugeon.getMemberList()) {
                DataCache.ROLE_DUNGEON.remove(uid);
            }
        }
    }
    
    public void leaveDungeon(final Role role, final int map, final int x, final int y, final String param) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            if (team.isLeader(role.getRoleId())) {
                final List<Member> list = team.getList();
                for (int i = 0; i < list.size(); ++i) {
                    final Member member = list.get(i);
                    final Role player = this.roleService.getPlayer(member.getId());
                    if (player != null) {
                        player.pushTempCache("not_check_change_map", true);
                    }
                }
            }
            else {
                role.pushTempCache("not_check_change_map", true);
            }
        }
        else {
            role.pushTempCache("not_check_change_map", true);
        }
        this.mapService.changeMap(role, map, x, y, param);
    }
    
    public void canSubmitTask(final Role role, final TaskSet taskSet) {
    }
    
    public boolean canTeamGrade(final Role role, final int needRei, final int needLevel, final String needNot) {
        final String names = this.teamService.checkMember(role, memberRole -> DataGlobalFunc.needGradeJudge(needRei, needLevel, memberRole.getRelive(), memberRole.getLevel(), "你的#<R>角色等级#不足") != null);
        if (names != null) {
            String msg = needLevel + "级";
            if (needRei > 0) {
                msg = needRei + "转" + msg;
            }
            MessagePusher.pushMessage(role, new RespMsg(String.format(needNot, names, msg)));
            return false;
        }
        return true;
    }
    
    public void createDungeon(final Team team) {
        final List<Long> memberList = new ArrayList<Long>();
        memberList.add(team.getLeaderUid());
        this.teamService.memberHandle(team, memberRole -> {
            if (!memberList.contains(memberRole.getId())) {
                memberList.add(memberRole.getId());
            }
            return;
        });
        final Dugeon dugeon = new Dugeon(IdGenerator.getNextId(), memberList);
        DataCache.DUNGEON_MAP.put(dugeon.getId(), dugeon);
        for (final long uid : memberList) {
            DataCache.ROLE_DUNGEON.put(uid, dugeon.getId());
        }
    }
    
    public void activationTips(final Role role, final int step) {
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你获得了#<G>" + step + "#点活跃度", true));
    }
}
