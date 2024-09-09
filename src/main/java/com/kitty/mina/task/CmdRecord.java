package com.kitty.mina.task;

public class CmdRecord implements Comparable<CmdRecord>
{
    private int cmd;
    private int count;
    
    public CmdRecord(int cmd, int count) {
        this.cmd = cmd;
        this.count = count;
    }
    
    public int getCmd() {
        return this.cmd;
    }
    
    public void setCmd(int cmd) {
        this.cmd = cmd;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    @Override
    public String toString() {
        return "CmdRecord{cmd=" + this.cmd + ", count=" + this.count + '}';
    }
    
    @Override
    public int compareTo(CmdRecord o) {
        if (this.count != o.getCount()) {
            return o.getCount() - this.count;
        }
        return o.getCmd() - this.cmd;
    }
}
