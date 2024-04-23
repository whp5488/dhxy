package com.kitty.system.game.team.domain;

import com.kitty.system.common.utils.NutConcurrentMap;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Team {
  private int id;
  
  private String name;
  
  private Date createTime;
  
  private List<Member> list;
  
  private ArrayList<Member> partner;
  
  private int lastTeamId;
  
  private String target;
  
  private String msg;

  private NutConcurrentMap fightLeaveNutMap = new NutConcurrentMap();
  
  private int[] raceLimit;
  
  private int sexLimit;

  private int minLevel = 0;

  private int minRelive = 0;

  private int maxLevel = 0;

  private int maxRelive = 0;

  public boolean isLeader(int roleId) {
    if (this.list == null || this.list.isEmpty())
      return false; 
    if (this.list.get(0) == null)
      return false; 
    return ((Member)this.list.get(0)).getRoleId().equals(Integer.valueOf(roleId));
  }
  
  public int getLeaderRoleId() {
    if (this.list == null || this.list.isEmpty())
      return 0; 
    if (this.list.get(0) == null)
      return 0; 
    return ((Member)this.list.get(0)).getRoleId().intValue();
  }
  
  public long getLeaderUid() {
    if (this.list == null || this.list.isEmpty())
      return 0L; 
    if (this.list.get(0) == null)
      return 0L; 
    return ((Member)this.list.get(0)).getId().longValue();
  }
  
  public Member getMember(int roleId) {
    for (int i = 0; i < this.list.size(); i++) {
      Member member = this.list.get(i);
      if (member != null && member.getRoleId().equals(Integer.valueOf(roleId)))
        return member; 
    } 
    return null;
  }
  
  public String getMemberNames() {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.list != null)
      for (int i = 0; i < this.list.size(); i++) {
        Member member = this.list.get(i);
        if (member != null && 
          member.isInTeam()) {
          if (stringBuilder.length() > 0)
            stringBuilder.append("、"); 
          stringBuilder.append(member.getName());
        } 
      }  
    return stringBuilder.toString();
  }
}
