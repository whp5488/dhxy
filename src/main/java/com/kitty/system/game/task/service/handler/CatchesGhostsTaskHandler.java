package com.kitty.system.game.task.service.handler;

import com.kitty.system.game.vo.utils.Const;
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
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.team.domain.Team;
import java.util.List;
import com.kitty.system.init.domain.SysDataTaskCatchesGhosts;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import org.springframework.stereotype.Component;

@Component
public class CatchesGhostsTaskHandler extends TaskHandler
{
    private static final String TASK_DIST = "ZG";
    private static final int MIN_TEAM_COUNT = 1;
    private static final int NEED_RELIVE = 0;
    private static final int NEED_LEVEL = 22;
    private static final int LIMIT_RELIVE = 1;
    private static final int LIMIT_LEVEL = 80;
    private static final String NEED_LEVEL_NOT = "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#";
    private static final String TEAM_NOT = "抓鬼之行无比险恶，请务必组齐至少%d位大侠应战#<E:51>#";
    private static final String TEAM_FIGHT_NOT = "想与我一战,至少要有%d位像你一样强壮的家伙，快去组几个人来吧!#<E:18>#";
    private static final String PET_EXP_TIPS;
    ActivityLivenessSet activityLivenessSet;
    
    public CatchesGhostsTaskHandler() {
        this.activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.CATCHES_GHOSTS_TASK);
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.CATCHES_GHOSTS;
    }
    
    @Override
    public boolean resetTask(Role role, TaskSet taskSet) {
        return true;
    }
    
    public void acceptCatchesGhostsTask(Role role, Npc npc) {
        this.randomAcceptTask(role);
    }
    
    public void randomAcceptTask(Role role) {
        if (!this.canAcceptTask(role)) {
            return;
        }
        List<SysDataTaskCatchesGhosts> list = TaskDataPool.taskCatchesGhosts;
        int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
        Integer taskId = Integer.valueOf(((SysDataTaskCatchesGhosts)list.get(random)).getSerial());
        TaskSet taskSet = this.taskService.getTaskSet((int)taskId);
        if (taskSet == null) {
            return;
        }
        this.acceptTask(role, taskSet, -1);
        Team team = this.teamService.getTeam((int)role.getRoleId());
        this.teamService.memberHandleThreadLocal(team, memberRole -> this.syncTask(memberRole));
        return;
    }
    
    private boolean canAcceptTask(Role role) {
        if (!this.teamService.isInTeam(role) || this.teamService.getTeamCount(role) < 1) {
            MessagePusher.pushMessage(role, new RespMsg(String.format("抓鬼之行无比险恶，请务必组齐至少%d位大侠应战#<E:51>#", Integer.valueOf(1))));
            return false;
        }
        return this.canTeamGrade(role, 0, 22, "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#") && !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(Role role) {
        List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.CATCHES_GHOSTS);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(Role role) {
        long giveUpTime = role.getTask().getGiveUpTime(TaskType.CATCHES_GHOSTS);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    public void startFight(Role role, Npc npc) {
        super.startFight(role, this.getTaskType(), npc, 1, 0, 22, "想与我一战,至少要有%d位像你一样强壮的家伙，快去组几个人来吧!#<E:18>#", "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#");
    }
    
    private void addActivity(Role role) {
        int sum = this.sum();
        int count = this.count(role);
        if (count < sum && this.activityLivenessSet != null) {
            this.activationTips(role, (int)this.activityLivenessSet.getPerLiveness());
        }
        ++count;
        role.getTaskProgress().getFinishCounts().setv(ActivityType.CATCHES_GHOSTS_TASK.getActivityId(), Integer.valueOf(count));
        int step = role.getTaskProgress().getFinishTallyCounts().getInt("ZG", 1);
        role.getTaskProgress().getFinishTallyCounts().setv("ZG", Integer.valueOf(step + 1));
    }
    
    private int sum() {
        return (int)((float)this.activityLivenessSet.getMaxLiveness() / this.activityLivenessSet.getPerLiveness());
    }
    
    private int count(Role role) {
        return role.getTaskProgress().getFinishCounts().getInt(ActivityType.CATCHES_GHOSTS_TASK.getActivityId(), 0);
    }
    
    @Override
    public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
        TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.CATCHES_GHOSTS)) {
            DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
            if (doingTask != null) {
                int step = role.getTaskProgress().getFinishTallyCounts().getInt("ZG", 1);
                if (step > 10) {
                    step = 1;
                    role.getTaskProgress().getFinishTallyCounts().setv("ZG", Integer.valueOf(step));
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
        int sum = this.sum();
        int cnt = role.getTaskProgress().getFinishTallyCounts().getInt("ZG", 1);
        int teamCount = this.teamService.getTeamCount(role);
        int current = (int)role.getRelive() * 1000 + (int)role.getLevel();
        int limit = 1080;
        if (current < limit) {
            if (count <= sum) {
                List<Param> list = (List)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
                if (list != null) {
                    ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "钟馗抓鬼", 0);
                }
            }
            long heroExp = calculate((int)role.getLevel(), (int)role.getRelive(), cnt, count, teamCount, ItemClassify.HERO_EXP);
            this.roleService.addExp(role, heroExp, "", true);
            Integer combatPet = role.getExtendBox().getCombatPet();
            if ((int)combatPet > 0) {
                boolean flag = true;
                Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
                long calculate = calculate((int)pet.getPetLv(), (int)pet.getRebirth(), cnt, count, teamCount, ItemClassify.PET_EXP);
                if (count > sum && calculate > 0L) {
                    flag = false;
                    MessagePusher.pushMessage(role, new RespMsg(String.format(CatchesGhostsTaskHandler.PET_EXP_TIPS, pet.getPetName(), Long.valueOf(calculate))));
                }
                this.petService.addExp(role, pet, calculate, flag);
            }
            this.addActivity(role);
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("大侠江湖历练过高,无法从小鬼身上获取经验。"));
        }
        if (this.teamService.isTeamLeader(role) && count < 60) {
            this.randomAcceptTask(role);
        }
    }
    
    private static long calculate(int grade, int rei, int cnt, int todayCnt, int factor, ItemClassify type) {
        double fold = ActivityType.CATCHES_GHOSTS_TASK.getFold();
        double exp = 0.0;
        if (ItemClassify.HERO_EXP == type) {
            exp = (double)factor * 0.0866 * (1.6E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0 * 1.2 * (6.0E-4 * (double)cnt + 0.83)) * (double)(1 - (10000 + todayCnt) / 10061);
        }
        if (ItemClassify.PET_EXP == type) {
            exp = 2.4 * (double)factor * 0.0866 * (1.6E7 * (1.0 + (double)(grade + rei * 15) / 140.0 * 2.5) / 30.0 * 1.2 * (6.0E-4 * (double)cnt + 0.83)) * (double)(1 - (10000 + todayCnt) / 10061);
        }
        return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0) ? (exp * fold) : exp)).longValue();
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
        List<SysDataTaskCatchesGhosts> list = TaskDataPool.taskCatchesGhosts;
        for (SysDataTaskCatchesGhosts catchesGhosts : list) {
            if (role.getTask().isAccepted(catchesGhosts.getSerial())) {
                this.delTask(role, this.taskService.getTaskSet(catchesGhosts.getSerial()));
            }
            else {
                continue;
            }
        }
        role.getTaskProgress().getFinishTallyCounts().setv("ZG", Integer.valueOf(1));
        role.save();
    }
    
    public void syncTask(Role role) {
        if (DataGlobalFunc.needGradeJudge(0, 22, (int)role.getRelive(), (int)role.getLevel(), "你的#<R>角色等级#不足") != null) {
            return;
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            Role leader = this.roleService.getPlayer(team.getLeaderRoleId());
            if (leader != null && !leader.getRoleId().equals(role.getRoleId())) {
                List<SysDataTaskCatchesGhosts> list = TaskDataPool.taskCatchesGhosts;
                for (SysDataTaskCatchesGhosts catchesGhosts : list) {
                    DoingTask doingTask = (DoingTask)leader.getTask().getDoingTask().get(Integer.valueOf(catchesGhosts.getSerial()));
                    if (doingTask != null) {
                        this.deleteTask(role);
                        int anInt = leader.getTaskProgress().getFinishTallyCounts().getInt("ZG", 1);
                        role.getTaskProgress().getFinishTallyCounts().setv("ZG", Integer.valueOf(anInt));
                        this.syncTeamTask(role, doingTask);
                        break;
                    }
                    continue;
                }
            }
        }
        return;
    }
    
    static {
        PET_EXP_TIPS = "为善最乐！你的召唤兽#<r:7,g:234,b:93>%s#获得了召唤兽经验#<IR" + Const.RES_TYPE_EXP_PET + ">##<r:7,g:234,b:93>%d#";
    }
}
