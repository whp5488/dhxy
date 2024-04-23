package com.kitty.system.game.fightnew.imbue.bean;

import java.util.ArrayList;
import java.util.List;

public class Imbue {
  public void setStatus(List<ImbueParam> status) {
    this.status = status;
  }
  
  public void setSkills(List<ImbueParam> skills) {
    this.skills = skills;
  }
  
  public void setRounds(List<ImbueParam> rounds) {
    this.rounds = rounds;
  }
  
  public void setTemp(double temp) {
    this.temp = temp;
  }
  
  private List<ImbueParam> status = new ArrayList<>();
  
  public List<ImbueParam> getStatus() {
    return this.status;
  }
  
  private List<ImbueParam> skills = new ArrayList<>();
  
  public List<ImbueParam> getSkills() {
    return this.skills;
  }
  
  private List<ImbueParam> rounds = new ArrayList<>();
  
  public List<ImbueParam> getRounds() {
    return this.rounds;
  }
  
  private double temp = 0.0D;
  
  public double getTemp() {
    return this.temp;
  }
}
