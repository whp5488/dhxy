
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
    
    public WildMonsterFight(final Team team, final Role role) {
        super(FIGHT_TYPE.MONSTER.getValue());
        final Pos position = role.getPos();
        final int mapId = position.getMapId();
        this.setMapId(mapId);
        this.role = role;
        this.team = team;
        final MonsterService monsterService = SpringUtils.getBean(MonsterService.class);
        final Object[] objs = monsterService.getMapMonster(role, mapId);
        final List<SysDataMonster> monsters = (List<SysDataMonster>)objs[1];
        this.monsters = monsters;
    }
    
    @Override
    protected void loadActors() {
        this.fightMembers = this.fightNewService.loadTeamBattleInfo(this.team, this.getFightMemberA(), this);
        final List<SysDataMonster> list = this.createRandMonsterSet(this.team, this.monsters, this.fightMembers);
        this.loadMonsterBattleInfo(list, this.getFightMemberB(), this);
    }
    
    private List<SysDataMonster> createRandMonsterSet(final Team team, final List<SysDataMonster> monsters, final List<Member> list) {
        return monsters;
    }
    
    private void loadMonsterBattleInfo(final List<SysDataMonster> monsters, final List<FightMember> monsterListB, final Fight battleground) {
        final List<FightObject> memberListB = battleground.getListB();
        int monsterId = 567898745;
        final NutMap expMap = NutMap.NEW();
        for (final SysDataMonster monster : monsters) {
            final FightObject fightObject = new MonsterFightObject(monster, monster.getPos(), monsterId);
            final FightMember fightMember = fightObject.asFightMember();
            monsterListB.add(fightMember);
            memberListB.add(fightObject);
            ++monsterId;
            battleground.setExpMap(expMap);
        }
    }
    
    @Override
    protected void notifyAndStart() {
        final RespCombatFriends respCombatFriends = new RespCombatFriends();
        final RespCombatOpponets respCombatOpponets = new RespCombatOpponets();
        respCombatOpponets.setList(this.getFightMemberB());
        respCombatFriends.setList(this.getFightMemberA());
        this.fightNewService.sendStartCombat(this, respCombatFriends, respCombatOpponets, this.fightMembers);
        this.fightNewService.whileSendStart(this);
        for (final FightObject fightObject : this.getListA()) {
            if (fightObject.isPlayer()) {
                this.fightNewService.autoFightOne(this, fightObject);
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
