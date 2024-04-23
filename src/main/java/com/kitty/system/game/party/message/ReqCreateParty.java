package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 32780)
public class ReqCreateParty extends Message {
  private String partyName;
  
  private String announce;
  
  private byte consent;

}
