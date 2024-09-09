package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 33025)
public class RespTaskFightEndNotify extends Message
{
    private int taskId;
    private int npcId;
    private byte result;
    
    public int getTaskId() {
        return this.taskId;
    }
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public byte getResult() {
        return this.result;
    }
    
    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    public void setResult(final byte result) {
        this.result = result;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTaskFightEndNotify)) {
            return false;
        }
        RespTaskFightEndNotify other = (RespTaskFightEndNotify)o;
        return other.canEqual(this) && this.getTaskId() == other.getTaskId() && this.getNpcId() == other.getNpcId() && this.getResult() == other.getResult();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTaskFightEndNotify;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTaskId();
        result = result * 59 + this.getNpcId();
        result = result * 59 + this.getResult();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTaskFightEndNotify(taskId=" + this.getTaskId() + ", npcId=" + this.getNpcId() + ", result=" + (int)this.getResult() + ")";
    }
    
    public RespTaskFightEndNotify(final int taskId, final int npcId, final byte result) {
        this.taskId = taskId;
        this.npcId = npcId;
        this.result = result;
    }
    
    public RespTaskFightEndNotify() {
    }
}
