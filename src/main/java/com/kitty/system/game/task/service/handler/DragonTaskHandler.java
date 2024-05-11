
package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.task.message.vo.TaskInfo;
import java.util.List;
import com.kitty.system.init.domain.SysDataTaskDragon;
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
public class DragonTaskHandler extends TaskHandler
{
    Logger logger;
    private static final String TASK_DIST = "DRAGON";
    private static final int NEED_RELIVE = 0;
    private static final int NEED_LEVEL = 60;
    
    public DragonTaskHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.DRAGON;
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
        final String msg = DataGlobalFunc.needGradeJudge(0, 60, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            return msg;
        }
        final boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.DRAGON);
        if (!isHave) {
            return "你今日为端午节做了很多事，辛苦你了，前去休息吧";
        }
        if (this.isWaitGiveUpCD(role)) {
            return "你刚才已经放弃端午节任务，还是休息一阵再来吧";
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
        final Random random = new Random();
        final List<SysDataTaskDragon> taskList = TaskDataPool.taskDragonList;
        final int index = random.nextInt(taskList.size());
        final Integer taskId = taskList.get(index).getSerial();
        final TaskSet taskSet = this.taskService.getTaskSet(taskId);
        return taskSet;
    }
    
    private boolean canAcceptTask(final Role role) {
        final boolean isHave = this.activityService.isHaveRemainCount(role, ActivityType.DRAGON);
        return isHave && !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(final Role role) {
        final List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.DRAGON);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(final Role role) {
        final long giveUpTime = role.getTask().getGiveUpTime(TaskType.DRAGON);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    @Override
    public TaskInfo getTaskInfo(final Role role, final TaskSet taskSet) {
        final TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.DRAGON)) {
            final int step = role.getTaskProgress().getFinishCounts().getInt(ActivityType.DRAGON.getActivityId(), 0) + 1;
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
    }
    
    @Override
    public void giveReward(final Role role, final TaskSet taskSet) {
        final int step = role.getTaskProgress().getFinishCounts().getInt(ActivityType.DRAGON.getActivityId(), 0) + 1;
        this.roleService.addExp(role, calculate(role.getLevel(), role.getRelive(), 1, 1), "任务", true);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet > 0) {
            final Pet pet = this.petService.selectPet(role).get(combatPet);
            this.petService.addExp(role, pet, calculate(pet.getPetLv(), pet.getRebirth(), 1, 2), true);
        }
        this.roleService.addCurrency(role, Const.RESTYPE_COIN, calculate(role.getLevel(), role.getRelive(), 1, 3), "任务", true, false);
        this.addActivityCount(role, taskSet);
        final List<Param> list = JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
        if (list != null) {
            SpringUtils.getBean(BagService.class).sendReward(role, list, "", 0);
        }
        if (step >= ActivityType.DRAGON.getTotalCount()) {
            final int round = RandomUtil.round(0, TaskDataPool.dragonRewardList.size() - 1);
            final Param param = TaskDataPool.dragonRewardList.get(round);
            final boolean flag = SkillAi.canAffectOnPro(param.getR() / 1000.0);
            if (flag) {
                SpringUtils.getBean(BagService.class).addGoods(role, param.getK(), param.getV(), 0, true, true, "端午最后一环");
            }
        }
    }
    
    private void addActivityCount(final Role role, final TaskSet taskSet) {
        if (this.activityService.isHaveRemainCount(role, ActivityType.DRAGON)) {
            final int step = 1;
            this.activityService.addFinishCount(role, ActivityType.DRAGON, step);
            final ActivityLivenessSet activityLivenessSet = ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.DRAGON);
            if (activityLivenessSet != null && activityLivenessSet.getPerLiveness() < 1.0f) {
                final int c = (int)(1.0f / activityLivenessSet.getPerLiveness());
                final int anInt = role.getTaskProgress().getFinishCounts().getInt(ActivityType.DRAGON.getActivityId(), 1) - 1;
                if (anInt % c == 0) {
                    this.activationTips(role, step);
                }
            }
        }
    }
    
    private static long calculate(final int grade, final int rei, final int cnt, final int type) {
        final double fold = ActivityType.DRAGON.getFold();
        double exp = 0.0;
        if (type == 1) {
            exp = 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 28.13 * (0.2 + 0.1 * cnt + Math.pow(cnt, 0.45)) * 1.5;
        }
        else if (type == 2) {
            exp = 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 28.13 * (0.2 + 0.1 * cnt + Math.pow(cnt, 0.45)) * 1.5 * 1.2;
        }
        else if (type == 3) {
            return BigDecimalUtil.decimalScaleDown(0, 7109.847138286527 * (0.2 + 0.1 * cnt + Math.pow(cnt, 0.45))).longValue();
        }
        return BigDecimalUtil.decimalScaleDown(0, (fold > 1.0) ? (exp * fold) : exp).longValue();
    }
}
