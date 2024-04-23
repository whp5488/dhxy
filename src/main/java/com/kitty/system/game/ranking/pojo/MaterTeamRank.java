package com.kitty.system.game.ranking.pojo;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MaterTeamRank implements Serializable {
  public void setList(List<MaterRank> list) {
    this.list = list;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setRestsScore(int restsScore) {
    this.restsScore = restsScore;
  }
  
  @ConstructorProperties({"list", "score", "restsScore"})
  public MaterTeamRank(List<MaterRank> list, int score, int restsScore) {
    this.list = list;
    this.score = score;
    this.restsScore = restsScore;
  }
  
  private List<MaterRank> list = new ArrayList<>();
  
  private int score;
  
  private int restsScore;
  
  public List<MaterRank> getList() {
    return this.list;
  }
  
  public int getScore() {
    return this.score;
  }
  
  public int getRestsScore() {
    return this.restsScore;
  }
  
  public MaterTeamRank() {}
}
