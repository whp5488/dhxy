package com.kitty.system.game.safe.domain;

public class ErrorRecord
{
    int errorSum;
    long lastErrorTime;
    
    public void addErrorCount() {
        ++this.errorSum;
    }
    
    public ErrorRecord() {
    }
    
    public int getErrorSum() {
        return this.errorSum;
    }
    
    public long getLastErrorTime() {
        return this.lastErrorTime;
    }
    
    public void setErrorSum(final int errorSum) {
        this.errorSum = errorSum;
    }
    
    public void setLastErrorTime(final long lastErrorTime) {
        this.lastErrorTime = lastErrorTime;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ErrorRecord)) {
            return false;
        }
        ErrorRecord other = (ErrorRecord)o;
        return other.canEqual(this) && this.getErrorSum() == other.getErrorSum() && this.getLastErrorTime() == other.getLastErrorTime();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ErrorRecord;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getErrorSum();
        long $lastErrorTime = this.getLastErrorTime();
        result = result * 59 + (int)($lastErrorTime >>> 32 ^ $lastErrorTime);
        return result;
    }
    
    @Override
    public String toString() {
        return "ErrorRecord(errorSum=" + this.getErrorSum() + ", lastErrorTime=" + this.getLastErrorTime() + ")";
    }
}
