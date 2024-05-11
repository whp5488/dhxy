
package com.kitty.system.game.fightnew.bean;

import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.mirror.model.PetMirror;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;

public class PetMirrorFightObject extends FightObject
{
    private Pet pet;
    
    public PetMirrorFightObject(final PetMirror petMirror, final int id, final FightObject master, final int pos, final boolean isASide) {
        final Pet pet = new Pet();
        pet.setPetId(id);
        pet.setPetName(petMirror.getName());
        this.pet = pet;
        this.setPos(pos);
        this.setName(petMirror.getName());
        this.setLevel(petMirror.getLevel());
        this.setId(id);
        this.setSpeed(petMirror.getSpeed());
        this.setPhyPower(petMirror.getPhyPower());
        this.setMaxLife(petMirror.getMaxLife());
        this.setMaxMana(petMirror.getMaxMana());
        this.setCurrLife(petMirror.getMaxLife());
        this.setIcon(petMirror.getIcon());
        this.setSkillIds(SkillFunc.initPetSkill(petMirror.getSkillIds()));
        this.setHostId(master.getId());
        this.setASide(isASide);
    }
    
    @Override
    public String toString() {
        return "PetMirrorFightObject{id=" + this.id + ", name='" + this.name + '\'' + '}';
    }
    
    @Override
    public Pet getPet() {
        return this.pet;
    }
}
