package com.kitty.system.game.fightnew.book.acsry.senior;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_76203 extends AcsrySkillProcessor
{
    public ACSRYSKILL_76203() {
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        RoleSkill skill = null;
        Boolean isHit = Boolean.valueOf(false);
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
        }
        catch (Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || !(boolean)isHit) {
            return;
        }
        int statusValue = target.getStatusValue(1022);
        if (SKILLATTR.SKILLATTR_ZHIYU.getValue() == (int)skill.getType() && statusValue > 0 && effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(76203))) {
                target.setStatus(8010, statusValue);
                double percent = 0.1;
                percent += (double)((int)playerFightObject.getRole().getGg() / 100) * 0.01;
                target.getStatusMap().setv("8010value", Double.valueOf(percent));
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 76203);
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return;
    }
    
    @Override
    public NutMap countRecoverBloodEnd(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.getStatusValue(1022) > 0 && fightObject.getStatusValue(8010) > 0) {
            double percent = fightObject.getStatusMap().getDouble("8010value", 0.0);
            if (percent > 0.0) {
                int addMp = (int)((double)fightObject.getMaxMana() * percent);
                if (fightObject.getCurrMana() + addMp > fightObject.getMaxMana()) {
                    addMp = fightObject.getMaxMana() - fightObject.getCurrMana();
                }
                if (addMp > 0) {
                    fightObject.setCurrMana(fightObject.getCurrMana() + addMp);
                    fightObject.getTempCache().put("recoverManaValue", Integer.valueOf(addMp));
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 76203);
                    skillRecord.setT((long)(int)fightObject.getId());
                }
            }
        }
        return null;
    }
}
