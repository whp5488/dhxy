
package com.kitty.system.game.pet.handler;

import com.kitty.system.game.pet.domain.vo.PetCorrection;
import java.util.Map;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.util.Comparator;
import com.kitty.system.init.domain.SysPetExercise;
import com.kitty.system.init.pool.PartDataPool;
import java.text.MessageFormat;
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
    
    @Override
    public String acceptFeed(final Pet pet, final Role role, final Integer param, final String type, final int count, final Object... args) {
        if (role.getLevel() < 60) {
            return MessageFormat.format("你的等级还不足{0}级，还不能给召唤兽修炼", 60);
        }
        final Map<Integer, SysPetExercise> exerciseMap = PartDataPool.petExerciseMap;
        final SysPetExercise maxExercise = exerciseMap.values().stream().max(Comparator.comparingInt(SysPetExercise::getSoul)).orElse(null);
        if (maxExercise == null) {
            return null;
        }
        String msg = "";
        final PetCorrection correctionInfo = pet.getCorrectionInfo();
        if (correctionInfo.getLevel() >= maxExercise.getLevel()) {
            return null;
        }
        SysPetExercise exercise = exerciseMap.get(correctionInfo.getLevel());
        if (exercise != null) {
            int needSoulExp = exercise.getExp();
            final boolean check = this.check(pet, exercise.getRelive(), exercise.getLevel());
            if (!check && correctionInfo.getExp() >= 1500000000L) {
                return "该召唤兽修炼已满";
            }
            if (param == 1) {
                if (check) {
                    final long needSoul = needSoulExp - correctionInfo.getExp();
                    if (pet.getExp() < 1000L) {
                        return "该召唤兽经验不足";
                    }
                    long remnant = 0L;
                    long consume = 0L;
                    long soul = 0L;
                    final long needPetExp = needSoul * 1000L;
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
                    msg = MessageFormat.format("本次消耗{0}召唤兽经验兑换了{1}魂魄", consume, soul);
                    this.petService.sendPetCorrection(role, pet);
                    pet.save();
                }
            }
            else {
                final Integer num = SpringUtils.getBean(BagService.class).consumeBagGoods(role, 75112, 1, true, true, "宠物修炼");
                if (num == null) {
                    long totalSoulExp = correctionInfo.getExp() + 50000L;
                    if (totalSoulExp < needSoulExp || !check) {
                        correctionInfo.setExp(totalSoulExp);
                        msg = MessageFormat.format("你的{0}获得了{1}魂魄", pet.getPetName(), 50000);
                        this.petService.sendPetCorrection(role, pet);
                    }
                    else {
                        while (totalSoulExp >= needSoulExp) {
                            totalSoulExp -= needSoulExp;
                            correctionInfo.setLevel(correctionInfo.getLevel() + 1);
                            correctionInfo.setPoints(correctionInfo.getPoints() + 1);
                            correctionInfo.setExp(totalSoulExp);
                            this.petService.sendPetCorrection(role, pet);
                            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("恭喜，你的#<Y>{0}#修炼等级提升了", pet.getPetName()), false));
                            exercise = exerciseMap.get(correctionInfo.getLevel());
                            if (exercise == null) {
                                msg = "该召唤兽修炼已满";
                                break;
                            }
                            needSoulExp = exercise.getExp();
                            if (!this.check(pet, exercise.getRelive(), exercise.getLevel())) {
                                msg = MessageFormat.format("你的{0}获得了{1}魂魄", pet.getPetName(), totalSoulExp);
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
    
    public boolean check(final Pet pet, final int relive, final int level) {
        final int need = relive * 1000 + level;
        final int curve = pet.getRebirth() * 1000 + pet.getPetLv();
        return curve >= need;
    }
}
