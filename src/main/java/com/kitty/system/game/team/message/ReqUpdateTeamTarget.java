
package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4159)
public class ReqUpdateTeamTarget extends Message
{
    private int roleId;
    private String activityType;
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getActivityType() {
        return this.activityType;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setActivityType(final String activityType) {
        this.activityType = activityType;
    }
}
