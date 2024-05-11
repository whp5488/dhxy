
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71333 extends AcsrySkillProcessor
{
    @Override
    public NutMap executeReduceHp(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        if (effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList.contains(71333)) {
                int hurt = 0;
                try {
                    hurt = (int)args[1];
                    if (hurt <= 0) {
                        return null;
                    }
                }
                catch (final Exception e) {
                    return null;
                }
                final double percent = playerFightObject.getRole().getGg() / 200 * 0.15;
                if (percent > 0.0) {
                    final int changeHurt = (int)(hurt * percent);
                    if (changeHurt > 0) {
                        target.getStatusMap().setv("1005perpetrator", (Object)("71333_" + effecter.getId()));
                        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 71333);
                        skillRecord.setT(target.getId());
                        return NutMap.NEW().setv("changeHurt", (Object)changeHurt);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public NutMap poisonReduceHp(final Fight fight, final FightObject target, final Object... args) {
        final String perpetrator = target.getStatusMap().getString("1005perpetrator", (String)null);
        if (perpetrator != null) {
            final String[] splits = perpetrator.split("_");
            int skillSerial = 0;
            FightObject effecter = null;
            int hurt = 0;
            try {
                hurt = (int)args[0];
                skillSerial = Integer.parseInt(splits[0]);
                effecter = fight.queryFightObject(Integer.parseInt(splits[1]));
            }
            catch (final NumberFormatException e) {
                return null;
            }
            if (effecter == null || 71333 != skillSerial) {
                return null;
            }
            if (hurt > 0) {
                final double percent = effecter.getRole().getGg() / 200 * 0.15;
                final int changeHurt = (int)(hurt * percent);
                if (changeHurt > 0) {
                    final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, target.getId(), 71333);
                    skillRecord.setT(target.getId());
                    return NutMap.NEW().setv("changeHurt", (Object)changeHurt);
                }
            }
        }
        return null;
    }
    
    @Override
    public void statusOver(final Fight fight, final FightObject fightObject, final Object... args) {
        Integer status = null;
        try {
            status = (Integer)args[0];
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (status == null || 1005 != status) {
            return;
        }
        final String perpetrator = fightObject.getStatusMap().getString("1005perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(71333))) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 71333);
            skillRecord.setT(fightObject.getId());
            fightObject.getStatusMap().unset("1005perpetrator");
        }
    }
}
