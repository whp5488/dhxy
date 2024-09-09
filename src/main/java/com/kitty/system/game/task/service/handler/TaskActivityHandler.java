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
    
    public TaskActivityHandler() {
    }
    
    public abstract TaskInfo getTaskInfo(Role role, TaskSet taskSet);
    
    protected boolean isMatchFightType(Role role, int fightType) {
        return this.getFightType(role) == fightType;
    }
    
    protected abstract int getFightType(Role role);
    
    protected abstract void addActivityFinishCount(Role role, TaskSet taskSet);
    
    protected abstract void giveReward(Role role, NutMap reward, TaskSet taskSet, int currPetId);
    
    protected abstract void doBeforeDelete(Role role, TaskSet taskSet);
    
    protected abstract void doAfterDelete(Role role, TaskSet taskSet);
    
    public abstract TaskType getTaskType();
    
    public abstract String getNpcContentNotFight(Role role);
    
    public abstract void doStartFight(Role role, TaskSet taskSet);
    
    public String acceptTask(Role role, TaskSet taskSet, int expire, boolean auto) {
        String content = this.canAcceptTask(role, taskSet, auto);
        if (content != null) {
            return content;
        }
        this.doBeforeAccept(role, taskSet);
        this.doAcceptTask(role, taskSet, expire);
        this.doAfterAccept(role, taskSet);
        return null;
    }
    
    public abstract String canAcceptTask(Role role, TaskSet taskSet, boolean auto);
    
    public void doBeforeAccept(Role role, TaskSet taskSet) {
    }
    
    public boolean isTeamAcceptTask() {
        return true;
    }
    
    public void doAcceptTask(Role role, TaskSet taskSet, int expire) {
        if (this.isTeamAcceptTask() && this.teamService.isInTeam(role)) {
            Team team = ((TeamService)SpringUtils.getBean(TeamService.class)).getTeam((int)role.getRoleId());
            this.doAcceptTaskForTeam(team, taskSet, expire);
        }
        else {
            this.doAcceptTaskForSingle(role, taskSet, expire);
        }
    }
    
    public void doAcceptTaskForTeam(Team team, TaskSet taskSet, int expire) {
        Role role = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
        List<Member> members = team.getList();
        for (int i = 0; i < members.size(); ++i) {
            Member member = (Member)members.get(i);
            if (member != null && member.isInTeam()) {
                if (team.isLeader((int)member.getRoleId())) {
                    this.acceptTask(role, taskSet, expire);
                }
                else {
                    this.acceptMemberTask(member, taskSet, expire);
                }
            }
        }
        TeamService teamService = (TeamService)SpringUtils.getBean(TeamService.class);
    }
    
    public boolean isMemberAcceptTask(Role role, TaskSet taskSet) {
        return true;
    }
    
    public void doAcceptTaskForSingle(Role role, TaskSet taskSet, int expire) {
        this.acceptTask(role, taskSet, expire);
    }
    
    public void acceptTask(Role role, TaskSet taskSet, int expire) {
        role.getTask().acceptTask(taskSet.getSerial(), taskSet.getPara(), expire);
        role.getTask().addTaskNpcId(taskSet.getSerial(), taskSet.getSubmitNpc(), expire);
        TaskInfo taskInfo = this.getTaskInfo(role, taskSet);
        this.taskService.pushTaskInfo(role, taskInfo);
        role.save();
    }
    
    public void acceptMemberTask(Member member, TaskSet taskSet, int expire) {
        Role role = SpringUtils.getRoleService().getPlayer((long)member.getId());
        if (role == null) {
            return;
        }
        if (!this.isMemberAcceptTask(role, taskSet)) {
            return;
        }
        ThreadLocalUtil.addLocalTask(role, () -> this.acceptTask(role, taskSet, expire));
        return;
    }
    
    public void doAfterAccept(Role role, TaskSet taskSet) {
    }
    
    public void startFight(Role role, TaskSet taskSet) {
        this.setNpcFightStatus(role, taskSet, true);
    }
    
    public void setNpcFightStatus(Role role, TaskSet taskSet, boolean isInFight) {
        MapService mapService = (MapService)SpringUtils.getBean(MapService.class);
    }
    
    public TaskSet getTaskSetByType(Role role) {
        return this.getTaskSetByType(role, this.getTaskType());
    }
    
    public TaskSet getTaskSetByType(Role role, TaskType taskType) {
        List<TaskSet> acceptedTaskListMatchTaskType = this.taskService.getAcceptedTaskListMatchTaskType(role, taskType);
        if (acceptedTaskListMatchTaskType.isEmpty()) {
            return null;
        }
        return (TaskSet)acceptedTaskListMatchTaskType.get(0);
    }
    
    public abstract boolean isCreateNpcForFight(TaskSet taskSet);
    
    public boolean isDeleteTaskTeamChange() {
        return false;
    }
    
    public boolean isDeleteTaskLogout() {
        return false;
    }
    
    @Override
    public void handleTeamLeave(Role role, boolean isLeader, Team team) {
        if (!this.isDeleteTaskTeamChange()) {
            return;
        }
        TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        List<Member> memberList = team.getList();
        if (isLeader) {
            this.taskService.doDeleteTaskForTeam(memberList, (int)role.getRoleId(), taskSet);
        }
        else {
            this.taskService.deleteTaskForSingle(role, taskSet);
        }
        return;
    }
    
    @Override
    public void handleTeamLeaveMoment(Role role) {
        if (!this.isDeleteTaskTeamChange()) {
            return;
        }
        TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        this.taskService.deleteTaskForSingle(role, taskSet);
        return;
    }
    
    @Override
    public void handleTeamChangeLeader(Role role, List<Member> memberList) {
        if (!this.isDeleteTaskTeamChange()) {
            return;
        }
        TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        this.taskService.doDeleteTaskForTeam(memberList, (int)role.getRoleId(), taskSet);
        return;
    }
    
    @Override
    public void handleLogout(Role role) {
        if (!this.isDeleteTaskLogout()) {
            return;
        }
        TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        if (!this.teamService.isInTeam(role)) {
            this.taskService.deleteTask(role, taskSet);
        }
        return;
    }
    
    @Override
    public void handleLogin(Role role) {
        TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet == null) {
            return;
        }
        if (this.isDeleteTaskLogout() && !this.teamService.isInTeam(role)) {
            this.taskService.deleteTask(role, taskSet);
        }
        else {
            TaskInfo taskInfo = this.getTaskInfo(role, taskSet);
            this.taskService.pushTaskInfo(role, taskInfo);
        }
        return;
    }
    
    @Override
    public void handleHeartBeat(Role role, long prevHeartTime) {
        TaskSet taskSet = this.getTaskSetByType(role);
    }
    
    @Override
    public void handleFightEnd(FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> this.doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    }
    
    private void doFightEnd(Role role, FightEndEvent fightEndEvent) {
        try {
            if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
                return;
            }
            TaskSet taskSet = this.getTaskSetByType(role);
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
        catch (Exception e) {
            e.printStackTrace();
            LoggerUtils.error(role.getId() + "|" + this.getClass().getSimpleName() + "|" + e.getMessage(), e);
        }
    }
    
    private void doFightWin(Role role, TaskSet taskSet, FightEndEvent fightEndEvent) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (this.teamService.isInTeam(role)) {
            this.doFightWinForTeam(team, taskSet, fightEndEvent);
        }
        else {
            this.doFightWinForSingle(role, taskSet, fightEndEvent);
        }
    }
    
    public void doFightWinForTeam(Team team, TaskSet taskSet, FightEndEvent fightEndEvent) {
        int leaderRoleId = team.getLeaderRoleId();
        List<Member> members = team.getList();
        for (int i = 0; i < members.size(); ++i) {
            Member member = (Member)members.get(i);
            if (member != null && member.isInTeam() && leaderRoleId != (int)member.getRoleId()) {
                Role role = SpringUtils.getRoleService().getPlayerBy((long)member.getId());
                if (role != null) {
                    ThreadLocalUtil.addLocalTask(role, () -> this.doFightWinForSingle(role, taskSet, fightEndEvent));
                }
            }
        }
        Role role2 = SpringUtils.getRoleService().getPlayer(leaderRoleId);
        this.doFightWinForSingle(role2, taskSet, fightEndEvent);
    }
    
    public void doFightWinForSingle(Role role, TaskSet taskSet, FightEndEvent fightEndEvent) {
        try {
            if (!this.isMatchFightType(role, fightEndEvent.getFightType())) {
                return;
            }
            if (!this.isMemberSubmitTask(role, taskSet)) {
                return;
            }
            this.addActivityFinishCount(role, taskSet);
            int currPetId = (int)role.getTempCache("fight_current_pet_id", Integer.valueOf(0));
            Npc npc = SpringUtils.getMapService().getNpc(role.getTask().getTaskNpcId(taskSet.getSubmitNpc()));
            role.pushTempCache("drop_monster_name", (npc != null) ? npc.getName() : "");
            this.giveReward(role, fightEndEvent.getReward(), taskSet, currPetId);
            role.popTempCache("drop_monster_name");
            this.doBeforeDelete(role, taskSet);
            this.taskService.deleteTaskForSingle(role, taskSet);
            this.doAfterDelete(role, taskSet);
        }
        catch (Exception e) {
            e.printStackTrace();
            LoggerUtils.error(role.getId() + "|" + this.getClass().getSimpleName() + "|" + e.getMessage(), e);
        }
    }
    
    @Override
    public void handleRoleExpire(Role role) {
        Fight fight = SpringUtils.getFightNewService().getFightByRoleId((int)role.getRoleId());
        if (fight == null) {
            return;
        }
        if (!this.isMatchFightType(role, fight.getType())) {
            return;
        }
        super.handleRoleExpire(role);
        TaskSet taskSet = this.getTaskSetByType(role);
        if (taskSet != null) {
            SpringUtils.getMapService().getNpc(role.getTask().getTaskNpcId(taskSet.getSubmitNpc()));
        }
        this.doRoleExpire(role, fight);
        return;
    }
    
    public void doRoleExpire(Role role, Fight fight) {
        TeamService teamService = SpringUtils.getTeamService();
        teamService.getTeamCount(role);
    }
    
    public boolean isMemberSubmitTask(Role role, TaskSet taskSet) {
        return true;
    }
    
    public void doFightFail(Role role, TaskSet taskSet, FightEndEvent fightEndEvent) {
    }
    
    public void doFightRunAway(Role role, TaskSet taskSet, FightEndEvent fightEndEvent) {
    }
    
    public void doFightMaxRound(Role role, TaskSet taskSet, FightEndEvent fightEndEvent) {
    }
}
