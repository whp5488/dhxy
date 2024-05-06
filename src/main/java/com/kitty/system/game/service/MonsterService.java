package com.kitty.system.game.service;

import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.MoveEvent;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.wardef.STAR_PERCENT;
import com.kitty.system.game.fightnew.book.monster.MonsterSkillUtils;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.init.Monster;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysGuajiMap;
import com.kitty.system.init.domain.SysGuajiWar;
import com.kitty.system.init.domain.SysWarRole;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.kitty.system.init.domain.vo.SkillParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MonsterService {
  Logger logger = LoggerFactory.getLogger(MonsterService.class);
  
  private ConcurrentMap<Integer, Integer> monsterTimes = new ConcurrentHashMap<>();
  
  public int updateMonsterTime(int roleId) {
    int pre = (Integer) this.monsterTimes.getOrDefault(roleId, 0);
    this.monsterTimes.put(roleId, pre + 1);
    return pre + 1;
  }
  
  public void flushMonsterTime(int roleId) {
    this.monsterTimes.remove(roleId);
  }
  
  public Object[] getMapMonster(Role role, int mapId) {
    Object[] objs = new Object[2];
    List<SysDataMonster> list = new ArrayList<>();
    Integer warRoleSerial = null;
    SysGuajiMap sysGuajiMap = (SysGuajiMap)MapDataPool.guaJiMap.get(mapId);
    if (sysGuajiMap != null) {
      Integer warDataId = sysGuajiMap.getWarDataId();
      List<SysGuajiWar> wars = (List<SysGuajiWar>)FightDataPool.data_guaji_war_list.get(warDataId);
      if (wars != null && wars.size() > 0) {
        Random random = new Random();
        int index = random.nextInt(wars.size());
        SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(((SysGuajiWar)wars.get(index)).getSerial());
        warRoleSerial = warRole.getSerial();
        if (warRole != null && 
          warRole.getMonster() != null && warRole.getMonster().size() > 0) {
          List<SysDataMonster> monsters = getMonstersByWarRandom(FightDataPool.data_monster, warRole, role);
          list.addAll(monsters);
        } 
      } 
    } 
    objs[0] = warRoleSerial;
    objs[1] = list;
    return objs;
  }
  
  public List<SysDataMonster> getMonstersByWarRandom(Map<Integer, SysDataMonster> data_monster, SysWarRole warRole, Role role) {
    List<SysDataMonster> list = new ArrayList<>();
    Team team = ((FightNewService)SpringUtils.getBean(FightNewService.class)).getRoleFightTeam(role);
    Integer monsterLevel = role.getRelive() * 1000 + role.getLevel();
    if (team != null) {
      List<Member> listTeam = team.getList();
      RoleService roleService = SpringUtils.getRoleService();
      int lvCount = 0;
      for (int j = 0; j < listTeam.size(); j++) {
        Member member = listTeam.get(j);
        Role teamRole = roleService.getPlayerBy(member.getRoleId());
        lvCount += teamRole.getRelive() * 1000 + teamRole.getLevel();
      } 
      monsterLevel = lvCount / listTeam.size();
    } 
    Random random = new Random();
    byte[] monsters = { 6, 7, 8, 9, 10 };
    byte count = monsters[random.nextInt(monsters.length)];
    List<Integer> poss = Const.poss;
    for (int i = 0; i < count; i++) {
      int monsterIndex = random.nextInt(warRole.getMonster().size());
      SysDataMonster monster = data_monster.get(((Monster)warRole.getMonster().get(monsterIndex)).getSerial());
      SysDataMonster dataMonster = new SysDataMonster();
      BeanUtils.copyBeanProp(dataMonster, monster);
      int pos = (Integer) poss.get(i);
      dataMonster.setPos(pos);
      DataGlobalFunc.createMonster(monsterLevel, dataMonster);
      list.add(dataMonster);
    } 
    return list;
  }
  
  public List<SysDataMonster> getMonsters(Map<Integer, SysDataMonster> data_monster, SysWarRole warRole) {
    List<SysDataMonster> list = new ArrayList<>();
    for (int i = 0; i < warRole.getMonster().size(); i++) {
      SysDataMonster monster = data_monster.get(warRole.getMonster().get(i).getSerial());
      monster.setPos(warRole.getMonster().get(i).getPos());
      SysDataMonster dataMonster = new SysDataMonster();
      BeanUtils.copyBeanProp(dataMonster, monster);
      list.add(dataMonster);
    } 
    if (warRole != null && warRole.getSpecialType() > 0)
      specialMonsterFight(list, data_monster, warRole); 
    return list;
  }
  
  @EventHandler({EventType.MOVE})
  public void handleMoveEvent(MoveEvent moveEvent) {
    Role role = moveEvent.getRole();
    if (role == null)
      return; 
    boolean flag = SkillAi.canAffectOnPro(0.1D);
    long theTime = System.currentTimeMillis() / 1000L;
    Integer incenseExpireTime = role.getExpireTimeMap().get("incenseExpireTime");
    if (incenseExpireTime != null && theTime <= incenseExpireTime && flag) {
        if (role.getLevel() < 33)
            return;
        MonsterService monsterService = SpringUtils.getBean(MonsterService.class);
        Object[] objs = monsterService.getMapMonster(role, role.getPos().getMapId());
        List<SysDataMonster> mapMonster = (List<SysDataMonster>) objs[1];
        if (mapMonster == null || mapMonster.size() <= 0)
            return;
        Team team = SpringUtils.getBean(FightNewService.class).getRoleFightTeam(role);
        SpringUtils.getBean(FightNewService.class).enterFieldFight(team, role);
        monsterService.flushMonsterTime(role.getRoleId());
    }
  }
  
  private void specialMonsterFight(List<SysDataMonster> list, Map<Integer, SysDataMonster> data_monster, SysWarRole warRole) {
    switch (warRole.getSpecialType()) {
      case 1:
        star18Excute(list, data_monster, warRole);
        break;
    } 
  }
  
  private void star18Excute(List<SysDataMonster> list, Map<Integer, SysDataMonster> data_monster, SysWarRole warRole) {
    if (list != null && list.size() > 0) {
      SysDataMonster dataMonster = null;
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
        SysDataMonster sysDataMonster = (SysDataMonster)var5.next();
        if (sysDataMonster.getPos() == 10003) {
          dataMonster = sysDataMonster;
          break;
        }
      }

      if (dataMonster != null) {
        ArrayList<SkillParameter> skills = dataMonster.getSkills();
        dataMonster.setSkills(new ArrayList<>());
        dataMonster.getSkills().addAll(skills);
        MonsterSkillUtils.addSkill(dataMonster, 30042, "骑马");
        MonsterSkillUtils.addSkill(dataMonster, 30043, "摄魂大法");
      }
    }

    ArrayList poss = new ArrayList() {
      {
        this.add(10001);
        this.add(10002);
        this.add(10004);
        this.add(10005);
      }
    };
    int i = poss.size() - 1;

    while(true) {
      STAR_PERCENT[] var9 = STAR_PERCENT.values();
      int var10 = var9.length;

      for(int var11 = 0; var11 < var10; ++var11) {
        STAR_PERCENT starPercent = var9[var11];
        if (i < 0) {
          SysDataMonster dataMonsterPet = MonsterSkillUtils.getStar18SelectPet();
          if (dataMonsterPet != null) {
            dataMonsterPet.setPos(10103);
            dataMonsterPet.setName(dataMonsterPet.getDes());
            MonsterSkillUtils.randomMonsterPetSkill(dataMonsterPet);
            list.add(dataMonsterPet);
          }

          return;
        }

        boolean flag = SkillAi.canAffectOnPro(starPercent.getPercent());
        if (flag) {
          SysDataMonster sysDataMonster = data_monster.get(starPercent.getSerial());
          if (sysDataMonster != null) {
            SysDataMonster dataMonster = new SysDataMonster();
            BeanUtils.copyBeanProp(dataMonster, sysDataMonster);
            dataMonster.setPos((Integer)poss.get(i));
            list.add(dataMonster);
            poss.remove(i);
            --i;
            flag = false;
          }
        }
      }
    }
  }
}
