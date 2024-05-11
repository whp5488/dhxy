
package com.kitty.system.game.fightnew.bean;

import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.enter.message.RespCombatFriends;
import java.util.Iterator;
import com.kitty.system.game.fightnew.FightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.team.domain.Member;
import java.util.List;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.role.domain.Role;

public class BossFight extends Fight
{
    private Role role;
    private Team team;
    private BossFightParam bossFightParam;
    private List<Member> fightMembers;
    
    public BossFight(final int type, final int mapId, final Role role, final Team team, final BossFightParam bossFightParam) {
        super(type);
        this.mapId = mapId;
        this.role = role;
        this.team = team;
        this.bossFightParam = bossFightParam;
    }
    
    @Override
    protected void loadActors() {
        this.fightMembers = this.fightNewService.loadTeamBattleInfo(this.team, this.getFightMemberA(), this);
        final List<SysDataMonster> list = this.createRandMonsterSet(this.team, this.bossFightParam.getBossParamList(), this.fightMembers);
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
        if (this.bossFightParam != null && this.bossFightParam.getSpecialMonsterType() == 1) {
            FightObject host = null;
            FightObject pet = null;
            for (final FightObject obj : memberListB) {
                if (obj.getPos() == 10003) {
                    host = obj;
                }
                if (obj.getPos() == 10103) {
                    pet = obj;
                }
            }
            if (host != null && pet != null) {
                host.setFightPetId(pet.getId());
                pet.setHostId(host.getId());
            }
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
    
    public Role getRole() {
        return this.role;
    }
    
    public Team getTeam() {
        return this.team;
    }
    
    public BossFightParam getBossFightParam() {
        return this.bossFightParam;
    }
    
    public List<Member> getFightMembers() {
        return this.fightMembers;
    }
    
    public void setRole(final Role role) {
        this.role = role;
    }
    
    public void setTeam(final Team team) {
        this.team = team;
    }
    
    public void setBossFightParam(final BossFightParam bossFightParam) {
        this.bossFightParam = bossFightParam;
    }
    
    public void setFightMembers(final List<Member> fightMembers) {
        this.fightMembers = fightMembers;
    }
}
