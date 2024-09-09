package com.kitty.system.game.role.domain;

import java.io.Serializable;

public class RoleOfflineNotice implements Serializable
{
    private int state;
    private int aloneOfflineTime;
    private int teamOfflineTime;
    
    public RoleOfflineNotice() {
        this.state = 0;
        this.aloneOfflineTime = 36000000;
        this.teamOfflineTime = 36000000;
    }
    
    public void setState(final int state) {
        this.state = state;
    }
    
    public void setAloneOfflineTime(final int aloneOfflineTime) {
        this.aloneOfflineTime = aloneOfflineTime;
    }
    
    public void setTeamOfflineTime(final int teamOfflineTime) {
        this.teamOfflineTime = teamOfflineTime;
    }
    
    public int getState() {
        return this.state;
    }
    
    public int getAloneOfflineTime() {
        return this.aloneOfflineTime;
    }
    
    public int getTeamOfflineTime() {
        return this.teamOfflineTime;
    }
}
