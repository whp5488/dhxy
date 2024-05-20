
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
import com.kitty.mina.message.Message;
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
public class CrusadeRenegadeTaskHandler extends TaskHandler
{
    private static final String TASK_DIST = "TFPZ";
    private static final int MIN_TEAM_COUNT = 1;
    private static final int NEED_RELIVE = 2;
    private static final int NEED_LEVEL = 120;
    private static final String NEED_LEVEL_NOT = "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#";
    private static final String TEAM_NOT = "讨伐叛贼之行无比险恶，请务必组齐至少%d位大侠应战#<E:51>#";
    private static final String TEAM_FIGHT_NOT = "想与我一战,至少要有%d位像你一样强壮的家伙，快去组几个人来吧!#<E:18>#";
    
    @Override
    public TaskType getTaskType() {
        return TaskType.CRUSADE_RENEGADE;
    }
    
    @Override
    public boolean resetTask(final Role role, final TaskSet taskSet) {
        return true;
    }
    
    public void acceptTask(final Role role, final Npc npc) {
        this.randomAcceptTask(role);
    }
    
    public void randomAcceptTask(final Role role) {
        if (!this.canAcceptTask(role)) {
            return;
        }
        final List<SysDataTaskCrusadeRenegade> list = TaskDataPool.crusadeRenegadeList;
        final int random = RandomUtil.round(0, list.size() - 1);
        final Integer taskId = list.get(random).getSerial();
        final TaskSet taskSet = this.taskService.getTaskSet(taskId);
        if (taskSet == null) {
            return;
        }
        this.acceptTask(role, taskSet, -1);
        final Team team = this.teamService.getTeam(role.getRoleId());
        this.teamService.memberHandleThreadLocal(team, memberRole -> this.syncTask(memberRole));
    }
    
    private boolean canAcceptTask(final Role role) {
        if (!this.teamService.isInTeam(role) || this.teamService.getTeamCount(role) < MIN_TEAM_COUNT) {
            MessagePusher.pushMessage(role, new RespMsg(String.format("讨伐叛贼之行无比险恶，请务必组齐至少%d位大侠应战#<E:51>#", MIN_TEAM_COUNT)));
            return false;
        }
        return this.canTeamGrade(role, 2, 120, "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#") && !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(final Role role) {
        final List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.CRUSADE_RENEGADE);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(final Role role) {
        final long giveUpTime = role.getTask().getGiveUpTime(TaskType.CRUSADE_RENEGADE);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    public void startFight(final Role role, final Npc npc) {
        this.startFight(role, this.getTaskType(), npc, MIN_TEAM_COUNT, 2, 120, "想与我一战,至少要有%d位像你一样强壮的家伙，快去组几个人来吧!#<E:18>#", "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#");
    }
    
    private void addActivity(final Role role) {
        int count = this.count(role);
        ++count;
        role.getTaskProgress().getFinishCounts().setv(ActivityType.CRUSADE_RENEGADE.getActivityId(), count);
        final int step = role.getTaskProgress().getFinishTallyCounts().getInt("TFPZ", 1);
        role.getTaskProgress().getFinishTallyCounts().setv("TFPZ", step + 1);
    }
    
    private int count(final Role role) {
        return role.getTaskProgress().getFinishCounts().getInt(ActivityType.CRUSADE_RENEGADE.getActivityId(), 0);
    }
    
    @Override
    public TaskInfo getTaskInfo(final Role role, final TaskSet taskSet) {
        final TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.CRUSADE_RENEGADE)) {
            final DoingTask doingTask = role.getTask().getDoingTask().get(taskSet.getSerial());
            if (doingTask != null) {
                int step = role.getTaskProgress().getFinishTallyCounts().getInt("TFPZ", 1);
                if (step > 10) {
                    step = 1;
                    role.getTaskProgress().getFinishTallyCounts().setv("TFPZ", step);
                }
                taskInfo.setCounter((short)step);
            }
        }
        return taskInfo;
    }
    
    @Override
    public boolean canGiveUpTask(final Role role, final TaskSet taskSet) {
        return true;
    }
    
    @Override
    public void doFightFail(final Role role, final FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
    }
    
    @Override
    public void giveReward(final Role role, final TaskSet taskSet) {
        final int count = this.count(role);
        final int cnt = role.getTaskProgress().getFinishTallyCounts().getInt("TFPZ", 1);
        final int teamCount = this.teamService.getTeamCount(role);
        final long heroExp = calculate(role.getLevel(), role.getRelive(), cnt, count, teamCount, ItemClassify.HERO_EXP);
        this.roleService.addExp(role, heroExp, "", true);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet > 0) {
            final Pet pet = this.petService.selectPet(role).get(combatPet);
            final long calculate = calculate(pet.getPetLv(), pet.getRebirth(), cnt, count, teamCount, ItemClassify.PET_EXP);
            this.petService.addExp(role, pet, calculate, true);
        }
        this.addActivity(role);
        if (this.teamService.isTeamLeader(role) && count < ActivityType.CRUSADE_RENEGADE.getTotalCount()) {
            this.randomAcceptTask(role);
        }
    }
    
    private static long calculate(final int grade, final int rei, final int cnt, final int todayCnt, final int factor, final ItemClassify type) {
        final double fold = ActivityType.CRUSADE_RENEGADE.getFold();
        double exp = 0.0;
        if (ItemClassify.HERO_EXP == type) {
            exp = factor * 0.0866 * (1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 * 1.2 * (6.0E-4 * cnt + 0.83)) * (1 - (10000 + todayCnt) / 10061);
        }
        if (ItemClassify.PET_EXP == type) {
            exp = 2.4 * factor * 0.0866 * (1.6E7 * (1.0 + (grade + rei * 15) / 140.0 * 2.5) / 30.0 * 1.2 * (6.0E-4 * cnt + 0.83)) * (1 - (10000 + todayCnt) / 10061);
        }
        return BigDecimalUtil.decimalScaleDown(0, (fold > 1.0) ? (exp * fold) : exp).longValue();
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        if (!this.teamService.isInTeam(role)) {
            this.deleteTask(role);
        }
    }
    
    @EventHandler({ EventType.TEAM_LEAVE })
    public void handleTeamLeaveEvent(final TeamLeaveEvent teamLeaveEvent) {
        final Role role = teamLeaveEvent.getRole();
        this.deleteTask(role);
    }
    
    @EventHandler({ EventType.TEAM_LEAVE_MOMENT })
    public void handleTeamLeaveMonmentEvent(final TeamLeaveMomentEvent teamLeaveMomentEvent) {
        final Role role = teamLeaveMomentEvent.getRole();
        this.deleteTask(role);
    }
    
    @EventHandler({ EventType.TEAM_JOIN })
    public void handleTeamJoinEvent(final TeamJoinEvent joinEvent) {
        final Role role = joinEvent.getRole();
        this.syncTask(role);
    }
    
    public void deleteTask(final Role role) {
        final List<SysDataTaskCrusadeRenegade> list = TaskDataPool.crusadeRenegadeList;
        for (final SysDataTaskCrusadeRenegade obj : list) {
            if (role.getTask().isAccepted(obj.getSerial())) {
                this.delTask(role, this.taskService.getTaskSet(obj.getSerial()));
            }
        }
        role.getTaskProgress().getFinishTallyCounts().setv("TFPZ", 1);
        role.save();
    }
    
    public void syncTask(final Role role) {
        if (DataGlobalFunc.needGradeJudge(2, 120, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足") != null) {
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final Role leader = this.roleService.getPlayer(team.getLeaderRoleId());
            if (leader != null && !leader.getRoleId().equals(role.getRoleId())) {
                final List<SysDataTaskCrusadeRenegade> list = TaskDataPool.crusadeRenegadeList;
                for (final SysDataTaskCrusadeRenegade obj : list) {
                    final DoingTask doingTask = leader.getTask().getDoingTask().get(obj.getSerial());
                    if (doingTask != null) {
                        this.deleteTask(role);
                        final int anInt = leader.getTaskProgress().getFinishTallyCounts().getInt("TFPZ", 1);
                        role.getTaskProgress().getFinishTallyCounts().setv("TFPZ", anInt);
                        this.syncTeamTask(role, doingTask);
                        break;
                    }
                }
            }
        }
    }
    
    public static int getMonster(final int level, int serial) {
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
