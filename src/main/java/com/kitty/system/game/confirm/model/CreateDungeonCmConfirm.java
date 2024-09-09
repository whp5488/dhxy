package com.kitty.system.game.confirm.model;

import com.kitty.system.enums.confirm.ConfirmType;
import java.util.concurrent.ScheduledFuture;
import java.util.Map;

public class CreateDungeonCmConfirm extends RoleConfirm
{
    private Map<Integer, Short> ballotResults;
    private ScheduledFuture<?> schedule;
    private String type;
    
    public CreateDungeonCmConfirm(Map<Integer, Short> ballotResults, ScheduledFuture<?> schedule, String type) {
        this.type = type;
        this.schedule = schedule;
        this.ballotResults = ballotResults;
    }
    
    @Override
    public ConfirmType getConfirmType() {
        return ConfirmType.CREATE_DUNGEON_CM_F;
    }
    
    public Map<Integer, Short> getBallotResults() {
        return this.ballotResults;
    }
    
    public ScheduledFuture<?> getSchedule() {
        return this.schedule;
    }
    
    public String getType() {
        return this.type;
    }
}
