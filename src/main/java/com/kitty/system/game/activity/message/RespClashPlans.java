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
    
    public RespClashPlans() {
    }
    
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
        RespClashPlans other = (RespClashPlans)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$countDown = this.getCountDown();
        Object other$countDown = other.getCountDown();
        if (!((this$countDown != null) ? this$countDown.equals(other$countDown) : (other$countDown == null))) {
            return false;
        }
        Object this$clashListA = this.getClashListA();
        Object other$clashListA = other.getClashListA();
        if (!((this$clashListA != null) ? this$clashListA.equals(other$clashListA) : (other$clashListA == null))) {
            return false;
        }
        Object this$clashListB = this.getClashListB();
        Object other$clashListB = other.getClashListB();
        return ((this$clashListB != null) ? this$clashListB.equals(other$clashListB) : (other$clashListB == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespClashPlans;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $countDown = this.getCountDown();
        result = result * 59 + (($countDown == null) ? 43 : $countDown.hashCode());
        Object $clashListA = this.getClashListA();
        result = result * 59 + (($clashListA == null) ? 43 : $clashListA.hashCode());
        Object $clashListB = this.getClashListB();
        result = result * 59 + (($clashListB == null) ? 43 : $clashListB.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespClashPlans(countDown=" + this.getCountDown() + ", clashListA=" + this.getClashListA() + ", clashListB=" + this.getClashListB() + ", type=" + this.getType() + ")";
    }
}
