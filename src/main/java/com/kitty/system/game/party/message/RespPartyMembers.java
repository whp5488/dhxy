package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.party.model.vo.PartyMemberObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 61603)
public class RespPartyMembers extends Message {

  private byte page = 0;

  private short tail = 0;
  
  private List<PartyMemberObject> list;

}
