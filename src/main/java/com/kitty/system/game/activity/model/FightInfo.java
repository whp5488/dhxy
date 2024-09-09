package com.kitty.system.game.activity.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class FightInfo implements Serializable
{
    private long itselfLeader;
    private List<Long> itself;
    private long enemyLeader;
    private List<Long> enemy;
    
    public FightInfo() {
        this.itselfLeader = 0L;
        this.itself = new ArrayList(5);
        this.enemyLeader = 0L;
        this.enemy = new ArrayList(5);
    }
    
    public long getItselfLeader() {
        return this.itselfLeader;
    }
    
    public List<Long> getItself() {
        return this.itself;
    }
    
    public long getEnemyLeader() {
        return this.enemyLeader;
    }
    
    public List<Long> getEnemy() {
        return this.enemy;
    }
    
    public void setItselfLeader(final long itselfLeader) {
        this.itselfLeader = itselfLeader;
    }
    
    public void setItself(final List<Long> itself) {
        this.itself = itself;
    }
    
    public void setEnemyLeader(final long enemyLeader) {
        this.enemyLeader = enemyLeader;
    }
    
    public void setEnemy(final List<Long> enemy) {
        this.enemy = enemy;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FightInfo)) {
            return false;
        }
        FightInfo other = (FightInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getItselfLeader() != other.getItselfLeader()) {
            return false;
        }
        Object this$itself = this.getItself();
        Object other$itself = other.getItself();
        if (!((this$itself != null) ? this$itself.equals(other$itself) : (other$itself == null))) {
            return false;
        }
        if (this.getEnemyLeader() != other.getEnemyLeader()) {
            return false;
        }
        Object this$enemy = this.getEnemy();
        Object other$enemy = other.getEnemy();
        return (this$enemy != null) ? this$enemy.equals(other$enemy) : (other$enemy == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FightInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $itselfLeader = this.getItselfLeader();
        result = result * 59 + (int)($itselfLeader >>> 32 ^ $itselfLeader);
        Object $itself = this.getItself();
        result = result * 59 + (($itself == null) ? 43 : $itself.hashCode());
        long $enemyLeader = this.getEnemyLeader();
        result = result * 59 + (int)($enemyLeader >>> 32 ^ $enemyLeader);
        Object $enemy = this.getEnemy();
        result = result * 59 + (($enemy == null) ? 43 : $enemy.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "FightInfo(itselfLeader=" + this.getItselfLeader() + ", itself=" + this.getItself() + ", enemyLeader=" + this.getEnemyLeader() + ", enemy=" + this.getEnemy() + ")";
    }
}
