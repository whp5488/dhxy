package com.kitty.system.init.domain.vo.surround;

import com.kitty.system.enums.task.TaskType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class SysDataTaskCrusadeRenegade implements Serializable {
  private int serial;
  
  private int nextTaskSerial;
  
  private int submitNpc;
  
  private int giveUp;
  
  private int needFight;
  
  private int getCondition;
  
  private int submitCondition;
  
  private List<Integer> addTaskEvent;
  
  private List<Integer> finishTaskEvent;
  
  private List<Integer> askEvent;
  
  private TaskType taskType;
  
  private String taskReward;

}
