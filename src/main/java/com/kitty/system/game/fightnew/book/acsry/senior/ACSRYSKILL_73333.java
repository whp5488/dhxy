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

public class ACSRYSKILL_73333 extends AcsrySkillProcessor
{
    public ACSRYSKILL_73333() {
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
        if (SKILLATTR.SKILLATTR_ZHEN.getValue() == (int)skill.getType() && effecter.isPlayer()) {
            target.getOtherMap().setv("skillRound", "73333_" + effecter.getId() + "_" + fight.getRound());
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 73333);
            skillRecord.setT((long)(int)target.getId());
        }
        return;
    }
    
    @Override
    public NutMap executeReduceHp(Fight fight, FightObject effecter, FightObject target, Object... args) {
        RoleSkill skill = null;
        int hurt = 0;
        try {
            skill = (RoleSkill)args[0];
            hurt = (int)args[1];
        }
        catch (Exception e) {
            return null;
        }
        if (skill == null) {
            return null;
        }
        if (hurt <= 0) {
            return null;
        }
        if (this.checkExcute(skill)) {
            Object obj = effecter.getOtherMap().get("skillRound", null);
            if (obj != null) {
                String str = obj.toString();
                String[] splits = str.split("_");
                if (splits.length >= 3) {
                    String roundStr = splits[2];
                    String objIdStr = splits[1];
                    String skillIdStr = splits[0];
                    if (roundStr.matches("^[0-9]+$") && objIdStr.matches("^[0-9]+$") && skillIdStr.matches("^[0-9]+$")) {
                        int round = Integer.parseInt(roundStr);
                        int objId = Integer.parseInt(objIdStr);
                        int skillId = Integer.parseInt(skillIdStr);
                        if (round == fight.getRound() && 73333 == skillId) {
                            FightObject fightObject = fight.queryFightObject(Integer.valueOf(objId));
                            if (fightObject != null) {
                                double property = 0.15;
                                property += (double)((int)fightObject.getRole().getMj() / 100) * 0.01;
                                if (property > 0.3) {
                                    property = 0.3;
                                }
                                int changeHurt = (int)((double)hurt * property);
                                SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 73333);
                                skillRecord.setT((long)(int)effecter.getId());
                                return NutMap.NEW().setv("changeHurt", Integer.valueOf(-changeHurt));
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private boolean checkExcute(RoleSkill skill) {
        return SKILLATTR.SKILLATTR_FIRE.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_WIND.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_THUNDER.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_WATER.getValue() == (int)skill.getType() || (SKILLATTR.SKILLATTR_HENGSAO.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_POJIA.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_ZHENJI.getValue() == (int)skill.getType()) || SKILLATTR.SKILLATTR_WULI.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_AIHAO.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_XIXUE.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_POISON.getValue() == (int)skill.getType() || SKILLATTR.SKILLATTR_ZHEN.getValue() == (int)skill.getType();
    }
}
