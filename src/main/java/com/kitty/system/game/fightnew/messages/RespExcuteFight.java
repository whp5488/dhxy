package com.kitty.system.game.fightnew.messages;

import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 3585)
public class RespExcuteFight extends Message
{
    private int round;
    private String endTime;
    private List<ExcuteFightParam> excuteFightParams;
    
    public RespExcuteFight() {
    }
    
    public int getRound() {
        return this.round;
    }
    
    public String getEndTime() {
        return this.endTime;
    }
    
    public List<ExcuteFightParam> getExcuteFightParams() {
        return this.excuteFightParams;
    }
    
    public void setRound(final int round) {
        this.round = round;
    }
    
    public void setEndTime(final String endTime) {
        this.endTime = endTime;
    }
    
    public void setExcuteFightParams(final List<ExcuteFightParam> excuteFightParams) {
        this.excuteFightParams = excuteFightParams;
    }
}
