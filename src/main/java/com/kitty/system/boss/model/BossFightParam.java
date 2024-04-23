package com.kitty.system.boss.model;

import com.kitty.system.init.domain.SysDataMonster;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BossFightParam {
  private List<SysDataMonster> bossParamList;
  
  private int fightType;
  
  private int taskId;
  
  private int npcId;
  
  private short npcLevel;
  
  private List<String> shoutingList;

  private boolean loadGuard = true;
  
  private byte specialMonsterType;

  public BossFightParam(List<SysDataMonster> bossParamList, int fightType) {
    this.bossParamList = bossParamList;
    this.fightType = fightType;
  }
  
  public BossFightParam(List<SysDataMonster> bossParamList, int fightType, int taskId, int npcId) {
    this.bossParamList = bossParamList;
    this.fightType = fightType;
    this.taskId = taskId;
    this.npcId = npcId;
  }

}
