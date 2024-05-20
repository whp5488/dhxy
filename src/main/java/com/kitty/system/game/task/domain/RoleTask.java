
package com.kitty.system.game.task.domain;

import com.kitty.system.enums.task.TaskType;
import com.kitty.system.enums.task.TaskEvent;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonIgnore;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;

public class RoleTask implements Serializable
{
    private int icon;
    public static final byte NOT_SUBMIT = 0;
    public static final byte CAN_SUBMIT = 1;
    private ConcurrentHashMap<Integer, Byte> acceptedTasks;
    private ConcurrentHashMap<Integer, DoingTask> doingTask;
    private ConcurrentHashMap<Integer, Long> giveUpTimes;
    
    public RoleTask() {
        this.acceptedTasks = new ConcurrentHashMap<>();
        this.doingTask = new ConcurrentHashMap<>();
        this.giveUpTimes = new ConcurrentHashMap<>();
    }
    
    public void acceptTask(final int taskId, final String para, final int expire) {
        this.acceptedTasks.put(taskId, (byte)0);
        final DoingTask task = new DoingTask(taskId, 0, expire);
        if (para != null) {
            task.setPara(para);
        }
        this.doingTask.put(taskId, task);
    }
    
    public void updateToCanSubmit(final int taskId) {
        final Byte status = this.acceptedTasks.get(taskId);
        if (status == null) {
            return;
        }
        if (status == 0) {
            this.acceptedTasks.put(taskId, (byte)1);
        }
    }
    
    public void updateToFinished(final int taskId) {
        this.removeAcceptedTask(taskId);
        this.removeDoingTask(taskId);
    }
    
    public boolean isCanSubmit(final int taskId) {
        final Byte status = this.acceptedTasks.get(taskId);
        return status != null && status == 1;
    }
    
    public boolean isAccepted(final int taskId) {
        return this.acceptedTasks.containsKey(taskId);
    }
    
    private void removeAcceptedTask(final int taskId) {
        this.acceptedTasks.remove(taskId);
    }
    
    private void removeDoingTask(final int taskId) {
        this.doingTask.remove(taskId);
    }
    
    @JsonIgnore
    public int getTargetProgress(final int taskId) {
        final DoingTask doingTask = this.doingTask.get(taskId);
        if (doingTask == null) {
            return 0;
        }
        return doingTask.getTargetProgress();
    }
    
    public void updateTargetProgress(final int taskId) {
        DoingTask doingTask = this.doingTask.get(taskId);
        if (doingTask == null) {
            try {
                System.out.println("提交任务时未找到任务 = " + this.doingTask);
            }
            catch (final Exception ex) {}
            doingTask = new DoingTask(taskId, 0, 0);
            this.doingTask.put(taskId, doingTask);
        }
        doingTask.addProgress();
        this.updateToCanSubmit(taskId);
    }
    
    public void revertTargetProgress(final int taskId, final int targetIndex) {
        DoingTask doingTask = this.doingTask.get(taskId);
        if (doingTask != null) {
            doingTask = new DoingTask(taskId, targetIndex, 0);
            this.doingTask.put(taskId, doingTask);
        }
        doingTask.setTargetProgress(targetIndex);
    }
    
    @JsonIgnore
    public Map<Integer, Integer> getAllAcceptedTaskProgress() {
        final Map<Integer, Integer> progressMap = new HashMap<Integer, Integer>(this.acceptedTasks.size());
        final Set<Integer> taskIds = this.acceptedTasks.keySet();
        for (final int taskId : taskIds) {
            final int progress = this.getTargetProgress(taskId);
            progressMap.put(taskId, progress);
        }
        return progressMap;
    }
    
    @JsonIgnore
    public List<Integer> getAcceptedTaskList() {
        final List<Integer> list = new ArrayList<>(this.acceptedTasks.size());
        list.addAll(this.acceptedTasks.keySet());
        return list;
    }
    
    public void addRecord4FightAndTask(final int fightId, final int taskId, final int expire) {
        DoingTask doingTask = this.doingTask.get(taskId);
        if (doingTask == null) {
            doingTask = new DoingTask(taskId, 0, expire);
            this.doingTask.put(taskId, doingTask);
        }
        doingTask.setFightId(fightId);
    }
    
    public Integer getTaskIdByFightId(final int fightId) {
        for (final DoingTask doTask : this.doingTask.values()) {
            if (fightId == doTask.getFightId()) {
                return doTask.getTaskId();
            }
        }
        return null;
    }
    
    public void resetTask(final int taskId) {
        this.deleteTask(taskId);
    }
    
    public void giveUpTask(final int taskId) {
        this.deleteTask(taskId);
    }
    
    public void deleteTask(final int taskId) {
        this.removeAcceptedTask(taskId);
        this.removeDoingTask(taskId);
    }
    
    public void updateChangeCounterTask(final int taskId, final TaskEvent taskEvent, final int serial, final int num) {
        final DoingTask doingTask = this.doingTask.get(taskId);
        int val = 0;
        final Map<Integer, Integer> changeCounter = doingTask.getChangeCounter();
        if (changeCounter == null) {
            doingTask.setChangeCounter(new ConcurrentHashMap<Integer, Integer>());
        }
        else {
            final Integer integer = changeCounter.get(serial);
            if (integer != null) {
                val = changeCounter.get(serial);
            }
        }
        if (taskEvent == TaskEvent.Add) {
            doingTask.getChangeCounter().put(serial, val + num);
        }
        else if (taskEvent == TaskEvent.Set) {
            doingTask.getChangeCounter().put(serial, num);
        }
    }
    
    public long getGiveUpTime(final TaskType taskType) {
        if (this.giveUpTimes != null && this.giveUpTimes.containsKey(taskType.getId())) {
            return this.giveUpTimes.get(taskType.getId());
        }
        return -1L;
    }
    
    public void addGiveUpTime(final TaskType taskType, final long time) {
        if (this.giveUpTimes == null) {
            this.giveUpTimes = new ConcurrentHashMap<Integer, Long>();
        }
        this.giveUpTimes.put(taskType.getId(), time);
    }
    
    public void removeGiveUpTime(final TaskType taskType) {
        if (this.giveUpTimes != null) {
            this.giveUpTimes.remove(taskType.getId());
        }
    }
    
    public void addTaskNpcId(final int taskId, final int npcId, final int expire) {
        DoingTask doingTask = this.doingTask.get(taskId);
        if (doingTask == null) {
            doingTask = new DoingTask(taskId, 0, expire);
            this.doingTask.put(taskId, doingTask);
        }
        doingTask.setNpcId(npcId);
    }
    
    public int getTaskNpcId(final int taskId) {
        final DoingTask doingTask = this.doingTask.get(taskId);
        if (doingTask != null) {
            return doingTask.getNpcId();
        }
        return 0;
    }
    
    @JsonIgnore
    public long getAcceptTime(final int taskId) {
        final DoingTask doingTask = this.doingTask.get(taskId);
        if (doingTask != null) {
            return doingTask.getCreate();
        }
        return 0L;
    }
    
    public Map<Integer, Byte> getAcceptedTasks() {
        return this.acceptedTasks;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public ConcurrentHashMap<Integer, DoingTask> getDoingTask() {
        return this.doingTask;
    }
    
    public ConcurrentHashMap<Integer, Long> getGiveUpTimes() {
        return this.giveUpTimes;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setAcceptedTasks(final ConcurrentHashMap<Integer, Byte> acceptedTasks) {
        this.acceptedTasks = acceptedTasks;
    }
    
    public void setDoingTask(final ConcurrentHashMap<Integer, DoingTask> doingTask) {
        this.doingTask = doingTask;
    }
    
    public void setGiveUpTimes(final ConcurrentHashMap<Integer, Long> giveUpTimes) {
        this.giveUpTimes = giveUpTimes;
    }
}
