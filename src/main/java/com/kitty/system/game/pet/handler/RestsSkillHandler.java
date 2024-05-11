
package com.kitty.system.game.pet.handler;

import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.role.utils.RoleUtils;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import java.text.MessageFormat;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("RestsSkillHandler")
public class RestsSkillHandler extends FeedPetHandler
{
    private static final int FORGET = 150000;
    private static final int RESTORE = 300;
    private static final int LOCK = 500;
    private static final String SUCCESS_LOCK = "锁定成功";
    private static final String SUCCESS_FORGET = "你成功遗忘了{0}技能";
    private static final String SUCCESS_RESTORE = "还原成功";
    
    @Override
    public String acceptFeed(final Pet pet, final Role role, final Integer param, final String type, final int count, final Object... args) {
        String msg = null;
        final int pos = param / 1000 - 1;
        final int genre = param % 1000;
        final RoleService roleService = SpringUtils.getRoleService();
        PetSkill skill = pet.getSkills().get(pos);
        if (skill == null && genre < 3) {
            this.logger.error("获取技能位置失败：宠物名称{}，技能数据 = {}", (Object)pet.getPetName(), (Object)JsonUtils.object2String(pet.getSkills()));
            return null;
        }
        final SysPetSkill petSkill = SkillDataPool.petSkillMap.get(skill.getSerial());
        if (petSkill == null && genre < 3) {
            this.logger.error("获取技能数据失败：宠物名称{}，技能编号 = {}", (Object)pet.getPetName(), (Object)skill.getSerial());
            return null;
        }
        if (genre == 1) {
            if (skill.getType() == PETSKILLSUB.SUBTYPE_SPECIAL.getValue()) {
                return "神兽技能不可以遗忘";
            }
            if (skill.getType() == PETSKILLSUB.SUB_TYPE_SOUL.getValue()) {
                return "灵兽技能不可以遗忘";
            }
            final boolean flag = roleService.deductCurrency(role, Const.RESTYPE_SILVER, 150000L, " 宠物遗忘");
            if (flag) {
                if (skill.getType() == PETSKILLSUB.SUBTYPE_SPECIAL.getValue()) {
                    return "神兽技能不可以遗忘";
                }
                skill.setSerial(0);
                skill.setType(0);
                msg = MessageFormat.format("你成功遗忘了{0}技能", petSkill.getName());
                if (SkillConst.assistSkills.contains(petSkill.getSerial()) && SkillConst.assistSkills.contains(petSkill.getSerial())) {
                    RoleUtils.removeAssistList(role, pet, petSkill.getSerial());
                    role.save();
                }
            }
        }
        else if (genre == 2) {
            if (skill.getType() == PETSKILLSUB.SUBTYPE_SPECIAL.getValue()) {
                return "神兽技能不可以锁定";
            }
            if (skill.getType() == PETSKILLSUB.SUB_TYPE_SOUL.getValue()) {
                return "灵兽技能不可以锁定";
            }
            if (skill.getType() == PETSKILLSUB.SUBTYPE_BORN.getValue()) {
                return "天生技能不可以锁定";
            }
            final boolean flag = roleService.deductCurrency(role, Const.RESTYPE_GOLD, 500L, "锁定技能");
            if (flag) {
                final List<PetSkill> lock = pet.getSkills().stream().filter(s -> s.getType() == 1).collect(Collectors.toList());
                final List<PetSkill> collect = pet.getSkills().stream().filter(s -> s.getSerial() >= 0 && s.getType() != PETSKILLSUB.SUB_TYPE_SOUL.getValue() && s.getType() != PETSKILLSUB.SUBTYPE_SPECIAL.getValue()).collect(Collectors.toList());
                if (collect.size() < 8 && lock.size() > 0) {
                    lock.get(0).setType(0);
                }
                if (lock.size() >= 2) {
                    lock.get(0).setType(0);
                }
                skill.setType(1);
                msg = "锁定成功";
            }
        }
        else if (genre == 3) {
            final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
            if (sysPet.getSkills().size() <= 0) {
                return "该宠物没有天生技能";
            }
            final boolean flag2 = roleService.deductCurrency(role, Const.RESTYPE_GOLD, 300L, "还原技能");
            if (flag2) {
                skill = pet.getSkills().get(0);
                skill.setSerial(sysPet.getSkills().get(0));
                skill.setType(PETSKILLSUB.SUBTYPE_BORN.getValue());
                msg = "还原成功";
            }
        }
        this.petService.getArenaScore(role.getId(), pet);
        this.petService.loadPet(role, pet);
        pet.save();
        role.save();
        return msg;
    }
}
