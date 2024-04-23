package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.ranking.pojo.CaRank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 42130)
public class RespCaNomRanking extends Message {
  
  private byte type = 0;

  private byte classify = 0;

  public List<CaRank> list = new ArrayList<>(100);

}
