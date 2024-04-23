package com.kitty.system.game.party.model;

import com.kitty.common.thread.IdGenerator;
import java.util.Map;

public class PartyRedBag {
  private long id;
  
  private short silver;
  
  private short count;
  
  private int remainSilver;
  
  private short remainCount;
  
  private int time;
  
  private long senderUid;
  
  private String senderName;
  
  private String msg;
  
  private Map<Long, FetchRedBagRecord> fetchRedBagRecordMap;
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setSilver(short silver) {
    this.silver = silver;
  }
  
  public void setCount(short count) {
    this.count = count;
  }
  
  public void setRemainSilver(int remainSilver) {
    this.remainSilver = remainSilver;
  }
  
  public void setRemainCount(short remainCount) {
    this.remainCount = remainCount;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setSenderUid(long senderUid) {
    this.senderUid = senderUid;
  }
  
  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public void setFetchRedBagRecordMap(Map<Long, FetchRedBagRecord> fetchRedBagRecordMap) {
    this.fetchRedBagRecordMap = fetchRedBagRecordMap;
  }
  
  public long getId() {
    return this.id;
  }
  
  public short getSilver() {
    return this.silver;
  }
  
  public short getCount() {
    return this.count;
  }
  
  public int getRemainSilver() {
    return this.remainSilver;
  }
  
  public short getRemainCount() {
    return this.remainCount;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public long getSenderUid() {
    return this.senderUid;
  }
  
  public String getSenderName() {
    return this.senderName;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  public Map<Long, FetchRedBagRecord> getFetchRedBagRecordMap() {
    return this.fetchRedBagRecordMap;
  }
  
  public PartyRedBag() {}
  
  public PartyRedBag(long senderUid, String senderName, short silver, short count, String msg) {
    this.id = IdGenerator.getNextId();
    this.silver = silver;
    this.count = count;
    this.remainSilver = silver;
    this.remainCount = count;
    this.time = (int)(System.currentTimeMillis() / 1000L);
    this.senderUid = senderUid;
    this.senderName = senderName;
    this.msg = msg;
  }
}
