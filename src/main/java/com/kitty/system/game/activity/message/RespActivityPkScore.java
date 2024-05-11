
package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49177)
public class RespActivityPkScore extends Message
{
    private byte stage;
    private short pkValue;
    private short score;
    private int startTime;
    private int stageDurationTime;
    private short rank;
    private byte type;
    
    public RespActivityPkScore() {
        this.stage = 2;
        this.stageDurationTime = 5400;
    }
    
    public byte getStage() {
        return this.stage;
    }
    
    public short getPkValue() {
        return this.pkValue;
    }
    
    public short getScore() {
        return this.score;
    }
    
    public int getStartTime() {
        return this.startTime;
    }
    
    public int getStageDurationTime() {
        return this.stageDurationTime;
    }
    
    public short getRank() {
        return this.rank;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setStage(final byte stage) {
        this.stage = stage;
    }
    
    public void setPkValue(final short pkValue) {
        this.pkValue = pkValue;
    }
    
    public void setScore(final short score) {
        this.score = score;
    }
    
    public void setStartTime(final int startTime) {
        this.startTime = startTime;
    }
    
    public void setStageDurationTime(final int stageDurationTime) {
        this.stageDurationTime = stageDurationTime;
    }
    
    public void setRank(final short rank) {
        this.rank = rank;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespActivityPkScore)) {
            return false;
        }
        final RespActivityPkScore other = (RespActivityPkScore)o;
        return other.canEqual(this) && this.getStage() == other.getStage() && this.getPkValue() == other.getPkValue() && this.getScore() == other.getScore() && this.getStartTime() == other.getStartTime() && this.getStageDurationTime() == other.getStageDurationTime() && this.getRank() == other.getRank() && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespActivityPkScore;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getStage();
        result = result * 59 + this.getPkValue();
        result = result * 59 + this.getScore();
        result = result * 59 + this.getStartTime();
        result = result * 59 + this.getStageDurationTime();
        result = result * 59 + this.getRank();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespActivityPkScore(stage=" + this.getStage() + ", pkValue=" + this.getPkValue() + ", score=" + this.getScore() + ", startTime=" + this.getStartTime() + ", stageDurationTime=" + this.getStageDurationTime() + ", rank=" + this.getRank() + ", type=" + this.getType() + ")";
    }
}
