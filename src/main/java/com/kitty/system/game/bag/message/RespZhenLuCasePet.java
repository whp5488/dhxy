package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.PetBase;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 20681)
public class RespZhenLuCasePet extends Message {
  private String id;
  
  private int serial;
  
  private int grow;
  
  private int grade;
  
  private int hp;
  
  private int mp;
  
  private int ap;
  
  private int sp;

  public RespZhenLuCasePet() {}
  
  public RespZhenLuCasePet(Pet pet) {
    PetBase petBase = pet.getPetBase();
    this.id = String.valueOf(pet.getPetId());
    this.serial = pet.getPetType().intValue();
    this.grow = petBase.getGrow_up().intValue();
    this.grade = petBase.getGrade_base().intValue();
    this.hp = petBase.getHp_base().intValue();
    this.mp = petBase.getMp_base().intValue();
    this.ap = petBase.getAp_base().intValue();
    this.sp = petBase.getSp_base().intValue();
  }
  
  public RespZhenLuCasePet(String id, int serial, int grow, int grade, int hp, int mp, int ap, int sp) {
    this.id = id;
    this.serial = serial;
    this.grow = grow;
    this.grade = grade;
    this.hp = hp;
    this.mp = mp;
    this.ap = ap;
    this.sp = sp;
  }
}
