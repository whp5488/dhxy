package com.kitty.system.game.fightRecord.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightRecord.pojo.MiniFightRecord;
import java.util.List;

@MessageMeta(module = 4340)
public class RespFightRecords extends Message {
  private String objId;
  
  private List<MiniFightRecord> records;
  
  public void setObjId(String objId) {
    this.objId = objId;
  }
  
  public void setRecords(List<MiniFightRecord> records) {
    this.records = records;
  }
  
  public String getObjId() {
    return this.objId;
  }
  
  public List<MiniFightRecord> getRecords() {
    return this.records;
  }
  
  public RespFightRecords(String objId, List<MiniFightRecord> records) {
    this.objId = objId;
    this.records = records;
  }
}
