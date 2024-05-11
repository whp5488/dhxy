
package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49164)
public class RespGangWarsIcon extends Message
{
    private byte state;
    private short max;
    private String sideName;
    private short sideLife;
    private byte sideGrade;
    private String enemyName;
    private short enemyLife;
    private byte enemyGrade;
    private byte stance;
    private int time;
    
    public RespGangWarsIcon() {
        this.state = 0;
        this.max = 5000;
        this.sideLife = 0;
        this.sideGrade = 0;
        this.enemyLife = 0;
        this.enemyGrade = 0;
        this.time = 0;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public short getMax() {
        return this.max;
    }
    
    public String getSideName() {
        return this.sideName;
    }
    
    public short getSideLife() {
        return this.sideLife;
    }
    
    public byte getSideGrade() {
        return this.sideGrade;
    }
    
    public String getEnemyName() {
        return this.enemyName;
    }
    
    public short getEnemyLife() {
        return this.enemyLife;
    }
    
    public byte getEnemyGrade() {
        return this.enemyGrade;
    }
    
    public byte getStance() {
        return this.stance;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setMax(final short max) {
        this.max = max;
    }
    
    public void setSideName(final String sideName) {
        this.sideName = sideName;
    }
    
    public void setSideLife(final short sideLife) {
        this.sideLife = sideLife;
    }
    
    public void setSideGrade(final byte sideGrade) {
        this.sideGrade = sideGrade;
    }
    
    public void setEnemyName(final String enemyName) {
        this.enemyName = enemyName;
    }
    
    public void setEnemyLife(final short enemyLife) {
        this.enemyLife = enemyLife;
    }
    
    public void setEnemyGrade(final byte enemyGrade) {
        this.enemyGrade = enemyGrade;
    }
    
    public void setStance(final byte stance) {
        this.stance = stance;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespGangWarsIcon)) {
            return false;
        }
        final RespGangWarsIcon other = (RespGangWarsIcon)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getState() != other.getState()) {
            return false;
        }
        if (this.getMax() != other.getMax()) {
            return false;
        }
        final Object this$sideName = this.getSideName();
        final Object other$sideName = other.getSideName();
        Label_0091: {
            if (this$sideName == null) {
                if (other$sideName == null) {
                    break Label_0091;
                }
            }
            else if (this$sideName.equals(other$sideName)) {
                break Label_0091;
            }
            return false;
        }
        if (this.getSideLife() != other.getSideLife()) {
            return false;
        }
        if (this.getSideGrade() != other.getSideGrade()) {
            return false;
        }
        final Object this$enemyName = this.getEnemyName();
        final Object other$enemyName = other.getEnemyName();
        if (this$enemyName == null) {
            if (other$enemyName == null) {
                return this.getEnemyLife() == other.getEnemyLife() && this.getEnemyGrade() == other.getEnemyGrade() && this.getStance() == other.getStance() && this.getTime() == other.getTime();
            }
        }
        else if (this$enemyName.equals(other$enemyName)) {
            return this.getEnemyLife() == other.getEnemyLife() && this.getEnemyGrade() == other.getEnemyGrade() && this.getStance() == other.getStance() && this.getTime() == other.getTime();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespGangWarsIcon;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getState();
        result = result * 59 + this.getMax();
        final Object $sideName = this.getSideName();
        result = result * 59 + (($sideName == null) ? 43 : $sideName.hashCode());
        result = result * 59 + this.getSideLife();
        result = result * 59 + this.getSideGrade();
        final Object $enemyName = this.getEnemyName();
        result = result * 59 + (($enemyName == null) ? 43 : $enemyName.hashCode());
        result = result * 59 + this.getEnemyLife();
        result = result * 59 + this.getEnemyGrade();
        result = result * 59 + this.getStance();
        result = result * 59 + this.getTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespGangWarsIcon(state=" + this.getState() + ", max=" + this.getMax() + ", sideName=" + this.getSideName() + ", sideLife=" + this.getSideLife() + ", sideGrade=" + this.getSideGrade() + ", enemyName=" + this.getEnemyName() + ", enemyLife=" + this.getEnemyLife() + ", enemyGrade=" + this.getEnemyGrade() + ", stance=" + this.getStance() + ", time=" + this.getTime() + ")";
    }
}
