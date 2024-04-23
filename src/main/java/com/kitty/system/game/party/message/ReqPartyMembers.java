package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 8376)
public class ReqPartyMembers extends Message {

  private byte page = 1;

  private byte size = 20;

  private byte sort = 4;

  private byte way = 0;

  private String para = "";

  private String list = "";

}
