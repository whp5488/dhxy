package com.kitty.system.game.fightRecord.pojo;

import com.kitty.record.domain.FightRecord;
import java.io.Serializable;

public class MiniFightRecord implements Serializable {
  private String recordId;
  
  private String createTime;
  
  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }
  
  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }
  
  public String getRecordId() {
    return this.recordId;
  }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public MiniFightRecord(FightRecord record) {
    this.recordId = String.valueOf(record.getId());
    this.createTime = String.valueOf(record.getCreateTime());
  }
}
