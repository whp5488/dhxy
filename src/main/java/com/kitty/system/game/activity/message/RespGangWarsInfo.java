
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
        final RespGangWarsInfo other = (RespGangWarsInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$sideName = this.getSideName();
        final Object other$sideName = other.getSideName();
        Label_0065: {
            if (this$sideName == null) {
                if (other$sideName == null) {
                    break Label_0065;
                }
            }
            else if (this$sideName.equals(other$sideName)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getSideNum() != other.getSideNum()) {
            return false;
        }
        final Object this$enemyName = this.getEnemyName();
        final Object other$enemyName = other.getEnemyName();
        if (this$enemyName == null) {
            if (other$enemyName == null) {
                return this.getEnemyNum() == other.getEnemyNum();
            }
        }
        else if (this$enemyName.equals(other$enemyName)) {
            return this.getEnemyNum() == other.getEnemyNum();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespGangWarsInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $sideName = this.getSideName();
        result = result * 59 + (($sideName == null) ? 43 : $sideName.hashCode());
        result = result * 59 + this.getSideNum();
        final Object $enemyName = this.getEnemyName();
        result = result * 59 + (($enemyName == null) ? 43 : $enemyName.hashCode());
        result = result * 59 + this.getEnemyNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespGangWarsInfo(sideName=" + this.getSideName() + ", sideNum=" + this.getSideNum() + ", enemyName=" + this.getEnemyName() + ", enemyNum=" + this.getEnemyNum() + ")";
    }
}
