package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;

public class TaskEndEvent extends BasePlayerEvent {
  private Role role;
  
  private int taskId;
  
  public Role getRole() {
    return this.role;
  }
  
  public int getTaskId() {
    return this.taskId;
  }
  
  public TaskEndEvent(EventType eventType, Role role, int taskId) {
    super(eventType, role);
    this.role = role;
    this.taskId = taskId;
  }
}
