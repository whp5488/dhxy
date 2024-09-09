package com.kitty.system.game.fightnew.book.acsry.senior;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71313 extends AcsrySkillProcessor
{
    public ACSRYSKILL_71313() {
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
            target.getStatusMap().setv("1005perpetrator", "71313_" + effecter.getId());
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 71313);
            skillRecord.setT((long)(int)target.getId());
        }
        return;
    }
    
    @Override
    public NutMap countRecoverBloodEnd(Fight fight, FightObject fightObject, Object... args) {
        String string = fightObject.getStatusMap().getString("1005perpetrator");
        if (string == null || !string.contains(String.valueOf(71313))) {
            return null;
        }
        FightObject effecter = null;
        int life = 0;
        try {
            life = (int)args[0];
            String[] splits = string.split("_");
            effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(splits[1])));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (effecter == null) {
            return null;
        }
        if (life > 0) {
            double percent = 0.3;
            percent += (double)((int)effecter.getRole().getGg() / 200) * 0.05;
            if (percent > 0.5) {
                percent = 0.5;
            }
            int reduceLife = (int)((double)life * percent);
            NutMap map = NutMap.NEW();
            map.setv("changeLife", Integer.valueOf(-reduceLife));
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 71313);
            skillRecord.setT((long)(int)fightObject.getId());
            return map;
        }
        else {
            return null;
        }
    }
    
    @Override
    public NutMap countRecoverMpEnd(Fight fight, FightObject fightObject, Object... args) {
        String string = fightObject.getStatusMap().getString("1005perpetrator");
        if (string == null || !string.contains(String.valueOf(71313))) {
            return null;
        }
        FightObject effecter = null;
        int mana = 0;
        try {
            mana = (int)args[0];
            String[] splits = string.split("_");
            effecter = fight.queryFightObject(Integer.valueOf(Integer.parseInt(splits[1])));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (mana > 0) {
            double percent = 0.3;
            percent += (double)((int)effecter.getRole().getGg() / 200) * 0.05;
            if (percent > 0.5) {
                percent = 0.5;
            }
            int reduceMana = (int)((double)mana * percent);
            NutMap map = NutMap.NEW();
            map.setv("changeMana", Integer.valueOf(-reduceMana));
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 71313);
            skillRecord.setT((long)(int)fightObject.getId());
            return map;
        }
        else {
            return null;
        }
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
        if (perpetrator != null && perpetrator.contains(String.valueOf(71313))) {
            fightObject.getStatusMap().unset("1005perpetrator");
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 71313);
            skillRecord.setT((long)(int)fightObject.getId());
        }
        return;
    }
}
