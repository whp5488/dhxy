
package com.kitty.system.game.activity.common;

import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.system.game.activity.model.user.ca.vo.CaFight;
import com.kitty.system.game.activity.model.user.ca.CaPkData;
import java.util.function.Function;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.activity.model.user.lu.vo.WaterFight;
import com.kitty.system.game.activity.model.user.lu.WaterPkData;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.activity.message.RespClashPlans;
import com.kitty.system.game.activity.message.vo.GeneralClash;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.team.domain.Member;
import java.util.Iterator;
import com.kitty.common.utils.text.Convert;
import java.util.List;
import java.util.Calendar;

public class EventsUtils
{
    public static Calendar getTime(final Calendar calendarStart) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendarStart.getTime());
        calendar.set(11, calendarStart.get(11));
        calendar.set(12, calendarStart.get(12));
        calendar.set(13, 0);
        return calendar;
    }
    
    public static String canEnterTips(final List<String> dayOfWeek, final String signUpTime, final String startTime, final String name) {
        String msg = "每周";
        for (final String s : dayOfWeek) {
            msg = msg + Convert.getArabicFromWeek(s) + "、";
        }
        return msg.substring(0, msg.length() - 1) + signUpTime + "-" + startTime + "之间才可以报名参加" + name;
    }
    
    public static void clashPlans(final List<Member> teamA, final List<Member> teamB, final int type, final int countdown) {
        final RoleService roleService = SpringUtils.getRoleService();
        final List<Role> objects = new ArrayList<Role>();
        final List<GeneralClash> listA = new ArrayList<GeneralClash>(teamA.size());
        for (int i = 0; i < teamA.size(); ++i) {
            final Member member1 = teamA.get(i);
            final Role role = roleService.getPlayer(member1.getId());
            if (role != null) {
                objects.add(role);
                listA.add(new GeneralClash(role.getName(), role.getIcon(), role.getRelive().byteValue(), role.getLevel().shortValue()));
            }
        }
        final List<GeneralClash> listB = new ArrayList<GeneralClash>(teamB.size());
        for (int j = 0; j < teamB.size(); ++j) {
            final Member member2 = teamB.get(j);
            final Role role2 = roleService.getPlayer(member2.getId());
            if (role2 != null) {
                listB.add(new GeneralClash(role2.getName(), role2.getIcon(), role2.getRelive().byteValue(), role2.getLevel().shortValue()));
                objects.add(role2);
            }
        }
        final RespClashPlans respClashPlans = new RespClashPlans();
        respClashPlans.setClashListA(listA);
        respClashPlans.setClashListB(listB);
        respClashPlans.setType(type);
        respClashPlans.setCountDown(String.valueOf(TimeUtil.timeInMillis() + countdown * 1000L));
        for (final Role role3 : objects) {
            MessagePusher.pushMessage(role3, respClashPlans);
        }
    }
    
    public static WaterFight getMinNotFight(final WaterPkData data) {
        final List<WaterFight> fights = data.getFight().stream().filter(e -> e.getState() == 0).collect(Collectors.toList());
        if (fights != null && fights.size() > 0) {
            return fights.stream().min(Comparator.comparing(WaterFight::getSession)).get();
        }
        return null;
    }
    
    public static CaFight getMinNotFight(final CaPkData data) {
        final List<CaFight> fights = data.getFight().stream().filter(e -> e.getState() == 0).collect(Collectors.toList());
        if (fights != null && fights.size() > 0) {
            return fights.stream().min(Comparator.comparing(CaFight::getSession)).get();
        }
        return null;
    }
    
    public static FightInfo getFightInfo(final List<Role> listA, final List<Role> listB, final long id) {
        final Role role = listA.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (role != null) {
            return getFightInfo(listA, listB);
        }
        return getFightInfo(listB, listA);
    }
    
    public static FightInfo getFightInfo(final List<Role> itself, final List<Role> enemy) {
        final TeamService teamService = SpringUtils.getTeamService();
        final FightInfo fightInfo = new FightInfo();
        try {
            if (itself.size() > 0) {
                for (final Role a : itself) {
                    fightInfo.getItself().add(a.getId());
                }
                final Team team = teamService.getTeam(itself.get(0).getRoleId());
                if (team != null) {
                    fightInfo.setItselfLeader(team.getLeaderUid());
                }
            }
            if (enemy.size() > 0) {
                for (final Role b : enemy) {
                    fightInfo.getEnemy().add(b.getId());
                }
                final Team teamB = teamService.getTeam(enemy.get(0).getRoleId());
                if (teamB != null) {
                    fightInfo.setEnemyLeader(teamB.getLeaderUid());
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return fightInfo;
    }
}
