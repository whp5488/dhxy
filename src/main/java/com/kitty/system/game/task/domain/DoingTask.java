package com.kitty.system.game.task.domain;

import com.kitty.system.game.vo.EasyNpc;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Getter
public class DoingTask implements Serializable {
  private int taskId;

  @ConstructorProperties({"taskId", "targetProgress", "fightId", "npcId", "easyNpcList", "finishedTask", "changeCounter", "para", "create", "update", "expire"})
  public DoingTask(int taskId, int targetProgress, int fightId, int npcId, List<EasyNpc> easyNpcList, int finishedTask, ConcurrentHashMap<Integer, Integer> changeCounter, String para, int create, int update, int expire) {
    this.taskId = taskId;
    this.targetProgress = targetProgress;
    this.fightId = fightId;
    this.npcId = npcId;
    this.easyNpcList = easyNpcList;
    this.finishedTask = finishedTask;
    this.changeCounter = changeCounter;
    this.para = para;
    this.create = create;
    this.update = update;
    this.expire = expire;
  }

  private int targetProgress = 0;
  
  private int fightId;
  
  private int npcId;

  private List<EasyNpc> easyNpcList = Collections.synchronizedList(new ArrayList<>());

  private int finishedTask = 0;

  private ConcurrentHashMap<Integer, Integer> changeCounter = new ConcurrentHashMap<>();
  
  private String para;
  
  private int create;
  
  private int update;
  
  private int expire;

  public DoingTask(int taskId, int targetProgress, int expire) {
    this.taskId = taskId;
    this.targetProgress = targetProgress;
    this.create = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    this.update = this.create;
    this.expire = expire;
  }
  
  public void addProgress() {
    this.targetProgress++;
    this.update = (new Long(System.currentTimeMillis() / 1000L)).intValue();
  }
  
  public DoingTask() {}
}
