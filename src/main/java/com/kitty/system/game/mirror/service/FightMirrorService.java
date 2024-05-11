
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
    
    public FightMirror createFightMirror(final long id, final Role role) {
        return this.createFightMirror(id, role, 3);
    }
    
    public FightMirror createFightMirror(final long id, final Role role, final int lifeRate) {
        final FightMirror fightMirror = new FightMirror();
        fightMirror.setId(id);
        fightMirror.setRoleMirror(this.createRoleMirror(role, lifeRate));
        fightMirror.setPetMirror(this.createPetMirror(role, lifeRate));
        return fightMirror;
    }
    
    private RoleMirror createRoleMirror(final Role role, final int lifeRate) {
        final RoleMirror roleMirror = new RoleMirror();
        roleMirror.setId(role.getId());
        roleMirror.setName(role.getName());
        roleMirror.setLevel(role.getLevel());
        roleMirror.setMaxLife(role.getMaxLife() * lifeRate);
        roleMirror.setMaxMana(role.getMaxMana());
        roleMirror.setIcon(role.getIcon());
        roleMirror.setWeaponIcon(role.getWeaponIcon());
        return roleMirror;
    }
    
    private PetMirror createPetMirror(final Role role, final int lifeRate) {
        final Map<Integer, Pet> petMap = this.petService.selectPet(role);
        final Pet pet = petMap.get(role.getExtendBox().getBuyPetGrid());
        final PetMirror petMirror = new PetMirror();
        petMirror.setName(pet.getPetName());
        petMirror.setIcon(pet.getShape());
        petMirror.setLevel(pet.getPetLv());
        petMirror.setSerial(pet.getPetType());
        petMirror.setIntimacy(pet.getPetClose());
        petMirror.setSkillIds(pet.getSkills());
        return petMirror;
    }
    
    public FightMirror getFightMirror(final long id) {
        return DataCache.FIGHT_MIRRORS.get(id);
    }
    
    public void addFightMirror(final FightMirror fightMirror) {
    }
    
    public void updateFightMirror(final FightMirror fightMirror) {
    }
    
    public void loadFightMirrors() {
    }
    
    public Fight startFight(final Role role, final MirrorFightParam mirrorFightParam) {
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        if (fightNewService.isInFight(role)) {
            return null;
        }
        if (SpringUtils.getTeamService().isInTeamAndNotLeader(role)) {
            this.logger.warn("{}({})不是队长触发战斗", (Object)role.getId(), (Object)role.getName());
            return null;
        }
        synchronized (role.getCanFight()) {
            if (!role.getCanFight()) {
                return null;
            }
            role.setCanFight(false);
        }
        final byte fightType = mirrorFightParam.getFightType();
        final int npcId = mirrorFightParam.getNpcId();
        final FightMirror fightMirror = mirrorFightParam.getFightMirror();
        final Fight fight = FightFactory.getFightFactory(fightType).create(0, role, fightType, npcId, (fightMirror != null) ? fightMirror : mirrorFightParam.getFightMirrorList());
        fight.start();
        return fight;
    }
}
