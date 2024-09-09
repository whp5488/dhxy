package com.kitty.system.game.activity.common;

import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.system.game.activity.model.user.ca.vo.CaFight;
import com.kitty.system.game.activity.model.user.ca.CaPkData;
import java.util.Comparator;
import java.util.stream.Collectors;
import com.kitty.system.game.activity.model.user.lu.vo.WaterFight;
import com.kitty.system.game.activity.model.user.lu.WaterPkData;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.activity.message.RespClashPlans;
import com.kitty.system.game.activity.message.vo.GeneralClash;
import java.util.ArrayList;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.team.domain.Member;
import java.util.Iterator;
import com.kitty.common.utils.text.Convert;
import java.util.List;
import java.util.Calendar;

public class EventsUtils
{
    public EventsUtils() {
    }
    
    public static Calendar getTime(Calendar calendarStart) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendarStart.getTime());
        calendar.set(11, calendarStart.get(11));
        calendar.set(12, calendarStart.get(12));
        calendar.set(13, 0);
        return calendar;
    }
    
    public static String canEnterTips(List<String> dayOfWeek, String signUpTime, String startTime, String name) {
        String msg = "每周";
        for (String s : dayOfWeek) {
            msg = msg + Convert.getArabicFromWeek(s) + "、";
        }
        return msg.substring(0, msg.length() - 1) + signUpTime + "-" + startTime + "之间才可以报名参加" + name;
    }
    
    public static void clashPlans(List<Member> teamA, List<Member> teamB, int type, int countdown) {
        RoleService roleService = SpringUtils.getRoleService();
        List<Role> objects = new ArrayList<>();
        List<GeneralClash> listA = new ArrayList(teamA.size());
        for (int i = 0; i < teamA.size(); ++i) {
            Member member1 = (Member)teamA.get(i);
            Role role = roleService.getPlayer((long)member1.getId());
            if (role != null) {
                objects.add(role);
                listA.add(new GeneralClash(role.getName(), role.getIcon(), role.getRelive().byteValue(), role.getLevel().shortValue()));
            }
        }
        List<GeneralClash> listB = new ArrayList(teamB.size());
        for (int j = 0; j < teamB.size(); ++j) {
            Member member2 = (Member)teamB.get(j);
            Role role2 = roleService.getPlayer((long)member2.getId());
            if (role2 != null) {
                listB.add(new GeneralClash(role2.getName(), role2.getIcon(), role2.getRelive().byteValue(), role2.getLevel().shortValue()));
                objects.add(role2);
            }
        }
        RespClashPlans respClashPlans = new RespClashPlans();
        respClashPlans.setClashListA(listA);
        respClashPlans.setClashListB(listB);
        respClashPlans.setType(type);
        respClashPlans.setCountDown(String.valueOf(TimeUtil.timeInMillis() + (long)countdown * 1000L));
        for (Role role3 : objects) {
            MessagePusher.pushMessage(role3, respClashPlans);
        }
    }
    
    public static WaterFight getMinNotFight(WaterPkData data) {
        List<WaterFight> fights = (List)data.getFight().stream().filter(e -> e.getState() == 0).collect(Collectors.toList());
        if (fights != null && fights.size() > 0) {
            return (WaterFight)fights.stream().min(Comparator.comparing(WaterFight::getSession)).get();
        }
        return null;
    }
    
    public static CaFight getMinNotFight(CaPkData data) {
        List<CaFight> fights = (List)data.getFight().stream().filter(e -> e.getState() == 0).collect(Collectors.toList());
        if (fights != null && fights.size() > 0) {
            return (CaFight)fights.stream().min(Comparator.comparing(CaFight::getSession)).get();
        }
        return null;
    }
    
    public static FightInfo getFightInfo(List<Role> listA, List<Role> listB, long id) {
        Role role = (Role)listA.stream().filter(e -> e.getId().equals(Long.valueOf(id))).findFirst().orElse(null);
        if (role != null) {
            return getFightInfo(listA, listB);
        }
        return getFightInfo(listB, listA);
    }
    
    public static FightInfo getFightInfo(List<Role> itself, List<Role> enemy) {
        TeamService teamService = SpringUtils.getTeamService();
        FightInfo fightInfo = new FightInfo();
        try {
            if (itself.size() > 0) {
                for (Role a : itself) {
                    fightInfo.getItself().add(a.getId());
                }
                Team team = teamService.getTeam((int)((Role)itself.get(0)).getRoleId());
                if (team != null) {
                    fightInfo.setItselfLeader(team.getLeaderUid());
                }
            }
            if (enemy.size() > 0) {
                for (Role b : enemy) {
                    fightInfo.getEnemy().add(b.getId());
                }
                Team teamB = teamService.getTeam((int)((Role)enemy.get(0)).getRoleId());
                if (teamB != null) {
                    fightInfo.setEnemyLeader(teamB.getLeaderUid());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return fightInfo;
    }
}
