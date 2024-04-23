package com.kitty.system.game.activity.model.user.at;

import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AtWillPkData implements Serializable {
  private int teamId;
  
  private short pkValue;
  
  public void setTeamId(int teamId) {
    this.teamId = teamId;
  }
  
  public void setPkValue(short pkValue) {
    this.pkValue = pkValue;
  }
  
  public void setScore(short score) {
    this.score = score;
  }
  
  public void setMembers(List<Member> members) {
    this.members = members;
  }
  
  public void setPrevPKTime(long prevPKTime) {
    this.prevPKTime = prevPKTime;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setLeave(long leave) {
    this.leave = leave;
  }
  
  public String toString() {
    return "AtWillPkData(teamId=" + getTeamId() + ", pkValue=" + getPkValue() + ", score=" + getScore() + ", members=" + getMembers() + ", prevPKTime=" + getPrevPKTime() + ", state=" + getState() + ", leave=" + getLeave() + ")";
  }
  
  public int getTeamId() {
    return this.teamId;
  }
  
  public short getPkValue() {
    return this.pkValue;
  }
  
  private short score = 0;
  
  private List<Member> members;
  
  private long prevPKTime;
  
  public short getScore() {
    return this.score;
  }
  
  public List<Member> getMembers() {
    return this.members;
  }
  
  public long getPrevPKTime() {
    return this.prevPKTime;
  }
  
  private byte state = 0;
  
  public byte getState() {
    return this.state;
  }
  
  private long leave = 0L;
  
  public long getLeave() {
    return this.leave;
  }
  
  public AtWillPkData(Team team, short pkValue) {
    this.teamId = team.getId();
    this.pkValue = pkValue;
    this.score = 0;
    List<Member> members = getMembers(team);
    this.members = members;
  }
  
  private List<Member> getMembers(Team team) {
    List<Member> members = new ArrayList<>();
    List<Member> list = team.getList();
    for (int i = 0; i < list.size(); i++) {
      Member member = list.get(i);
      if (member.isInTeam())
        if (!members.contains(member))
          members.add(member);  
    } 
    return members;
  }
  
  public void addTotalScore(short score) {
    this.score = (short)(this.score + score);
  }
  
  public void subTotalScore() {
    this.score = (short)(this.score - 2);
    if (this.score <= 0)
      this.score = 0; 
  }
  
  public void substractPkValue(short pkValue) {
    this.pkValue = (short)(this.pkValue - pkValue);
  }
  
  public void updateTeamMember(Team team, byte state) {
    List<Member> members = getMembers(team);
    this.members = members;
    this.state = state;
  }
  
  public void updateTeamMember(Team team) {
    List<Member> members = getMembers(team);
    this.members = members;
  }
  
  public AtWillPkData() {}
}
