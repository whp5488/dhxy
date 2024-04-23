package com.kitty.system.admin.domain;

import com.kitty.system.game.email.domain.EmailSystemAccessory;
import java.io.Serializable;
import java.util.List;

public class BriefEmail implements Serializable {
  private int rec;
  
  private String title;
  
  private String con;
  
  private Long time;
  
  private int sen;
  
  private List<EmailSystemAccessory> list;
  
  public void setRec(int rec) {
    this.rec = rec;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public void setCon(String con) {
    this.con = con;
  }
  
  public void setTime(Long time) {
    this.time = time;
  }
  
  public void setSen(int sen) {
    this.sen = sen;
  }
  
  public void setList(List<EmailSystemAccessory> list) {
    this.list = list;
  }
  
  public int getRec() {
    return this.rec;
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public String getCon() {
    return this.con;
  }
  
  public Long getTime() {
    return this.time;
  }
  
  public int getSen() {
    return this.sen;
  }
  
  public List<EmailSystemAccessory> getList() {
    return this.list;
  }
}
