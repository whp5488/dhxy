package com.kitty.system.game.activity.model;

import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnore;

public class Dugeon {
  private long id;
  
  private long createTime;
  
  private long endTime;
  
  private List<Long> memberList;
  
  public Dugeon() {}
  
  public Dugeon(long id, List<Long> memberList) {
    this.id = id;
    this.createTime = System.currentTimeMillis();
    this.endTime = this.createTime + 7200000L;
    this.memberList = memberList;
  }
  
  @JsonIgnore
  public boolean isCreator(long uid) {
    return (((Long)this.memberList.get(0)).longValue() == uid);
  }
  
  @JsonIgnore
  public boolean isOverTime() {
    return (System.currentTimeMillis() >= this.endTime);
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }
  
  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }
  
  public void setMemberList(List<Long> memberList) {
    this.memberList = memberList;
  }
  
  public long getId() {
    return this.id;
  }
  
  public long getCreateTime() {
    return this.createTime;
  }
  
  public long getEndTime() {
    return this.endTime;
  }
  
  public List<Long> getMemberList() {
    return this.memberList;
  }
}
