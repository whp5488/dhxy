package com.kitty.system.game.vo.init;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Default;

@Setter
@Getter
public class Summon implements Serializable {
  private Integer serial;
  
  private Integer num;
  
  private Integer rate;
  
  @Default("[]")
  private List<Integer> monster;
  
  public Summon() {}
  
  @ConstructorProperties({"serial", "num", "rate", "monster"})
  public Summon(Integer serial, Integer num, Integer rate, List<Integer> monster) {
    this.serial = serial;
    this.num = num;
    this.rate = rate;
    this.monster = monster;
  }

}
