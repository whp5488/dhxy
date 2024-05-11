
package com.kitty.system.game.fightnew.messages;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 3471)
public class ReqReturnAnimationPredictTime extends Message
{
    private int fightId;
    private int round;
    private int predictTime;
    
    public int getFightId() {
        return this.fightId;
    }
    
    public int getRound() {
        return this.round;
    }
    
    public int getPredictTime() {
        return this.predictTime;
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
    
    @ConstructorProperties({ "fightId", "round", "predictTime" })
    public ReqReturnAnimationPredictTime(final int fightId, final int round, final int predictTime) {
        this.fightId = fightId;
        this.round = round;
        this.predictTime = predictTime;
    }
    
    public ReqReturnAnimationPredictTime() {
    }
}
