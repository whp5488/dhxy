package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 11803)
public class ReqSavePartyFlag extends Message {
  private short flag;
  
  private short flagColor;
  
  private short lace;
  
  private short emblem;
  
  private short emblemColor;
  
  private String text;

}
