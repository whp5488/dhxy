package com.kitty.system.game.fightnew.bean;

import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.enter.message.RespCombatFriends;
import java.util.Iterator;
import com.kitty.system.game.fightnew.FightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.common.model.Pos;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.service.MonsterService;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.init.domain.SysDataMonster;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;

public class WildMonsterFight extends Fight
{
    private Team team;
    private Role role;
    private List<SysDataMonster> monsters;
    private List<Member> fightMembers;
    
    public WildMonsterFight(Team team, Role role) {
        super(FIGHT_TYPE.MONSTER.getValue());
        Pos position = role.getPos();
        int mapId = (int)position.getMapId();
        this.setMapId(mapId);
        this.role = role;
        this.team = team;
        MonsterService monsterService = (MonsterService)SpringUtils.getBean(MonsterService.class);
        Object[] objs = monsterService.getMapMonster(role, mapId);
        List<SysDataMonster> monsters = (List)objs[1];
        this.monsters = monsters;
    }
    
    @Override
    protected void loadActors() {
        this.fightMembers = this.fightNewService.loadTeamBattleInfo(this.team, this.getFightMemberA(), this);
        List<SysDataMonster> list = this.createRandMonsterSet(this.team, this.monsters, this.fightMembers);
        this.loadMonsterBattleInfo(list, this.getFightMemberB(), this);
    }
    
    private List<SysDataMonster> createRandMonsterSet(Team team, List<SysDataMonster> monsters, List<Member> list) {
        return monsters;
    }
    
    private void loadMonsterBattleInfo(List<SysDataMonster> monsters, List<FightMember> monsterListB, Fight battleground) {
        List<FightObject> memberListB = battleground.getListB();
        int monsterId = 567898745;
        NutMap expMap = NutMap.NEW();
        for (SysDataMonster monster : monsters) {
            FightObject fightObject = new MonsterFightObject(monster, Integer.valueOf(monster.getPos()), monsterId);
            FightMember fightMember = fightObject.asFightMember();
            monsterListB.add(fightMember);
            memberListB.add(fightObject);
            ++monsterId;
            battleground.setExpMap(expMap);
        }
    }
    
    @Override
    protected void notifyAndStart() {
        RespCombatFriends respCombatFriends = new RespCombatFriends();
        RespCombatOpponets respCombatOpponets = new RespCombatOpponets();
        respCombatOpponets.setList(this.getFightMemberB());
        respCombatFriends.setList(this.getFightMemberA());
        this.fightNewService.sendStartCombat(this, respCombatFriends, respCombatOpponets, this.fightMembers);
        this.fightNewService.whileSendStart(this);
        for (FightObject fightObject : this.getListA()) {
            if (fightObject.isPlayer()) {
                this.fightNewService.autoFightOne(this, fightObject);
            }
            else {
                continue;
            }
        }
    }
    
    public Team getTeam() {
        return this.team;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public List<SysDataMonster> getMonsters() {
        return this.monsters;
    }
    
    public List<Member> getFightMembers() {
        return this.fightMembers;
    }
}
