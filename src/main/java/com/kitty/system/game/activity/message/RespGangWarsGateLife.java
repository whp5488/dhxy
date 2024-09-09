package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49168)
public class RespGangWarsGateLife extends Message
{
    private short sideLife;
    private short enemyLife;
    private byte type;
    private byte stance;
    
    public short getSideLife() {
        return this.sideLife;
    }
    
    public short getEnemyLife() {
        return this.enemyLife;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public byte getStance() {
        return this.stance;
    }
    
    public void setSideLife(final short sideLife) {
        this.sideLife = sideLife;
    }
    
    public void setEnemyLife(final short enemyLife) {
        this.enemyLife = enemyLife;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setStance(final byte stance) {
        this.stance = stance;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespGangWarsGateLife)) {
            return false;
        }
        RespGangWarsGateLife other = (RespGangWarsGateLife)o;
        return other.canEqual(this) && this.getSideLife() == other.getSideLife() && this.getEnemyLife() == other.getEnemyLife() && this.getType() == other.getType() && this.getStance() == other.getStance();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespGangWarsGateLife;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSideLife();
        result = result * 59 + this.getEnemyLife();
        result = result * 59 + this.getType();
        result = result * 59 + this.getStance();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespGangWarsGateLife(sideLife=" + (int)this.getSideLife() + ", enemyLife=" + (int)this.getEnemyLife() + ", type=" + (int)this.getType() + ", stance=" + (int)this.getStance() + ")";
    }
    
    public RespGangWarsGateLife(final short sideLife, final short enemyLife, final byte type, final byte stance) {
        this.sideLife = 0;
        this.enemyLife = 0;
        this.sideLife = sideLife;
        this.enemyLife = enemyLife;
        this.type = type;
        this.stance = stance;
    }
    
    public RespGangWarsGateLife() {
        this.sideLife = 0;
        this.enemyLife = 0;
    }
}
