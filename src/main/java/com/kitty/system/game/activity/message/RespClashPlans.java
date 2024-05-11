
package com.kitty.system.game.activity.message;

import com.kitty.system.game.activity.message.vo.GeneralClash;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45245)
public class RespClashPlans extends Message
{
    private String countDown;
    private List<GeneralClash> clashListA;
    private List<GeneralClash> clashListB;
    private int type;
    
    public String getCountDown() {
        return this.countDown;
    }
    
    public List<GeneralClash> getClashListA() {
        return this.clashListA;
    }
    
    public List<GeneralClash> getClashListB() {
        return this.clashListB;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setCountDown(final String countDown) {
        this.countDown = countDown;
    }
    
    public void setClashListA(final List<GeneralClash> clashListA) {
        this.clashListA = clashListA;
    }
    
    public void setClashListB(final List<GeneralClash> clashListB) {
        this.clashListB = clashListB;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespClashPlans)) {
            return false;
        }
        final RespClashPlans other = (RespClashPlans)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$countDown = this.getCountDown();
        final Object other$countDown = other.getCountDown();
        Label_0065: {
            if (this$countDown == null) {
                if (other$countDown == null) {
                    break Label_0065;
                }
            }
            else if (this$countDown.equals(other$countDown)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$clashListA = this.getClashListA();
        final Object other$clashListA = other.getClashListA();
        Label_0102: {
            if (this$clashListA == null) {
                if (other$clashListA == null) {
                    break Label_0102;
                }
            }
            else if (this$clashListA.equals(other$clashListA)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$clashListB = this.getClashListB();
        final Object other$clashListB = other.getClashListB();
        if (this$clashListB == null) {
            if (other$clashListB == null) {
                return this.getType() == other.getType();
            }
        }
        else if (this$clashListB.equals(other$clashListB)) {
            return this.getType() == other.getType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespClashPlans;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $countDown = this.getCountDown();
        result = result * 59 + (($countDown == null) ? 43 : $countDown.hashCode());
        final Object $clashListA = this.getClashListA();
        result = result * 59 + (($clashListA == null) ? 43 : $clashListA.hashCode());
        final Object $clashListB = this.getClashListB();
        result = result * 59 + (($clashListB == null) ? 43 : $clashListB.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespClashPlans(countDown=" + this.getCountDown() + ", clashListA=" + this.getClashListA() + ", clashListB=" + this.getClashListB() + ", type=" + this.getType() + ")";
    }
}
