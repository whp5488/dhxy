package com.kitty.system.game.role.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class RoleOfflineNotice implements Serializable {

  private int state = 0;

  private int aloneOfflineTime = 1200000;

  private int teamOfflineTime = 2400000;

}
