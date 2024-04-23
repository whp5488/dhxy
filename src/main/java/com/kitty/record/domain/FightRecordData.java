package com.kitty.record.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Id;

@Setter
@Getter
public class FightRecordData implements Serializable {
  @Id
  private long id;
  
  private long createTime;
  
  private byte[] msgData;

  @ConstructorProperties({"id", "createTime", "msgData"})
  public FightRecordData(long id, long createTime, byte[] msgData) {
    this.id = id;
    this.createTime = createTime;
    this.msgData = msgData;
  }
  
  public FightRecordData() {}

}
