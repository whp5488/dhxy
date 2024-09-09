package com.kitty.system.game.mirror.service;

import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.mirror.model.MirrorFightParam;
import com.kitty.mina.cache.DataCache;
import java.util.Map;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.mirror.model.PetMirror;
import com.kitty.system.game.mirror.model.RoleMirror;
import com.kitty.system.game.mirror.model.FightMirror;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.pet.service.PetService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FightMirrorService
{
    private Logger logger;
    @Autowired
    PetService petService;
    private static final int LIFE_RATE = 3;
    
    public FightMirrorService() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    public FightMirror createFightMirror(long id, Role role) {
        return this.createFightMirror(id, role, 3);
    }
    
    public FightMirror createFightMirror(long id, Role role, int lifeRate) {
        FightMirror fightMirror = new FightMirror();
        fightMirror.setId(Long.valueOf(id));
        fightMirror.setRoleMirror(this.createRoleMirror(role, lifeRate));
        fightMirror.setPetMirror(this.createPetMirror(role, lifeRate));
        return fightMirror;
    }
    
    private RoleMirror createRoleMirror(Role role, int lifeRate) {
        RoleMirror roleMirror = new RoleMirror();
        roleMirror.setId((long)role.getId());
        roleMirror.setName(role.getName());
        roleMirror.setLevel((int)role.getLevel());
        roleMirror.setMaxLife((int)role.getMaxLife() * lifeRate);
        roleMirror.setMaxMana((int)role.getMaxMana());
        roleMirror.setIcon(role.getIcon());
        roleMirror.setWeaponIcon(role.getWeaponIcon());
        return roleMirror;
    }
    
    private PetMirror createPetMirror(Role role, int lifeRate) {
        Map<Integer, Pet> petMap = this.petService.selectPet(role);
        Pet pet = (Pet)petMap.get(Integer.valueOf(role.getExtendBox().getBuyPetGrid()));
        PetMirror petMirror = new PetMirror();
        petMirror.setName(pet.getPetName());
        petMirror.setIcon((int)pet.getShape());
        petMirror.setLevel((int)pet.getPetLv());
        petMirror.setSerial((int)pet.getPetType());
        petMirror.setIntimacy(pet.getPetClose());
        petMirror.setSkillIds(pet.getSkills());
        return petMirror;
    }
    
    public FightMirror getFightMirror(long id) {
        return (FightMirror)DataCache.FIGHT_MIRRORS.get(Long.valueOf(id));
    }
    
    public void addFightMirror(FightMirror fightMirror) {
    }
    
    public void updateFightMirror(FightMirror fightMirror) {
    }
    
    public void loadFightMirrors() {
    }
    
    public Fight startFight(Role role, MirrorFightParam mirrorFightParam) {
        FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        if (fightNewService.isInFight(role)) {
            return null;
        }
        if (SpringUtils.getTeamService().isInTeamAndNotLeader(role)) {
            this.logger.warn("{}({})不是队长触发战斗", role.getId(), role.getName());
            return null;
        }
        synchronized (role.getCanFight()) {
            if (!(boolean)role.getCanFight()) {
                return null;
            }
            role.setCanFight(Boolean.valueOf(false));
        }
        byte fightType = mirrorFightParam.getFightType();
        int npcId = mirrorFightParam.getNpcId();
        FightMirror fightMirror = mirrorFightParam.getFightMirror();
        Fight fight = FightFactory.getFightFactory((int)fightType).create(0, role, Byte.valueOf(fightType), Integer.valueOf(npcId), (fightMirror != null) ? fightMirror : mirrorFightParam.getFightMirrorList());
        fight.start();
        return fight;
    }
}
