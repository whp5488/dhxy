package com.kitty.system.game.activity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FightInfo implements Serializable {
  public void setItselfLeader(long itselfLeader) {
    this.itselfLeader = itselfLeader;
  }
  
  public void setItself(List<Long> itself) {
    this.itself = itself;
  }
  
  public void setEnemyLeader(long enemyLeader) {
    this.enemyLeader = enemyLeader;
  }
  
  public void setEnemy(List<Long> enemy) {
    this.enemy = enemy;
  }
  
  private long itselfLeader = 0L;
  
  public long getItselfLeader() {
    return this.itselfLeader;
  }
  
  private List<Long> itself = new ArrayList<>(5);
  
  public List<Long> getItself() {
    return this.itself;
  }
  
  private long enemyLeader = 0L;
  
  public long getEnemyLeader() {
    return this.enemyLeader;
  }
  
  private List<Long> enemy = new ArrayList<>(5);
  
  public List<Long> getEnemy() {
    return this.enemy;
  }
}
