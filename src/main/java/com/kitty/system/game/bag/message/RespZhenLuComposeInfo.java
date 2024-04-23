package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FieldValue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 20680)
public class RespZhenLuComposeInfo extends Message {
  private int serial;

  private List<FieldValue> list = new ArrayList<>();

}
