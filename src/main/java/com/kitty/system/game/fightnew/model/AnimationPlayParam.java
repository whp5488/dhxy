package com.kitty.system.game.fightnew.model;

public class AnimationPlayParam
{
    private int fightId;
    private int round;
    private int predictTime;
    private int practicalTime;
    private boolean isEnd;
    
    public int getFightId() {
        return this.fightId;
    }
    
    public int getRound() {
        return this.round;
    }
    
    public int getPredictTime() {
        return this.predictTime;
    }
    
    public int getPracticalTime() {
        return this.practicalTime;
    }
    
    public boolean isEnd() {
        return this.isEnd;
    }
    
    public void setFightId(final int fightId) {
        this.fightId = fightId;
    }
    
    public void setRound(final int round) {
        this.round = round;
    }
    
    public void setPredictTime(final int predictTime) {
        this.predictTime = predictTime;
    }
    
    public void setPracticalTime(final int practicalTime) {
        this.practicalTime = practicalTime;
    }
    
    public void setEnd(final boolean isEnd) {
        this.isEnd = isEnd;
    }
    
    public AnimationPlayParam(final int fightId, final int round, final int predictTime, final int practicalTime, final boolean isEnd) {
        this.isEnd = false;
        this.fightId = fightId;
        this.round = round;
        this.predictTime = predictTime;
        this.practicalTime = practicalTime;
        this.isEnd = isEnd;
    }
    
    public AnimationPlayParam() {
        this.isEnd = false;
    }
}
