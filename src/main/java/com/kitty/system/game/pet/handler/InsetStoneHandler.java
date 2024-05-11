
package com.kitty.system.game.pet.handler;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import com.kitty.system.game.role.utils.RoleUtils;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.ArrayList;
import java.text.MessageFormat;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("InsetStoneHandler")
public class InsetStoneHandler extends FeedPetHandler
{
    private static final String SUCCESS = "恭喜，你的#<Y>{0}#学会了{1}";
    private static final String PITY = "{0}（但{1}被顶替了）";
    private static final String GRID_LACK = "你的#<Y>{0}#由于技能栏不足，无法获得新的技能";
    private static final String EXIST = "#<G>{0}#已经学会{1}技能了";
    
    @Override
    public String acceptFeed(final Pet pet, final Role role, final Integer param, final String type, final int count, final Object... args) {
        int pos = -1;
        int hit = 0;
        try {
            final SysOtherItem otherItem = ItemDataPool.data_other_item.get(param);
            if (otherItem == null) {
                return "该技能书无法对此宠物使用";
            }
            final int serial = otherItem.getValue();
            final Integer subType = SkillFunc.getPetSkillSubType(serial);
            final Map<Integer, SysPetSkill> petSkillMap = SkillDataPool.petSkillMap;
            final SysPetSkill skill = petSkillMap.get(serial);
            if (skill != null) {
                final Optional<PetSkill> any = pet.getSkills().stream().filter(s -> skill.getSerial().equals(s.getSerial())).findAny();
                if (any.isPresent()) {
                    return MessageFormat.format("#<G>{0}#已经学会{1}技能了", pet.getPetName(), skill.getName());
                }
                final List<Integer> unset = new ArrayList<Integer>();
                final List<Integer> normal = new ArrayList<Integer>();
                final List<Integer> senior = new ArrayList<Integer>();
                for (int i = 0; i < pet.getSkills().size(); ++i) {
                    final PetSkill petSkill = pet.getSkills().get(i);
                    if (petSkill.getType() != 1) {
                        if (petSkill.getSerial() == 0 && petSkill.getType() != PETSKILLSUB.SUBTYPE_SUPREME.getValue() && petSkill.getType() != PETSKILLSUB.SUBTYPE_BORN.getValue()) {
                            unset.add(i);
                        }
                        else if (petSkill.getSerial() >= 0 && petSkill.getType() != PETSKILLSUB.SUBTYPE_SUPREME.getValue() && petSkill.getType() != PETSKILLSUB.SUBTYPE_BORN.getValue()) {
                            final int current = SkillFunc.getPetSkillSubType(petSkill.getSerial());
                            if (current == PETSKILLSUB.SUBTYPE_NORMAL.getValue()) {
                                normal.add(i);
                            }
                            else if (current == PETSKILLSUB.SUBTYPE_SENIOR.getValue() || current == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                                senior.add(i);
                            }
                        }
                    }
                }
                if (subType == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                    if (normal.size() > 0 && senior.size() > 0) {
                        hit = RandomUtil.round(1, 10000);
                    }
                    else if (normal.size() > 0) {
                        hit = RandomUtil.round(1, 100);
                    }
                    else if (senior.size() > 0) {
                        hit = RandomUtil.round(1, 10000);
                    }
                    else if (unset.size() <= 0) {
                        return MessageFormat.format("你的#<Y>{0}#由于技能栏不足，无法获得新的技能", pet.getPetName());
                    }
                    if (senior.size() > 0 && hit > 0 && hit < 1500) {
                        pos = senior.get(RandomUtil.round(0, senior.size() - 1));
                    }
                    else if (normal.size() > 0 && hit > 50) {
                        pos = normal.get(RandomUtil.round(0, normal.size() - 1));
                    }
                }
                else if (subType == PETSKILLSUB.SUBTYPE_SENIOR.getValue() || subType == PETSKILLSUB.SUBTYPE_NORMAL.getValue()) {
                    final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
                    final int limit = (sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) ? 2 : 1;
                    if (senior.size() > limit) {
                        final Integer[] dct = ConvertDataPool.petSkillReplace.get(senior.get(RandomUtil.round(0, senior.size() - 1)));
                        final int choose = choose(dct);
                        if (choose == 0 && senior.size() > 0) {
                            pos = senior.get(RandomUtil.round(0, senior.size() - 1));
                        }
                        else if (normal.size() > 0) {
                            pos = normal.get(RandomUtil.round(0, normal.size() - 1));
                        }
                        else if (unset.size() <= 0 && senior.size() > 0) {
                            pos = senior.get(RandomUtil.round(0, senior.size() - 1));
                        }
                    }
                    else {
                        if (unset.size() <= 0 && normal.size() <= 0) {
                            return MessageFormat.format("你的#<Y>{0}#由于技能栏不足，无法获得新的技能", pet.getPetName());
                        }
                        if (normal.size() > 0) {
                            pos = normal.get(RandomUtil.round(0, normal.size() - 1));
                        }
                    }
                }
                boolean flag = true;
                if (pos < 0 && unset.size() > 0) {
                    pos = unset.get(0);
                    flag = false;
                }
                if (pos < 0 && flag) {
                    if (senior.size() <= 0 && normal.size() <= 0) {
                        this.logger.error("技能格子不足");
                        return null;
                    }
                    final int round = RandomUtil.round(1, 100);
                    if (round < 50) {
                        if (senior.size() > 0) {
                            pos = senior.get(RandomUtil.round(0, senior.size() - 1));
                        }
                        else if (normal.size() > 0) {
                            pos = normal.get(RandomUtil.round(0, normal.size() - 1));
                        }
                    }
                    else if (normal.size() > 0) {
                        pos = normal.get(RandomUtil.round(0, normal.size() - 1));
                    }
                    else if (senior.size() > 0) {
                        pos = senior.get(RandomUtil.round(0, senior.size() - 1));
                    }
                }
                final int way = Integer.valueOf(String.valueOf(args[0]));
                if (way == 3) {
                    final Integer num = SpringUtils.getBean(BagService.class).consumeBagGoods(role, param, 1, true, true, "宠物学习技能");
                    if (num == null) {
                        String msg = MessageFormat.format("恭喜，你的#<Y>{0}#学会了{1}", pet.getPetName(), skill.getName());
                        final PetSkill petSkill2 = pet.getSkills().get(pos);
                        if (flag) {
                            msg = MessageFormat.format("{0}（但{1}被顶替了）", msg, petSkillMap.get(petSkill2.getSerial()).getName());
                            if (SkillConst.assistSkills.contains(petSkill2.getSerial())) {
                                RoleUtils.removeAssistList(role, pet, petSkill2.getSerial());
                            }
                        }
                        petSkill2.setSerial(skill.getSerial());
                        this.petService.calculateBase(role.getId(), pet);
                        this.petService.getArenaScore(role.getId(), pet);
                        this.petService.loadPet(role, pet);
                        pet.save();
                        if (SkillConst.assistSkills.contains(skill.getSerial())) {
                            RoleUtils.updateAssistList(role, pet.getPetId(), true);
                        }
                        role.save();
                        return msg;
                    }
                }
            }
            return "该技能书无法对此宠物使用";
        }
        catch (final Exception e) {
            this.logger.error("宠物学习技能错误宠物信息：{}，学习技能 = {}，hit = {}，pos = {}", new Object[] { pet, param, hit, pos });
            e.printStackTrace();
            return "宠物学习技能失败";
        }
    }
    
    public static int choose(final Integer[] dct) {
        int sum = 0;
        for (final int ratio : dct) {
            sum += ratio;
        }
        final int hit = RandomUtil.round(1, sum);
        int limit = 0;
        for (int i = 0; i < dct.length; ++i) {
            limit += dct[i];
            if (hit <= limit) {
                return i;
            }
        }
        return 1;
    }
}
