package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 33024)
public class ReqAskTaskFight extends Message
{
    private int taskId;
    private int npcId;
    
    public ReqAskTaskFight() {
    }
    
    public int getTaskId() {
        return this.taskId;
    }
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAskTaskFight)) {
            return false;
        }
        ReqAskTaskFight other = (ReqAskTaskFight)o;
        return other.canEqual(this) && this.getTaskId() == other.getTaskId() && this.getNpcId() == other.getNpcId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAskTaskFight;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTaskId();
        result = result * 59 + this.getNpcId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAskTaskFight(taskId=" + this.getTaskId() + ", npcId=" + this.getNpcId() + ")";
    }
}
