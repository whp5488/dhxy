package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.task.message.vo.ActivityData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 36883)
public class RespLivenessInfo extends Message {
  private List<ActivityData> list;
  
  private byte rewardStatus = 0;
  
  public RespLivenessInfo() {}
  
  public RespLivenessInfo(List<ActivityData> list, byte rewardStatus) {
    this.list = list;
    this.rewardStatus = rewardStatus;
  }

}
