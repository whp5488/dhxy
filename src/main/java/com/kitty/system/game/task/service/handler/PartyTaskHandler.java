package com.kitty.system.game.task.service.handler;

import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import java.util.Iterator;
import com.kitty.system.game.party.event.QuitPartyEvent;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.task.message.vo.TaskInfo;
import java.util.List;
import com.kitty.system.init.domain.SysDataTaskParty;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.common.utils.spring.SpringUtils;
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
public class PartyTaskHandler extends TaskHandler
{
    Logger logger;
    private static final String TASK_DIST = "PARTY";
    private static final int NEED_RELIVE = 0;
    private static final int NEED_LEVEL = 30;
    
    public PartyTaskHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.PARTY;
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
    
    public void acceptPartyTask(Role role, Npc npc) {
        String content = this.getNpcContentNotAccept(role);
        if (content != null) {
            MessagePusher.pushMessage(role, new RespMsg(content));
            return;
        }
        this.acceptTask(role);
        return;
    }
    
    public String getNpcContentNotAccept(Role role) {
        String msg = DataGlobalFunc.needGradeJudge(0, 30, (int)role.getRelive(), (int)role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            return msg;
        }
        if (SpringUtils.getPartyService().getParty(role.getFaction()) == null) {
            return "你还没有帮派哦#<E:51>#";
        }
        ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
        boolean isHave = activityService.isHaveRemainCount(role, ActivityType.PARTY_TASK);
        if (!isHave) {
            return "英雄今天已经打了很多，请明天再来吧";
        }
        if (this.isWaitGiveUpCD(role)) {
            return "你刚才已经放弃帮派任务，还是休息一阵再来吧";
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
        List<SysDataTaskParty> list = TaskDataPool.taskPartyList;
        int random = (int)(Math.random() * (double)list.size());
        Integer taskId = Integer.valueOf(((SysDataTaskParty)list.get(random)).getSerial());
        return this.taskService.getTaskSet((int)taskId);
    }
    
    private boolean canAcceptTask(Role role) {
        ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
        boolean isHave = activityService.isHaveRemainCount(role, ActivityType.PARTY_TASK);
        return isHave && !this.isAcceptedSchoolTask(role) && !this.isWaitGiveUpCD(role);
    }
    
    public boolean isAcceptedSchoolTask(Role role) {
        List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.PARTY);
        return taskList != null && !taskList.isEmpty();
    }
    
    private boolean isWaitGiveUpCD(Role role) {
        long giveUpTime = role.getTask().getGiveUpTime(TaskType.PARTY);
        return giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L;
    }
    
    @Override
    public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
        TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        if (this.taskService.isMatchTaskType(taskSet, TaskType.PARTY)) {
            int step = role.getTaskProgress().getFinishTallyCounts().getInt("PARTY", 1);
            if (step > 10) {
                step = 1;
                role.getTaskProgress().getFinishTallyCounts().setv("PARTY", Integer.valueOf(step));
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
    public void giveReward(Role role, TaskSet taskSet) {
        int doStep = role.getTaskProgress().getFinishTallyCounts().getInt("PARTY", 1);
        this.roleService.addExp(role, calculate((int)role.getLevel(), (int)role.getRelive(), doStep, 1), "帮派任务", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if ((int)combatPet > 0) {
            Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
            this.petService.addExp(role, pet, calculate((int)pet.getPetLv(), (int)pet.getRebirth(), doStep, 2), true);
        }
        long calculate = 0L;
        if (taskSet.getTaskReward() != null) {
            calculate = calculate((int)role.getLevel(), (int)role.getRelive(), doStep, 3);
            this.roleService.addCurrency(role, Const.RESTYPE_TRIBUTE, Long.valueOf(calculate), "帮派任务", false, false);
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你获得了帮贡#<G>" + calculate + "#", true));
        }
        if (doStep % 10 == 0) {
            this.roleService.addCurrency(role, Const.RES_TYPE_ACHIEVEMENT, Long.valueOf(3000L), "帮派任务", true, false);
        }
        this.addActivityCount(role, taskSet, calculate);
        if (doStep < ActivityType.PARTY_TASK.getTotalCount()) {
            this.acceptTask(role);
        }
    }
    
    private void addActivityCount(Role role, TaskSet taskSet, long calculate) {
        int step = 1;
        if (this.activityService.isHaveRemainCount(role, ActivityType.PARTY_TASK)) {
            ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
            activityService.addFinishCount(role, ActivityType.PARTY_TASK, step);
            int doStep = activityService.getFinishCount(role, ActivityType.PARTY_TASK, 1);
            if (doStep % 2 == 0) {
                this.activationTips(role, step);
            }
            int count = role.getTaskProgress().getFinishTallyCounts().getInt("PARTY", 1);
            role.getTaskProgress().getFinishTallyCounts().setv("PARTY", Integer.valueOf(count + 1));
            SpringUtils.getPartyService().updatePartyTask(role, doStep, calculate);
        }
    }
    
    private static long calculate(int grade, int rei, int cnt, int type) {
        double fold = ActivityType.PARTY_TASK.getFold();
        if (type == 1) {
            double exp = 1.6E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0 / 25.59 * (0.1 + 0.13 * (double)cnt + Math.pow((double)cnt, 0.35)) * 1.5;
            return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0) ? (exp * fold) : exp)).longValue();
        }
        if (type == 2) {
            double exp = 1.6E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0 / 25.59 * (0.1 + 0.13 * (double)cnt + Math.pow((double)cnt, 0.35)) * 1.5 * 1.2;
            return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0) ? (exp * fold) : exp)).longValue();
        }
        if (type == 3) {
            return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Integer.valueOf(69 + (cnt - 1) * 18)).longValue() * 2L;
        }
        return 0L;
    }
    
    @EventHandler({ EventType.QUIT_PARTY })
    public void handleQuitPartyEvent(QuitPartyEvent quitPartyEvent) {
        Role role = quitPartyEvent.getRole();
        List<SysDataTaskParty> list = TaskDataPool.taskPartyList;
        for (SysDataTaskParty obj : list) {
            if (role.getTask().isAccepted(obj.getSerial())) {
                this.delTask(role, this.taskService.getTaskSet(obj.getSerial()));
            }
            else {
                continue;
            }
        }
        role.save();
    }
}
