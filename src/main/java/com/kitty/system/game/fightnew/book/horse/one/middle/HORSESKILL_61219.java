
package com.kitty.system.game.fightnew.book.horse.one.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_61219 extends HorseSkillProcessor
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
        if (args != null && args.length >= 3) {
            final RoleSkill skill = (RoleSkill)args[2];
            if (skill != null && skill.getSkillId().equals(81056)) {
                final HorseSkillParam horseSkillParam = effector.getHorseSkillMap().get(61219);
                if (horseSkillParam == null) {
                    return null;
                }
                double percenta = (30.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
                percenta = HorseSkillUtils.getMathRoundPercent(percenta);
                if (percenta == 0.0) {
                    return null;
                }
                final Integer hurt = (Integer)args[0];
                if (hurt == null) {
                    return null;
                }
                final int alterHurt = (int)(hurt * percenta);
                final NutMap map = NutMap.NEW();
                map.setv("alterHurt", (Object)alterHurt);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 61219);
                skillRecord.setT(effector.getId());
                return map;
            }
        }
        return null;
    }
}
