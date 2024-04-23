package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.ranking.pojo.field.WeaponsRankingField;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 42126)
public class RespWeaponsRankingList extends Message {
  private byte race;
  
  private byte equipType;

  private List<WeaponsRankingField> weaponsRankingFields = new ArrayList<>(100);

  public RespWeaponsRankingList(byte race, byte equipType, List<WeaponsRankingField> weaponsRankingFields) {
    this.race = race;
    this.equipType = equipType;
    this.weaponsRankingFields = weaponsRankingFields;
  }
}
