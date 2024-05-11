
package com.kitty.system.game.activity.message;

import java.util.ArrayList;
import com.kitty.system.game.activity.message.vo.WaterMatchResultInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49163)
public class RespWaterEnd extends Message
{
    private byte round;
    private short rank;
    private List<Integer> itself;
    private List<WaterMatchResultInfo> matchResult;
    private int score;
    private int feats;
    
    public RespWaterEnd() {
        this.round = 0;
        this.rank = 0;
        this.matchResult = new ArrayList<WaterMatchResultInfo>(3);
        this.score = 0;
        this.feats = 0;
    }
    
    public byte getRound() {
        return this.round;
    }
    
    public short getRank() {
        return this.rank;
    }
    
    public List<Integer> getItself() {
        return this.itself;
    }
    
    public List<WaterMatchResultInfo> getMatchResult() {
        return this.matchResult;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public int getFeats() {
        return this.feats;
    }
    
    public void setRound(final byte round) {
        this.round = round;
    }
    
    public void setRank(final short rank) {
        this.rank = rank;
    }
    
    public void setItself(final List<Integer> itself) {
        this.itself = itself;
    }
    
    public void setMatchResult(final List<WaterMatchResultInfo> matchResult) {
        this.matchResult = matchResult;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setFeats(final int feats) {
        this.feats = feats;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespWaterEnd)) {
            return false;
        }
        final RespWaterEnd other = (RespWaterEnd)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRound() != other.getRound()) {
            return false;
        }
        if (this.getRank() != other.getRank()) {
            return false;
        }
        final Object this$itself = this.getItself();
        final Object other$itself = other.getItself();
        Label_0091: {
            if (this$itself == null) {
                if (other$itself == null) {
                    break Label_0091;
                }
            }
            else if (this$itself.equals(other$itself)) {
                break Label_0091;
            }
            return false;
        }
        final Object this$matchResult = this.getMatchResult();
        final Object other$matchResult = other.getMatchResult();
        if (this$matchResult == null) {
            if (other$matchResult == null) {
                return this.getScore() == other.getScore() && this.getFeats() == other.getFeats();
            }
        }
        else if (this$matchResult.equals(other$matchResult)) {
            return this.getScore() == other.getScore() && this.getFeats() == other.getFeats();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespWaterEnd;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRound();
        result = result * 59 + this.getRank();
        final Object $itself = this.getItself();
        result = result * 59 + (($itself == null) ? 43 : $itself.hashCode());
        final Object $matchResult = this.getMatchResult();
        result = result * 59 + (($matchResult == null) ? 43 : $matchResult.hashCode());
        result = result * 59 + this.getScore();
        result = result * 59 + this.getFeats();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespWaterEnd(round=" + this.getRound() + ", rank=" + this.getRank() + ", itself=" + this.getItself() + ", matchResult=" + this.getMatchResult() + ", score=" + this.getScore() + ", feats=" + this.getFeats() + ")";
    }
}
