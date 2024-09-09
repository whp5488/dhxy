package com.kitty.system.game.fightnew.model;

import com.kitty.system.game.fightnew.utils.FightOtherUtil;
import com.kitty.system.game.pet.domain.Pet;
import java.util.List;
import java.io.Serializable;

public class PetFightInParam implements Serializable
{
    private int petId;
    private List<Integer> skillIds;
    
    public PetFightInParam() {
    }
    
    public void addSkillIds(Pet pet) {
        List<Integer> skillIds = FightOtherUtil.addSkillIds(pet);
        if (skillIds.size() > 0) {
            this.skillIds = skillIds;
        }
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    public void setSkillIds(final List<Integer> skillIds) {
        this.skillIds = skillIds;
    }
    
    public int getPetId() {
        return this.petId;
    }
    
    public List<Integer> getSkillIds() {
        return this.skillIds;
    }
}
