package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45056)
public class ReqTaskSubmit extends Message
{
    private int taskId;
    
    public ReqTaskSubmit() {
    }
    
    public int getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqTaskSubmit)) {
            return false;
        }
        ReqTaskSubmit other = (ReqTaskSubmit)o;
        return other.canEqual(this) && this.getTaskId() == other.getTaskId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqTaskSubmit;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTaskId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqTaskSubmit(taskId=" + this.getTaskId() + ")";
    }
}
