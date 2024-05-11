
package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.common.utils.RandomUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.init.domain.SysDataTaskFive;
import java.util.List;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import org.springframework.stereotype.Component;

@Component
public class FiveTaskHandler extends TaskHandler
{
    public static final String TASK_DIST = "FIVE_NOM";
    private static final int NEED_RELIVE = 0;
    private static final int NEED_LEVEL = 33;
    private static final Float NEED_LIVENESS;
    private static final String NEED_LIVENESS_TIPS = "我需要精力特别旺盛的人来做五环任务，请少侠今天的活跃度达到%d再来吧#<E:17>#";
    ActivityLivenessSet activityLivenessSet;
    
    public FiveTaskHandler() {
        this.activityLivenessSet = ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.FIVE_TASK);
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.FIVE;
    }
    
    @Override
    public boolean canResetTask(final Role role, final TaskSet taskSet) {
        return true;
    }
    
    public void acceptFiveTask(final Role role, final Npc npc) {
        final String msg = DataGlobalFunc.needGradeJudge(0, 33, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            MessagePusher.pushMessage(role, new RespMsg(msg));
            return;
        }
        if (this.activityService.getLiveness(role) < FiveTaskHandler.NEED_LIVENESS) {
            MessagePusher.pushMessage(role, new RespMsg(String.format("我需要精力特别旺盛的人来做五环任务，请少侠今天的活跃度达到%d再来吧#<E:17>#", FiveTaskHandler.NEED_LIVENESS.intValue())));
            return;
        }
        final boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.FIVE_TASK);
        if (!isHave) {
            MessagePusher.pushMessage(role, new RespMsg("#<E:17>#英雄今天已经帮忙很多了,请明天再来吧"));
            return;
        }
        this.randomAcceptTask(role);
        this.activityService.increaseFinishCount(role, ActivityType.FIVE_TASK, 1);
        //MessagePusher.pushMessage(role, new RespMsg("成功领取五环任务"));
        MessagePusher.pushMessage(role, new RespMsg("成功领取地宫任务除魔"));
    }
    
    public void randomAcceptTask(final Role role) {
        if (!this.canAcceptTask(role)) {
            return;
        }
        final List<SysDataTaskFive> list = TaskDataPool.taskFiveList.stream().filter(e -> e.getType() == 0).collect(Collectors.toList());
        final int random = RandomUtil.round(0, list.size() - 1);
        final Integer taskId = list.get(random).getSerial();
        final TaskSet taskSet = this.taskService.getTaskSet(taskId);
        if (taskSet == null) {
            return;
        }
        this.acceptTask(role, taskSet, -1);
    }
    
    private boolean canAcceptTask(final Role role) {
        return !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(final Role role) {
        final List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.FIVE);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(final Role role) {
        final long giveUpTime = role.getTask().getGiveUpTime(TaskType.LOOP);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
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
    
    private void addActivity(final Role role, final TaskSet taskSet) {
        final int sum = this.sum();
        int count = this.count(role);
        if (count < sum && taskSet.getNeedCounter() == 1) {
            if (this.activityLivenessSet != null) {
                this.activationTips(role, (int)this.activityLivenessSet.getPerLiveness());
            }
            ++count;
            role.getTaskProgress().getFinishRecord().setv("FIVE_NOM", count);
            this.giveReward(role, taskSet, count, sum);
        }
        if (count < sum) {
            this.randomAcceptTask(role);
        }
    }
    
    private int sum() {
        return (int)(this.activityLivenessSet.getMaxLiveness() / this.activityLivenessSet.getPerLiveness());
    }
    
    private int count(final Role role) {
        return role.getTaskProgress().getFinishRecord().getInt("FIVE_NOM", 0);
    }
    
    @Override
    public void doAfterSubmit(final Role role, final TaskSet taskSet) {
        if (taskSet.getNeedCounter() == 1) {
            this.addActivity(role, taskSet);
        }
    }
    
    @Override
    public TaskInfo getTaskInfo(final Role role, final TaskSet taskSet) {
        final TaskInfo taskInfo = this.taskService.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.FIVE)) {
            final int count = this.count(role) + 1;
            taskInfo.setCounter((short)count);
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
    
    public void giveReward(final Role role, final TaskSet taskSet, final int cun, final int sum) {
        if (cun <= sum) {
            this.roleService.addCurrency(role, Const.RESTYPE_COIN, calculate(role.getLevel(), role.getRelive(), cun, ItemClassify.RESTYPE_COIN), "五环任务", true, false);
            this.roleService.addCurrency(role, Const.RESTYPE_SILVER, calculate(role.getLevel(), role.getRelive(), cun, ItemClassify.RESTYPE_SILVER), "五环任务", true, false);
            final List<Param> list = JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
            if (list != null) {
                SpringUtils.getBean(BagService.class).sendReward(role, list, " 五环单人", 0);
            }
        }
    }
    
    private static long calculate(final int grade, final int rei, final int cnt, final ItemClassify type) {
        if (ItemClassify.RESTYPE_COIN == type) {
            final double randomDouble = RandomUtil.getRandomDouble(70.0, 80.0);
            return BigDecimalUtil.decimalScaleDown(0, 8000.0 * cnt + BigDecimalUtil.decimalScaleDown(0, 850.0 / (80.0 + grade) * grade * randomDouble * cnt).longValue()).longValue();
        }
        if (ItemClassify.RESTYPE_SILVER == type) {
            final double randomDouble = RandomUtil.getRandomDouble(13.0, 14.0);
            return BigDecimalUtil.decimalScaleDown(0, 8000.0 * cnt + BigDecimalUtil.decimalScaleDown(0, 850.0 / (80.0 + grade) * grade * randomDouble * cnt).longValue()).longValue();
        }
        return 0L;
    }
    
    static {
        NEED_LIVENESS = 0.0f;
    }
}
