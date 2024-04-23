package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 16383)
public class RespChat extends Message {
  private int type;

  private int roleId = 0;

  private String roleName = "";
  
  private String msg;

  private int time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
  
  private String token;

  private List<FiedValue> list = new ArrayList<>();

  private int friendId = 0;
  
  private String partyName;
  
  private String para;

}
