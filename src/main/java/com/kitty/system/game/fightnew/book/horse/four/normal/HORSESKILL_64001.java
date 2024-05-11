
package com.kitty.system.game.fightnew.book.horse.four.normal;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_64001 extends HorseSkillProcessor
{
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final PetFightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap studyExcute(final Pet pet, final HorseSkillParam horseSkillParam, final Object... obj) {
        double percenta = this.getPercent(horseSkillParam) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            final NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), (Object)percenta);
            return map;
        }
        return null;
    }
    
    private double getPercent(final HorseSkillParam horseSkillParam) {
        final double percent = (1.0E-4 * Math.pow(horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * horseSkillParam.getLevel()) * (0.6 * horseSkillParam.getLevel() + 0.4 * horseSkillParam.getProficient() / 1000.0), 2.0) + 0.05 * (horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * horseSkillParam.getLevel()) * (0.6 * horseSkillParam.getLevel() + 0.4 * horseSkillParam.getProficient() / 1000.0))) * 0.6 + 0.05;
        return percent;
    }
    
    @Override
    public void attackOver(final Fight fight, final PetFightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (effector.isPet() && effector.getStatusValue(1010) <= 0) {
            final HorseSkillParam horseSkillParam = effector.getHorseSkillMap().get(64001);
            if (horseSkillParam == null) {
                return;
            }
            double percentb = (1.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
            percentb = HorseSkillUtils.getMathRoundPercent(percentb);
            final boolean flag = SkillAi.canAffectOnPro(percentb);
            if (!flag) {
                return;
            }
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 64001);
            final int cnt = (int)Math.floor(2 + horseSkillParam.getLevel() / 80);
            double percentc = this.getPercent(horseSkillParam) / 100.0;
            percentc = HorseSkillUtils.getMathRoundPercent(percentc);
            ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), cnt, percentc);
            effector.getStatusMap().setv(String.valueOf(1010), (Object)cnt);
            final FightObjectParam targetParam = actionDetailParam.gainTargetParam(effector.getId(), false);
            if (targetParam != null) {
                targetParam.getStatusMap().put(1010, cnt);
                skillRecord.setT(effector.getId());
            }
        }
    }
}
