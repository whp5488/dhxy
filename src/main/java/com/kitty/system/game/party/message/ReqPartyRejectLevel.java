package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 32784)
public class ReqPartyRejectLevel extends Message {

  private byte consent = 0;

  private short consentLevel = 0;

  private byte refuse = 1;

  private short refuseLevel = 0;

}
