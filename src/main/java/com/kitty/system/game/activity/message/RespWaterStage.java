package com.kitty.system.game.activity.message;

import java.util.ArrayList;
import com.kitty.system.game.activity.message.vo.MemberInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49162)
public class RespWaterStage extends Message
{
    private byte round;
    private byte currentSession;
    private List<MemberInfo> itself;
    private List<MemberInfo> enemy;
    private byte state;
    private int time;
    
    public RespWaterStage() {
        this.round = 2;
        this.currentSession = 1;
        this.itself = new ArrayList(5);
        this.enemy = new ArrayList(5);
        this.time = 0;
    }
    
    public byte getRound() {
        return this.round;
    }
    
    public byte getCurrentSession() {
        return this.currentSession;
    }
    
    public List<MemberInfo> getItself() {
        return this.itself;
    }
    
    public List<MemberInfo> getEnemy() {
        return this.enemy;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setRound(final byte round) {
        this.round = round;
    }
    
    public void setCurrentSession(final byte currentSession) {
        this.currentSession = currentSession;
    }
    
    public void setItself(final List<MemberInfo> itself) {
        this.itself = itself;
    }
    
    public void setEnemy(final List<MemberInfo> enemy) {
        this.enemy = enemy;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespWaterStage)) {
            return false;
        }
        RespWaterStage other = (RespWaterStage)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRound() != other.getRound()) {
            return false;
        }
        if (this.getCurrentSession() != other.getCurrentSession()) {
            return false;
        }
        Object this$itself = this.getItself();
        Object other$itself = other.getItself();
        if (!((this$itself != null) ? this$itself.equals(other$itself) : (other$itself == null))) {
            return false;
        }
        Object this$enemy = this.getEnemy();
        Object other$enemy = other.getEnemy();
        return ((this$enemy != null) ? this$enemy.equals(other$enemy) : (other$enemy == null)) && this.getState() == other.getState() && this.getTime() == other.getTime();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespWaterStage;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRound();
        result = result * 59 + this.getCurrentSession();
        Object $itself = this.getItself();
        result = result * 59 + (($itself == null) ? 43 : $itself.hashCode());
        Object $enemy = this.getEnemy();
        result = result * 59 + (($enemy == null) ? 43 : $enemy.hashCode());
        result = result * 59 + this.getState();
        result = result * 59 + this.getTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespWaterStage(round=" + (int)this.getRound() + ", currentSession=" + (int)this.getCurrentSession() + ", itself=" + this.getItself() + ", enemy=" + this.getEnemy() + ", state=" + (int)this.getState() + ", time=" + this.getTime() + ")";
    }
}
