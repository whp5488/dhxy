package com.kitty.system.game.activity.model.user.ca.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class CaCache implements Serializable {
  private Long id;
  
  private int roleId;
  
  private int serial;
  
  private String name;
  
  private int score;
  
  private int grade;
  
  private String factionName;
  
  private String marking;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setGrade(int grade) {
    this.grade = grade;
  }
  
  public void setFactionName(String factionName) {
    this.factionName = factionName;
  }
  
  public void setMarking(String marking) {
    this.marking = marking;
  }
  
  public CaCache() {}
  
  @ConstructorProperties({"id", "roleId", "serial", "name", "score", "grade", "factionName", "marking"})
  public CaCache(Long id, int roleId, int serial, String name, int score, int grade, String factionName, String marking) {
    this.id = id;
    this.roleId = roleId;
    this.serial = serial;
    this.name = name;
    this.score = score;
    this.grade = grade;
    this.factionName = factionName;
    this.marking = marking;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getScore() {
    return this.score;
  }
  
  public int getGrade() {
    return this.grade;
  }
  
  public String getFactionName() {
    return this.factionName;
  }
  
  public String getMarking() {
    return this.marking;
  }
}
