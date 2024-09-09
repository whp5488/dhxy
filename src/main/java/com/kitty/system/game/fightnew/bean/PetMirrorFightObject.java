package com.kitty.system.game.fightnew.bean;

import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.mirror.model.PetMirror;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;

public class PetMirrorFightObject extends FightObject
{
    private Pet pet;
    
    public PetMirrorFightObject(PetMirror petMirror, int id, FightObject master, int pos, boolean isASide) {
        Pet pet = new Pet();
        pet.setPetId(Integer.valueOf(id));
        pet.setPetName(petMirror.getName());
        this.pet = pet;
        this.setPos(pos);
        this.setName(petMirror.getName());
        this.setLevel(petMirror.getLevel());
        this.setId(Integer.valueOf(id));
        this.setSpeed(petMirror.getSpeed());
        this.setPhyPower(petMirror.getPhyPower());
        this.setMaxLife(petMirror.getMaxLife());
        this.setMaxMana(petMirror.getMaxMana());
        this.setCurrLife(petMirror.getMaxLife());
        this.setIcon(petMirror.getIcon());
        this.setSkillIds(SkillFunc.initPetSkill(petMirror.getSkillIds()));
        this.setHostId((int)master.getId());
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
