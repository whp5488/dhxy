
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
        final boolean flag = this.taskService.delItemTaskEvent(role, taskSet);
        if (flag) {
            this.taskService.updateSingleTargetProgress(role, taskSet);
        }
    }
    
    public void acceptSchoolTask(final Role role, final Npc npc) {
        final String content = this.getNpcContentNotAccept(role);
        if (content != null) {
            MessagePusher.pushMessage(role, new RespMsg(content));
            return;
        }
        this.acceptTask(role);
    }
    
    public String getNpcContentNotAccept(final Role role) {
        final String msg = DataGlobalFunc.needGradeJudge(0, 15, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            return msg;
        }
        final boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.SCHOOL_TASK);
        if (!isHave) {
            return "你今日为师门做了很多事，辛苦你了，前去休息吧";
        }
        if (this.isWaitGiveUpCD(role)) {
            return "你刚才已经放弃师门任务，还是休息一阵再来吧";
        }
        return null;
    }
    
    public void acceptTask(final Role role) {
        this.logger.info("领取师门任务");
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
        final int doStep = this.activityService.getFinishCount(role, ActivityType.SCHOOL_TASK, 1);
        final Random random = new Random();
        Integer taskId = 0;
        if (doStep % 10 == 0) {
            final List<SysDataTaskSchool> taskSchoolPurchase = TaskDataPool.taskSchoolPurchase;
            final int index = random.nextInt(taskSchoolPurchase.size());
            taskId = taskSchoolPurchase.get(index).getSerial();
        }
        else {
            final List<SysDataTaskSchool> taskSchoolElse = TaskDataPool.taskSchoolElse;
            final int index = random.nextInt(taskSchoolElse.size());
            taskId = taskSchoolElse.get(index).getSerial();
        }
        TaskSet taskSet = this.taskService.getTaskSet(taskId);
        if (taskSet == null) {
            taskSet = this.taskService.getTaskSet(400002);
            this.logger.error("接取师门任务获取任务数据为空任务编号：{}", (Object)taskId);
        }
        return taskSet;
    }
    
    private boolean canAcceptTask(final Role role) {
        final boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.SCHOOL_TASK);
        return isHave && !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(final Role role) {
        final List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.SCHOOL);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(final Role role) {
        final long giveUpTime = role.getTask().getGiveUpTime(TaskType.SCHOOL);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    @Override
    public TaskInfo getTaskInfo(final Role role, final TaskSet taskSet) {
        final TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.SCHOOL)) {
            int step = role.getTaskProgress().getFinishTallyCounts().getInt("SCHOOL", 1);
            if (step > 10) {
                step = 1;
                role.getTaskProgress().getFinishTallyCounts().setv("SCHOOL", step);
            }
            taskInfo.setCounter((short)step);
        }
        return taskInfo;
    }
    
    @Override
    public boolean canGiveUpTask(final Role role, final TaskSet taskSet) {
        return true;
    }
    
    @Override
    public void endTaskTips(final Role role) {
        MessagePusher.pushMessage(role, new RespMsg("好大的胆子啊，师父交给哦你的任务都敢不做了"));
    }
    
    @Override
    public void giveReward(final Role role, final TaskSet taskSet) {
        final int doStep = role.getTaskProgress().getFinishTallyCounts().getInt("SCHOOL", 1);
        this.roleService.addExp(role, calculate(role.getLevel(), role.getRelive(), doStep, 1), "师门任务", true);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet > 0) {
            final Pet pet = this.petService.selectPet(role).get(combatPet);
            if (pet != null) {
                this.petService.addExp(role, pet, calculate(pet.getPetLv(), pet.getRebirth(), doStep, 2), true);
            }
        }
        this.roleService.addCurrency(role, Const.RESTYPE_COIN, calculate(role.getLevel(), role.getRelive(), doStep, 3), "师门任务", true, false);
        this.addActivityCount(role, taskSet);
    }
    
    private void addActivityCount(final Role role, final TaskSet taskSet) {
        if (this.activityService.isHaveRemainCount(role, ActivityType.SCHOOL_TASK)) {
            final int step = 1;
            this.activityService.addFinishCount(role, ActivityType.SCHOOL_TASK, step);
            this.activationTips(role, step);
            final int count = role.getTaskProgress().getFinishTallyCounts().getInt("SCHOOL", 1);
            role.getTaskProgress().getFinishTallyCounts().setv("SCHOOL", count + 1);
        }
    }
    
    private static long calculate(final int grade, final int rei, final int cnt, final int type) {
        final double fold = ActivityType.SCHOOL_TASK.getFold();
        double exp = 0.0;
        if (type == 1) {
            exp = 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 28.13 * (0.2 + 0.1 * cnt + Math.pow(cnt, 0.45)) * 1.5;
        }
        else if (type == 2) {
            exp = 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 28.13 * (0.2 + 0.1 * cnt + Math.pow(cnt, 0.45)) * 1.5 * 1.2;
        }
        else if (type == 3) {
            return BigDecimalUtil.decimalScaleDown(0, 7109.847138286527 * (0.2 + 0.1 * cnt + Math.pow(cnt, 0.45))).longValue() * 8L;
        }
        return BigDecimalUtil.decimalScaleDown(0, (fold > 1.0) ? (exp * fold) : exp).longValue();
    }
}
