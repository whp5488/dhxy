package com.kitty.system.init.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class PetAccColor implements Serializable {
  
  private int silver = 0;

  private Map<Integer, Integer> mat = new HashMap<>();

  private Map<Integer, Integer> switchMat = new HashMap<>();

}
