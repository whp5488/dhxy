package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.ranking.pojo.field.PetPropertyRankingField;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 42123)
public class RespPropertyPetRankingList extends Message {
  private byte type;
  
  private byte serverId;

  private List<PetPropertyRankingField> petPropertyRankingFields = new ArrayList<>();

  public RespPropertyPetRankingList(byte type, byte serverId, List<PetPropertyRankingField> petPropertyRankingFields) {
    this.type = type;
    this.serverId = serverId;
    this.petPropertyRankingFields = petPropertyRankingFields;
  }
}
