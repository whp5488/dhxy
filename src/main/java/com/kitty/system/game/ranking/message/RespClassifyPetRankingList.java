package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.ranking.pojo.field.PetClassifyRankingField;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 42121)
public class RespClassifyPetRankingList extends Message {
  private int petSerial;
  
  private byte serverId;

  private List<PetClassifyRankingField> petClassifyRankingFields = new ArrayList<>();

  public RespClassifyPetRankingList(int petSerial, byte serverId, List<PetClassifyRankingField> petClassifyRankingFields) {
    this.petSerial = petSerial;
    this.serverId = serverId;
    this.petClassifyRankingFields = petClassifyRankingFields;
  }
}
