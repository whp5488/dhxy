package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.ranking.pojo.field.PractitionersField;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 42125)
public class RespPractitionersRankingList extends Message {
  List<PractitionersField> practitionersFields = new ArrayList<>();

  public RespPractitionersRankingList(List<PractitionersField> practitionersFields) {
    this.practitionersFields = practitionersFields;
  }
}
