package com.kitty.system.game.pet.handler;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import com.kitty.system.game.role.utils.RoleUtils;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.init.pool.ConvertDataPool;
import java.text.MessageFormat;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.ArrayList;
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
    
    public InsetStoneHandler() {
    }
    
    @Override
    public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
        int pos = -1;
        int hit = 0;
        try {
            SysOtherItem otherItem = (SysOtherItem)ItemDataPool.data_other_item.get(param);
            if (otherItem == null) {
                return "该技能书无法对此宠物使用";
            }
            int serial = (int)otherItem.getValue();
            Integer subType = SkillFunc.getPetSkillSubType(Integer.valueOf(serial));
            Map<Integer, SysPetSkill> petSkillMap = SkillDataPool.petSkillMap;
            SysPetSkill skill = (SysPetSkill)petSkillMap.get(Integer.valueOf(serial));
            if (skill != null) {
                Optional<PetSkill> any = pet.getSkills().stream().filter(s -> skill.getSerial().equals(Integer.valueOf(s.getSerial()))).findAny();
                if (any.isPresent()) {
                    return MessageFormat.format("#<G>{0}#已经学会{1}技能了", pet.getPetName(), skill.getName());
                }
                List<Integer> unset = new ArrayList<>();
                List<Integer> normal = new ArrayList<>();
                List<Integer> senior = new ArrayList<>();
                for (int i = 0; i < pet.getSkills().size(); ++i) {
                    PetSkill petSkill = (PetSkill)pet.getSkills().get(i);
                    if (petSkill.getType() != 1) {
                        if (petSkill.getSerial() == 0 && petSkill.getType() != PETSKILLSUB.SUBTYPE_SUPREME.getValue() && petSkill.getType() != PETSKILLSUB.SUBTYPE_BORN.getValue()) {
                            unset.add(Integer.valueOf(i));
                        }
                        else if (petSkill.getSerial() >= 0 && petSkill.getType() != PETSKILLSUB.SUBTYPE_SUPREME.getValue() && petSkill.getType() != PETSKILLSUB.SUBTYPE_BORN.getValue()) {
                            int current = (int)SkillFunc.getPetSkillSubType(Integer.valueOf(petSkill.getSerial()));
                            if (current == PETSKILLSUB.SUBTYPE_NORMAL.getValue()) {
                                normal.add(Integer.valueOf(i));
                            }
                            else if (current == PETSKILLSUB.SUBTYPE_SENIOR.getValue() || current == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                                senior.add(Integer.valueOf(i));
                            }
                        }
                    }
                }
                if ((int)subType == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                    if (normal.size() > 0 && senior.size() > 0) {
                        hit = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(10000));
                    }
                    else if (normal.size() > 0) {
                        hit = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(100));
                    }
                    else if (senior.size() > 0) {
                        hit = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(10000));
                    }
                    else if (unset.size() <= 0) {
                        return MessageFormat.format("你的#<Y>{0}#由于技能栏不足，无法获得新的技能", pet.getPetName());
                    }
                    if (senior.size() > 0 && hit > 0 && hit < 1500) {
                        pos = (int)senior.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(senior.size() - 1)));
                    }
                    else if (normal.size() > 0 && hit > 50) {
                        pos = (int)normal.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(normal.size() - 1)));
                    }
                }
                else if ((int)subType == PETSKILLSUB.SUBTYPE_SENIOR.getValue() || (int)subType == PETSKILLSUB.SUBTYPE_NORMAL.getValue()) {
                    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
                    int limit = ((int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || (int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) ? 2 : 1;
                    if (senior.size() > limit) {
                        Integer[] dct = (Integer[])ConvertDataPool.petSkillReplace.get(senior.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(senior.size() - 1))));
                        int choose = choose(dct);
                        if (choose == 0 && senior.size() > 0) {
                            pos = (int)senior.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(senior.size() - 1)));
                        }
                        else if (normal.size() > 0) {
                            pos = (int)normal.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(normal.size() - 1)));
                        }
                        else if (unset.size() <= 0 && senior.size() > 0) {
                            pos = (int)senior.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(senior.size() - 1)));
                        }
                    }
                    else {
                        if (unset.size() <= 0 && normal.size() <= 0) {
                            return MessageFormat.format("你的#<Y>{0}#由于技能栏不足，无法获得新的技能", pet.getPetName());
                        }
                        if (normal.size() > 0) {
                            pos = (int)normal.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(normal.size() - 1)));
                        }
                    }
                }
                boolean flag = true;
                if (pos < 0 && unset.size() > 0) {
                    pos = (int)unset.get(0);
                    flag = false;
                }
                if (pos < 0 && flag) {
                    if (senior.size() <= 0 && normal.size() <= 0) {
                        this.logger.error("技能格子不足");
                        return null;
                    }
                    int round = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(100));
                    if (round < 50) {
                        if (senior.size() > 0) {
                            pos = (int)senior.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(senior.size() - 1)));
                        }
                        else if (normal.size() > 0) {
                            pos = (int)normal.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(normal.size() - 1)));
                        }
                    }
                    else if (normal.size() > 0) {
                        pos = (int)normal.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(normal.size() - 1)));
                    }
                    else if (senior.size() > 0) {
                        pos = (int)senior.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(senior.size() - 1)));
                    }
                }
                int way = (int)Integer.valueOf(String.valueOf(args[0]));
                if (way == 3) {
                    Integer num = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, (int)param, 1, true, true, "宠物学习技能");
                    if (num == null) {
                        String msg = MessageFormat.format("恭喜，你的#<Y>{0}#学会了{1}", pet.getPetName(), skill.getName());
                        PetSkill petSkill2 = (PetSkill)pet.getSkills().get(pos);
                        if (flag) {
                            msg = MessageFormat.format("{0}（但{1}被顶替了）", msg, ((SysPetSkill)petSkillMap.get(Integer.valueOf(petSkill2.getSerial()))).getName());
                            if (SkillConst.assistSkills.contains(Integer.valueOf(petSkill2.getSerial()))) {
                                RoleUtils.removeAssistList(role, pet, Integer.valueOf(petSkill2.getSerial()));
                            }
                        }
                        petSkill2.setSerial((int)skill.getSerial());
                        this.petService.calculateBase((long)role.getId(), pet);
                        this.petService.getArenaScore((long)role.getId(), pet);
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
        catch (Exception e) {
            this.logger.error("宠物学习技能错误宠物信息：{}，学习技能 = {}，hit = {}，pos = {}", new Object[] { pet, param, Integer.valueOf(hit), Integer.valueOf(pos) });
            e.printStackTrace();
            return "宠物学习技能失败";
        }
    }
    
    public static int choose(Integer[] dct) {
        int sum = 0;
        for (int length = dct.length, j = 0; j < length; ++j) {
            int ratio = (int)dct[j];
            sum += ratio;
        }
        int hit = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(sum));
        int limit = 0;
        int i = 0;
        while (i < dct.length) {
            limit += (int)dct[i];
            if (hit <= limit) {
                return i;
            }
            ++i;
        }
        return 1;
    }
}
