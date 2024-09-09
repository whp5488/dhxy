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
    public void canSubmitTask(Role role, TaskSet taskSet) {
        this.addActivityCount(role, taskSet);
    }
    
    public void acceptTreasureMapTask(Role role, Npc npc) {
        String content = this.getNpcContentNotAccept(role);
        if (content != null) {
            MessagePusher.pushMessage(role, new RespMsg(content));
            return;
        }
        this.acceptTask(role);
        return;
    }
    
    public String getNpcContentNotAccept(Role role) {
        String msg = DataGlobalFunc.needGradeJudge(0, 33, (int)role.getRelive(), (int)role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            return msg;
        }
        ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
        boolean isHave = activityService.isHaveRemainCount(role, ActivityType.TREASURE_MAP_TASK);
        if (!isHave) {
            return "英雄今天已经打了很多，请明天再来吧";
        }
        if (this.isWaitGiveUpCD(role)) {
            return "你刚才已经放弃宝图任务，还是休息一阵再来吧";
        }
        return null;
    }
    
    public void acceptTask(Role role) {
        if (!this.canAcceptTask(role)) {
            return;
        }
        TaskSet randTaskSet = this.getRandomSchoolTask(role);
        if (randTaskSet == null) {
            return;
        }
        super.acceptTask(role, randTaskSet, -1);
        return;
    }
    
    private TaskSet getRandomSchoolTask(Role role) {
        List<SysDataTaskTreasureMap> list = TaskDataPool.taskTreasureMap;
        int random = (int)(Math.random() * (double)list.size());
        Integer taskId = Integer.valueOf(((SysDataTaskTreasureMap)list.get(random)).getSerial());
        return this.taskService.getTaskSet((int)taskId);
    }
    
    private boolean canAcceptTask(Role role) {
        ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
        boolean isHave = activityService.isHaveRemainCount(role, ActivityType.TREASURE_MAP_TASK);
        return isHave && !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(Role role) {
        List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.TREASURE_MAP);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(Role role) {
        long giveUpTime = role.getTask().getGiveUpTime(TaskType.TREASURE_MAP);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    @Override
    public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
        TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.TREASURE_MAP)) {
            int doStep = this.activityService.getFinishCount(role, ActivityType.TREASURE_MAP_TASK, 1);
            taskInfo.setCounter((short)doStep);
            return taskInfo;
        }
        return taskInfo;
    }
    
    @Override
    public boolean canGiveUpTask(Role role, TaskSet taskSet) {
        return true;
    }
    
    @Override
    public void giveReward(Role role, TaskSet taskSet) {
        this.addActivityCount(role, taskSet);
        int doStep = this.activityService.getFinishCount(role, ActivityType.TREASURE_MAP_TASK, 1);
        if (doStep <= ActivityType.TREASURE_MAP_TASK.getTotalCount()) {
            this.acceptTask(role);
        }
        List<Param> list = (List)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
        ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "藏宝图", 0);
    }
    
    private void addActivityCount(Role role, TaskSet taskSet) {
        int step = 1;
        if (this.activityService.isHaveRemainCount(role, ActivityType.TREASURE_MAP_TASK)) {
            ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
            activityService.addFinishCount(role, ActivityType.TREASURE_MAP_TASK, step);
            this.activationTips(role, step);
        }
    }
}
