
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
        this.itself = new ArrayList<Long>(5);
        this.enemyLeader = 0L;
        this.enemy = new ArrayList<Long>(5);
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
        final FightInfo other = (FightInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getItselfLeader() != other.getItselfLeader()) {
            return false;
        }
        final Object this$itself = this.getItself();
        final Object other$itself = other.getItself();
        Label_0079: {
            if (this$itself == null) {
                if (other$itself == null) {
                    break Label_0079;
                }
            }
            else if (this$itself.equals(other$itself)) {
                break Label_0079;
            }
            return false;
        }
        if (this.getEnemyLeader() != other.getEnemyLeader()) {
            return false;
        }
        final Object this$enemy = this.getEnemy();
        final Object other$enemy = other.getEnemy();
        if (this$enemy == null) {
            if (other$enemy == null) {
                return true;
            }
        }
        else if (this$enemy.equals(other$enemy)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FightInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $itselfLeader = this.getItselfLeader();
        result = result * 59 + (int)($itselfLeader >>> 32 ^ $itselfLeader);
        final Object $itself = this.getItself();
        result = result * 59 + (($itself == null) ? 43 : $itself.hashCode());
        final long $enemyLeader = this.getEnemyLeader();
        result = result * 59 + (int)($enemyLeader >>> 32 ^ $enemyLeader);
        final Object $enemy = this.getEnemy();
        result = result * 59 + (($enemy == null) ? 43 : $enemy.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "FightInfo(itselfLeader=" + this.getItselfLeader() + ", itself=" + this.getItself() + ", enemyLeader=" + this.getEnemyLeader() + ", enemy=" + this.getEnemy() + ")";
    }
}
