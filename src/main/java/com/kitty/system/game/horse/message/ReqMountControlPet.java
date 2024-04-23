package com.kitty.system.game.horse.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 41044)
public class ReqMountControlPet extends Message {
  private String mountId;
  
  private int petId;

  @ConstructorProperties({"mountId", "petId"})
  public ReqMountControlPet(String mountId, int petId) {
    this.mountId = mountId;
    this.petId = petId;
  }
  
  public ReqMountControlPet() {}

}
