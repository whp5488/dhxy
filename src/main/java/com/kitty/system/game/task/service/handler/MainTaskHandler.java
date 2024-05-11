
package com.kitty.system.game.task.service.handler;

import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.vo.Npc;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Service;

@Service
public class MainTaskHandler extends TaskHandler
{
    public void acceptFirstMainTask(final Role role) {
        final TaskSet taskSet = this.taskService.getTaskSet(100001);
        this.acceptTask(role, taskSet, -1);
    }
    
    @Override
    public boolean resetTask(final Role role, final TaskSet taskSet) {
        return false;
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.MAIN_LINE;
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
    
    @Override
    public TaskInfo getTaskInfo(final Role role, final TaskSet taskSet) {
        final TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        return taskInfo;
    }
    
    @Override
    public boolean canGiveUpTask(final Role role, final TaskSet taskSet) {
        return false;
    }
}
