package com.kitty.system.game.fightnew.bean;

import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.game.enter.message.RespCombatFriends;
import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.init.domain.SysDataMonster;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class BossFight extends Fight {
  private Role role;
  
  private Team team;
  
  private BossFightParam bossFightParam;
  
  private List<Member> fightMembers;
  
  public void setRole(Role role) {
    this.role = role;
  }
  
  public void setTeam(Team team) {
    this.team = team;
  }
  
  public void setBossFightParam(BossFightParam bossFightParam) {
    this.bossFightParam = bossFightParam;
  }
  
  public void setFightMembers(List<Member> fightMembers) {
    this.fightMembers = fightMembers;
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
  
  public BossFight(int type, int mapId, Role role, Team team, BossFightParam bossFightParam) {
    super(type);
    this.mapId = mapId;
    this.role = role;
    this.team = team;
    this.bossFightParam = bossFightParam;
  }
  
  protected void loadActors() {
    this.fightMembers = this.fightNewService.loadTeamBattleInfo(this.team, getFightMemberA(), this);
    List<SysDataMonster> list = createRandMonsterSet(this.team, this.bossFightParam.getBossParamList(), this.fightMembers);
    loadMonsterBattleInfo(list, getFightMemberB(), this);
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
      monsterId++;
      battleground.setExpMap(expMap);
    } 
    if (this.bossFightParam != null && this.bossFightParam.getSpecialMonsterType() == 1) {
      FightObject host = null;
      FightObject pet = null;
      for (FightObject obj : memberListB) {
        if (obj.getPos() == 10003)
          host = obj; 
        if (obj.getPos() == 10103)
          pet = obj; 
      } 
      if (host != null && pet != null) {
        host.setFightPetId(pet.getId().intValue());
        pet.setHostId(host.getId().intValue());
      } 
    } 
  }
  
  protected void notifyAndStart() {
    RespCombatFriends respCombatFriends = new RespCombatFriends();
    RespCombatOpponets respCombatOpponets = new RespCombatOpponets();
    respCombatOpponets.setList(getFightMemberB());
    respCombatFriends.setList(getFightMemberA());
    this.fightNewService.sendStartCombat(this, respCombatFriends, respCombatOpponets, this.fightMembers);
    this.fightNewService.whileSendStart(this);
    for (FightObject fightObject : getListA()) {
      if (fightObject.isPlayer())
        this.fightNewService.autoFightOne(this, fightObject); 
    } 
  }
}
