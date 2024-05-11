
package com.kitty.system.game.fightnew.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ActionParam
{
    private int fightRound;
    private List<Long> playerList;
    List<ActionDetailParam> actionDetailParams;
    
    public ActionParam() {
        this.actionDetailParams = Collections.synchronizedList(new LinkedList<ActionDetailParam>());
    }
    
    public int getFightRound() {
        return this.fightRound;
    }
    
    public List<Long> getPlayerList() {
        return this.playerList;
    }
    
    public List<ActionDetailParam> getActionDetailParams() {
        return this.actionDetailParams;
    }
    
    public void setFightRound(final int fightRound) {
        this.fightRound = fightRound;
    }
    
    public void setPlayerList(final List<Long> playerList) {
        this.playerList = playerList;
    }
    
    public void setActionDetailParams(final List<ActionDetailParam> actionDetailParams) {
        this.actionDetailParams = actionDetailParams;
    }
}
