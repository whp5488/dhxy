package com.kitty.system.init.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Move implements Serializable {

  private List<String> list = new ArrayList<>();

}
