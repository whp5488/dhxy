package com.kitty.system.game.partner.enter;

import java.beans.ConstructorProperties;

public class PartnerFiedValue {
  private int uid;
  
  private int typeId;
  
  private byte relive;
  
  private short level;
  
  private String exp;
  
  private byte isRecruit;
  
  private byte isFight;
  
  private byte partnerOrder;
  
  private int dsId;
  
  public void setUid(int uid) {
    this.uid = uid;
  }
  
  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }
  
  public void setRelive(byte relive) {
    this.relive = relive;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setExp(String exp) {
    this.exp = exp;
  }
  
  public void setIsRecruit(byte isRecruit) {
    this.isRecruit = isRecruit;
  }
  
  public void setIsFight(byte isFight) {
    this.isFight = isFight;
  }
  
  public void setPartnerOrder(byte partnerOrder) {
    this.partnerOrder = partnerOrder;
  }
  
  public void setDsId(int dsId) {
    this.dsId = dsId;
  }
  
  public PartnerFiedValue() {}
  
  @ConstructorProperties({"uid", "typeId", "relive", "level", "exp", "isRecruit", "isFight", "partnerOrder", "dsId"})
  public PartnerFiedValue(int uid, int typeId, byte relive, short level, String exp, byte isRecruit, byte isFight, byte partnerOrder, int dsId) {
    this.uid = uid;
    this.typeId = typeId;
    this.relive = relive;
    this.level = level;
    this.exp = exp;
    this.isRecruit = isRecruit;
    this.isFight = isFight;
    this.partnerOrder = partnerOrder;
    this.dsId = dsId;
  }
  
  public int getUid() {
    return this.uid;
  }
  
  public int getTypeId() {
    return this.typeId;
  }
  
  public byte getRelive() {
    return this.relive;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public String getExp() {
    return this.exp;
  }
  
  public byte getIsRecruit() {
    return this.isRecruit;
  }
  
  public byte getIsFight() {
    return this.isFight;
  }
  
  public byte getPartnerOrder() {
    return this.partnerOrder;
  }
  
  public int getDsId() {
    return this.dsId;
  }
}
