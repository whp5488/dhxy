
package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class TaskEndEvent extends BasePlayerEvent
{
    private Role role;
    private int taskId;
    
    public TaskEndEvent(final EventType eventType, final Role role, final int taskId) {
        super(eventType, role);
        this.role = role;
        this.taskId = taskId;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public int getTaskId() {
        return this.taskId;
    }
}
