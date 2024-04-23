package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 8254)
public class ReqAssignAttrib extends Message {
  private int id;

  private int gg = 0;

  private int lx = 0;

  private int ll = 0;

  private int mj = 0;

}
