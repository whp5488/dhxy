package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 61661)
public class RespUpdateAppearance extends Message {
  private int roleId;
  
  private int serial;
  
  private String roleName;
  
  private int level;
  
  private int relive;

  private int weapon = 0;

  private int gem = 0;
  
  private int title;

  private int leaderId = 0;

  private String partyName = "";

  private byte job = 0;

  private int mount_icon = 0;

  private int vip = 0;

  private int moveSpeed = 0;

  private int portrait = 0;

  private List<Integer> light_effect = new ArrayList<>();

  private List<Integer> special_icon = new ArrayList<>();

  private int fly_type = 0;
  
  private List<FiedValue> list;

}
