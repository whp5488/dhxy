package com.kitty.system.game.fightnew.book.acsry.middle;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_76312 extends AcsrySkillProcessor
{
    public ACSRYSKILL_76312() {
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
        if (SKILLATTR.SKILLATTR_ZHIYU.getValue() == (int)skill.getType() && effecter.isPlayer()) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 76312);
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(76313))) {
                return;
            }
            target.getStatusMap().setv("1022perpetrator", "76312_" + effecter.getId());
            skillRecord.setT((long)(int)target.getId());
        }
        return;
    }
    
    @Override
    public NutMap countRecoverBloodEnd(Fight fight, FightObject fightObject, Object... args) {
        String string = fightObject.getStatusMap().getString("1022perpetrator");
        if (string == null || !string.contains(String.valueOf(76312))) {
            return null;
        }
        int life = 0;
        try {
            life = (int)args[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 76312);
        if (life > 0) {
            double percent = 0.04;
            int addLife = (int)((double)life * percent);
            NutMap map = NutMap.NEW();
            map.setv("changeLife", Integer.valueOf(addLife));
            skillRecord.setT((long)(int)fightObject.getId());
            return map;
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
        if (status == null || 1022 != (int)status) {
            return;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 76312);
        String perpetrator = fightObject.getStatusMap().getString("1022perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(76312))) {
            fightObject.getStatusMap().unset("1022perpetrator");
            skillRecord.setT((long)(int)fightObject.getId());
        }
        return;
    }
}
