package com.kitty.system.game.bag.message.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class TemporaryVo implements Serializable {
  private String id;
  
  private Integer serial;
  
  private Integer num;
}
