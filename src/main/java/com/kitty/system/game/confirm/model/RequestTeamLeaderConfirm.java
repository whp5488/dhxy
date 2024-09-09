package com.kitty.system.game.confirm.model;

import com.kitty.system.enums.confirm.ConfirmType;

public class RequestTeamLeaderConfirm extends RoleConfirm
{
    private int requestId;
    private boolean agree;
    
    @Override
    public ConfirmType getConfirmType() {
        return ConfirmType.REQUEST_TEAM_LEADER;
    }
    
    public RequestTeamLeaderConfirm(int requestId) {
        this.agree = false;
        this.requestId = requestId;
    }
    
    public int getRequestId() {
        return this.requestId;
    }
    
    public boolean isAgree() {
        return this.agree;
    }
    
    public void setAgree(boolean agree) {
        this.agree = agree;
    }
}
