
package com.kitty.system.game.task.message;

import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 33023)
public class ReqUpTaskEvent extends Message
{
    private int taskId;
    private List<Integer> event;
    
    public int getTaskId() {
        return this.taskId;
    }
    
    public List<Integer> getEvent() {
        return this.event;
    }
    
    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }
    
    public void setEvent(final List<Integer> event) {
        this.event = event;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqUpTaskEvent)) {
            return false;
        }
        final ReqUpTaskEvent other = (ReqUpTaskEvent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTaskId() != other.getTaskId()) {
            return false;
        }
        final Object this$event = this.getEvent();
        final Object other$event = other.getEvent();
        if (this$event == null) {
            if (other$event == null) {
                return true;
            }
        }
        else if (this$event.equals(other$event)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqUpTaskEvent;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTaskId();
        final Object $event = this.getEvent();
        result = result * 59 + (($event == null) ? 43 : $event.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqUpTaskEvent(taskId=" + this.getTaskId() + ", event=" + this.getEvent() + ")";
    }
}
