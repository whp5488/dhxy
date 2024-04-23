package com.kitty.record.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Arrays;
import org.nutz.dao.entity.annotation.Id;

public class FightRecordData implements Serializable {
  @Id
  private long id;
  
  private long createTime;
  
  private byte[] msgData;
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }
  
  public void setMsgData(byte[] msgData) {
    this.msgData = msgData;
  }
  
  @ConstructorProperties({"id", "createTime", "msgData"})
  public FightRecordData(long id, long createTime, byte[] msgData) {
    this.id = id;
    this.createTime = createTime;
    this.msgData = msgData;
  }
  
  public FightRecordData() {}
  
  public long getId() {
    return this.id;
  }
  
  public long getCreateTime() {
    return this.createTime;
  }
  
  public byte[] getMsgData() {
    return this.msgData;
  }
}
