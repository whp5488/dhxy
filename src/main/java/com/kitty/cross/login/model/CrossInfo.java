package com.kitty.cross.login.model;

public class CrossInfo
{
    private int fromServer;
    private String fromIp;
    private boolean isCross;
    private int crossType;
    
    public CrossInfo() {
    }
    
    public int getFromServer() {
        return this.fromServer;
    }
    
    public String getFromIp() {
        return this.fromIp;
    }
    
    public boolean isCross() {
        return this.isCross;
    }
    
    public int getCrossType() {
        return this.crossType;
    }
    
    public void setFromServer(final int fromServer) {
        this.fromServer = fromServer;
    }
    
    public void setFromIp(final String fromIp) {
        this.fromIp = fromIp;
    }
    
    public void setCross(final boolean isCross) {
        this.isCross = isCross;
    }
    
    public void setCrossType(final int crossType) {
        this.crossType = crossType;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CrossInfo)) {
            return false;
        }
        CrossInfo other = (CrossInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getFromServer() != other.getFromServer()) {
            return false;
        }
        Object this$fromIp = this.getFromIp();
        Object other$fromIp = other.getFromIp();
        return ((this$fromIp != null) ? this$fromIp.equals(other$fromIp) : (other$fromIp == null)) && this.isCross() == other.isCross() && this.getCrossType() == other.getCrossType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CrossInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getFromServer();
        Object $fromIp = this.getFromIp();
        result = result * 59 + (($fromIp == null) ? 43 : $fromIp.hashCode());
        result = result * 59 + (this.isCross() ? 79 : 97);
        result = result * 59 + this.getCrossType();
        return result;
    }
    
    @Override
    public String toString() {
        return "CrossInfo(fromServer=" + this.getFromServer() + ", fromIp=" + this.getFromIp() + ", isCross=" + this.isCross() + ", crossType=" + this.getCrossType() + ")";
    }
}
