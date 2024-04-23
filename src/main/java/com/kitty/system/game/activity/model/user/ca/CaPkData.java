package com.kitty.system.game.activity.model.user.ca;

import com.kitty.system.game.activity.model.user.ca.vo.CaFight;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class CaPkData implements Serializable {
  private int teamId;
  
  private short pkValue;
  
  private short score;
  
  public void setTeamId(int teamId) {
    this.teamId = teamId;
  }
  
  public void setPkValue(short pkValue) {
    this.pkValue = pkValue;
  }
  
  public void setScore(short score) {
    this.score = score;
  }
  
  public void setMemberGrade(int memberGrade) {
    this.memberGrade = memberGrade;
  }
  
  public void setMemberEquipScore(int memberEquipScore) {
    this.memberEquipScore = memberEquipScore;
  }
  
  public void setMembers(List<Long> members) {
    this.members = members;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setGroup(int group) {
    this.group = group;
  }
  
  public void setFight(List<CaFight> fight) {
    this.fight = fight;
  }
  
  public void setSchedule(ScheduledFuture<?> schedule) {
    this.schedule = schedule;
  }
  
  public String toString() {
    return "CaPkData(teamId=" + getTeamId() + ", pkValue=" + getPkValue() + ", score=" + getScore() + ", memberGrade=" + getMemberGrade() + ", memberEquipScore=" + getMemberEquipScore() + ", members=" + getMembers() + ", time=" + getTime() + ", state=" + getState() + ", group=" + getGroup() + ", fight=" + getFight() + ", schedule=" + getSchedule() + ")";
  }
  
  public int getTeamId() {
    return this.teamId;
  }
  
  public short getPkValue() {
    return this.pkValue;
  }
  
  public short getScore() {
    return this.score;
  }
  
  private int memberGrade = 0;
  
  public int getMemberGrade() {
    return this.memberGrade;
  }
  
  private int memberEquipScore = 0;
  
  private List<Long> members;
  
  public int getMemberEquipScore() {
    return this.memberEquipScore;
  }
  
  public List<Long> getMembers() {
    return this.members;
  }
  
  private int time = 0;
  
  public int getTime() {
    return this.time;
  }
  
  private byte state = 0;
  
  private int group;
  
  public byte getState() {
    return this.state;
  }
  
  public int getGroup() {
    return this.group;
  }
  
  private List<CaFight> fight = new ArrayList<>(3);
  
  private ScheduledFuture<?> schedule;
  
  public List<CaFight> getFight() {
    return this.fight;
  }
  
  public ScheduledFuture<?> getSchedule() {
    return this.schedule;
  }
  
  public CaPkData(Team team, short pkValue) {
    this.teamId = team.getId();
    this.pkValue = pkValue;
    this.members = getMembers(team);
  }
  
  private List<Long> getMembers(Team team) {
    List<Long> members = new ArrayList<>();
    List<Member> list = team.getList();
    for (int i = 0; i < list.size(); i++) {
      Member member = list.get(i);
      if (member.isInTeam() && 
        !members.contains(member))
        members.add(member.getId()); 
    } 
    return members;
  }
  
  public void subPkValue(short pkValue) {
    this.pkValue = (short)(this.pkValue - pkValue);
  }
  
  public void updateTeamMember(Team team) {
    this.members = getMembers(team);
  }
  
  public void updateTeamMember(byte state) {
    this.state = state;
  }
  
  public CaPkData(int teamId, short pkValue, int memberGrade, int memberEquipScore) {
    this.teamId = teamId;
    this.pkValue = pkValue;
    this.memberGrade = memberGrade;
    this.memberEquipScore = memberEquipScore;
  }
  
  public CaPkData(short group, int teamId) {
    this.group = group;
    this.teamId = teamId;
  }
  
  public CaPkData() {}
}
