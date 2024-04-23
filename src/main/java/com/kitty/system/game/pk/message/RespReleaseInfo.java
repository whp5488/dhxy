package com.kitty.system.game.pk.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 45232)
public class RespReleaseInfo extends Message {
  private int roleId;
  
  private int serial;
  
  private String name;

  private byte relive = 0;

  private short level = 0;

  private int weapon = 0;

  private int gem = 0;

  private List<Integer> lightEffect = new ArrayList<>();

  private List<Integer> specialIcon = new ArrayList<>();
  
  private List<FiedValue> list;
  
  private String bail;

}
