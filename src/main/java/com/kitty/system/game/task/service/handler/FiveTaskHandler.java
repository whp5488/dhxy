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
import com.kitty.system.init.domain.SysDataTaskFive;
import com.kitty.common.utils.RandomUtil;
import java.util.stream.Collectors;
import com.kitty.system.game.task.pool.TaskDataPool;
import java.util.List;
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
        this.activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.FIVE_TASK);
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.FIVE;
    }
    
    @Override
    public boolean canResetTask(Role role, TaskSet taskSet) {
        return true;
    }
    
    public void acceptFiveTask(Role role, Npc npc) {
        String msg = DataGlobalFunc.needGradeJudge(0, 33, (int)role.getRelive(), (int)role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            MessagePusher.pushMessage(role, new RespMsg(msg));
            return;
        }
        if ((float)this.activityService.getLiveness(role) < (float)FiveTaskHandler.NEED_LIVENESS) {
            MessagePusher.pushMessage(role, new RespMsg(String.format("我需要精力特别旺盛的人来做五环任务，请少侠今天的活跃度达到%d再来吧#<E:17>#", Integer.valueOf(FiveTaskHandler.NEED_LIVENESS.intValue()))));
            return;
        }
        boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.FIVE_TASK);
        if (!isHave) {
            MessagePusher.pushMessage(role, new RespMsg("#<E:17>#英雄今天已经帮忙很多了,请明天再来吧"));
            return;
        }
        this.randomAcceptTask(role);
        this.activityService.increaseFinishCount(role, ActivityType.FIVE_TASK, 1);
        MessagePusher.pushMessage(role, new RespMsg("成功领取五环任务"));
        return;
    }
    
    public void randomAcceptTask(Role role) {
        if (!this.canAcceptTask(role)) {
            return;
        }
        List<SysDataTaskFive> list = (List)TaskDataPool.taskFiveList.stream().filter(e -> e.getType() == 0).collect(Collectors.toList());
        int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
        Integer taskId = Integer.valueOf(((SysDataTaskFive)list.get(random)).getSerial());
        TaskSet taskSet = this.taskService.getTaskSet((int)taskId);
        if (taskSet == null) {
            return;
        }
        this.acceptTask(role, taskSet, -1);
        return;
    }
    
    private boolean canAcceptTask(Role role) {
        return !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(Role role) {
        List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.FIVE);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(Role role) {
        long giveUpTime = role.getTask().getGiveUpTime(TaskType.LOOP);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    @Override
    public void canSubmitTask(Role role, TaskSet taskSet) {
        int submitNpc = taskSet.getSubmitNpc();
        if (submitNpc > 0) {
            Npc npc = this.mapService.getNpc(submitNpc);
            if (npc != null) {
                boolean b = this.npcService.canClickNpc(role, npc);
                if (!b) {
                    MessagePusher.pushMessage(role, new RespMsg("请前往" + npc.getName() + "处提交任务"));
                    return;
                }
            }
        }
        int submitCondition = taskSet.getSubmitCondition();
        if (submitCondition > 0 && !this.taskService.condition(role, taskSet, (byte)1)) {
            return;
        }
        this.taskService.updateSingleTargetProgress(role, taskSet);
        return;
    }
    
    private void addActivity(Role role, TaskSet taskSet) {
        int sum = this.sum();
        int count = this.count(role);
        if (count < sum && taskSet.getNeedCounter() == 1) {
            if (this.activityLivenessSet != null) {
                this.activationTips(role, (int)this.activityLivenessSet.getPerLiveness());
            }
            ++count;
            role.getTaskProgress().getFinishRecord().setv("FIVE_NOM", Integer.valueOf(count));
            this.giveReward(role, taskSet, count, sum);
        }
        if (count < sum) {
            this.randomAcceptTask(role);
        }
    }
    
    private int sum() {
        return (int)((float)this.activityLivenessSet.getMaxLiveness() / this.activityLivenessSet.getPerLiveness());
    }
    
    private int count(Role role) {
        return role.getTaskProgress().getFinishRecord().getInt("FIVE_NOM", 0);
    }
    
    @Override
    public void doAfterSubmit(Role role, TaskSet taskSet) {
        if (taskSet.getNeedCounter() == 1) {
            this.addActivity(role, taskSet);
        }
    }
    
    @Override
    public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
        TaskInfo taskInfo = this.taskService.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.FIVE)) {
            int count = this.count(role) + 1;
            taskInfo.setCounter((short)count);
            return taskInfo;
        }
        return taskInfo;
    }
    
    @Override
    public boolean canGiveUpTask(Role role, TaskSet taskSet) {
        return false;
    }
    
    @Override
    public void doFightFail(Role role, FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
    }
    
    public void giveReward(Role role, TaskSet taskSet, int cun, int sum) {
        if (cun <= sum) {
            this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf(calculate((int)role.getLevel(), (int)role.getRelive(), cun, ItemClassify.RESTYPE_COIN)), "五环任务", true, false);
            this.roleService.addCurrency(role, Const.RESTYPE_SILVER, Long.valueOf(calculate((int)role.getLevel(), (int)role.getRelive(), cun, ItemClassify.RESTYPE_SILVER)), "五环任务", true, false);
            List<Param> list = (List)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
            if (list != null) {
                ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, " 五环单人", 0);
            }
        }
    }
    
    private static long calculate(int grade, int rei, int cnt, ItemClassify type) {
        if (ItemClassify.RESTYPE_COIN == type) {
            double randomDouble = RandomUtil.getRandomDouble(Double.valueOf(70.0), Double.valueOf(80.0));
            return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(8000.0 * (double)cnt + (double)BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(850.0 / (80.0 + (double)grade) * (double)grade * randomDouble * (double)cnt)).longValue())).longValue();
        }
        if (ItemClassify.RESTYPE_SILVER == type) {
            double randomDouble = RandomUtil.getRandomDouble(Double.valueOf(13.0), Double.valueOf(14.0));
            return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(8000.0 * (double)cnt + (double)BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(850.0 / (80.0 + (double)grade) * (double)grade * randomDouble * (double)cnt)).longValue())).longValue();
        }
        return 0L;
    }
    
    static {
        NEED_LIVENESS = Float.valueOf(0.0f);
    }
}
