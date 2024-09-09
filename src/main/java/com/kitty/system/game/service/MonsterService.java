package com.kitty.system.game.service;

import com.kitty.system.init.domain.vo.SkillParameter;
import java.util.Iterator;
import com.kitty.system.enums.war.wardef.STAR_PERCENT;
import com.kitty.system.game.fightnew.book.monster.MonsterSkillUtils;
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
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysGuajiWar;
import com.kitty.system.init.domain.SysWarRole;
import java.util.Random;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import java.util.List;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.init.domain.SysGuajiMap;
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
        this.monsterTimes = new ConcurrentHashMap();
    }
    
    public int updateMonsterTime(int roleId) {
        int pre = (int)this.monsterTimes.getOrDefault(Integer.valueOf(roleId), Integer.valueOf(0));
        this.monsterTimes.put(Integer.valueOf(roleId), Integer.valueOf(pre + 1));
        return pre + 1;
    }
    
    public void flushMonsterTime(int roleId) {
        this.monsterTimes.remove(Integer.valueOf(roleId));
    }
    
    public Object[] getMapMonster(Role role, int mapId) {
        Object[] objs = new Object[2];
        List<SysDataMonster> list = new ArrayList<>();
        Integer warRoleSerial = null;
        SysGuajiMap sysGuajiMap = (SysGuajiMap)MapDataPool.guaJiMap.get(Integer.valueOf(mapId));
        if (sysGuajiMap != null) {
            Integer warDataId = sysGuajiMap.getWarDataId();
            List<SysGuajiWar> wars = (List)FightDataPool.data_guaji_war_list.get(warDataId);
            if (wars != null && wars.size() > 0) {
                Random random = new Random();
                int index = random.nextInt(wars.size());
                SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(((SysGuajiWar)wars.get(index)).getSerial());
                warRoleSerial = warRole.getSerial();
                if (warRole != null && warRole.getMonster() != null && warRole.getMonster().size() > 0) {
                    List<SysDataMonster> monsters = this.getMonstersByWarRandom(FightDataPool.data_monster, warRole, role);
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
        Integer monsterLevel = Integer.valueOf((int)role.getRelive() * 1000 + (int)role.getLevel());
        if (team != null) {
            List<Member> listTeam = team.getList();
            RoleService roleService = SpringUtils.getRoleService();
            int lvCount = 0;
            for (int i = 0; i < listTeam.size(); ++i) {
                Member member = (Member)listTeam.get(i);
                Role teamRole = roleService.getPlayerBy((int)member.getRoleId());
                lvCount += (int)teamRole.getRelive() * 1000 + (int)teamRole.getLevel();
            }
            monsterLevel = Integer.valueOf(lvCount / listTeam.size());
        }
        Random random = new Random();
        byte[] monsters = { 6, 7, 8, 9, 10 };
        byte count = monsters[random.nextInt(monsters.length)];
        List<Integer> poss = Const.poss;
        for (int j = 0; j < count; ++j) {
            int monsterIndex = random.nextInt(warRole.getMonster().size());
            SysDataMonster monster = (SysDataMonster)data_monster.get(((Monster)warRole.getMonster().get(monsterIndex)).getSerial());
            SysDataMonster dataMonster = new SysDataMonster();
            BeanUtils.copyBeanProp(dataMonster, monster);
            int pos = (int)poss.get(j);
            dataMonster.setPos(pos);
            DataGlobalFunc.createMonster((int)monsterLevel, dataMonster);
            list.add(dataMonster);
        }
        return list;
    }
    
    public List<SysDataMonster> getMonsters(Map<Integer, SysDataMonster> data_monster, SysWarRole warRole) {
        List<SysDataMonster> list = new ArrayList<>();
        for (int i = 0; i < warRole.getMonster().size(); ++i) {
            SysDataMonster monster = (SysDataMonster)data_monster.get(((Monster)warRole.getMonster().get(i)).getSerial());
            monster.setPos((int)((Monster)warRole.getMonster().get(i)).getPos());
            SysDataMonster dataMonster = new SysDataMonster();
            BeanUtils.copyBeanProp(dataMonster, monster);
            list.add(dataMonster);
        }
        if (warRole != null && warRole.getSpecialType() > 0) {
            this.specialMonsterFight(list, data_monster, warRole);
            return list;
        }
        return list;
    }
    
    @EventHandler({ EventType.MOVE })
    public void handleMoveEvent(MoveEvent moveEvent) {
        Role role = moveEvent.getRole();
        if (role == null) {
            return;
        }
        boolean flag = SkillAi.canAffectOnPro(0.1);
        long theTime = System.currentTimeMillis() / 1000L;
        Integer incenseExpireTime = (Integer)role.getExpireTimeMap().get("incenseExpireTime");
        if (flag && theTime <= (long)(int)incenseExpireTime) {
            if ((int)role.getLevel() < 33) {
                return;
            }
            MonsterService monsterService = (MonsterService)SpringUtils.getBean(MonsterService.class);
            Object[] objs = monsterService.getMapMonster(role, (int)role.getPos().getMapId());
            List<SysDataMonster> mapMonster = (List)objs[1];
            if (mapMonster == null || mapMonster.size() <= 0) {
                return;
            }
            Team team = ((FightNewService)SpringUtils.getBean(FightNewService.class)).getRoleFightTeam(role);
            ((FightNewService)SpringUtils.getBean(FightNewService.class)).enterFieldFight(team, role);
            monsterService.flushMonsterTime((int)role.getRoleId());
        }
        return;
    }
    
    private void specialMonsterFight(List<SysDataMonster> list, Map<Integer, SysDataMonster> data_monster, SysWarRole warRole) {
        switch (warRole.getSpecialType()) {
            case 1: {
                this.star18Excute(list, data_monster, warRole);
                break;
            }
        }
    }
    
    private void star18Excute(List<SysDataMonster> list, Map<Integer, SysDataMonster> data_monster, SysWarRole warRole) {
        if (list != null && list.size() > 0) {
            SysDataMonster dataMonster = null;
            for (SysDataMonster sysDataMonster : list) {
                if (sysDataMonster.getPos() == 10003) {
                    dataMonster = sysDataMonster;
                    break;
                }
                continue;
            }
            if (dataMonster != null) {
                ArrayList<SkillParameter> skills = dataMonster.getSkills();
                dataMonster.setSkills(new ArrayList<>());
                dataMonster.getSkills().addAll(skills);
                MonsterSkillUtils.addSkill(dataMonster, 30042, "骑马");
                MonsterSkillUtils.addSkill(dataMonster, 30043, "摄魂大法");
            }
        }
        List<Integer> poss = new ArrayList<Integer>() {
            {
                this.add(Integer.valueOf(10001));
                this.add(Integer.valueOf(10002));
                this.add(Integer.valueOf(10004));
                this.add(Integer.valueOf(10005));
            }
        };
        int i = poss.size() - 1;
    Block_7:
        while (true) {
            STAR_PERCENT[] values = STAR_PERCENT.values();
            int length = values.length;
            int j = 0;
            while (j < length) {
                STAR_PERCENT starPercent = values[j];
                if (i < 0) {
                    break Block_7;
                }
                boolean flag = SkillAi.canAffectOnPro(starPercent.getPercent());
                if (flag) {
                    SysDataMonster sysDataMonster2 = (SysDataMonster)data_monster.get(Integer.valueOf(starPercent.getSerial()));
                    if (sysDataMonster2 != null) {
                        SysDataMonster dataMonster2 = new SysDataMonster();
                        BeanUtils.copyBeanProp(dataMonster2, sysDataMonster2);
                        dataMonster2.setPos((int)poss.get(i));
                        list.add(dataMonster2);
                        poss.remove(i);
                        --i;
                        flag = false;
                    }
                }
                ++j;
            }
        }
        SysDataMonster dataMonsterPet = MonsterSkillUtils.getStar18SelectPet();
        if (dataMonsterPet != null) {
            dataMonsterPet.setPos(10103);
            dataMonsterPet.setName(dataMonsterPet.getDes());
            MonsterSkillUtils.randomMonsterPetSkill(dataMonsterPet);
            list.add(dataMonsterPet);
        }
    }
}
