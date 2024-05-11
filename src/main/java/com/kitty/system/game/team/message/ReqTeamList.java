
package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4158)
public class ReqTeamList extends Message
{
    private String activityType;
    
    public String getActivityType() {
        return this.activityType;
    }
    
    public void setActivityType(final String activityType) {
        this.activityType = activityType;
    }
}
