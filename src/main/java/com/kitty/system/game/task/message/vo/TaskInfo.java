
package com.kitty.system.game.task.message.vo;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.vo.EasyNpc;
import java.util.List;

public class TaskInfo
{
    private int serial;
    private short counter;
    private int taskUpdateTime;
    private int taskEndTime;
    private byte quit;
    private byte refresh;
    private List<EasyNpc> npcList;
    private List<FiedValue> list;
    
    public TaskInfo() {
        this.counter = 0;
        this.taskUpdateTime = 0;
        this.taskEndTime = 0;
        this.quit = 0;
        this.refresh = 0;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public short getCounter() {
        return this.counter;
    }
    
    public int getTaskUpdateTime() {
        return this.taskUpdateTime;
    }
    
    public int getTaskEndTime() {
        return this.taskEndTime;
    }
    
    public byte getQuit() {
        return this.quit;
    }
    
    public byte getRefresh() {
        return this.refresh;
    }
    
    public List<EasyNpc> getNpcList() {
        return this.npcList;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setCounter(final short counter) {
        this.counter = counter;
    }
    
    public void setTaskUpdateTime(final int taskUpdateTime) {
        this.taskUpdateTime = taskUpdateTime;
    }
    
    public void setTaskEndTime(final int taskEndTime) {
        this.taskEndTime = taskEndTime;
    }
    
    public void setQuit(final byte quit) {
        this.quit = quit;
    }
    
    public void setRefresh(final byte refresh) {
        this.refresh = refresh;
    }
    
    public void setNpcList(final List<EasyNpc> npcList) {
        this.npcList = npcList;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TaskInfo)) {
            return false;
        }
        final TaskInfo other = (TaskInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getCounter() != other.getCounter()) {
            return false;
        }
        if (this.getTaskUpdateTime() != other.getTaskUpdateTime()) {
            return false;
        }
        if (this.getTaskEndTime() != other.getTaskEndTime()) {
            return false;
        }
        if (this.getQuit() != other.getQuit()) {
            return false;
        }
        if (this.getRefresh() != other.getRefresh()) {
            return false;
        }
        final Object this$npcList = this.getNpcList();
        final Object other$npcList = other.getNpcList();
        Label_0143: {
            if (this$npcList == null) {
                if (other$npcList == null) {
                    break Label_0143;
                }
            }
            else if (this$npcList.equals(other$npcList)) {
                break Label_0143;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TaskInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getCounter();
        result = result * 59 + this.getTaskUpdateTime();
        result = result * 59 + this.getTaskEndTime();
        result = result * 59 + this.getQuit();
        result = result * 59 + this.getRefresh();
        final Object $npcList = this.getNpcList();
        result = result * 59 + (($npcList == null) ? 43 : $npcList.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "TaskInfo(serial=" + this.getSerial() + ", counter=" + this.getCounter() + ", taskUpdateTime=" + this.getTaskUpdateTime() + ", taskEndTime=" + this.getTaskEndTime() + ", quit=" + this.getQuit() + ", refresh=" + this.getRefresh() + ", npcList=" + this.getNpcList() + ", list=" + this.getList() + ")";
    }
}
