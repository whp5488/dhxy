
package com.kitty.system.game.role.utils;

import java.util.List;
import com.kitty.system.game.pet.domain.vo.SuitParam;
import java.util.ArrayList;
import java.util.Iterator;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.io.Serializable;

public class RoleUtils implements Serializable
{
    public static void removeAssistList(final Role role, final Pet pet, final Integer oldSkillSerial) {
        boolean isRemove = true;
        for (final PetSkill obj : pet.getSkills()) {
            if (obj.getSerial() != oldSkillSerial && SkillConst.assistSkills.contains(obj.getSerial())) {
                isRemove = false;
                break;
            }
        }
        if (isRemove) {
            updateAssistList(role, pet.getPetId(), false);
        }
    }
    
    public static void updateAssistList(final Role role, final Integer petId, final boolean isAdd) {
        final List<SuitParam> suits = role.getExtendBox().getSuits();
        if (isAdd) {
            if (suits.size() <= 0) {
                final List<Integer> petIds = new ArrayList<Integer>();
                petIds.add(petId);
                final SuitParam suit = new SuitParam();
                suit.setSerial(suits.size() + 1);
                suit.setPetIds(petIds);
                suits.add(suit);
            }
            else {
                for (final SuitParam suit : suits) {
                    if (suit.getPetIds().contains(petId)) {
                        continue;
                    }
                    suit.getPetIds().add(petId);
                }
            }
        }
        else {
            if (suits.size() <= 0) {
                return;
            }
            final List<SuitParam> removeList = new ArrayList<SuitParam>();
            for (final SuitParam suit2 : suits) {
                suit2.getPetIds().remove(petId);
                if (suit2.getPetIds() == null || suit2.getPetIds().size() <= 0) {
                    removeList.add(suit2);
                }
            }
            if (removeList.size() > 0) {
                for (final SuitParam suit2 : removeList) {
                    suits.remove(suit2);
                }
            }
        }
    }
    
    public static void toUpdateAssistList(final Role role, final Pet pet, final boolean isAdd) {
        final List<PetSkill> skills = pet.getSkills();
        if (skills.size() > 0) {
            for (final PetSkill petSkill : skills) {
                if (SkillConst.assistSkills.contains(petSkill.getSerial())) {
                    updateAssistList(role, pet.getPetId(), isAdd);
                }
            }
        }
    }
}
