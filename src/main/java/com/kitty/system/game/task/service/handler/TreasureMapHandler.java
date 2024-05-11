
package com.kitty.system.game.task.service.handler;

import com.kitty.system.game.bag.service.BagService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.game.task.message.vo.TaskInfo;
import java.util.List;
import com.kitty.system.init.domain.SysDataTaskTreasureMap;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.task.TaskType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class TreasureMapHandler extends TaskHandler
{
    Logger logger;
    private static final int NEED_RELIVE = 0;
    private static final int NEED_LEVEL = 33;
    
    public TreasureMapHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.TREASURE_MAP;
    }
    
    @Override
    public void canSubmitTask(final Role role, final TaskSet taskSet) {
        this.addActivityCount(role, taskSet);
    }
    
    public void acceptTreasureMapTask(final Role role, final Npc npc) {
        final String content = this.getNpcContentNotAccept(role);
        if (content != null) {
            MessagePusher.pushMessage(role, new RespMsg(content));
            return;
        }
        this.acceptTask(role);
    }
    
    public String getNpcContentNotAccept(final Role role) {
        final String msg = DataGlobalFunc.needGradeJudge(0, 33, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            return msg;
        }
        final ActivityService activityService = SpringUtils.getBean(ActivityService.class);
        final boolean isHave = activityService.isHaveRemainCount(role, ActivityType.TREASURE_MAP_TASK);
        if (!isHave) {
            return "英雄今天已经打了很多，请明天再来吧";
        }
        if (this.isWaitGiveUpCD(role)) {
            return "你刚才已经放弃宝图任务，还是休息一阵再来吧";
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
        final List<SysDataTaskTreasureMap> list = TaskDataPool.taskTreasureMap;
        final int random = (int)(Math.random() * list.size());
        final Integer taskId = list.get(random).getSerial();
        return this.taskService.getTaskSet(taskId);
    }
    
    private boolean canAcceptTask(final Role role) {
        final ActivityService activityService = SpringUtils.getBean(ActivityService.class);
        final boolean isHave = activityService.isHaveRemainCount(role, ActivityType.TREASURE_MAP_TASK);
        return isHave && !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(final Role role) {
        final List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.TREASURE_MAP);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(final Role role) {
        final long giveUpTime = role.getTask().getGiveUpTime(TaskType.TREASURE_MAP);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    @Override
    public TaskInfo getTaskInfo(final Role role, final TaskSet taskSet) {
        final TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.TREASURE_MAP)) {
            final int doStep = this.activityService.getFinishCount(role, ActivityType.TREASURE_MAP_TASK, 1);
            taskInfo.setCounter((short)doStep);
        }
        return taskInfo;
    }
    
    @Override
    public boolean canGiveUpTask(final Role role, final TaskSet taskSet) {
        return true;
    }
    
    @Override
    public void giveReward(final Role role, final TaskSet taskSet) {
        this.addActivityCount(role, taskSet);
        final int doStep = this.activityService.getFinishCount(role, ActivityType.TREASURE_MAP_TASK, 1);
        if (doStep <= ActivityType.TREASURE_MAP_TASK.getTotalCount()) {
            this.acceptTask(role);
        }
        final List<Param> list = JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
        SpringUtils.getBean(BagService.class).sendReward(role, list, "藏宝图", 0);
    }
    
    private void addActivityCount(final Role role, final TaskSet taskSet) {
        final int step = 1;
        if (this.activityService.isHaveRemainCount(role, ActivityType.TREASURE_MAP_TASK)) {
            final ActivityService activityService = SpringUtils.getBean(ActivityService.class);
            activityService.addFinishCount(role, ActivityType.TREASURE_MAP_TASK, step);
            this.activationTips(role, step);
        }
    }
}
