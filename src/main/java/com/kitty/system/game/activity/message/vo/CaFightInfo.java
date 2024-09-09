package com.kitty.system.game.activity.message.vo;

import java.io.Serializable;

public class CaFightInfo implements Serializable
{
    private int itselfLeader;
    private int itselfLeaderSerial;
    private String itselfLeaderName;
    private int enemyLeaderSerial;
    private String enemyLeaderName;
    
    public CaFightInfo() {
        this.itselfLeader = 0;
        this.itselfLeaderSerial = 0;
        this.enemyLeaderSerial = 0;
    }
    
    public int getItselfLeader() {
        return this.itselfLeader;
    }
    
    public int getItselfLeaderSerial() {
        return this.itselfLeaderSerial;
    }
    
    public String getItselfLeaderName() {
        return this.itselfLeaderName;
    }
    
    public int getEnemyLeaderSerial() {
        return this.enemyLeaderSerial;
    }
    
    public String getEnemyLeaderName() {
        return this.enemyLeaderName;
    }
    
    public void setItselfLeader(final int itselfLeader) {
        this.itselfLeader = itselfLeader;
    }
    
    public void setItselfLeaderSerial(final int itselfLeaderSerial) {
        this.itselfLeaderSerial = itselfLeaderSerial;
    }
    
    public void setItselfLeaderName(final String itselfLeaderName) {
        this.itselfLeaderName = itselfLeaderName;
    }
    
    public void setEnemyLeaderSerial(final int enemyLeaderSerial) {
        this.enemyLeaderSerial = enemyLeaderSerial;
    }
    
    public void setEnemyLeaderName(final String enemyLeaderName) {
        this.enemyLeaderName = enemyLeaderName;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CaFightInfo)) {
            return false;
        }
        CaFightInfo other = (CaFightInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getItselfLeader() != other.getItselfLeader()) {
            return false;
        }
        if (this.getItselfLeaderSerial() != other.getItselfLeaderSerial()) {
            return false;
        }
        Object this$itselfLeaderName = this.getItselfLeaderName();
        Object other$itselfLeaderName = other.getItselfLeaderName();
        if (!((this$itselfLeaderName != null) ? this$itselfLeaderName.equals(other$itselfLeaderName) : (other$itselfLeaderName == null))) {
            return false;
        }
        if (this.getEnemyLeaderSerial() != other.getEnemyLeaderSerial()) {
            return false;
        }
        Object this$enemyLeaderName = this.getEnemyLeaderName();
        Object other$enemyLeaderName = other.getEnemyLeaderName();
        return (this$enemyLeaderName != null) ? this$enemyLeaderName.equals(other$enemyLeaderName) : (other$enemyLeaderName == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CaFightInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getItselfLeader();
        result = result * 59 + this.getItselfLeaderSerial();
        Object $itselfLeaderName = this.getItselfLeaderName();
        result = result * 59 + (($itselfLeaderName == null) ? 43 : $itselfLeaderName.hashCode());
        result = result * 59 + this.getEnemyLeaderSerial();
        Object $enemyLeaderName = this.getEnemyLeaderName();
        result = result * 59 + (($enemyLeaderName == null) ? 43 : $enemyLeaderName.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "CaFightInfo(itselfLeader=" + this.getItselfLeader() + ", itselfLeaderSerial=" + this.getItselfLeaderSerial() + ", itselfLeaderName=" + this.getItselfLeaderName() + ", enemyLeaderSerial=" + this.getEnemyLeaderSerial() + ", enemyLeaderName=" + this.getEnemyLeaderName() + ")";
    }
}
