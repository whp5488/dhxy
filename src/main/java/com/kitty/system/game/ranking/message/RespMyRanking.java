package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.ranking.pojo.RankingField;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 42122)
public class RespMyRanking extends Message {
  private byte type;
  
  private int serial;
  
  private byte serverId;
  
  private RankingField rankingFields;

  public RespMyRanking(byte type, int serial, byte serverId, RankingField rankingFields) {
    this.type = type;
    this.serial = serial;
    this.serverId = serverId;
    this.rankingFields = rankingFields;
  }
}
