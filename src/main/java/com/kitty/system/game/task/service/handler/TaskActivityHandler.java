
package com.kitty.system.game.task.service.handler;

import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.vo.Npc;
import com.kitty.logs.LoggerUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.map.service.MapService;
import com.kitty.mina.task.ThreadLocalUtil;
import java.util.List;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.task.TaskType;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.task.service.TaskService;
import org.springframework.stereotype.Component;
import com.kitty.system.game.activity.service.ActivityHandler;

@Component
public abstract class TaskActivityHandler extends ActivityHandler
{
    @Autowired
    TaskService taskService;
    @Autowired
    TeamService teamService;
    
    public abstract TaskInfo getTaskInfo(final Role p0, final TaskSet p1);
    
    protected boolean isMatchFightType(final Role role, final int fightType) {
        return this.getFightType(role) == fightType;
    }
    
    protected abstract int getFightType(final Role p0);
    
    protected abstract void addActivityFinishCount(final Role p0, final TaskSet p1);
    
    protected abstract void giveReward(final Role p0, final NutMap p1, final TaskSet p2, final int p3);
    
    protected abstract void doBeforeDelete(final Role p0, final TaskSet p1);
    
    protected abstract void doAfterDelete(final Role p0, final TaskSet p1);
    
    public abstract TaskType getTaskType();
    
    public abstract String getNpcContentNotFight(final Role p0);
    
    public abstract void doStartFight(final Role p0, final TaskSet p1);
    
    public String acceptTask(final Role role, final TaskSet taskSet, final int expire, final boolean auto) {
        final String content = this.canAcceptTask(role, taskSet, auto);
        if (content != null) {
            return content;
        }
        this.doBeforeAccept(role, taskSet);
        this.doAcceptTask(role, taskSet, expire);
        this.doAfterAccept(role, taskSet);
        return null;
    }
    
    public abstract String canAcceptTask(final Role p0, final TaskSet p1, final boolean p2);
    
    public void doBeforeAccept(final Role role, final TaskSet taskSet) {
    }
    
    public boolean isTeamAcceptTask() {
        return true;
    }
    
    public void doAcceptTask(final Role role, final TaskSet taskSet, final int expire) {
        if (this.isTeamAcceptTask() && this.teamService.isInTeam(role)) {
            final Team team = SpringUtils.getBean(TeamService.class).getTeam(role.getRoleId());
            this.doAcceptTaskForTeam(team, taskSet, expire);
        }
        else {
            this.doAcceptTaskForSingle(role, taskSet, expire);
        }
    }
    
    public void doAcceptTaskForTeam(final Team team, final TaskSet taskSet, final int expire) {
        final Role role = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
        final List<Member> members = team.getList();
        for (int i = 0; i < members.size(); ++i) {
            final Member member = members.get(i);
            if (member != null) {
                if (member.isInTeam()) {
                    if (team.isLeader(member.getRoleId())) {
                        this.acceptTask(role, taskSet, expire);
                    }
                    else {
                        this.acceptMemberTask(member, taskSet, expire);
                    }
                }
            }
        }
        final TeamService teamService = SpringUtils.getBean(TeamService.class);
    }
    
    public boolean isMemberAcceptTask(final Role role, final TaskSet taskSet) {
        return true;
    }
    
    public void doAcceptTaskForSingle(final Role role, final TaskSet taskSet, final int expire) {
        this.acceptTask(role, taskSet, expire);
    }
    
    public void acceptTask(final Role role, final TaskSet taskSet, final int expire) {
        role.getTask().acceptTask(taskSet.getSerial(), taskSet.getPara(), expire);
        role.getTask().addTaskNpcId(taskSet.getSerial(), taskSet.getSubmitNpc(), expire);
        final TaskInfo taskInfo = this.getTaskInfo(role, taskSet);
        this.taskService.pushTaskInfo(role, taskInfo);
        role.save();
    }
    
    public void acceptMemberTask(final Member member, final TaskSet taskSet, final int expire) {
        final Role role = SpringUtils.getRoleService().getPlayer(member.getId());
        if (role == null) {
            return;
        }
        if (!this.isMemberAcceptTask(role, taskSet)) {
            return;
        }
        ThreadLocalUtil.addLocalTask(role, () -> this.acceptTask(role, taskSet, expire));
    }
    
    public void doAfterAccept(final Role role, final TaskSet taskSet) {
    }
    
    public void startFight(final Role role, final TaskSet taskSet) {
        this.setNpcFightStatus(role, taskSet, true);
    }
    
    public void setNpcFightStatus(final Role role, final TaskSet taskSet, final boolean isInFight) {
        final MapService mapService = SpringUtils.getBean(MapService.class);
    }
    
    public TaskSet getTaskSetByType(final Role role) {
        return this.getTaskSetByType(role, this.getTaskType());
    }
    
    public TaskSet getTaskSetByType(final Role role, final TaskType taskType) {
        final List<TaskSet> acceptedTaskListMatchTaskType = this.taskService.getAcceptedTaskListMatchTaskType(role, taskType);
        if (acceptedTaskListMatchTaskType.isEmpty()) {
            return null;
        }
        return acceptedTaskListMatchTaskType.get(0);
    }
    
    public abstract boolean isCreateNpcForFight(final TaskSet p0);
    
    public boolean isDeleteTaskTeamChange() {
        return false;
    }
    
    public boolean isDeleteTaskLogout() {
        return false;
    }
    
    @Override
    public void handleTeamLeave(final Role role, final boolean isLeader, final Team team) {
        if (!this.isDeleteTaskTeamChange()) {
            return;
        }
        final TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        final List<Member> memberList = team.getList();
        if (isLeader) {
            this.taskService.doDeleteTaskForTeam(memberList, role.getRoleId(), taskSet);
        }
        else {
            this.taskService.deleteTaskForSingle(role, taskSet);
        }
    }
    
    @Override
    public void handleTeamLeaveMoment(final Role role) {
        if (!this.isDeleteTaskTeamChange()) {
            return;
        }
        final TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        this.taskService.deleteTaskForSingle(role, taskSet);
    }
    
    @Override
    public void handleTeamChangeLeader(final Role role, final List<Member> memberList) {
        if (!this.isDeleteTaskTeamChange()) {
            return;
        }
        final TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        this.taskService.doDeleteTaskForTeam(memberList, role.getRoleId(), taskSet);
    }
    
    @Override
    public void handleLogout(final Role role) {
        if (!this.isDeleteTaskLogout()) {
            return;
        }
        final TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        if (!this.teamService.isInTeam(role)) {
            this.taskService.deleteTask(role, taskSet);
        }
    }
    
    @Override
    public void handleLogin(final Role role) {
        final TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        if (this.isDeleteTaskLogout() && !this.teamService.isInTeam(role)) {
            this.taskService.deleteTask(role, taskSet);
        }
        else {
            final TaskInfo taskInfo = this.getTaskInfo(role, taskSet);
            this.taskService.pushTaskInfo(role, taskInfo);
        }
    }
    
    @Override
    public void handleHeartBeat(final Role role, final long prevHeartTime) {
        final TaskSet taskSet = this.getTaskSetByType(role);
    }
    
    @Override
    public void handleFightEnd(final FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> this.doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    }
    
    private void doFightEnd(final Role role, final FightEndEvent fightEndEvent) {
        try {
            if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
                return;
            }
            final TaskSet taskSet = this.getTaskSetByType(role);
            if (taskSet == null) {
                return;
            }
            if (!this.isMatchFightType(role, fightEndEvent.getFightType())) {
                return;
            }
            this.setNpcFightStatus(role, taskSet, false);
            switch (fightEndEvent.getStatus()) {
                case 1: {
                    this.doFightWin(role, taskSet, fightEndEvent);
                    break;
                }
                case 2: {
                    this.doFightFail(role, taskSet, fightEndEvent);
                    break;
                }
                case 3: {
                    this.doFightRunAway(role, taskSet, fightEndEvent);
                    break;
                }
                case 40: {
                    this.doFightMaxRound(role, taskSet, fightEndEvent);
                    break;
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            LoggerUtils.error(role.getId() + "|" + this.getClass().getSimpleName() + "|" + e.getMessage(), e);
        }
    }
    
    private void doFightWin(final Role role, final TaskSet taskSet, final FightEndEvent fightEndEvent) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (this.teamService.isInTeam(role)) {
            this.doFightWinForTeam(team, taskSet, fightEndEvent);
        }
        else {
            this.doFightWinForSingle(role, taskSet, fightEndEvent);
        }
    }
    
    public void doFightWinForTeam(final Team team, final TaskSet taskSet, final FightEndEvent fightEndEvent) {
        final int leaderRoleId = team.getLeaderRoleId();
        final List<Member> members = team.getList();
        for (int i = 0; i < members.size(); ++i) {
            final Member member = members.get(i);
            if (member != null) {
                if (member.isInTeam()) {
                    if (leaderRoleId != member.getRoleId()) {
                        final Role role = SpringUtils.getRoleService().getPlayerBy(member.getId());
                        if (role != null) {
                            ThreadLocalUtil.addLocalTask(role, () -> this.doFightWinForSingle(role, taskSet, fightEndEvent));
                        }
                    }
                }
            }
        }
        final Role role2 = SpringUtils.getRoleService().getPlayer(leaderRoleId);
        this.doFightWinForSingle(role2, taskSet, fightEndEvent);
    }
    
    public void doFightWinForSingle(final Role role, final TaskSet taskSet, final FightEndEvent fightEndEvent) {
        try {
            if (!this.isMatchFightType(role, fightEndEvent.getFightType())) {
                return;
            }
            if (!this.isMemberSubmitTask(role, taskSet)) {
                return;
            }
            this.addActivityFinishCount(role, taskSet);
            final int currPetId = role.getTempCache("fight_current_pet_id", 0);
            final Npc npc = SpringUtils.getMapService().getNpc(role.getTask().getTaskNpcId(taskSet.getSubmitNpc()));
            role.pushTempCache("drop_monster_name", (npc != null) ? npc.getName() : "");
            this.giveReward(role, fightEndEvent.getReward(), taskSet, currPetId);
            role.popTempCache("drop_monster_name");
            this.doBeforeDelete(role, taskSet);
            this.taskService.deleteTaskForSingle(role, taskSet);
            this.doAfterDelete(role, taskSet);
        }
        catch (final Exception e) {
            e.printStackTrace();
            LoggerUtils.error(role.getId() + "|" + this.getClass().getSimpleName() + "|" + e.getMessage(), e);
        }
    }
    
    @Override
    public void handleRoleExpire(final Role role) {
        final Fight fight = SpringUtils.getFightNewService().getFightByRoleId(role.getRoleId());
        if (fight == null) {
            return;
        }
        if (!this.isMatchFightType(role, fight.getType())) {
            return;
        }
        super.handleRoleExpire(role);
        final TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet != null) {
            SpringUtils.getMapService().getNpc(role.getTask().getTaskNpcId(taskSet.getSubmitNpc()));
        }
        this.doRoleExpire(role, fight);
    }
    
    public void doRoleExpire(final Role role, final Fight fight) {
        final TeamService teamService = SpringUtils.getTeamService();
        if (teamService.getTeamCount(role) <= 1) {}
    }
    
    public boolean isMemberSubmitTask(final Role role, final TaskSet taskSet) {
        return true;
    }
    
    public void doFightFail(final Role role, final TaskSet taskSet, final FightEndEvent fightEndEvent) {
    }
    
    public void doFightRunAway(final Role role, final TaskSet taskSet, final FightEndEvent fightEndEvent) {
    }
    
    public void doFightMaxRound(final Role role, final TaskSet taskSet, final FightEndEvent fightEndEvent) {
    }
}
