package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 32775)
public class RespPreUpgradeEquip extends Message {
  private byte type;
  
  private String id;
  
  private List<EquipNeo> list;
  
  private byte ask;

  public RespPreUpgradeEquip() {}
  
  @ConstructorProperties({"type", "id", "list", "ask"})
  public RespPreUpgradeEquip(byte type, String id, List<EquipNeo> list, byte ask) {
    this.type = type;
    this.id = id;
    this.list = list;
    this.ask = ask;
  }

}
