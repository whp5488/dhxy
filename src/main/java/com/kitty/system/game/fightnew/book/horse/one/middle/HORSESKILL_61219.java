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
    public HORSESKILL_61219() {
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
        if (args != null && args.length >= 3) {
            RoleSkill skill = (RoleSkill)args[2];
            if (skill != null && skill.getSkillId().equals(Integer.valueOf(81056))) {
                HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(61219));
                if (horseSkillParam == null) {
                    return null;
                }
                double percenta = (30.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
                percenta = HorseSkillUtils.getMathRoundPercent(percenta);
                if (percenta == 0.0) {
                    return null;
                }
                Integer hurt = (Integer)args[0];
                if (hurt == null) {
                    return null;
                }
                int alterHurt = (int)((double)(int)hurt * percenta);
                NutMap map = NutMap.NEW();
                map.setv("alterHurt", Integer.valueOf(alterHurt));
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 61219);
                skillRecord.setT((long)(int)effector.getId());
                return map;
            }
        }
        return null;
    }
}
