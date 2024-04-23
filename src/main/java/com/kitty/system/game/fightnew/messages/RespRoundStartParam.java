package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.messages.vo.SkillCool;
import com.kitty.system.game.fightnew.model.AutoFightSkillParam;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 3586)
public class RespRoundStartParam extends Message {
  private int objId;
  
  private int round;
  
  private String startTime;
  
  private boolean isAutoFight;
  
  private List<AutoFightSkillParam> autoFightSkillParams;
  
  private List<SkillCool> skillCools;
  
  public void setObjId(int objId) {
    this.objId = objId;
  }
  
  public void setRound(int round) {
    this.round = round;
  }
  
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }
  
  public void setAutoFight(boolean isAutoFight) {
    this.isAutoFight = isAutoFight;
  }
  
  public void setAutoFightSkillParams(List<AutoFightSkillParam> autoFightSkillParams) {
    this.autoFightSkillParams = autoFightSkillParams;
  }
  
  public void setSkillCools(List<SkillCool> skillCools) {
    this.skillCools = skillCools;
  }
  
  @ConstructorProperties({"objId", "round", "startTime", "isAutoFight", "autoFightSkillParams", "skillCools"})
  public RespRoundStartParam(int objId, int round, String startTime, boolean isAutoFight, List<AutoFightSkillParam> autoFightSkillParams, List<SkillCool> skillCools) {
    this.isAutoFight = false;
    this.objId = objId;
    this.round = round;
    this.startTime = startTime;
    this.isAutoFight = isAutoFight;
    this.autoFightSkillParams = autoFightSkillParams;
    this.skillCools = skillCools;
  }
  
  public RespRoundStartParam() {
    this.isAutoFight = false;
  }
  
  public int getObjId() {
    return this.objId;
  }
  
  public int getRound() {
    return this.round;
  }
  
  public String getStartTime() {
    return this.startTime;
  }
  
  public boolean isAutoFight() {
    return this.isAutoFight;
  }
  
  public List<AutoFightSkillParam> getAutoFightSkillParams() {
    return this.autoFightSkillParams;
  }
  
  public List<SkillCool> getSkillCools() {
    return this.skillCools;
  }
  
  public void addSkillCools(List<SkillCool> cools) {
    if (this.skillCools == null)
      this.skillCools = new ArrayList<>(); 
    this.skillCools.addAll(cools);
  }
  
  public void addAutoFightSkillParam(AutoFightSkillParam autoFightSkillParam) {
    if (this.autoFightSkillParams == null)
      this.autoFightSkillParams = new ArrayList<>(); 
    this.autoFightSkillParams.add(autoFightSkillParam);
  }
}
