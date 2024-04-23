package com.kitty.system.game.mirror.service;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.mirror.model.FightMirror;
import com.kitty.system.game.mirror.model.MirrorFightParam;
import com.kitty.system.game.mirror.model.PetMirror;
import com.kitty.system.game.mirror.model.RoleMirror;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FightMirrorService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  PetService petService;
  
  private static final int LIFE_RATE = 3;
  
  public FightMirror createFightMirror(long id, Role role) {
    return createFightMirror(id, role, 3);
  }
  
  public FightMirror createFightMirror(long id, Role role, int lifeRate) {
    FightMirror fightMirror = new FightMirror();
    fightMirror.setId(Long.valueOf(id));
    fightMirror.setRoleMirror(createRoleMirror(role, lifeRate));
    fightMirror.setPetMirror(createPetMirror(role, lifeRate));
    return fightMirror;
  }
  
  private RoleMirror createRoleMirror(Role role, int lifeRate) {
    RoleMirror roleMirror = new RoleMirror();
    roleMirror.setId(role.getId().longValue());
    roleMirror.setName(role.getName());
    roleMirror.setLevel(role.getLevel().intValue());
    roleMirror.setMaxLife(role.getMaxLife().intValue() * lifeRate);
    roleMirror.setMaxMana(role.getMaxMana().intValue());
    roleMirror.setIcon(role.getIcon());
    roleMirror.setWeaponIcon(role.getWeaponIcon());
    return roleMirror;
  }
  
  private PetMirror createPetMirror(Role role, int lifeRate) {
    Map<Integer, Pet> petMap = this.petService.selectPet(role);
    Pet pet = petMap.get(Integer.valueOf(role.getExtendBox().getBuyPetGrid()));
    PetMirror petMirror = new PetMirror();
    petMirror.setName(pet.getPetName());
    petMirror.setIcon(pet.getShape().intValue());
    petMirror.setLevel(pet.getPetLv().intValue());
    petMirror.setSerial(pet.getPetType().intValue());
    petMirror.setIntimacy(pet.getPetClose());
    petMirror.setSkillIds(pet.getSkills());
    return petMirror;
  }
  
  public FightMirror getFightMirror(long id) {
    return (FightMirror)DataCache.FIGHT_MIRRORS.get(Long.valueOf(id));
  }
  
  public void addFightMirror(FightMirror fightMirror) {}
  
  public void updateFightMirror(FightMirror fightMirror) {}
  
  public void loadFightMirrors() {}
  
  public Fight startFight(Role role, MirrorFightParam mirrorFightParam) {
    FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
    if (fightNewService.isInFight(role))
      return null; 
    if (SpringUtils.getTeamService().isInTeamAndNotLeader(role)) {
      this.logger.warn("{}({})不是队长触发战斗", role.getId(), role.getName());
      return null;
    } 
    synchronized (role.getCanFight()) {
      if (!role.getCanFight().booleanValue())
        return null; 
      role.setCanFight(Boolean.valueOf(false));
    } 
    byte fightType = mirrorFightParam.getFightType();
    int npcId = mirrorFightParam.getNpcId();
    FightMirror fightMirror = mirrorFightParam.getFightMirror();
    Fight fight = FightFactory.getFightFactory(fightType).create(0, new Object[] { role, Byte.valueOf(fightType), Integer.valueOf(npcId), (fightMirror != null) ? fightMirror : mirrorFightParam.getFightMirrorList() });
    fight.start();
    return fight;
  }
}
