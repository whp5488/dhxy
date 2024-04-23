package com.kitty.system.game.config;

import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.vo.utils.Const;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TaskSet implements Cloneable {
  private int serial;
  
  private int nextTaskSerial;
  
  private int submitNpc;
  
  private int giveUp;
  
  private int getCondition;
  
  private int submitCondition;
  
  private List<Integer> addTaskEvent;
  
  private List<Integer> finishTaskEvent;
  
  private List<Integer> askEvent;
  
  private TaskType taskType;

  private byte kind = 1;

  private List<Integer> dstNpc = new ArrayList<>();

  private int recGrade = -1;
  
  private String taskReward;
  
  private int timeoutSec;
  
  private int needCounter;
  
  private String para;
  
  private int previousId;

  public TaskSet clone() throws CloneNotSupportedException {
    return (TaskSet)super.clone();
  }
  
  public boolean isTeamAccept() {
    if (Const.TEAM_ACCEPT.contains(this.taskType))
      return true; 
    return false;
  }
}
