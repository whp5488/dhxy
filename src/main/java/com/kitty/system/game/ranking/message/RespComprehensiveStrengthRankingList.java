package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.ranking.pojo.field.ComprehensiveStrengthRankingField;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 42124)
public class RespComprehensiveStrengthRankingList extends Message {
  private byte race;
  
  private byte serverId;
  
  List<ComprehensiveStrengthRankingField> ComprehensiveStrengthList = new ArrayList<>();

  public RespComprehensiveStrengthRankingList(byte race, byte serverId, List<ComprehensiveStrengthRankingField> comprehensiveStrengthList) {
    this.race = race;
    this.serverId = serverId;
    this.ComprehensiveStrengthList = comprehensiveStrengthList;
  }
}
