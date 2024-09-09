package com.kitty.system.game.fightnew.book.horse.one.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61104 extends HorseSkillProcessor
{
    public HORSESKILL_61104() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
        if (effector.getType() != 0) {
            return null;
        }
        List<FightObject> mySide = (List)fight.getMySide((int)effector.getId()).stream().filter(obj -> obj.getIcon() == effector.getIcon() && obj.isPet() && !obj.getId().equals(effector.getId())).collect(Collectors.toList());
        if (mySide != null && mySide.size() > 0) {
            Integer hurt = (Integer)args[0];
            if (hurt == null || (int)hurt <= 0) {
                return null;
            }
            HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(61104));
            if (horseSkillParam == null) {
                return null;
            }
            double percent = HorseSkillUtils.getPercentSenior(horseSkillParam);
            double percenta = (10.0 * percent + 0.2) / 100.0;
            percenta = HorseSkillUtils.getMathRoundPercent(percenta);
            int alterHurt = (int)((double)(int)hurt * (percenta * (double)mySide.size()));
            if (alterHurt != 0) {
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 61104);
                skillRecord.setT((long)(int)effector.getId());
                NutMap map = NutMap.NEW();
                map.setv("alterHurt", Integer.valueOf(alterHurt));
                return map;
            }
        }
        return null;
    }
}
