package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.welfare.message.vo.MagicBox;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 45387)
public class RespMagicBoxInfo extends Message {
  private int affiliation;

  private byte type = -1;

  private int serial = 0;

  private List<MagicBox> left = new ArrayList<>();

  private List<MagicBox> right = new ArrayList<>();

}
