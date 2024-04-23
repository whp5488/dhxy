package com.kitty.system.common.data;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.vo.Approximate;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import java.util.ArrayList;
import java.util.List;

public class ActivityGlobalFunc {
  public static Team getApproximate(Team team, ArrayList<Team> arrayList) {
    if (team == null || arrayList.isEmpty())
      return null; 
    List<Approximate> objects = new ArrayList<>(arrayList.size());
    for (Team t : arrayList) {
      int score = getTeamSunEquipScore(t);
      objects.add(new Approximate(t.getId(), score));
    } 
    Approximate approximate = getApproximate(getTeamSunEquipScore(team), objects);
    return arrayList.stream().filter(e -> (e.getId() == approximate.getId())).findFirst().orElse(null);
  }
  
  public static int getTeamSunEquipScore(Team team) {
    int score = 0;
    if (team != null) {
      List<Member> list = team.getList();
      for (int i = 0; i < list.size(); i++) {
        Member member = list.get(i);
        Role role = SpringUtils.getRoleService().getPlayer(member.getRoleId());
        if (role != null)
          score += DataGlobalFunc.getEquipScore(SpringUtils.getEquipService().selectWearEquip(role)); 
      } 
    } 
    return score;
  }
  
  public static Approximate getApproximate(int x, List<Approximate> src) {
    if (src == null)
      return null; 
    if (src.size() == 1)
      return src.get(0); 
    int minDifference = Math.abs(src.get(0).getNum() - x);
    int minIndex = 0;
    for (int i = 1; i < src.size(); i++) {
      int temp = Math.abs(src.get(i).getNum() - x);
      if (temp < minDifference) {
        minIndex = i;
        minDifference = temp;
      } 
    } 
    return src.get(minIndex);
  }
  
  public static void main(String[] args) {
    List<Approximate> list = new ArrayList<>();
    list.add(new Approximate(1, 10));
    list.add(new Approximate(2, 16));
    list.add(new Approximate(3, 8));
    list.add(new Approximate(4, 20));
    list.add(new Approximate(5, 6));
    Approximate approximate = getApproximate(7, list);
    System.out.println("approximate = " + approximate.getNum());
  }
}
