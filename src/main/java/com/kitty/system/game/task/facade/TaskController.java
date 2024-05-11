
package com.kitty.system.game.task.facade;

import com.kitty.system.game.task.message.ReqAskTaskFight;
import com.kitty.system.game.task.message.ReqUpTaskEvent;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.task.service.handler.TaskHandler;
import com.kitty.system.game.config.TaskSet;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.task.message.ReqTaskSubmit;
import org.apache.mina.core.session.IoSession;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.service.handler.TimeTaskHandler;
import com.kitty.system.game.task.service.handler.TaskHelper;
import com.kitty.listener.event.HeartBeatEvent;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class TaskController
{
    Logger logger;
    @Autowired
    RoleService roleService;
    @Autowired
    TaskService taskService;
    
    public TaskController() {
        this.logger = LoggerFactory.getLogger(TaskController.class);
    }
    
    @EventHandler({ EventType.HEART_BEAT })
    public void handleHeartBeat(final HeartBeatEvent heartBeatEvent) {
        final Role role = heartBeatEvent.getRole();
        if (role == null) {
            return;
        }
        TaskHelper.INSTANCE.getHandlers().values().forEach(taskHandler -> {
            if (taskHandler instanceof TimeTaskHandler) {
                ((TimeTaskHandler)taskHandler).handleHeartBeat(role, heartBeatEvent.getPrevHeartTime());
            }
        });
    }
    
    @RequestMapping
    public void taskSubmit(final IoSession session, final ReqTaskSubmit reqTaskSubmit) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final boolean accepted = role.getTask().isAccepted(reqTaskSubmit.getTaskId());
        if (accepted) {
            final TaskSet taskSet = this.taskService.getTaskSet(reqTaskSubmit.getTaskId());
            if (taskSet == null) {
                this.logger.error("提交任务事件未获取任务异常：角色 = {}，任务编号 = {}", (Object)role.getRoleId(), (Object)reqTaskSubmit.getTaskId());
                return;
            }
            final TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
            if (taskHandler != null) {
                try {
                    taskHandler.canSubmitTask(role, taskSet);
                }
                catch (final Exception e) {
                    this.logger.error("提交任务事件任务异常：角色 = {}，任务编号 = {}", (Object)role.getRoleId(), (Object)reqTaskSubmit.getTaskId());
                    e.printStackTrace();
                }
            }
            else {
                this.logger.error("提交任务事件未获取到任务处理类异常：角色 = {}，任务编号 = {}，任务类型：{}", new Object[] { role.getRoleId(), taskSet.getSerial(), taskSet.getTaskType() });
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("请先领取该任务，再进行提交"));
        }
    }
    
    @RequestMapping
    public void askUpEvent(final IoSession session, final ReqUpTaskEvent reqUpTaskEvent) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.taskService.askUpEvent(role, reqUpTaskEvent);
    }
    
    @RequestMapping
    public void taskTriggerFight(final IoSession session, final ReqAskTaskFight reqAskTaskFight) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final TaskSet taskSet = this.taskService.getTaskSet(reqAskTaskFight.getTaskId());
        if (taskSet != null) {
            this.taskService.taskTriggerFight(role, taskSet, reqAskTaskFight.getNpcId());
        }
    }
}
