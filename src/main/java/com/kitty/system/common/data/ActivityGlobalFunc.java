
package com.kitty.system.common.data;

import com.kitty.system.game.role.domain.Role;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.team.domain.Member;
import java.util.Iterator;
import java.util.List;
import com.kitty.common.utils.vo.Approximate;
import java.util.ArrayList;
import com.kitty.system.game.team.domain.Team;

public class ActivityGlobalFunc
{
    public static Team getApproximate(final Team team, final ArrayList<Team> arrayList) {
        if (team == null || arrayList.size() <= 0) {
            return null;
        }
        final List<Approximate> objects = new ArrayList<Approximate>(arrayList.size());
        for (final Team t : arrayList) {
            final int score = getTeamSunEquipScore(t);
            objects.add(new Approximate(t.getId(), score));
        }
        final Approximate approximate = getApproximate(getTeamSunEquipScore(team), objects);
        return arrayList.stream().filter(e -> e.getId() == approximate.getId()).findFirst().orElse(null);
    }
    
    public static int getTeamSunEquipScore(final Team team) {
        int score = 0;
        if (team != null) {
            final List<Member> list = team.getList();
            for (int i = 0; i < list.size(); ++i) {
                final Member member = list.get(i);
                final Role role = SpringUtils.getRoleService().getPlayer(member.getRoleId());
                if (role != null) {
                    score += DataGlobalFunc.getEquipScore(SpringUtils.getEquipService().selectWearEquip(role));
                }
            }
        }
        return score;
    }
    
    public static Approximate getApproximate(final int x, final List<Approximate> src) {
        if (src == null) {
            return null;
        }
        if (src.size() == 1) {
            return src.get(0);
        }
        int minDifference = Math.abs(src.get(0).getNum() - x);
        int minIndex = 0;
        for (int i = 1; i < src.size(); ++i) {
            final int temp = Math.abs(src.get(i).getNum() - x);
            if (temp < minDifference) {
                minIndex = i;
                minDifference = temp;
            }
        }
        return src.get(minIndex);
    }
    
    public static void main(final String[] args) {
        final List<Approximate> list = new ArrayList<Approximate>();
        list.add(new Approximate(1, 10));
        list.add(new Approximate(2, 16));
        list.add(new Approximate(3, 8));
        list.add(new Approximate(4, 20));
        list.add(new Approximate(5, 6));
        final Approximate approximate = getApproximate(7, list);
        System.out.println("approximate = " + approximate.getNum());
    }
}
