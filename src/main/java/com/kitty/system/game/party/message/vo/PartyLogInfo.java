package com.kitty.system.game.party.message.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class PartyLogInfo implements Serializable {
  private int time;
  private List<LogInfo> list;
}
