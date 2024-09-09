package com.kitty.system.game.activity.message;

import java.util.ArrayList;
import com.kitty.system.game.activity.message.vo.MemberInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45248)
public class RespCaClashResult extends Message
{
    private byte result;
    private short score;
    private short maxScore;
    private List<MemberInfo> itself;
    private List<MemberInfo> enemy;
    
    public RespCaClashResult() {
        this.score = 0;
        this.maxScore = 0;
        this.itself = new ArrayList(5);
        this.enemy = new ArrayList(5);
    }
    
    public byte getResult() {
        return this.result;
    }
    
    public short getScore() {
        return this.score;
    }
    
    public short getMaxScore() {
        return this.maxScore;
    }
    
    public List<MemberInfo> getItself() {
        return this.itself;
    }
    
    public List<MemberInfo> getEnemy() {
        return this.enemy;
    }
    
    public void setResult(final byte result) {
        this.result = result;
    }
    
    public void setScore(final short score) {
        this.score = score;
    }
    
    public void setMaxScore(final short maxScore) {
        this.maxScore = maxScore;
    }
    
    public void setItself(final List<MemberInfo> itself) {
        this.itself = itself;
    }
    
    public void setEnemy(final List<MemberInfo> enemy) {
        this.enemy = enemy;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespCaClashResult)) {
            return false;
        }
        RespCaClashResult other = (RespCaClashResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getResult() != other.getResult()) {
            return false;
        }
        if (this.getScore() != other.getScore()) {
            return false;
        }
        if (this.getMaxScore() != other.getMaxScore()) {
            return false;
        }
        Object this$itself = this.getItself();
        Object other$itself = other.getItself();
        if (!((this$itself != null) ? this$itself.equals(other$itself) : (other$itself == null))) {
            return false;
        }
        Object this$enemy = this.getEnemy();
        Object other$enemy = other.getEnemy();
        return (this$enemy != null) ? this$enemy.equals(other$enemy) : (other$enemy == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespCaClashResult;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getResult();
        result = result * 59 + this.getScore();
        result = result * 59 + this.getMaxScore();
        Object $itself = this.getItself();
        result = result * 59 + (($itself == null) ? 43 : $itself.hashCode());
        Object $enemy = this.getEnemy();
        result = result * 59 + (($enemy == null) ? 43 : $enemy.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCaClashResult(result=" + (int)this.getResult() + ", score=" + (int)this.getScore() + ", maxScore=" + (int)this.getMaxScore() + ", itself=" + this.getItself() + ", enemy=" + this.getEnemy() + ")";
    }
}
