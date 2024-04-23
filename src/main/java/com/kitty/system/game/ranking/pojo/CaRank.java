package com.kitty.system.game.ranking.pojo;

import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.system.game.activity.model.user.ca.vo.CaCache;
import java.beans.ConstructorProperties;
import java.io.Serializable;

public class CaRank implements Serializable {
  private int roleId;
  
  private String name;
  
  private int serial;
  
  private byte relive;
  
  private short level;
  
  private int score;
  
  private int rank;
  
  private String server;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setRelive(byte relive) {
    this.relive = relive;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setRank(int rank) {
    this.rank = rank;
  }
  
  public void setServer(String server) {
    this.server = server;
  }
  
  @ConstructorProperties({"roleId", "name", "serial", "relive", "level", "score", "rank", "server"})
  public CaRank(int roleId, String name, int serial, byte relive, short level, int score, int rank, String server) {
    this.roleId = roleId;
    this.name = name;
    this.serial = serial;
    this.relive = relive;
    this.level = level;
    this.score = score;
    this.rank = rank;
    this.server = server;
  }
  
  public CaRank() {}
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public byte getRelive() {
    return this.relive;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public int getScore() {
    return this.score;
  }
  
  public int getRank() {
    return this.rank;
  }
  
  public String getServer() {
    return this.server;
  }
  
  public CaRank(CaRankRecord record) {
    this.roleId = record.getRoleId();
    this.name = record.getName();
    this.serial = record.getSerial();
    this.relive = (byte)(record.getGrade() / 1000);
    this.level = (short)(record.getGrade() % 1000);
    this.score = record.getScore();
    this.rank = record.getFullRank();
  }
  
  public CaRank(CaCache caCache, String server) {
    this.roleId = caCache.getRoleId();
    this.name = caCache.getName();
    this.serial = caCache.getSerial();
    this.relive = (byte)(caCache.getGrade() / 1000);
    this.level = (short)(caCache.getGrade() % 1000);
    this.score = caCache.getScore();
    this.server = server;
  }
}
