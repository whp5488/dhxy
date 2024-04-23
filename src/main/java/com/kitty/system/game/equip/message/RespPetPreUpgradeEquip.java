package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.equip.helper.vo.PetEquipNeo;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.util.List;

@Getter
@MessageMeta(module = 32777)
public class RespPetPreUpgradeEquip extends Message {
  private List<PetEquipNeo> list;
  
  private byte type;
  
  private byte ask;
  
  private byte grade;
  
  public void setList(List<PetEquipNeo> list) {
    this.list = list;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setAsk(byte ask) {
    this.ask = ask;
  }
  
  public void setGrade(byte grade) {
    this.grade = grade;
  }
  
  public RespPetPreUpgradeEquip() {}
  
  @ConstructorProperties({"list", "type", "ask", "grade"})
  public RespPetPreUpgradeEquip(List<PetEquipNeo> list, byte type, byte ask, byte grade) {
    this.list = list;
    this.type = type;
    this.ask = ask;
    this.grade = grade;
  }

}
