
package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.activity.service.ActivityService;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.init.domain.SysDataTaskLoop;
import java.util.List;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.vo.Npc;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.task.TaskType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LoopTaskHandler extends TaskHandler
{
    private static final int NEED_RELIVE = 1;
    private static final int NEED_LEVEL = 80;
    Logger logger;
    
    public LoopTaskHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.LOOP;
    }
    
    @Override
    public boolean resetTask(final Role role, final TaskSet taskSet) {
        return this.roleService.isCanWeeklyReset(role);
    }
    
    @Override
    public boolean canResetTask(final Role role, final TaskSet taskSet) {
        return this.roleService.isCanWeeklyReset(role);
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
        this.addActivityCount(role, taskSet);
    }
    
    public void acceptLoopTask(final Role role, final Npc npc) {
        final String msg = DataGlobalFunc.needGradeJudge(1, 80, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            MessagePusher.pushMessage(role, new RespMsg(msg));
            return;
        }
        final String content = this.getNpcContentNotAccept(role);
        if (content != null) {
            MessagePusher.pushMessage(role, new RespMsg(content));
            return;
        }
        final int count = role.getTaskProgress().getLoop() - 1;
        role.getTaskProgress().setLoop((count < 0) ? 0 : count);
        role.getTaskProgress().getFinishCounts().setv(ActivityType.LOOP.getActivityId(), 0);
        this.acceptTask(role);
    }
    
    public String getNpcContentNotAccept(final Role role) {
        if (!this.canAcceptTask(role)) {
            return "少侠的任务还没有完成啊#<E:32>#，完成后再来找我吧";
        }
        if (role.getTaskProgress().getLoop() <= 0) {
            return "英雄本周已经打了很多，请下周再来吧";
        }
        if (this.isWaitGiveUpCD(role)) {
            return "你刚才已经放弃200环任务，还是休息一阵再来吧";
        }
        return null;
    }
    
    public void acceptTask(final Role role) {
        if (!this.canAcceptTask(role)) {
            return;
        }
        final TaskSet randTaskSet = this.getRandomSchoolTask(role);
        if (randTaskSet == null) {
            return;
        }
        this.acceptTask(role, randTaskSet, -1);
    }
    
    private TaskSet getRandomSchoolTask(final Role role) {
        final List<SysDataTaskLoop> list = TaskDataPool.taskLoopList.stream().filter(e -> e.getKind() == 0).collect(Collectors.toList());
        final int random = (int)(Math.random() * list.size());
        final Integer taskId = list.get(random).getSerial();
        return this.taskService.getTaskSet(taskId);
    }
    
    private boolean canAcceptTask(final Role role) {
        final ActivityService activityService = SpringUtils.getBean(ActivityService.class);
        final boolean isHave = activityService.isHaveRemainCount(role, ActivityType.LOOP);
        return isHave && !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(final Role role) {
        final List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.LOOP);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(final Role role) {
        final long giveUpTime = role.getTask().getGiveUpTime(TaskType.LOOP);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    @Override
    public TaskInfo getTaskInfo(final Role role, final TaskSet taskSet) {
        final TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.LOOP)) {
            final int doStep = this.activityService.getFinishCount(role, ActivityType.LOOP, 0);
            taskInfo.setCounter((short)doStep);
        }
        return taskInfo;
    }
    
    @Override
    public boolean canGiveUpTask(final Role role, final TaskSet taskSet) {
        return false;
    }
    
    @Override
    public void giveReward(final Role role, final TaskSet taskSet) {
        final int doStep = this.activityService.getFinishCount(role, ActivityType.LOOP, 1);
        if (taskSet.getTaskReward() != null) {
            this.roleService.addCurrency(role, Const.RESTYPE_COIN, calculate(role.getLevel(), role.getRelive(), doStep, Const.RESTYPE_COIN), "任务奖励", true, false);
            this.roleService.addCurrency(role, Const.RESTYPE_SILVER, calculate(role.getLevel(), role.getRelive(), doStep, Const.RESTYPE_SILVER), "任务奖励", true, false);
        }
        if (doStep < ActivityType.LOOP.getTotalCount()) {
            this.acceptTask(role);
        }
    }
    
    private void addActivityCount(final Role role, final TaskSet taskSet) {
        final int step = 1;
        if (this.activityService.isHaveRemainCount(role, ActivityType.LOOP)) {
            this.activityService.addFinishCount(role, ActivityType.LOOP, step);
        }
    }
    
    private static long calculate(final int grade, final int rei, final int cnt, final int type) {
        final double fold = ActivityType.LOOP.getFold();
        double exp = 0.0;
        if (type == Const.RESTYPE_SILVER) {
            exp = 30000.0 + Math.floor((cnt - 1) / 10.0) * 2760.0 + (cnt - Math.floor((cnt - 1) / 10.0) * 10.0 - 1.0) * 4166.0;
        }
        else if (type == Const.RESTYPE_COIN) {
            exp = 30000.0 + Math.floor((cnt - 1) / 10.0) * 2760.0 + (cnt - Math.floor((cnt - 1) / 10.0) * 10.0 - 1.0) * 4166.0;
        }
        return BigDecimalUtil.decimalScaleDown(0, (fold > 1.0) ? (exp * fold) : exp).longValue();
    }
}
