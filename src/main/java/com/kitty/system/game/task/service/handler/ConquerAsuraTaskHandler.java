package com.kitty.system.game.task.service.handler;

import com.kitty.system.common.data.DataGlobalFunc;
import java.util.Iterator;
import com.kitty.listener.event.TeamJoinEvent;
import com.kitty.listener.event.TeamLeaveMomentEvent;
import com.kitty.listener.event.TeamLeaveEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.LoginEvent;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.team.domain.Team;
import java.util.List;
import com.kitty.system.init.domain.vo.surround.SysDataTaskCrusadeRenegade;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.task.TaskType;
import org.springframework.stereotype.Component;

@Component
public class ConquerAsuraTaskHandler extends TaskHandler
{
    private static final String TASK_DIST = "XL";
    private static final int MIN_TEAM_COUNT = 1;
    private static final int NEED_RELIVE = 2;
    private static final int NEED_LEVEL = 120;
    private static final String NEED_LEVEL_NOT = "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#";
    private static final String TEAM_NOT = "收复修罗之行无比险恶，请务必组齐至少%d位大侠应战#<E:51>#";
    private static final String TEAM_FIGHT_NOT = "想与我一战,至少要有%d位像你一样强壮的家伙，快去组几个人来吧!#<E:18>#";
    
    public ConquerAsuraTaskHandler() {
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.XL;
    }
    
    @Override
    public boolean resetTask(Role role, TaskSet taskSet) {
        return true;
    }
    
    public void acceptTask(Role role, Npc npc) {
        this.randomAcceptTask(role);
    }
    
    public void randomAcceptTask(Role role) {
        if (!this.canAcceptTask(role)) {
            return;
        }
        List<SysDataTaskCrusadeRenegade> list = TaskDataPool.conquerAsuraList;
        int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
        Integer taskId = Integer.valueOf(((SysDataTaskCrusadeRenegade)list.get(random)).getSerial());
        TaskSet taskSet = this.taskService.getTaskSet((int)taskId);
        if (taskSet == null) {
            return;
        }
        super.acceptTask(role, taskSet, -1);
        Team team = this.teamService.getTeam((int)role.getRoleId());
        this.teamService.memberHandleThreadLocal(team, memberRole -> this.syncTask(memberRole));
        return;
    }
    
    private boolean canAcceptTask(Role role) {
        if (!this.teamService.isInTeam(role) || this.teamService.getTeamCount(role) < 1) {
            MessagePusher.pushMessage(role, new RespMsg(String.format("收复修罗之行无比险恶，请务必组齐至少%d位大侠应战#<E:51>#", Integer.valueOf(1))));
            return false;
        }
        return this.canTeamGrade(role, 2, 120, "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#") && !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(Role role) {
        List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.XL);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(Role role) {
        long giveUpTime = role.getTask().getGiveUpTime(TaskType.XL);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    public void startFight(Role role, Npc npc) {
        super.startFight(role, this.getTaskType(), npc, 1, 2, 120, "想与我一战,至少要有%d位像你一样强壮的家伙，快去组几个人来吧!#<E:18>#", "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#");
    }
    
    private void addActivity(Role role) {
        int count = this.count(role);
        ++count;
        role.getTaskProgress().getFinishCounts().setv(ActivityType.XL_TASK.getActivityId(), Integer.valueOf(count));
        int step = role.getTaskProgress().getFinishTallyCounts().getInt("XL", 1);
        role.getTaskProgress().getFinishTallyCounts().setv("XL", Integer.valueOf(step + 1));
    }
    
    private int count(Role role) {
        return role.getTaskProgress().getFinishCounts().getInt(ActivityType.XL_TASK.getActivityId(), 0);
    }
    
    @Override
    public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
        TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.XL)) {
            DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
            if (doingTask != null) {
                int step = role.getTaskProgress().getFinishTallyCounts().getInt("XL", 1);
                if (step > 10) {
                    step = 1;
                    role.getTaskProgress().getFinishTallyCounts().setv("XL", Integer.valueOf(step));
                }
                taskInfo.setCounter((short)step);
            }
        }
        return taskInfo;
    }
    
    @Override
    public boolean canGiveUpTask(Role role, TaskSet taskSet) {
        return true;
    }
    
    @Override
    public void doFightFail(Role role, FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
    }
    
    @Override
    public void giveReward(Role role, TaskSet taskSet) {
        int count = this.count(role);
        int cnt = role.getTaskProgress().getFinishTallyCounts().getInt("XL", 1);
        int teamCount = this.teamService.getTeamCount(role);
        long heroExp = calculate((int)role.getLevel(), (int)role.getRelive(), cnt, count, teamCount, ItemClassify.HERO_EXP);
        this.roleService.addExp(role, heroExp, "", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if ((int)combatPet > 0) {
            Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
            long calculate = calculate((int)pet.getPetLv(), (int)pet.getRebirth(), cnt, count, teamCount, ItemClassify.PET_EXP);
            this.petService.addExp(role, pet, calculate, true);
        }
        this.addActivity(role);
        if (this.teamService.isTeamLeader(role) && count < ActivityType.XL_TASK.getTotalCount()) {
            this.randomAcceptTask(role);
        }
    }
    
    private static long calculate(int grade, int rei, int cnt, int todayCnt, int factor, ItemClassify type) {
        double fold = ActivityType.XL_TASK.getFold();
        double exp = 0.0;
        if (ItemClassify.HERO_EXP == type) {
            exp = (double)factor * 0.0866 * (1.6E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0 * 1.2 * (6.0E-4 * (double)cnt + 0.83)) * (double)(1 - (10000 + todayCnt) / 10121);
        }
        if (ItemClassify.PET_EXP == type) {
            exp = 2.4 * (double)factor * 0.0866 * (1.6E7 * (1.0 + (double)(grade + rei * 15) / 140.0 * 2.5) / 30.0 * 1.2 * (6.0E-4 * (double)cnt + 0.83)) * (double)(1 - (10000 + todayCnt) / 10121);
        }
        return BigDecimalUtil.decimalScaleDown(0, (fold > 1.0) ? (exp * fold) : exp).longValue();
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        if (!this.teamService.isInTeam(role)) {
            this.deleteTask(role);
        }
    }
    
    @EventHandler({ EventType.TEAM_LEAVE })
    public void handleTeamLeaveEvent(TeamLeaveEvent teamLeaveEvent) {
        Role role = teamLeaveEvent.getRole();
        this.deleteTask(role);
    }
    
    @EventHandler({ EventType.TEAM_LEAVE_MOMENT })
    public void handleTeamLeaveMonmentEvent(TeamLeaveMomentEvent teamLeaveMomentEvent) {
        Role role = teamLeaveMomentEvent.getRole();
        this.deleteTask(role);
    }
    
    @EventHandler({ EventType.TEAM_JOIN })
    public void handleTeamJoinEvent(TeamJoinEvent joinEvent) {
        Role role = joinEvent.getRole();
        this.syncTask(role);
    }
    
    public void deleteTask(Role role) {
        List<SysDataTaskCrusadeRenegade> list = TaskDataPool.conquerAsuraList;
        for (SysDataTaskCrusadeRenegade obj : list) {
            if (role.getTask().isAccepted(obj.getSerial())) {
                this.delTask(role, this.taskService.getTaskSet(obj.getSerial()));
            }
            else {
                continue;
            }
        }
        role.getTaskProgress().getFinishTallyCounts().setv("XL", 1);
        role.save();
    }
    
    public void syncTask(Role role) {
        if (DataGlobalFunc.needGradeJudge(2, 120, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足") != null) {
            return;
        }
        Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            Role leader = this.roleService.getPlayer(team.getLeaderRoleId());
            if (leader != null && !leader.getRoleId().equals(role.getRoleId())) {
                List<SysDataTaskCrusadeRenegade> list = TaskDataPool.conquerAsuraList;
                for (SysDataTaskCrusadeRenegade obj : list) {
                    DoingTask doingTask = leader.getTask().getDoingTask().get(obj.getSerial());
                    if (doingTask != null) {
                        this.deleteTask(role);
                        int anInt = leader.getTaskProgress().getFinishTallyCounts().getInt("XL", 1);
                        role.getTaskProgress().getFinishTallyCounts().setv("XL", anInt);
                        this.syncTeamTask(role, doingTask);
                        break;
                    }
                }
            }
        }
    }
    
    public static int getMonster(int level, int serial) {
        if (level >= 1080 && level <= 1100) {
            serial += 20;
        }
        else if (level >= 1101 && level <= 1120) {
            serial += 40;
        }
        else if (level >= 2100 && level <= 2110) {
            serial += 40;
        }
        else if (level >= 2111 && level <= 2120) {
            serial += 80;
        }
        else if (level >= 2121 && level <= 2130) {
            serial += 100;
        }
        else if (level >= 2131 && level <= 2140) {
            serial += 120;
        }
        else if (level >= 3120 && level <= 3130) {
            serial += 140;
        }
        else if (level >= 3131 && level <= 3140) {
            serial += 160;
        }
        else if (level >= 3141 && level <= 3150) {
            serial += 180;
        }
        else if (level >= 3151 && level <= 3160) {
            serial += 200;
        }
        else if (level >= 3161 && level <= 3180) {
            serial += 220;
        }
        return serial;
    }
}
