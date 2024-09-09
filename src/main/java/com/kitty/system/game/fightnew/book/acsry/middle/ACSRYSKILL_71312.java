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

public class ACSRYSKILL_71312 extends AcsrySkillProcessor
{
    public ACSRYSKILL_71312() {
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
        if (SKILLATTR.SKILLATTR_POISON.getValue() == (int)skill.getType() && effecter.isPlayer()) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 71312);
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(Integer.valueOf(71313))) {
                return;
            }
            target.getStatusMap().setv("1005perpetrator", "71312_" + effecter.getId());
            skillRecord.setT((long)(int)target.getId());
        }
        return;
    }
    
    @Override
    public NutMap countRecoverBloodEnd(Fight fight, FightObject fightObject, Object... args) {
        String string = fightObject.getStatusMap().getString("1005perpetrator");
        if (string == null || !string.contains(String.valueOf(71312))) {
            return null;
        }
        int life = 0;
        try {
            life = (int)args[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (life > 0) {
            double percent = 0.3;
            int reduceLife = (int)((double)life * percent);
            NutMap map = NutMap.NEW();
            map.setv("changeLife", Integer.valueOf(-reduceLife));
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap countRecoverMpEnd(Fight fight, FightObject fightObject, Object... args) {
        String string = fightObject.getStatusMap().getString("1005perpetrator");
        if (string == null || !string.contains(String.valueOf(71312))) {
            return null;
        }
        int mana = 0;
        try {
            mana = (int)args[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (mana > 0) {
            double percent = 0.3;
            int reduceMana = (int)((double)mana * percent);
            NutMap map = NutMap.NEW();
            map.setv("changeMana", Integer.valueOf(-reduceMana));
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
        if (status == null || 1005 != (int)status) {
            return;
        }
        String perpetrator = fightObject.getStatusMap().getString("1005perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(71312))) {
            fightObject.getStatusMap().unset("1005perpetrator");
        }
        return;
    }
}
