package com.kitty.system.game.bag.domain;

import com.kitty.system.game.bag.domain.vo.Temporary;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Overflow implements Serializable {

  private List<Temporary> list = new ArrayList<>();
  private boolean flag = false;

}
