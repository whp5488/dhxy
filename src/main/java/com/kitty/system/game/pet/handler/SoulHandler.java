package com.kitty.system.game.pet.handler;

import java.util.Arrays;
import java.text.MessageFormat;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.ArrayList;
import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("SoulHandler")
public class SoulHandler extends FeedPetHandler
{
    private static final String SUCCESS = "恭喜，你的召唤兽#<Y>{0}#学会了{1}";
    private static final Integer SILVER;
    private static final List<Integer> SOUL;
    
    public SoulHandler() {
    }
    
    @Override
    public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
        if (sysPet == null) {
            return null;
        }
        if ((int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() || (int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
            if (!SoulHandler.SOUL.contains(param)) {
                return null;
            }
            List<Integer> unset = new ArrayList<>();
            for (int i = 0; i < pet.getSkills().size(); ++i) {
                PetSkill petSkill = (PetSkill)pet.getSkills().get(i);
                if (petSkill.getSerial() >= 0) {
                    unset.add(Integer.valueOf(i));
                }
            }
            if (unset.size() < 5) {
                return "召唤兽解封第5技能格才可以学习哦";
            }
            SysPetSkill skill = (SysPetSkill)SkillDataPool.petSkillMap.get(param);
            if (skill != null) {
                boolean flag = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, (long)(int)SoulHandler.SILVER, "SoulHandler");
                if (flag) {
                    pet.getSkills().forEach(e -> {
                        if (e.getType() == PETSKILLSUB.SUB_TYPE_SOUL.getValue()) {
                            e.setSerial((int)param);
                            return;
                        }
                        return;
                    });
                }
                this.petService.getArenaScore((long)role.getId(), pet);
                this.petService.loadPet(role, pet);
                pet.save();
                return MessageFormat.format("恭喜，你的召唤兽#<Y>{0}#学会了{1}", pet.getPetName(), skill.getName());
            }
        }
        return null;
    }
    
    static {
        SILVER = Integer.valueOf(3000000);
        SOUL = Arrays.asList(Integer.valueOf(81055), Integer.valueOf(81007), Integer.valueOf(81006));
    }
}
