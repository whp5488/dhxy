package com.kitty.system.game.task.message.vo;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.vo.EasyNpc;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TaskInfo {
  private int serial;

  private short counter = 0;

  private int taskUpdateTime = 0;

  private int taskEndTime = 0;

  private byte quit = 0;

  private byte refresh = 0;
  
  private List<EasyNpc> npcList;
  
  private List<FiedValue> list;

}
