
package com.kitty.cross.login.model;

public class CrossInfo
{
    private int fromServer;
    private String fromIp;
    private boolean isCross;
    private int crossType;
    
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
        final CrossInfo other = (CrossInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getFromServer() != other.getFromServer()) {
            return false;
        }
        final Object this$fromIp = this.getFromIp();
        final Object other$fromIp = other.getFromIp();
        if (this$fromIp == null) {
            if (other$fromIp == null) {
                return this.isCross() == other.isCross() && this.getCrossType() == other.getCrossType();
            }
        }
        else if (this$fromIp.equals(other$fromIp)) {
            return this.isCross() == other.isCross() && this.getCrossType() == other.getCrossType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CrossInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getFromServer();
        final Object $fromIp = this.getFromIp();
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
