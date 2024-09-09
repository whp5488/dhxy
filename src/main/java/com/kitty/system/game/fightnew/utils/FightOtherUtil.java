package com.kitty.system.game.fightnew.utils;

import java.util.Set;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.enums.war.wardef.LOGIC_SKILL;
import java.util.Iterator;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.pet.domain.Pet;

public class FightOtherUtil
{
    public FightOtherUtil() {
    }
    
    public static List<Integer> addSkillIds(Pet pet) {
        if (pet.getSkills() != null && pet.getSkills().size() >= 0) {
            List<Integer> skills = new ArrayList<>();
            for (PetSkill petSkill : pet.getSkills()) {
                skills.add(Integer.valueOf(petSkill.getSerial()));
            }
            if (skills.size() > 0) {
                return skills;
            }
        }
        return null;
    }
    
    public static int isPlayerSkill(int skillSerial) {
        int returnValue = 0;
        if (skillSerial <= 0) {
            return returnValue;
        }
        int length = (int)(Math.log((double)skillSerial) / Math.log(10.0) + 1.0);
        if (length == 5) {
            int index = skillSerial / 10000;
            if (index == 3) {
                returnValue = LOGIC_SKILL.LOGIC_TYPE_SKILL.getValue();
            }
            else if (index == 8) {
                returnValue = LOGIC_SKILL.LOGIC_TYPE_PET_SKILL.getValue();
            }
        }
        else if (length == 6) {
            returnValue = LOGIC_SKILL.LOGIC_TYPE_PET_SKILL.getValue();
        }
        return returnValue;
    }
    
    public static boolean isHaveStatus(FightObject fightObject, List<Integer> statusList) {
        Set<String> statusStr = fightObject.getStatusMap().keySet();
        for (Integer status : statusList) {
            if (statusStr.contains(String.valueOf(status))) {
                return true;
            }
            continue;
        }
        return false;
    }
}
