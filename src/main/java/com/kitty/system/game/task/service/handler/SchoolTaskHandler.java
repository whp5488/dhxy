package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.task.message.vo.TaskInfo;
import java.util.List;
import com.kitty.system.init.domain.SysDataTaskSchool;
import com.kitty.system.game.task.pool.TaskDataPool;
import java.util.Random;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.vo.Npc;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.task.TaskType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SchoolTaskHandler extends TaskHandler
{
    Logger logger;
    private static final String TASK_DIST = "SCHOOL";
    private static final int NEED_RELIVE = 0;
    private static final int NEED_LEVEL = 15;
    
    public SchoolTaskHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.SCHOOL;
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
        boolean flag = this.taskService.delItemTaskEvent(role, taskSet);
        if (flag) {
            this.taskService.updateSingleTargetProgress(role, taskSet);
        }
        return;
    }
    
    public void acceptSchoolTask(Role role, Npc npc) {
        String content = this.getNpcContentNotAccept(role);
        if (content != null) {
            MessagePusher.pushMessage(role, new RespMsg(content));
            return;
        }
        this.acceptTask(role);
        return;
    }
    
    public String getNpcContentNotAccept(Role role) {
        String msg = DataGlobalFunc.needGradeJudge(0, 15, (int)role.getRelive(), (int)role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            return msg;
        }
        boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.SCHOOL_TASK);
        if (!isHave) {
            return "你今日为师门做了很多事，辛苦你了，前去休息吧";
        }
        if (this.isWaitGiveUpCD(role)) {
            return "你刚才已经放弃师门任务，还是休息一阵再来吧";
        }
        return null;
    }
    
    public void acceptTask(Role role) {
        this.logger.info("领取师门任务");
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
        int doStep = this.activityService.getFinishCount(role, ActivityType.SCHOOL_TASK, 1);
        Random random = new Random();
        Integer taskId = Integer.valueOf(0);
        if (doStep % 10 == 0) {
            List<SysDataTaskSchool> taskSchoolPurchase = TaskDataPool.taskSchoolPurchase;
            int index = random.nextInt(taskSchoolPurchase.size());
            taskId = Integer.valueOf(((SysDataTaskSchool)taskSchoolPurchase.get(index)).getSerial());
        }
        else {
            List<SysDataTaskSchool> taskSchoolElse = TaskDataPool.taskSchoolElse;
            int index = random.nextInt(taskSchoolElse.size());
            taskId = Integer.valueOf(((SysDataTaskSchool)taskSchoolElse.get(index)).getSerial());
        }
        TaskSet taskSet = this.taskService.getTaskSet((int)taskId);
        if (taskSet == null) {
            taskSet = this.taskService.getTaskSet(400002);
            this.logger.error("接取师门任务获取任务数据为空任务编号：{}", taskId);
            return taskSet;
        }
        return taskSet;
    }
    
    private boolean canAcceptTask(Role role) {
        boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.SCHOOL_TASK);
        return isHave && !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(Role role) {
        List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.SCHOOL);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(Role role) {
        long giveUpTime = role.getTask().getGiveUpTime(TaskType.SCHOOL);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    @Override
    public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
        TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.SCHOOL)) {
            int step = role.getTaskProgress().getFinishTallyCounts().getInt("SCHOOL", 1);
            if (step > 10) {
                step = 1;
                role.getTaskProgress().getFinishTallyCounts().setv("SCHOOL", Integer.valueOf(step));
            }
            taskInfo.setCounter((short)step);
            return taskInfo;
        }
        else {
            return taskInfo;
        }
    }
    
    @Override
    public boolean canGiveUpTask(Role role, TaskSet taskSet) {
        return true;
    }
    
    @Override
    public void endTaskTips(Role role) {
        MessagePusher.pushMessage(role, new RespMsg("好大的胆子啊，师父交给哦你的任务都敢不做了"));
    }
    
    @Override
    public void giveReward(Role role, TaskSet taskSet) {
        int doStep = role.getTaskProgress().getFinishTallyCounts().getInt("SCHOOL", 1);
        this.roleService.addExp(role, calculate((int)role.getLevel(), (int)role.getRelive(), doStep, 1), "师门任务", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if ((int)combatPet > 0) {
            Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
            if (pet != null) {
                this.petService.addExp(role, pet, calculate((int)pet.getPetLv(), (int)pet.getRebirth(), doStep, 2), true);
            }
        }
        this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf(calculate((int)role.getLevel(), (int)role.getRelive(), doStep, 3)), "师门任务", true, false);
        this.addActivityCount(role, taskSet);
    }
    
    private void addActivityCount(Role role, TaskSet taskSet) {
        if (this.activityService.isHaveRemainCount(role, ActivityType.SCHOOL_TASK)) {
            int step = 1;
            this.activityService.addFinishCount(role, ActivityType.SCHOOL_TASK, step);
            this.activationTips(role, step);
            int count = role.getTaskProgress().getFinishTallyCounts().getInt("SCHOOL", 1);
            role.getTaskProgress().getFinishTallyCounts().setv("SCHOOL", Integer.valueOf(count + 1));
        }
    }
    
    private static long calculate(int grade, int rei, int cnt, int type) {
        double fold = ActivityType.SCHOOL_TASK.getFold();
        double exp = 0.0;
        if (type == 1) {
            exp = 1.6E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0 / 28.13 * (0.2 + 0.1 * (double)cnt + Math.pow((double)cnt, 0.45)) * 1.5;
        }
        else if (type == 2) {
            exp = 1.6E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0 / 28.13 * (0.2 + 0.1 * (double)cnt + Math.pow((double)cnt, 0.45)) * 1.5 * 1.2;
        }
        else if (type == 3) {
            return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(7109.847138286527 * (0.2 + 0.1 * (double)cnt + Math.pow((double)cnt, 0.45)))).longValue() * 8L;
        }
        return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0) ? (exp * fold) : exp)).longValue();
    }
}
