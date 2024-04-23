package com.kitty.system.game.fightnew.bean;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.enter.message.RespCombatFriends;
import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.nutz.lang.util.NutMap;

public class TeamPkFight extends Fight {
  private List<Member> listA;
  
  private List<Member> listB;
  
  private static NutMap publicAutoSet = NutMap.NEW();
  
  static {
    publicAutoSet.setv("type", Integer.valueOf(0));
    publicAutoSet.setv("action", Integer.valueOf(1));
    publicAutoSet.setv("skillId", Integer.valueOf(0));
  }
  
  public TeamPkFight(List<Member> listA, List<Member> listB) {
    super(FIGHT_TYPE.PLAY.getValue());
    this.listA = listA;
    this.listB = listB;
    FightExecutorNewService.FIGHTS.put(Integer.valueOf(getId()), this);
  }
  
  public TeamPkFight(int type, List<Member> listA, List<Member> listB) {
    super(type);
    this.listA = listA;
    this.listB = listB;
    FightExecutorNewService.FIGHTS.put(Integer.valueOf(getId()), this);
  }
  
  protected void loadActors() {
    initRoleAndPetInfo(true);
    initRoleAndPetInfo(false);
  }
  
  protected void notifyAndStart() {
    sendToSide(Boolean.TRUE.booleanValue());
    sendToSide(Boolean.FALSE.booleanValue());
    ((FightNewService)SpringUtils.getBean(FightNewService.class)).whileSendStart(this);
    for (FightObject fightObject : getAllFightObjects()) {
      if (fightObject.isPlayer())
        ((FightNewService)SpringUtils.getBean(FightNewService.class)).autoFightOne(this, fightObject); 
    } 
  }
  
  public boolean isPkFight() {
    return true;
  }
  
  private void initRoleAndPetInfo(boolean isA) {
    List<Member> memberList;
    List<FightObject> fightObjectList;
    int index = 0, petIndex = 5;
    if (isA) {
      fightObjectList = getListA();
      memberList = this.listA;
    } else {
      fightObjectList = getListB();
      memberList = this.listB;
    } 
    int customIndex = 0;
    List<Member> partners = new ArrayList<>();
    for (Member member : memberList) {
      if (!member.isInTeam())
        continue; 
      Role teamRole = SpringUtils.getRoleService().getOnlinePlayer(member.getId().longValue());
      if (teamRole == null) {
        partners.add(member);
        continue;
      } 
      FightExecutorNewService.FIGHT_ROOMS.put(member.getRoleId(), Integer.valueOf(getId()));
      ((FightNewService)SpringUtils.getBean(FightNewService.class)).fullLifeRoleAndPet(this, teamRole);
      this.fightNewService.checkAtFightStart(teamRole);
      FightObject fightObject = new PlayerFightObject(teamRole, index, customIndex, isA);
      fightObjectList.add(fightObject);
      if (isA) {
        FightMember fightMember = fightObject.asFightMember();
        getFightMemberA().add(fightMember);
      } else {
        FightMember fightMember = fightObject.asFightMember();
        getFightMemberB().add(fightMember);
      } 
      index++;
      customIndex++;
      Pet pet = (Pet)SpringUtils.getPetService().selectPet(teamRole).get(teamRole.getExtendBox().getCombatPet());
      if (pet != null) {
        customIndex++;
        PetFightObject petFightObj = new PetFightObject(pet, fightObject);
        fightObjectList.add(petFightObj);
        FightMember petFightMember = petFightObj.asFightMember();
        if (isA) {
          getFightMemberA().add(petFightMember);
        } else {
          getFightMemberB().add(petFightMember);
        } 
        FightExecutorNewService.PETID_ROLEID.put(pet.getPetId(), member.getRoleId());
        FightExecutorNewService.FIGHT_ROOMS.put(pet.getPetId(), Integer.valueOf(getId()));
      } 
      petIndex++;
    } 
    if (partners.size() > 0)
      for (Member member : partners) {
        if (!member.isInTeam())
          continue; 
        Role teamRole = SpringUtils.getRoleService().getOnlinePlayer(member.getRoleId().intValue());
        if (teamRole == null)
          continue; 
        Map<Integer, Partner> partnerMap = (Map<Integer, Partner>)DataCache.PARTNER_MAP.get(teamRole.getId());
        if (partnerMap == null || partnerMap.size() <= 0)
          continue; 
        Partner partner = partnerMap.get(member.getSerial());
        if (partner == null)
          continue; 
        FightObject hostFightObject = queryFightObject(member.getRoleId());
        FightObject partnerFightObject = new PartnerFightObject(partner, hostFightObject, index);
        fightObjectList.add(partnerFightObject);
        FightMember partnerFightMember = partnerFightObject.asFightMember();
        if (isA) {
          getFightMemberA().add(partnerFightMember);
        } else {
          getFightMemberB().add(partnerFightMember);
        } 
        index++;
      }  
  }
  
  private void sendToSide(boolean isA) {
    List<Member> members;
    RespCombatFriends respCombatFriends = new RespCombatFriends();
    RespCombatOpponets respCombatOpponets = new RespCombatOpponets();
    if (isA) {
      members = this.listA;
      respCombatFriends.setList(getFightMemberA());
      respCombatOpponets.setList(getFightMemberB());
    } else {
      members = this.listB;
      respCombatFriends.setList(getFightMemberB());
      respCombatOpponets.setList(getFightMemberA());
    } 
    ((FightNewService)SpringUtils.getBean(FightNewService.class)).sendStartCombat(this, respCombatFriends, respCombatOpponets, members);
  }
}
