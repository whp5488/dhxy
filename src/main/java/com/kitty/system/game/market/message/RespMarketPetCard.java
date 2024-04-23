package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.market.message.vo.HorseSkillCard;
import com.kitty.system.game.market.message.vo.PetEquipCard;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;

@MessageMeta(module = 45105)
public class RespMarketPetCard extends Message {
  private String id;
  
  private int roleId;
  
  private int serial;
  
  private List<FiedValue> fields;
  
  private List<PetSkill> skills;
  
  private List<FieldProp> proList;
  
  private List<PetEquipCard> equips;
  
  private List<HorseSkillCard> horseSkillCard;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setFields(List<FiedValue> fields) {
    this.fields = fields;
  }
  
  public void setSkills(List<PetSkill> skills) {
    this.skills = skills;
  }
  
  public void setProList(List<FieldProp> proList) {
    this.proList = proList;
  }
  
  public void setEquips(List<PetEquipCard> equips) {
    this.equips = equips;
  }
  
  public void setHorseSkillCard(List<HorseSkillCard> horseSkillCard) {
    this.horseSkillCard = horseSkillCard;
  }
  
  public void setStatus(byte status) {
    this.status = status;
  }
  
  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }
  
  public String getId() {
    return this.id;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public List<FiedValue> getFields() {
    return this.fields;
  }
  
  public List<PetSkill> getSkills() {
    return this.skills;
  }
  
  public List<FieldProp> getProList() {
    return this.proList;
  }
  
  public List<PetEquipCard> getEquips() {
    return this.equips;
  }
  
  public List<HorseSkillCard> getHorseSkillCard() {
    return this.horseSkillCard;
  }
  
  private byte status = 2;
  
  private int endTime;
  
  public byte getStatus() {
    return this.status;
  }
  
  public int getEndTime() {
    return this.endTime;
  }
}
