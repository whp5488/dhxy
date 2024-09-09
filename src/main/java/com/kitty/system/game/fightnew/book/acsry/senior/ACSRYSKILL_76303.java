package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_76303 extends AcsrySkillProcessor
{
    public ACSRYSKILL_76303() {
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
        int status = this.getStatus(skill);
        if ((1020 == status || 1021 == status) && effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(76303))) {
                target.getStatusMap().setv(status + "perpetrator", "76303_" + effecter.getId());
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 76303);
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return;
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
        if (1021 == (int)status || 1020 == (int)status) {
            String perpetrator = fightObject.getStatusMap().getString(status + "perpetrator");
            if (perpetrator != null && perpetrator.contains(String.valueOf(76303))) {
                fightObject.getStatusMap().unset(status + "perpetrator");
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 76303);
                skillRecord.setT((long)(int)fightObject.getId());
            }
        }
    }
    
    @Override
    public NutMap reduceMp(Fight fight, FightObject fightObject, Object... args) {
        int mana = 0;
        try {
            mana = (int)args[0];
        }
        catch (Exception e) {
            return null;
        }
        if (mana <= 0) {
            return null;
        }
        int status = this.getStatusExcute(fightObject);
        if (status != 0) {
            String perpetrator = fightObject.getStatusMap().getString(status + "perpetrator");
            if (perpetrator != null && perpetrator.contains(String.valueOf(76303))) {
                double percent = 0.4;
                try {
                    FightObject effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(perpetrator.split("_")[1])));
                    percent += (double)((int)effecter.getRole().getLl() / 100) * 0.04;
                }
                catch (NumberFormatException e2) {
                    return null;
                }
                int changeMp = (int)((double)mana * percent);
                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 76303);
                skillRecord.setT((long)(int)fightObject.getId());
                return NutMap.NEW().setv("changeMana", Integer.valueOf(changeMp));
            }
        }
        return null;
    }
    
    private int getStatus(RoleSkill skill) {
        if (SKILLATTR.SKILLATTR_POJIA.getValue() == (int)skill.getType()) {
            return 1021;
        }
        if (SKILLATTR.SKILLATTR_ZHENJI.getValue() == (int)skill.getType()) {
            return 1020;
        }
        return 0;
    }
    
    private int getStatusExcute(FightObject fightObject) {
        if (fightObject.getStatusValue(1020) > 0) {
            return 1020;
        }
        if (fightObject.getStatusValue(1021) > 0) {
            return 1021;
        }
        return 0;
    }
}
