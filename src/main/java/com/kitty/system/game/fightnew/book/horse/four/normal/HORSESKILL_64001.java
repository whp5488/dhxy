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
    public HORSESKILL_64001() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
        double percenta = this.getPercent(horseSkillParam) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
    
    private double getPercent(HorseSkillParam horseSkillParam) {
        double percent = (1.0E-4 * Math.pow((double)horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * (double)horseSkillParam.getLevel()) * (0.6 * (double)horseSkillParam.getLevel() + 0.4 * (double)horseSkillParam.getProficient() / 1000.0), 2.0) + 0.05 * ((double)horseSkillParam.getGrowUp() / 1000.0 / (1.175 + 0.0013 * (double)horseSkillParam.getLevel()) * (0.6 * (double)horseSkillParam.getLevel() + 0.4 * (double)horseSkillParam.getProficient() / 1000.0))) * 0.6 + 0.05;
        return percent;
    }
    
    @Override
    public void attackOver(Fight fight, PetFightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        if (effector.isPet() && effector.getStatusValue(1010) <= 0) {
            HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(64001));
            if (horseSkillParam == null) {
                return;
            }
            double percentb = (1.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
            percentb = HorseSkillUtils.getMathRoundPercent(percentb);
            boolean flag = SkillAi.canAffectOnPro(percentb);
            if (!flag) {
                return;
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 64001);
            int cnt = (int)Math.floor((double)(2 + horseSkillParam.getLevel() / 80));
            double percentc = this.getPercent(horseSkillParam) / 100.0;
            percentc = HorseSkillUtils.getMathRoundPercent(percentc);
            ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), cnt, percentc);
            effector.getStatusMap().setv(String.valueOf(1010), Integer.valueOf(cnt));
            FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)effector.getId(), false);
            if (targetParam != null) {
                targetParam.getStatusMap().put(Integer.valueOf(1010), Integer.valueOf(cnt));
                skillRecord.setT((long)(int)effector.getId());
            }
        }
    }
}
