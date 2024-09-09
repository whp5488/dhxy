package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49166)
public class RespGangWarsMatchInfo extends Message
{
    private byte type;
    private short max;
    private String sideName;
    private short sideNum;
    private short sideGateLife;
    private short sideWinRonda;
    private short sideContestWinRonda;
    private short sidePylonRonda;
    private short sideCannonRonda;
    private String enemyName;
    private short enemyNum;
    private short enemyGateLife;
    private short enemyWinRonda;
    private short enemyContestWinRonda;
    private short enemyPylonRonda;
    private short enemyCannonRonda;
    
    public RespGangWarsMatchInfo() {
        this.type = 2;
        this.max = 5000;
        this.sideNum = 0;
        this.sideGateLife = 0;
        this.sideWinRonda = 0;
        this.sideContestWinRonda = 0;
        this.sidePylonRonda = 0;
        this.sideCannonRonda = 0;
        this.enemyNum = 0;
        this.enemyGateLife = 0;
        this.enemyWinRonda = 0;
        this.enemyContestWinRonda = 0;
        this.enemyPylonRonda = 0;
        this.enemyCannonRonda = 0;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public short getMax() {
        return this.max;
    }
    
    public String getSideName() {
        return this.sideName;
    }
    
    public short getSideNum() {
        return this.sideNum;
    }
    
    public short getSideGateLife() {
        return this.sideGateLife;
    }
    
    public short getSideWinRonda() {
        return this.sideWinRonda;
    }
    
    public short getSideContestWinRonda() {
        return this.sideContestWinRonda;
    }
    
    public short getSidePylonRonda() {
        return this.sidePylonRonda;
    }
    
    public short getSideCannonRonda() {
        return this.sideCannonRonda;
    }
    
    public String getEnemyName() {
        return this.enemyName;
    }
    
    public short getEnemyNum() {
        return this.enemyNum;
    }
    
    public short getEnemyGateLife() {
        return this.enemyGateLife;
    }
    
    public short getEnemyWinRonda() {
        return this.enemyWinRonda;
    }
    
    public short getEnemyContestWinRonda() {
        return this.enemyContestWinRonda;
    }
    
    public short getEnemyPylonRonda() {
        return this.enemyPylonRonda;
    }
    
    public short getEnemyCannonRonda() {
        return this.enemyCannonRonda;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setMax(final short max) {
        this.max = max;
    }
    
    public void setSideName(final String sideName) {
        this.sideName = sideName;
    }
    
    public void setSideNum(final short sideNum) {
        this.sideNum = sideNum;
    }
    
    public void setSideGateLife(final short sideGateLife) {
        this.sideGateLife = sideGateLife;
    }
    
    public void setSideWinRonda(final short sideWinRonda) {
        this.sideWinRonda = sideWinRonda;
    }
    
    public void setSideContestWinRonda(final short sideContestWinRonda) {
        this.sideContestWinRonda = sideContestWinRonda;
    }
    
    public void setSidePylonRonda(final short sidePylonRonda) {
        this.sidePylonRonda = sidePylonRonda;
    }
    
    public void setSideCannonRonda(final short sideCannonRonda) {
        this.sideCannonRonda = sideCannonRonda;
    }
    
    public void setEnemyName(final String enemyName) {
        this.enemyName = enemyName;
    }
    
    public void setEnemyNum(final short enemyNum) {
        this.enemyNum = enemyNum;
    }
    
    public void setEnemyGateLife(final short enemyGateLife) {
        this.enemyGateLife = enemyGateLife;
    }
    
    public void setEnemyWinRonda(final short enemyWinRonda) {
        this.enemyWinRonda = enemyWinRonda;
    }
    
    public void setEnemyContestWinRonda(final short enemyContestWinRonda) {
        this.enemyContestWinRonda = enemyContestWinRonda;
    }
    
    public void setEnemyPylonRonda(final short enemyPylonRonda) {
        this.enemyPylonRonda = enemyPylonRonda;
    }
    
    public void setEnemyCannonRonda(final short enemyCannonRonda) {
        this.enemyCannonRonda = enemyCannonRonda;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespGangWarsMatchInfo)) {
            return false;
        }
        RespGangWarsMatchInfo other = (RespGangWarsMatchInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getMax() != other.getMax()) {
            return false;
        }
        Object this$sideName = this.getSideName();
        Object other$sideName = other.getSideName();
        if (!((this$sideName != null) ? this$sideName.equals(other$sideName) : (other$sideName == null))) {
            return false;
        }
        if (this.getSideNum() != other.getSideNum()) {
            return false;
        }
        if (this.getSideGateLife() != other.getSideGateLife()) {
            return false;
        }
        if (this.getSideWinRonda() != other.getSideWinRonda()) {
            return false;
        }
        if (this.getSideContestWinRonda() != other.getSideContestWinRonda()) {
            return false;
        }
        if (this.getSidePylonRonda() != other.getSidePylonRonda()) {
            return false;
        }
        if (this.getSideCannonRonda() != other.getSideCannonRonda()) {
            return false;
        }
        Object this$enemyName = this.getEnemyName();
        Object other$enemyName = other.getEnemyName();
        return ((this$enemyName != null) ? this$enemyName.equals(other$enemyName) : (other$enemyName == null)) && this.getEnemyNum() == other.getEnemyNum() && this.getEnemyGateLife() == other.getEnemyGateLife() && this.getEnemyWinRonda() == other.getEnemyWinRonda() && this.getEnemyContestWinRonda() == other.getEnemyContestWinRonda() && this.getEnemyPylonRonda() == other.getEnemyPylonRonda() && this.getEnemyCannonRonda() == other.getEnemyCannonRonda();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespGangWarsMatchInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getMax();
        Object $sideName = this.getSideName();
        result = result * 59 + (($sideName == null) ? 43 : $sideName.hashCode());
        result = result * 59 + this.getSideNum();
        result = result * 59 + this.getSideGateLife();
        result = result * 59 + this.getSideWinRonda();
        result = result * 59 + this.getSideContestWinRonda();
        result = result * 59 + this.getSidePylonRonda();
        result = result * 59 + this.getSideCannonRonda();
        Object $enemyName = this.getEnemyName();
        result = result * 59 + (($enemyName == null) ? 43 : $enemyName.hashCode());
        result = result * 59 + this.getEnemyNum();
        result = result * 59 + this.getEnemyGateLife();
        result = result * 59 + this.getEnemyWinRonda();
        result = result * 59 + this.getEnemyContestWinRonda();
        result = result * 59 + this.getEnemyPylonRonda();
        result = result * 59 + this.getEnemyCannonRonda();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespGangWarsMatchInfo(type=" + (int)this.getType() + ", max=" + (int)this.getMax() + ", sideName=" + this.getSideName() + ", sideNum=" + (int)this.getSideNum() + ", sideGateLife=" + (int)this.getSideGateLife() + ", sideWinRonda=" + (int)this.getSideWinRonda() + ", sideContestWinRonda=" + (int)this.getSideContestWinRonda() + ", sidePylonRonda=" + (int)this.getSidePylonRonda() + ", sideCannonRonda=" + (int)this.getSideCannonRonda() + ", enemyName=" + this.getEnemyName() + ", enemyNum=" + (int)this.getEnemyNum() + ", enemyGateLife=" + (int)this.getEnemyGateLife() + ", enemyWinRonda=" + (int)this.getEnemyWinRonda() + ", enemyContestWinRonda=" + (int)this.getEnemyContestWinRonda() + ", enemyPylonRonda=" + (int)this.getEnemyPylonRonda() + ", enemyCannonRonda=" + (int)this.getEnemyCannonRonda() + ")";
    }
}
