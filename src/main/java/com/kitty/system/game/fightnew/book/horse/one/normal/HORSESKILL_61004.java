
package com.kitty.system.game.fightnew.book.horse.one.normal;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61004 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final PetFightObject effector, final FightObject target, final Object... args) {
        if (effector.getType() != 0) {
            return null;
        }
        final List<FightObject> mySide = fight.getMySide(effector.getId()).stream().filter(obj -> obj.getIcon() == effector.getIcon() && obj.isPet() && !obj.getId().equals(effector.getId())).collect(Collectors.toList());
        if (mySide != null && mySide.size() > 0) {
            final Integer hurt = (Integer)args[0];
            if (hurt == null || hurt <= 0) {
                return null;
            }
            final HorseSkillParam horseSkillParam = effector.getHorseSkillMap().get(61004);
            if (horseSkillParam == null) {
                return null;
            }
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 61004);
            final double percent = HorseSkillUtils.getPercent(horseSkillParam);
            double percenta = (10.0 * percent + 0.1) / 100.0;
            percenta = HorseSkillUtils.getMathRoundPercent(percenta);
            final int alterHurt = (int)(hurt * (percenta * mySide.size()));
            if (alterHurt != 0) {
                final NutMap map = NutMap.NEW();
                map.setv("alterHurt", (Object)alterHurt);
                skillRecord.setT(effector.getId());
                return map;
            }
        }
        return null;
    }
}
