package com.kitty.system.game.task.message;

import com.kitty.system.game.task.message.vo.TaskInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61553)
public class RespTasks extends Message
{
    private List<TaskInfo> list;
    
    public RespTasks() {
    }
    
    public List<TaskInfo> getList() {
        return this.list;
    }
    
    public void setList(final List<TaskInfo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTasks)) {
            return false;
        }
        RespTasks other = (RespTasks)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTasks;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTasks(list=" + this.getList() + ")";
    }
}
