package com.kitty.system.game.fightnew.bean;

import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.enter.message.RespCombatFriends;
import java.util.Iterator;
import com.kitty.system.game.fightnew.FightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.init.domain.SysDataMonster;
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
    
    public BossFight(int type, int mapId, Role role, Team team, BossFightParam bossFightParam) {
        super(type);
        this.mapId = mapId;
        this.role = role;
        this.team = team;
        this.bossFightParam = bossFightParam;
    }
    
    @Override
    protected void loadActors() {
        this.fightMembers = this.fightNewService.loadTeamBattleInfo(this.team, this.getFightMemberA(), this);
        List<SysDataMonster> list = this.createRandMonsterSet(this.team, this.bossFightParam.getBossParamList(), this.fightMembers);
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
        if (this.bossFightParam != null && this.bossFightParam.getSpecialMonsterType() == 1) {
            FightObject host = null;
            FightObject pet = null;
            for (FightObject obj : memberListB) {
                if (obj.getPos() == 10003) {
                    host = obj;
                }
                if (obj.getPos() == 10103) {
                    pet = obj;
                }
                else {
                    continue;
                }
            }
            if (host != null && pet != null) {
                host.setFightPetId((int)pet.getId());
                pet.setHostId((int)host.getId());
            }
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
