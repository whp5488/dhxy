
package com.kitty.mina.task;

public class CmdRecord implements Comparable<CmdRecord>
{
    private int cmd;
    private int count;
    
    public CmdRecord(final int cmd, final int count) {
        this.cmd = cmd;
        this.count = count;
    }
    
    public int getCmd() {
        return this.cmd;
    }
    
    public void setCmd(final int cmd) {
        this.cmd = cmd;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public void setCount(final int count) {
        this.count = count;
    }
    
    @Override
    public String toString() {
        return "CmdRecord{cmd=" + this.cmd + ", count=" + this.count + '}';
    }
    
    @Override
    public int compareTo(final CmdRecord o) {
        if (this.count != o.getCount()) {
            return o.getCount() - this.count;
        }
        return o.getCmd() - this.cmd;
    }
}
