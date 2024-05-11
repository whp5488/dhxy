
package com.kitty.system.game.service;

import java.util.Iterator;
import com.kitty.system.enums.war.wardef.STAR_PERCENT;
import com.kitty.system.game.fightnew.book.monster.MonsterSkillUtils;
import com.kitty.system.init.domain.vo.SkillParameter;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.listener.event.MoveEvent;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.vo.init.Monster;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.team.domain.Member;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import java.util.Map;
import java.util.Collection;
import com.kitty.system.init.domain.SysWarRole;
import java.util.Random;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysGuajiWar;
import java.util.List;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.init.domain.SysGuajiMap;
import com.kitty.system.init.domain.SysDataMonster;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MonsterService
{
    Logger logger;
    private ConcurrentMap<Integer, Integer> monsterTimes;
    
    public MonsterService() {
        this.logger = LoggerFactory.getLogger(MonsterService.class);
        this.monsterTimes = new ConcurrentHashMap<Integer, Integer>();
    }
    
    public int updateMonsterTime(final int roleId) {
        final int pre = this.monsterTimes.getOrDefault(roleId, 0);
        this.monsterTimes.put(roleId, pre + 1);
        return pre + 1;
    }
    
    public void flushMonsterTime(final int roleId) {
        this.monsterTimes.remove(roleId);
    }
    
    public Object[] getMapMonster(final Role role, final int mapId) {
        final Object[] objs = new Object[2];
        final List<SysDataMonster> list = new ArrayList<SysDataMonster>();
        Integer warRoleSerial = null;
        final SysGuajiMap sysGuajiMap = MapDataPool.guaJiMap.get(mapId);
        if (sysGuajiMap != null) {
            final Integer warDataId = sysGuajiMap.getWarDataId();
            final List<SysGuajiWar> wars = FightDataPool.data_guaji_war_list.get(warDataId);
            if (wars != null && wars.size() > 0) {
                final Random random = new Random();
                final int index = random.nextInt(wars.size());
                final SysWarRole warRole = FightDataPool.data_war_role.get(wars.get(index).getSerial());
                warRoleSerial = warRole.getSerial();
                if (warRole != null && warRole.getMonster() != null && warRole.getMonster().size() > 0) {
                    final List<SysDataMonster> monsters = this.getMonstersByWarRandom(FightDataPool.data_monster, warRole, role);
                    list.addAll(monsters);
                }
            }
        }
        objs[0] = warRoleSerial;
        objs[1] = list;
        return objs;
    }
    
    public List<SysDataMonster> getMonstersByWarRandom(final Map<Integer, SysDataMonster> data_monster, final SysWarRole warRole, final Role role) {
        final List<SysDataMonster> list = new ArrayList<SysDataMonster>();
        final Team team = SpringUtils.getBean(FightNewService.class).getRoleFightTeam(role);
        Integer monsterLevel = role.getRelive() * 1000 + role.getLevel();
        if (team != null) {
            final List<Member> listTeam = team.getList();
            final RoleService roleService = SpringUtils.getRoleService();
            int lvCount = 0;
            for (int i = 0; i < listTeam.size(); ++i) {
                final Member member = listTeam.get(i);
                final Role teamRole = roleService.getPlayerBy(member.getRoleId());
                lvCount += teamRole.getRelive() * 1000 + teamRole.getLevel();
            }
            monsterLevel = lvCount / listTeam.size();
        }
        final Random random = new Random();
        final byte[] monsters = { 6, 7, 8, 9, 10 };
        final byte count = monsters[random.nextInt(monsters.length)];
        final List<Integer> poss = Const.poss;
        for (int j = 0; j < count; ++j) {
            final int monsterIndex = random.nextInt(warRole.getMonster().size());
            final SysDataMonster monster = data_monster.get(warRole.getMonster().get(monsterIndex).getSerial());
            final SysDataMonster dataMonster = new SysDataMonster();
            BeanUtils.copyBeanProp(dataMonster, monster);
            final int pos = poss.get(j);
            dataMonster.setPos(pos);
            DataGlobalFunc.createMonster(monsterLevel, dataMonster);
            list.add(dataMonster);
        }
        return list;
    }
    
    public List<SysDataMonster> getMonsters(final Map<Integer, SysDataMonster> data_monster, final SysWarRole warRole) {
        final List<SysDataMonster> list = new ArrayList<SysDataMonster>();
        for (int i = 0; i < warRole.getMonster().size(); ++i) {
            final SysDataMonster monster = data_monster.get(warRole.getMonster().get(i).getSerial());
            monster.setPos(warRole.getMonster().get(i).getPos());
            final SysDataMonster dataMonster = new SysDataMonster();
            BeanUtils.copyBeanProp(dataMonster, monster);
            list.add(dataMonster);
        }
        if (warRole != null && warRole.getSpecialType() > 0) {
            this.specialMonsterFight(list, data_monster, warRole);
        }
        return list;
    }
    
    @EventHandler({ EventType.MOVE })
    public void handleMoveEvent(final MoveEvent moveEvent) {
        final Role role = moveEvent.getRole();
        if (role == null) {
            return;
        }
        final boolean flag = SkillAi.canAffectOnPro(0.1);
        final long theTime = System.currentTimeMillis() / 1000L;
        final Integer incenseExpireTime = role.getExpireTimeMap().get("incenseExpireTime");
        if (flag && theTime <= incenseExpireTime) {
            if (role.getLevel() < 33) {
                return;
            }
            final MonsterService monsterService = SpringUtils.getBean(MonsterService.class);
            final Object[] objs = monsterService.getMapMonster(role, role.getPos().getMapId());
            final List<SysDataMonster> mapMonster = (List<SysDataMonster>)objs[1];
            if (mapMonster == null || mapMonster.size() <= 0) {
                return;
            }
            final Team team = SpringUtils.getBean(FightNewService.class).getRoleFightTeam(role);
            SpringUtils.getBean(FightNewService.class).enterFieldFight(team, role);
            monsterService.flushMonsterTime(role.getRoleId());
        }
    }
    
    private void specialMonsterFight(final List<SysDataMonster> list, final Map<Integer, SysDataMonster> data_monster, final SysWarRole warRole) {
        switch (warRole.getSpecialType()) {
            case 1: {
                this.star18Excute(list, data_monster, warRole);
                break;
            }
        }
    }
    
    private void star18Excute(final List<SysDataMonster> list, final Map<Integer, SysDataMonster> data_monster, final SysWarRole warRole) {
        if (list != null && list.size() > 0) {
            SysDataMonster dataMonster = null;
            for (final SysDataMonster sysDataMonster : list) {
                if (sysDataMonster.getPos() == 10003) {
                    dataMonster = sysDataMonster;
                    break;
                }
            }
            if (dataMonster != null) {
                final ArrayList<SkillParameter> skills = dataMonster.getSkills();
                dataMonster.setSkills(new ArrayList<SkillParameter>());
                dataMonster.getSkills().addAll(skills);
                MonsterSkillUtils.addSkill(dataMonster, 30042, "骑马");
                MonsterSkillUtils.addSkill(dataMonster, 30043, "摄魂大法");
            }
        }
        final List<Integer> poss = new ArrayList<Integer>() {
            {
                this.add(10001);
                this.add(10002);
                this.add(10004);
                this.add(10005);
            }
        };
        int i = poss.size() - 1;
    Block_7:
        while (true) {
            for (final STAR_PERCENT starPercent : STAR_PERCENT.values()) {
                if (i < 0) {
                    break Block_7;
                }
                boolean flag = SkillAi.canAffectOnPro(starPercent.getPercent());
                if (flag) {
                    final SysDataMonster sysDataMonster2 = data_monster.get(starPercent.getSerial());
                    if (sysDataMonster2 != null) {
                        final SysDataMonster dataMonster2 = new SysDataMonster();
                        BeanUtils.copyBeanProp(dataMonster2, sysDataMonster2);
                        dataMonster2.setPos(poss.get(i));
                        list.add(dataMonster2);
                        poss.remove(i);
                        --i;
                        flag = false;
                    }
                }
            }
        }
        final SysDataMonster dataMonsterPet = MonsterSkillUtils.getStar18SelectPet();
        if (dataMonsterPet != null) {
            dataMonsterPet.setPos(10103);
            dataMonsterPet.setName(dataMonsterPet.getDes());
            MonsterSkillUtils.randomMonsterPetSkill(dataMonsterPet);
            list.add(dataMonsterPet);
        }
    }
}
