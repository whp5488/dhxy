package com.kitty.system.game.player.bean;

import com.kitty.system.game.role.domain.vo.PartyPractice;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class PlayerRest implements Serializable {

  private PartyPractice partyPractice = new PartyPractice();

  private Map<Integer, Integer> correction = new HashMap<>();

  private Map<Integer, Integer> party = new HashMap<>();

}
