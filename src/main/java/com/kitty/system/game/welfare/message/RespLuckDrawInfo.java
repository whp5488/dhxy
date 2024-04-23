package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.welfare.message.vo.LuckDrawInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 45381)
public class RespLuckDrawInfo extends Message {
  private List<LuckDrawInfo> list;
  
  private byte classify;

}
