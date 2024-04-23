package com.kitty.system.game.task.domain;

import com.kitty.system.enums.task.TaskEvent;
import com.kitty.system.enums.task.TaskType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

@Setter
@Getter
public class RoleTask implements Serializable {
  private int icon;
  
  public static final byte NOT_SUBMIT = 0;
  
  public static final byte CAN_SUBMIT = 1;

  private ConcurrentHashMap<Integer, Byte> acceptedTasks = new ConcurrentHashMap<>();
  
  private ConcurrentHashMap<Integer, DoingTask> doingTask = new ConcurrentHashMap<>();

  private ConcurrentHashMap<Integer, Long> giveUpTimes = new ConcurrentHashMap<>();
  
  public void acceptTask(int taskId, String para, int expire) {
    this.acceptedTasks.put(Integer.valueOf(taskId), Byte.valueOf((byte)0));
    DoingTask task = new DoingTask(taskId, 0, expire);
    if (para != null)
      task.setPara(para); 
    this.doingTask.put(Integer.valueOf(taskId), task);
  }
  
  public void updateToCanSubmit(int taskId) {
    Byte status = this.acceptedTasks.get(Integer.valueOf(taskId));
    if (status == null)
      return; 
    if (status.byteValue() == 0)
      this.acceptedTasks.put(Integer.valueOf(taskId), Byte.valueOf((byte)1)); 
  }
  
  public void updateToFinished(int taskId) {
    removeAcceptedTask(taskId);
    removeDoingTask(taskId);
  }
  
  public boolean isCanSubmit(int taskId) {
    Byte status = this.acceptedTasks.get(Integer.valueOf(taskId));
    if (status == null)
      return false; 
    if (status.byteValue() != 1)
      return false; 
    return true;
  }
  
  public boolean isAccepted(int taskId) {
    return this.acceptedTasks.containsKey(Integer.valueOf(taskId));
  }
  
  private void removeAcceptedTask(int taskId) {
    this.acceptedTasks.remove(Integer.valueOf(taskId));
  }
  
  private void removeDoingTask(int taskId) {
    this.doingTask.remove(Integer.valueOf(taskId));
  }
  
  @JsonIgnore
  public int getTargetProgress(int taskId) {
    DoingTask doingTask = this.doingTask.get(Integer.valueOf(taskId));
    if (doingTask == null)
      return 0; 
    return doingTask.getTargetProgress();
  }
  
  public void updateTargetProgress(int taskId) {
    DoingTask doingTask = this.doingTask.get(Integer.valueOf(taskId));
    if (doingTask == null) {
      try {
        System.out.println("提交任务时未找到任务 = " + this.doingTask);
      } catch (Exception exception) {}
      doingTask = new DoingTask(taskId, 0, 0);
      this.doingTask.put(Integer.valueOf(taskId), doingTask);
    } 
    doingTask.addProgress();
    updateToCanSubmit(taskId);
  }
  
  public void revertTargetProgress(int taskId, int targetIndex) {
    DoingTask doingTask = this.doingTask.get(Integer.valueOf(taskId));
    if (doingTask != null) {
      doingTask = new DoingTask(taskId, targetIndex, 0);
      this.doingTask.put(Integer.valueOf(taskId), doingTask);
    } 
    doingTask.setTargetProgress(targetIndex);
  }
  
  @JsonIgnore
  public Map<Integer, Integer> getAllAcceptedTaskProgress() {
    Map<Integer, Integer> progressMap = new HashMap<>(this.acceptedTasks.size());
    Set<Integer> taskIds = this.acceptedTasks.keySet();
    for (Iterator<Integer> iterator = taskIds.iterator(); iterator.hasNext(); ) {
      int taskId = ((Integer)iterator.next()).intValue();
      int progress = getTargetProgress(taskId);
      progressMap.put(Integer.valueOf(taskId), Integer.valueOf(progress));
    } 
    return progressMap;
  }
  
  @JsonIgnore
  public List<Integer> getAcceptedTaskList() {
    List<Integer> list = new ArrayList<>(this.acceptedTasks.size());
    list.addAll(this.acceptedTasks.keySet());
    return list;
  }
  
  public void addRecord4FightAndTask(int fightId, int taskId, int expire) {
    DoingTask doingTask = this.doingTask.get(Integer.valueOf(taskId));
    if (doingTask == null) {
      doingTask = new DoingTask(taskId, 0, expire);
      this.doingTask.put(Integer.valueOf(taskId), doingTask);
    } 
    doingTask.setFightId(fightId);
  }
  
  public Integer getTaskIdByFightId(int fightId) {
    for (DoingTask doTask : this.doingTask.values()) {
      if (fightId == doTask.getFightId())
        return Integer.valueOf(doTask.getTaskId()); 
    } 
    return null;
  }
  
  public void resetTask(int taskId) {
    deleteTask(taskId);
  }
  
  public void giveUpTask(int taskId) {
    deleteTask(taskId);
  }
  
  public void deleteTask(int taskId) {
    removeAcceptedTask(taskId);
    removeDoingTask(taskId);
  }
  
  public void updateChangeCounterTask(int taskId, TaskEvent taskEvent, int serial, int num) {
    DoingTask doingTask = this.doingTask.get(Integer.valueOf(taskId));
    int val = 0;
    Map<Integer, Integer> changeCounter = doingTask.getChangeCounter();
    if (changeCounter == null) {
      doingTask.setChangeCounter(new ConcurrentHashMap<>());
    } else {
      Integer integer = changeCounter.get(Integer.valueOf(serial));
      if (integer != null)
        val = ((Integer)changeCounter.get(Integer.valueOf(serial))).intValue(); 
    } 
    if (taskEvent == TaskEvent.Add) {
      doingTask.getChangeCounter().put(Integer.valueOf(serial), Integer.valueOf(val + num));
    } else if (taskEvent == TaskEvent.Set) {
      doingTask.getChangeCounter().put(Integer.valueOf(serial), Integer.valueOf(num));
    } 
  }
  
  public long getGiveUpTime(TaskType taskType) {
    if (this.giveUpTimes != null && this.giveUpTimes.containsKey(Integer.valueOf(taskType.getId())))
      return ((Long)this.giveUpTimes.get(Integer.valueOf(taskType.getId()))).longValue(); 
    return -1L;
  }
  
  public void addGiveUpTime(TaskType taskType, long time) {
    if (this.giveUpTimes == null)
      this.giveUpTimes = new ConcurrentHashMap<>(); 
    this.giveUpTimes.put(Integer.valueOf(taskType.getId()), Long.valueOf(time));
  }
  
  public void removeGiveUpTime(TaskType taskType) {
    if (this.giveUpTimes != null)
      this.giveUpTimes.remove(Integer.valueOf(taskType.getId())); 
  }
  
  public void addTaskNpcId(int taskId, int npcId, int expire) {
    DoingTask doingTask = this.doingTask.get(Integer.valueOf(taskId));
    if (doingTask == null) {
      doingTask = new DoingTask(taskId, 0, expire);
      this.doingTask.put(Integer.valueOf(taskId), doingTask);
    } 
    doingTask.setNpcId(npcId);
  }
  
  public int getTaskNpcId(int taskId) {
    DoingTask doingTask = this.doingTask.get(Integer.valueOf(taskId));
    if (doingTask != null)
      return doingTask.getNpcId(); 
    return 0;
  }
  
  @JsonIgnore
  public long getAcceptTime(int taskId) {
    DoingTask doingTask = this.doingTask.get(Integer.valueOf(taskId));
    if (doingTask != null)
      return doingTask.getCreate(); 
    return 0L;
  }
  
  public Map<Integer, Byte> getAcceptedTasks() {
    return this.acceptedTasks;
  }
}
