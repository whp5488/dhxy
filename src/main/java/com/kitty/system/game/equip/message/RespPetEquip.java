package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 32778)
public class RespPetEquip extends Message {

  @ConstructorProperties({"list"})
  public RespPetEquip(List<String> list) {
    this.list = list;
  }
  
  private List<String> list = new ArrayList<>();

  public RespPetEquip() {}
}
