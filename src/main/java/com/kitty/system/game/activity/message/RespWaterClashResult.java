
package com.kitty.system.game.activity.message;

import java.util.ArrayList;
import com.kitty.system.game.activity.message.vo.MemberInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45247)
public class RespWaterClashResult extends Message
{
    private byte result;
    private short score;
    private short feats;
    private List<MemberInfo> itself;
    private List<MemberInfo> enemy;
    
    public RespWaterClashResult() {
        this.score = 0;
        this.feats = 0;
        this.itself = new ArrayList<MemberInfo>(5);
        this.enemy = new ArrayList<MemberInfo>(5);
    }
    
    public byte getResult() {
        return this.result;
    }
    
    public short getScore() {
        return this.score;
    }
    
    public short getFeats() {
        return this.feats;
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
    
    public void setFeats(final short feats) {
        this.feats = feats;
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
        if (!(o instanceof RespWaterClashResult)) {
            return false;
        }
        final RespWaterClashResult other = (RespWaterClashResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getResult() != other.getResult()) {
            return false;
        }
        if (this.getScore() != other.getScore()) {
            return false;
        }
        if (this.getFeats() != other.getFeats()) {
            return false;
        }
        final Object this$itself = this.getItself();
        final Object other$itself = other.getItself();
        Label_0104: {
            if (this$itself == null) {
                if (other$itself == null) {
                    break Label_0104;
                }
            }
            else if (this$itself.equals(other$itself)) {
                break Label_0104;
            }
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
        return other instanceof RespWaterClashResult;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getResult();
        result = result * 59 + this.getScore();
        result = result * 59 + this.getFeats();
        final Object $itself = this.getItself();
        result = result * 59 + (($itself == null) ? 43 : $itself.hashCode());
        final Object $enemy = this.getEnemy();
        result = result * 59 + (($enemy == null) ? 43 : $enemy.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespWaterClashResult(result=" + this.getResult() + ", score=" + this.getScore() + ", feats=" + this.getFeats() + ", itself=" + this.getItself() + ", enemy=" + this.getEnemy() + ")";
    }
}
