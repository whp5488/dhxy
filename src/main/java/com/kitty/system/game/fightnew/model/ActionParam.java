package com.kitty.system.game.fightnew.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ActionParam {
  private int fightRound;
  
  private List<Long> playerList;
  
  public void setFightRound(int fightRound) {
    this.fightRound = fightRound;
  }
  
  public void setPlayerList(List<Long> playerList) {
    this.playerList = playerList;
  }
  
  public void setActionDetailParams(List<ActionDetailParam> actionDetailParams) {
    this.actionDetailParams = actionDetailParams;
  }
  
  public int getFightRound() {
    return this.fightRound;
  }
  
  public List<Long> getPlayerList() {
    return this.playerList;
  }
  
  List<ActionDetailParam> actionDetailParams = Collections.synchronizedList(new LinkedList<>());
  
  public List<ActionDetailParam> getActionDetailParams() {
    return this.actionDetailParams;
  }
}
