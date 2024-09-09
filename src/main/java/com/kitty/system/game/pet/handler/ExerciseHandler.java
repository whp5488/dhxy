package com.kitty.system.game.pet.handler;

import com.kitty.system.game.pet.domain.vo.PetCorrection;
import java.util.Map;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.text.MessageFormat;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import java.util.Comparator;
import com.kitty.system.init.domain.SysPetExercise;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("ExerciseHandler")
public class ExerciseHandler extends FeedPetHandler
{
    private static final int SOUL = 50000;
    private static final int RATE = 1000;
    private static final int LIMIT_LEVEL = 60;
    private static final int GOODS_SERIAL = 75112;
    private static final String LIMIT = "你的等级还不足{0}级，还不能给召唤兽修炼";
    private static final String SUCCESS = "恭喜，你的#<Y>{0}#修炼等级提升了";
    private static final String PITY = "本次消耗{0}召唤兽经验兑换了{1}魂魄";
    private static final String TIPS = "你的{0}获得了{1}魂魄";
    private static final String EXP_LACK = "该召唤兽经验不足";
    private static final String SUPER = "该召唤兽修炼已满";
    
    public ExerciseHandler() {
    }
    
    @Override
    public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
        if ((int)role.getLevel() < 60) {
            return MessageFormat.format("你的等级还不足{0}级，还不能给召唤兽修炼", Integer.valueOf(60));
        }
        Map<Integer, SysPetExercise> exerciseMap = PartDataPool.petExerciseMap;
        SysPetExercise maxExercise = (SysPetExercise)exerciseMap.values().stream().max(Comparator.comparingInt(SysPetExercise::getSoul)).orElse(null);
        if (maxExercise == null) {
            return null;
        }
        String msg = "";
        PetCorrection correctionInfo = pet.getCorrectionInfo();
        if (correctionInfo.getLevel() >= maxExercise.getLevel()) {
            return null;
        }
        SysPetExercise exercise = (SysPetExercise)exerciseMap.get(Integer.valueOf(correctionInfo.getLevel()));
        if (exercise != null) {
            int needSoulExp = exercise.getExp();
            boolean check = this.check(pet, exercise.getRelive(), exercise.getLevel());
            if (!check && correctionInfo.getExp() >= 1500000000L) {
                return "该召唤兽修炼已满";
            }
            if ((int)param == 1) {
                if (check) {
                    long needSoul = (long)needSoulExp - correctionInfo.getExp();
                    if (pet.getExp() >= 1000L) {
                        long remnant = 0L;
                        long consume = 0L;
                        long soul = 0L;
                        long needPetExp = needSoul * 1000L;
                        if (pet.getExp() > needPetExp) {
                            consume = (remnant = pet.getExp() - needPetExp);
                            soul = needSoul;
                            correctionInfo.setExp(0L);
                            correctionInfo.setLevel(correctionInfo.getLevel() + 1);
                            correctionInfo.setPoints(correctionInfo.getPoints() + 1);
                            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("恭喜，你的#<Y>{0}#修炼等级提升了", pet.getPetName()), false));
                        }
                        else {
                            remnant = pet.getExp() % 1000L;
                            consume = pet.getExp() - remnant;
                            soul = consume / 1000L;
                            correctionInfo.setExp(soul);
                        }
                        pet.setExp(remnant);
                        msg = MessageFormat.format("本次消耗{0}召唤兽经验兑换了{1}魂魄", Long.valueOf(consume), Long.valueOf(soul));
                        this.petService.sendPetCorrection(role, pet);
                        pet.save();
                    }
                    else {
                        return "该召唤兽经验不足";
                    }
                }
            }
            else {
                Integer num = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, 75112, 1, true, true, "宠物修炼");
                if (num == null) {
                    long totalSoulExp = correctionInfo.getExp() + 50000L;
                    if (totalSoulExp < (long)needSoulExp || !check) {
                        correctionInfo.setExp(totalSoulExp);
                        msg = MessageFormat.format("你的{0}获得了{1}魂魄", pet.getPetName(), Integer.valueOf(50000));
                        this.petService.sendPetCorrection(role, pet);
                    }
                    else {
                        while (totalSoulExp >= (long)needSoulExp) {
                            totalSoulExp -= (long)needSoulExp;
                            correctionInfo.setLevel(correctionInfo.getLevel() + 1);
                            correctionInfo.setPoints(correctionInfo.getPoints() + 1);
                            correctionInfo.setExp(totalSoulExp);
                            this.petService.sendPetCorrection(role, pet);
                            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("恭喜，你的#<Y>{0}#修炼等级提升了", pet.getPetName()), false));
                            exercise = (SysPetExercise)exerciseMap.get(Integer.valueOf(correctionInfo.getLevel()));
                            if (exercise != null) {
                                needSoulExp = exercise.getExp();
                                if (this.check(pet, exercise.getRelive(), exercise.getLevel())) {
                                    continue;
                                }
                                msg = MessageFormat.format("你的{0}获得了{1}魂魄", pet.getPetName(), Long.valueOf(totalSoulExp));
                                break;
                            }
                            else {
                                msg = "该召唤兽修炼已满";
                                break;
                            }
                        }
                        pet.save();
                    }
                }
                else {
                    msg = "凝魂丹不足";
                }
            }
        }
        return msg;
    }
    
    public boolean check(Pet pet, int relive, int level) {
        int need = relive * 1000 + level;
        int curve = (int)pet.getRebirth() * 1000 + (int)pet.getPetLv();
        return curve >= need;
    }
}
