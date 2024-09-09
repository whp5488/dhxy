package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4160)
public class ReqJoinTeamList extends Message
{
    private String activityType;
    
    public ReqJoinTeamList() {
    }
    
    public String getActivityType() {
        return this.activityType;
    }
    
    public void setActivityType(final String activityType) {
        this.activityType = activityType;
    }
}
