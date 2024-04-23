package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.ranking.pojo.field.QlkRankingField;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 42127)
public class RespQlkRankingList extends Message {
  private short qlkType;
  
  List<QlkRankingField> qlkRankingFields = new ArrayList<>();

  public RespQlkRankingList(short qlkType, List<QlkRankingField> qlkRankingFields) {
    this.qlkType = qlkType;
    this.qlkRankingFields = qlkRankingFields;
  }
}
