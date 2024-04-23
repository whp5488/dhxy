package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.task.message.vo.BallotInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 45127)
public class RespBallotResult extends Message {
  private short meTime = -1;
  
  private List<BallotInfo> resultList;

}
