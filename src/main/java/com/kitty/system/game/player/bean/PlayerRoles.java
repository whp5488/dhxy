package com.kitty.system.game.player.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class PlayerRoles implements Serializable {
  private Set<Long> list = new HashSet<>();

}
