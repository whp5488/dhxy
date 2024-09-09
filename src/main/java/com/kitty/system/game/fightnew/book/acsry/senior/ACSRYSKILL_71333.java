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
    public ACSRYSKILL_71333() {
    }
    
    @Override
    public NutMap executeReduceHp(Fight fight, FightObject effecter, FightObject target, Object... args) {
        if (effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList.contains(Integer.valueOf(71333))) {
                int hurt = 0;
                try {
                    hurt = (int)args[1];
                    if (hurt <= 0) {
                        return null;
                    }
                }
                catch (Exception e) {
                    return null;
                }
                double percent = (double)((int)playerFightObject.getRole().getGg() / 200) * 0.15;
                if (percent > 0.0) {
                    int changeHurt = (int)((double)hurt * percent);
                    if (changeHurt > 0) {
                        target.getStatusMap().setv("1005perpetrator", "71333_" + effecter.getId());
                        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 71333);
                        skillRecord.setT((long)(int)target.getId());
                        return NutMap.NEW().setv("changeHurt", Integer.valueOf(changeHurt));
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public NutMap poisonReduceHp(Fight fight, FightObject target, Object... args) {
        String perpetrator = target.getStatusMap().getString("1005perpetrator", null);
        if (perpetrator != null) {
            String[] splits = perpetrator.split("_");
            int skillSerial = 0;
            FightObject effecter = null;
            int hurt = 0;
            try {
                hurt = (int)args[0];
                skillSerial = Integer.parseInt(splits[0]);
                effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(splits[1])));
            }
            catch (NumberFormatException e) {
                return null;
            }
            if (effecter == null || 71333 != skillSerial) {
                return null;
            }
            if (hurt > 0) {
                double percent = (double)((int)effecter.getRole().getGg() / 200) * 0.15;
                int changeHurt = (int)((double)hurt * percent);
                if (changeHurt > 0) {
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)target.getId(), 71333);
                    skillRecord.setT((long)(int)target.getId());
                    return NutMap.NEW().setv("changeHurt", Integer.valueOf(changeHurt));
                }
            }
        }
        return null;
    }
    
    @Override
    public void statusOver(Fight fight, FightObject fightObject, Object... args) {
        Integer status = null;
        try {
            status = (Integer)args[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (status == null || 1005 != (int)status) {
            return;
        }
        String perpetrator = fightObject.getStatusMap().getString("1005perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(71333))) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 71333);
            skillRecord.setT((long)(int)fightObject.getId());
            fightObject.getStatusMap().unset("1005perpetrator");
        }
        return;
    }
}
