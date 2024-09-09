package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49165)
public class RespGangWarsInfo extends Message
{
    private String sideName;
    private short sideNum;
    private String enemyName;
    private short enemyNum;
    
    public RespGangWarsInfo() {
        this.sideNum = 0;
        this.enemyNum = 0;
    }
    
    public String getSideName() {
        return this.sideName;
    }
    
    public short getSideNum() {
        return this.sideNum;
    }
    
    public String getEnemyName() {
        return this.enemyName;
    }
    
    public short getEnemyNum() {
        return this.enemyNum;
    }
    
    public void setSideName(final String sideName) {
        this.sideName = sideName;
    }
    
    public void setSideNum(final short sideNum) {
        this.sideNum = sideNum;
    }
    
    public void setEnemyName(final String enemyName) {
        this.enemyName = enemyName;
    }
    
    public void setEnemyNum(final short enemyNum) {
        this.enemyNum = enemyNum;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespGangWarsInfo)) {
            return false;
        }
        RespGangWarsInfo other = (RespGangWarsInfo)o;
        if (!other.canEqual(this)) {
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
        Object this$enemyName = this.getEnemyName();
        Object other$enemyName = other.getEnemyName();
        return ((this$enemyName != null) ? this$enemyName.equals(other$enemyName) : (other$enemyName == null)) && this.getEnemyNum() == other.getEnemyNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespGangWarsInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $sideName = this.getSideName();
        result = result * 59 + (($sideName == null) ? 43 : $sideName.hashCode());
        result = result * 59 + this.getSideNum();
        Object $enemyName = this.getEnemyName();
        result = result * 59 + (($enemyName == null) ? 43 : $enemyName.hashCode());
        result = result * 59 + this.getEnemyNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespGangWarsInfo(sideName=" + this.getSideName() + ", sideNum=" + (int)this.getSideNum() + ", enemyName=" + this.getEnemyName() + ", enemyNum=" + (int)this.getEnemyNum() + ")";
    }
}
